package elena.chernenkova.security.controller;

import elena.chernenkova.common.utils.TimeProvider;
import elena.chernenkova.model.security.User;
import elena.chernenkova.model.security.UserEnterWrapper;
import elena.chernenkova.model.security.UserWrapper;
import elena.chernenkova.security.JwtUser;
import elena.chernenkova.security.repository.UserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 123 on 03.11.2017.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private TimeProvider timeProvider;

    @Autowired
    public UserService(UserRepository userRepository, TimeProvider timeProvider) {
        this.userRepository = userRepository;
        this.timeProvider = timeProvider;
    }

    public ResponseEntity getUser(Long userId){
        return new ResponseEntity(userRepository.findFirstByUserId(userId), HttpStatus.OK);
    }

    public ResponseEntity getAllUsers(){
        return new ResponseEntity(userRepository.findAll(new Sort("username")), HttpStatus.OK);
    }

    public ResponseEntity createUser(UserWrapper userWrapper){
        User newUser = new User(userWrapper);
        userRepository.save(newUser);
        return new ResponseEntity(newUser, HttpStatus.CREATED);
    }

    public ResponseEntity authenticateUser(UserEnterWrapper userEnterWrapper,
                                           UserDetailsService userDetailsService){
        Long id = ((JwtUser) userDetailsService.loadUserByUsername(userEnterWrapper.getUsername())).getId();
        User user = (User)getUser(id).getBody();
        if(user.getUserPassword().equals(userEnterWrapper.getUserPassword())){
            return new ResponseEntity(getToken(user), HttpStatus.OK);
        }
        else
            return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity updateUser(UserWrapper userWrapper, Long userId){
        User currentUser = userRepository.findFirstByUserId(userId);
        currentUser.setUserPassword(userWrapper.getUserPassword());
        currentUser.setUsername(userWrapper.getUsername());
        currentUser.setDateofbirth(User.toDate(userWrapper.getUserDateOfBirth()));
        currentUser.setFirstname(userWrapper.getUserFirstname());
        currentUser.setLastname(userWrapper.getUserLastname());
        userRepository.saveAndFlush(currentUser);
        return new ResponseEntity(currentUser, HttpStatus.OK);
    }

    public String getToken(User user){
        String token = "";
        try {
            Date currentDate = timeProvider.now();
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("clientType", "user");
            tokenData.put("userID", user.getUserId().toString());
            tokenData.put("sub", user.getUsername());
            tokenData.put("exp", calculateExpirationDate(currentDate));
            tokenData.put("iat", currentDate);
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setClaims(tokenData);
            token = jwtBuilder.signWith(SignatureAlgorithm.HS512, secret).compact();
        }
        catch (Exception e){
            System.out.println("Error");
        }
        return token;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
