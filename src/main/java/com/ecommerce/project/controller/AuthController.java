package com.ecommerce.project.controller;

import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.loginRequest.LoginRequest;
import com.ecommerce.project.security.loginResponse.UserInfoResponse;
import com.ecommerce.project.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginReq.getUserName(), loginReq.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "BAD Credientials");
            body.put("status", false);
            return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtils.generateTokenFromUser(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item ->
                item.getAuthority()).collect(Collectors.toList());
        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                token,
                roles);
        return ResponseEntity.ok(response);

    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Signup signup){

        if(userRepository.existsByUserName(signup)){
            return new ResponseEntity<>(new MessageResponse("User Name already taken"),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
