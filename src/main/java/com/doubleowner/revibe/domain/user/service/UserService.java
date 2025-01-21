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
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import com.doubleowner.revibe.global.util.AuthenticationScheme;
import com.doubleowner.revibe.global.util.JwtProvider;
import com.doubleowner.revibe.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    /**
     * 회원가입
     */
    @Transactional
    public UserSignupResponseDto signUpUser(UserSignupRequestDto requestDto) throws DuplicateKeyException {

        boolean duplicated = this.userRepository.findByEmail(requestDto.getEmail()).isPresent();
        if (duplicated) {
            throw new CommonException(ErrorCode.ALREADY_EXIST, "중복된 이메일 입니다.");
        }

        String profileImage = null;
        if(requestDto.getProfileImage() != null) {
            try{
                profileImage = s3Uploader.upload(requestDto.getProfileImage());
            } catch (IOException e){
                throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
            }
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
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"사용자를 찾을 수 없습니다."));

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> generatedTokens = jwtProvider.generateTokens(user.getEmail(), user.getRole());
        String accessToken = generatedTokens.get("access_token");

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(String username, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CommonException(ErrorCode.ILLEGAL_ARGUMENT, "패스워드가 올바르지 않습니다.");
        }

        String profileImage = findUser.getProfileImage();
        if(profileImage != null) {
            try {
                s3Uploader.deleteImage(findUser.getProfileImage());
            } catch (IOException e) {
                throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
            }
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
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CommonException(ErrorCode.ILLEGAL_ARGUMENT, "패스워드가 올바르지 않습니다.");
        }

        String profileImage = findUser.getProfileImage();
        if(requestDto.getProfileImage() != null) {
            try {
                // 기존 이미지 삭제
                s3Uploader.deleteImage(findUser.getProfileImage());
                // 새 이미지 업로드
                profileImage = s3Uploader.upload(requestDto.getProfileImage());
            } catch (IOException e) {
                throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
            }
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
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"사용자를 찾을 수 없습니다."));

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