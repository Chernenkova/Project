package edu.nc.controller;

import edu.nc.dataaccess.model.security.UserEnterWrapper;
import edu.nc.dataaccess.model.security.UserWrapper;
import edu.nc.dataaccess.wrapper.registration.LoginAndPassword;
import edu.nc.dataaccess.wrapper.registration.PersonalData;
import edu.nc.security.JwtTokenUtil;
import edu.nc.security.JwtUser;
import edu.nc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/welcome")
public class UserController {
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
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
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody UserEnterWrapper userWrapper) {
        return userService.createUser(userWrapper);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity authenticateUser(@RequestBody UserEnterWrapper userEnterWrapper) {
        return userService.authenticateUser(userEnterWrapper, userDetailsService);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
    public ResponseEntity updateUser(@RequestBody UserWrapper userWrapper, @PathVariable Long userId) {
        return userService.updateUser(userWrapper, userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity registerUser(@RequestBody LoginAndPassword wrapper){
        return userService.registerUser(wrapper);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm/{uuid}")
    public ResponseEntity confirmRegistration(@PathVariable String uuid, @RequestBody PersonalData wrapper){
        return userService.confirmRegistration(uuid, wrapper);
    }

}
