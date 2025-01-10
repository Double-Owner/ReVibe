package com.doubleowner.revibe.domain.user.service;

import com.doubleowner.revibe.domain.user.dto.request.UserLoginRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserSignupRequestDto;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.domain.user.entity.Role;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.config.dto.JwtAuthResponse;
import com.doubleowner.revibe.global.util.AuthenticationScheme;
import com.doubleowner.revibe.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserSignupResponseDto signUpUser(UserSignupRequestDto requestDto) throws DuplicateKeyException {

        boolean duplicated = this.userRepository.findByEmail(requestDto.getEmail()).isPresent();

        if (duplicated) {
            throw new DuplicateKeyException("중복된 이메일입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getEmail(),
                requestDto.getNickname(),
                encodedPassword,
                requestDto.getProfileImage(),
                requestDto.getAddress(),
                requestDto.getPhoneNumber(),
                Role.of(requestDto.getRole())
        );

        User savedUser = userRepository.save(user);

        return new UserSignupResponseDto(
                savedUser.getId(),
                savedUser.getNickname(),
                savedUser.getEmail(),
                savedUser.getProfileImage(),
                savedUser.getAddress(),
                savedUser.getPhoneNumber(),
                savedUser.getCreatedAt()
        );
    }

    /**
     * 로그인
     * @param requestDto
     * @return
     */
    @Transactional
    public JwtAuthResponse login(UserLoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. email= " + requestDto.getEmail()));

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> generatedTokens = jwtProvider.generateTokens(user.getId());
        String accessToken = generatedTokens.get("access_token");

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
    }
}