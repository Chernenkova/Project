package edu.nc.service;

import edu.nc.dataaccess.entity.RegUserEntity;
import edu.nc.dataaccess.entity.User;
import edu.nc.dataaccess.model.security.UserEnterWrapper;
import edu.nc.dataaccess.repository.RegistrationRepository;
import edu.nc.dataaccess.wrapper.UpdateUserWrapper;
import edu.nc.dataaccess.wrapper.registration.LoginAndPassword;
import edu.nc.dataaccess.wrapper.registration.PersonalData;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private RegistrationRepository registrationRepository;

    private static final String EMAIL_REGULAR_EXPRESSION = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String REGISTRATION_THEME = "Registration";


    private static final long DELAY_TO_CONFIRM = 1000 * 3600 * 3;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private TimeProvider timeProvider;

    @Autowired
    public UserService(UserRepository userRepository, TimeProvider timeProvider, RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.timeProvider = timeProvider;
        this.registrationRepository = registrationRepository;
    }

    public ResponseEntity<User> getUser(Long userId) {
        return new ResponseEntity<>(userRepository.findFirstByUserId(userId), HttpStatus.OK);
    }

    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(new Sort("username")), HttpStatus.OK);
    }

    public ResponseEntity<User> createUser(UserEnterWrapper userWrapper) {
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

    public ResponseEntity updateUser(UpdateUserWrapper updateUserWrapper, Long userId) {
        User currentUser = userRepository.findFirstByUserId(userId);
        currentUser.setFirstname(updateUserWrapper.getUserFirstname());
        currentUser.setLastname(updateUserWrapper.getUserLastname());
        currentUser = userRepository.saveAndFlush(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    private String getToken(User user){

        String token = "\"name\" : \"" + user.getFirstname() + "\", " + "\"raiting\" : \"" + user.getRaiting() + "\"" +
                 "\"id\" : \"" + user.getUserId() + "\"";
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
            token += jwtBuilder.signWith(SignatureAlgorithm.HS512, secret).compact();
        }
        catch (Exception e){
            System.out.println("Error");
        }
        System.out.println(token);
        return token;
    }

    /**
     * adds the entry into DB and send the E'mail on login
     * @param wrapper - contains email and password to create Entity
     * @return -    BAD_REQUEST, if e-mail pattern is incorrect
     *              OK
     */
    public ResponseEntity registerUser(LoginAndPassword wrapper){
        Pattern p = Pattern.compile(EMAIL_REGULAR_EXPRESSION);
        Matcher m = p.matcher(wrapper.getLogin());
        if(!m.matches()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        //find the same entry
        Optional<RegUserEntity> optionalRegUserEntity = registrationRepository.findByLogin(wrapper.getLogin());
        //delete the entry
        optionalRegUserEntity.ifPresent(regUserEntity -> registrationRepository.delete(regUserEntity.getId()));

        RegUserEntity entity = new RegUserEntity(wrapper.getLogin(), wrapper.getPassword());
        String secret = entity.getUuid();
        registrationRepository.saveAndFlush(entity);

        EmailSender sender = new EmailSender(wrapper.getLogin(), REGISTRATION_THEME, secret);
        sender.send();

        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity confirmRegistration(String uuid, PersonalData wrapper){
        Optional<RegUserEntity> optionalRegUserEntity = registrationRepository.findByUuid(uuid);
        if(!optionalRegUserEntity.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        RegUserEntity current = optionalRegUserEntity.get();
        if(new Date().getTime() - current.getDate().getTime() > DELAY_TO_CONFIRM){
            return new ResponseEntity(HttpStatus.REQUEST_TIMEOUT);
        }
        registrationRepository.delete(current.getId());
//        return this.createUser(new UserWrapper( current.getLogin(),
//                                                current.getPasswordHash(),
//                                                wrapper.getUserFirstname(),
//                                                wrapper.getUserLastname(),
//                                                wrapper.getUserDateOfBirth(),
//                                                0,
//                                                true));
        return null;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
