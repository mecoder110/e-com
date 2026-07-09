package com.ecommerce.project.security.loginRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;


import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @Length(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;
    @Length(min = 6, max = 40)
    private String password;

    @Email(message = "Email should be valid")
    @Length(min = 6, max = 40)
    private String email;

    private Set<String> roles;


}
