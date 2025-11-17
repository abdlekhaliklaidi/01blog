package com.dev.backend.controllers;

import com.dev.backend.dto.LoginRequest;
import com.dev.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.dev.backend.services.CustomUserDetailsService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Email ou mot de passe invalide");
        }
    }

    public static class AuthResponse {
        private String token;
        public AuthResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
}


// package com.dev.backend.controllers;

// import com.dev.backend.dto.LoginRequest;
// import com.dev.backend.entities.Notification;
// import com.dev.backend.entities.User;
// import com.dev.backend.repositories.UserRepository;
// import com.dev.backend.services.CustomUserDetailsService;
// import com.dev.backend.services.NotificationService;
// import com.dev.backend.util.JwtUtil;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.*;
// import org.springframework.security.authentication.*;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/users")
// @CrossOrigin(origins = "http://localhost:4200")
// public class AuthController {

//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private CustomUserDetailsService userDetailsService;

//     @Autowired
//     private JwtUtil jwtUtil;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private NotificationService notificationService;

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//         try {
//             authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                     loginRequest.getEmail(),
//                     loginRequest.getPassword()
//                 )
//             );

//             UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
//             String token = jwtUtil.generateToken(userDetails.getUsername());

//             User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

//             Notification notif = new Notification();
//             notif.setUser(user);
//             notif.setMessage("You logged in successfully.");
//             notificationService.create(notif);

//             return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getFirstname(), user.getLastname()));

//         } catch (BadCredentialsException e) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                                  .body("Email ou mot de passe invalide");
//         }
//     }

//     public static class AuthResponse {
//         private String token;
//         private Long userId;
//         private String firstname;
//         private String lastname;

//         public AuthResponse(String token, Long userId, String firstname, String lastname) {
//             this.token = token;
//             this.userId = userId;
//             this.firstname = firstname;
//             this.lastname = lastname;
//         }

//         public String getToken() { return token; }
//         public Long getUserId() { return userId; }
//         public String getFirstname() { return firstname; }
//         public String getLastname() { return lastname; }
//     }
// }
