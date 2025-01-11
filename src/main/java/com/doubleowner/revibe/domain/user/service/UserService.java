package com.doubleowner.revibe.domain.user.service;

import com.doubleowner.revibe.domain.user.dto.request.UserDeleteRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserLoginRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.doubleowner.revibe.domain.user.dto.request.UserSignupRequestDto;
import com.doubleowner.revibe.domain.user.dto.response.UserProfileResponseDto;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.domain.user.entity.Role;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
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
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
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
     */
    @Transactional
    public JwtAuthResponse login(UserLoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다." ));

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

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(String username, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        findUser.deletedUser();
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public UserProfileResponseDto updateProfile(UserProfileUpdateRequestDto requestDto,
                                                UserDetailsImpl userDetails) {

        User findUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        findUser.updateProfile(requestDto, encodedPassword);

        return new UserProfileResponseDto(
                findUser.getId(),
                findUser.getNickname(),
                findUser.getEmail(),
                findUser.getProfileImage(),
                findUser.getAddress(),
                findUser.getPhoneNumber(),
                findUser.getStatus(),
                findUser.getCreatedAt(),
                findUser.getUpdatedAt()
        );

    }

}