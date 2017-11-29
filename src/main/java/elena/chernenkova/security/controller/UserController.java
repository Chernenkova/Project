package elena.chernenkova.security.controller;

import elena.chernenkova.model.security.UserEnterWrapper;
import elena.chernenkova.model.security.UserWrapper;
import elena.chernenkova.security.JwtTokenUtil;
import elena.chernenkova.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 123 on 03.11.2017.
 */
@RestController
@RequestMapping("/welcome")
public class UserController {
    private  UserService userService;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService){
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user")
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String input = request.getHeader(tokenHeader).substring(7);
        System.out.println(input);
        String username = jwtTokenUtil.getUsernameFromToken(input);
        System.out.println("username" + username);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody UserWrapper userWrapper){
        return userService.createUser(userWrapper);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity authenticateUser(@RequestBody UserEnterWrapper userEnterWrapper){
        return userService.authenticateUser(userEnterWrapper, userDetailsService);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
    public ResponseEntity updateUser(@RequestBody UserWrapper userWrapper, @PathVariable Long userId){
        return userService.updateUser(userWrapper, userId);
    }

}
