package edu.nc.service;

import edu.nc.dataaccess.entity.User;
import edu.nc.dataaccess.model.security.UserEnterWrapper;
import edu.nc.security.JwtUser;
import edu.nc.dataaccess.wrapper.login.TokenWrapper;
import edu.nc.common.utils.TimeProvider;
import edu.nc.dataaccess.model.security.UserWrapper;
import edu.nc.dataaccess.repository.UserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public ResponseEntity<User> getUser(Long userId) {
        return new ResponseEntity<>(userRepository.findFirstByUserId(userId), HttpStatus.OK);
    }

    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(new Sort("username")), HttpStatus.OK);
    }

    public ResponseEntity<User> createUser(UserWrapper userWrapper) {
        User newUser = new User(userWrapper);
        newUser = userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    public ResponseEntity<TokenWrapper> authenticateUser(UserEnterWrapper userEnterWrapper,
                                                         UserDetailsService userDetailsService) {
        Long id = ((JwtUser) userDetailsService.loadUserByUsername(userEnterWrapper.getUsername())).getId();
        User user = getUser(id).getBody();
        if (user.getUserPassword().equals(userEnterWrapper.getUserPassword())) {
            return new ResponseEntity<>(new TokenWrapper(getToken(user)), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity updateUser(UserWrapper userWrapper, Long userId) {
        User currentUser = userRepository.findFirstByUserId(userId);
        currentUser.setUserPassword(userWrapper.getUserPassword());
        currentUser.setUsername(userWrapper.getUsername());
        currentUser.setDateofbirth(User.toDate(userWrapper.getUserDateOfBirth()));
        currentUser.setFirstname(userWrapper.getUserFirstname());
        currentUser.setLastname(userWrapper.getUserLastname());
        userRepository.saveAndFlush(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    private String getToken(User user) {
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
        } catch (Exception e) {
            System.out.println("Error");
        }
        return token;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
//local storage