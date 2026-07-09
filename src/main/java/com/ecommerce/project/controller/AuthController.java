package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.loginRequest.LoginRequest;
import com.ecommerce.project.security.loginRequest.SignupRequest;
import com.ecommerce.project.security.loginResponse.MessageResponse;
import com.ecommerce.project.security.loginResponse.UserInfoResponse;
import com.ecommerce.project.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder coder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq) {

        Authentication authentication = null;
        try {
            log.info("Login ====>>>");
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginReq.getUserName(), loginReq.getPassword()));
            log.info("User {} == {} => ", loginReq.getUserName(), loginReq.getPassword());
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
        log.info("User {} logged in successfully", token);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByUserName(signupRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("User Name already taken"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("Email already taken"),
                    HttpStatus.BAD_REQUEST);
        }
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                coder.encode(signupRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
            Role role = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(role);
        } else {
            signupRequest.getRoles().forEach(roleName -> {
                switch (roleName) {
                    case "admin":
                        Role role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(role);
                        break;
                    case "seller":
                        Role role1 = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(role1);
                        break;
                    default:
                        Role role2 = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(role2);
                }
            });
        }
        user.setRoles(roles);
        User save = userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }
}



