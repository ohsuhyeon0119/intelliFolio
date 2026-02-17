package com.suhyeon.intelli_folio.module.user;

import com.suhyeon.intelli_folio.core.auth.AuthDtos;
import com.suhyeon.intelli_folio.core.auth.AuthExceptions;
import com.suhyeon.intelli_folio.core.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Transactional
    public AuthDtos.SignupResponse signup(AuthDtos.SignupRequest req) {
        var exists = userMapper.findByEmail(req.email());
        if (exists != null) {
            throw AuthExceptions.emailAlreadyExists();
        }

        String hash = passwordEncoder.encode(req.password());

        userMapper.insert(req.email(), hash,req.displayName());
        long userId = userMapper.lastInsertId();

        return new AuthDtos.SignupResponse(userId, req.email(), req.displayName());

    }

    public AuthDtos.LoginResponse login(AuthDtos.LoginRequest req) {
        var user = userMapper.findByEmail(req.email());
        if (user == null) {
            throw AuthExceptions.invalidCredentials();
        }

        boolean ok = passwordEncoder.matches(req.password(), user.passwordHash());
        if (!ok) {
            throw AuthExceptions.invalidCredentials();
        }

        String token = authService.issueAccessToken(user.id(), user.email());
        return new AuthDtos.LoginResponse(user.id(), user.email(), user.displayName(), token);
    }
}
