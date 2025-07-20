package ru.dzhenbaz.AuthServiceT1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.AuthServiceT1.dto.AuthResponse;
import ru.dzhenbaz.AuthServiceT1.dto.LoginRequest;
import ru.dzhenbaz.AuthServiceT1.dto.RefreshTokenRequest;
import ru.dzhenbaz.AuthServiceT1.dto.RegisterRequest;
import ru.dzhenbaz.AuthServiceT1.ex.InvalidRefreshTokenException;
import ru.dzhenbaz.AuthServiceT1.ex.UserNotCreatedException;
import ru.dzhenbaz.AuthServiceT1.ex.UserNotFoundException;
import ru.dzhenbaz.AuthServiceT1.model.RevokedToken;
import ru.dzhenbaz.AuthServiceT1.model.Role;
import ru.dzhenbaz.AuthServiceT1.model.User;
import ru.dzhenbaz.AuthServiceT1.repository.RevokedTokenRepository;
import ru.dzhenbaz.AuthServiceT1.repository.RoleRepository;
import ru.dzhenbaz.AuthServiceT1.repository.UserRepository;
import ru.dzhenbaz.AuthServiceT1.security.JwtService;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RevokedTokenRepository revokedTokenRepository;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserNotCreatedException("Username already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new UserNotCreatedException("Email already taken");
        }

        Set<Role> roles = Optional.ofNullable(request.roles())
                .orElse(Set.of("ROLE_GUEST"))
                .stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() ->  new UserNotCreatedException("Invalid role: " + name)))
                .collect(Collectors.toSet());

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(mapToUserDetails(user));
        String refreshToken = jwtService.generateRefreshToken(mapToUserDetails(user));

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String accessToken = jwtService.generateToken(mapToUserDetails(user));
        String refreshToken = jwtService.generateRefreshToken(mapToUserDetails(user));

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (revokedTokenRepository.existsByToken(refreshToken)) {
            throw new RuntimeException("Token has been revoked");
        }

        String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, mapToUserDetails(user))) {
            throw new InvalidRefreshTokenException("Refresh token is invalid or expired");
        }

        String newAccessToken = jwtService.generateToken(mapToUserDetails(user));

        return new AuthResponse(newAccessToken, refreshToken, "Bearer");
        //refresh-токен не обновляется, так как он еще валиден (не истек)
    }

    public void logout(String refreshToken) {
        Instant expiry = jwtService.extractExpiration(refreshToken);

        if (!revokedTokenRepository.existsByToken(refreshToken)) {
            revokedTokenRepository.save(
                    RevokedToken.builder()
                            .token(refreshToken)
                            .expiryDate(expiry)
                            .build()
            );
        }
    }

    private UserDetails mapToUserDetails(User user) {

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet())
        );
    }

}
