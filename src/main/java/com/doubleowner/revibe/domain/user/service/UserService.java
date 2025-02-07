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
import com.doubleowner.revibe.global.common.service.ImageService;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import com.doubleowner.revibe.global.config.dto.JwtAuthResponse;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
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

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ImageService imageService;

    /**
     * 회원가입
     */
    @Transactional
    public UserSignupResponseDto signUpUser(UserSignupRequestDto requestDto) throws DuplicateKeyException {

        boolean duplicated = this.userRepository.findByEmail(requestDto.getEmail()).isPresent();
        if (duplicated) {
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }

        String profileImage = null;
        if(requestDto.getProfileImage() != null) {
                profileImage = imageService.uploadImage(profileImage, requestDto.getProfileImage());
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getEmail(),
                requestDto.getNickname(),
                encodedPassword,
                profileImage,
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
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> generatedTokens = jwtProvider.generateTokens(user.getEmail(), user.getRole());
        String accessToken = generatedTokens.get("access_token");
        String refreshToken = generatedTokens.get("refresh_token");

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken, refreshToken);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(String username, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
        }

        String profileImage = findUser.getProfileImage();
        if(profileImage != null) {
                imageService.deleteImage(findUser.getProfileImage());
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
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
        }

        String profileImage = findUser.getProfileImage();
        if(requestDto.getProfileImage() != null) {
                // 기존 이미지 삭제
                imageService.deleteImage(findUser.getProfileImage());
                // 새 이미지 업로드
                profileImage = imageService.uploadImage(profileImage, requestDto.getProfileImage());
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        findUser.updateProfile(requestDto, encodedPassword);

        userRepository.save(findUser);

        return new UserProfileResponseDto(
                findUser.getId(),
                findUser.getNickname(),
                findUser.getEmail(),
                profileImage,
                findUser.getAddress(),
                findUser.getPhoneNumber(),
                findUser.getStatus(),
                findUser.getCreatedAt(),
                findUser.getUpdatedAt()
        );
    }

    /**
     * 프로필 조회
     */
    @Transactional(readOnly = true)
    public UserProfileResponseDto getProfile(UserDetailsImpl userDetails) {

        User findUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));

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