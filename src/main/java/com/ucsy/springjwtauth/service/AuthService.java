package com.ucsy.springjwtauth.service;

import com.ucsy.springjwtauth.dtos.LoginRequest;
import com.ucsy.springjwtauth.dtos.RefreshTokenRequest;
import com.ucsy.springjwtauth.dtos.TokenPairs;
import com.ucsy.springjwtauth.dtos.RegisterRequest;
import com.ucsy.springjwtauth.exception.GeneralException;
import com.ucsy.springjwtauth.model.User;
import com.ucsy.springjwtauth.repository.UserRepository;
import com.ucsy.springjwtauth.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailsService userDetailsService;

    public void registerUser(RegisterRequest request) {

        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new GeneralException("User is already exist");
        }

        User user = User.builder().
                username(request.getUsername()).
                password(passwordEncoder.encode(request.getPassword())).
                role(request.getRole()).
                build();

        userRepository.save(user);
    }

    public TokenPairs loginUser (LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateTokenPair(authentication);
    }

    public TokenPairs refreshToken (RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();

        if(!jwtService.isRefreshToken(refreshToken)) {
            throw new GeneralException("Refresh token is invalid");
        }

        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String accessToken = jwtService.generateAccessToken(authentication);
        return new TokenPairs(accessToken, refreshToken);

    }
}
