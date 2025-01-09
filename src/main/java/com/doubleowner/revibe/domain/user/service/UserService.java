package com.doubleowner.revibe.domain.user.service;

import com.doubleowner.revibe.domain.user.dto.request.UserSignupRequestDto;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.domain.user.entity.Role;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.config.PasswordEncoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSignupResponseDto signUpUser(@Valid UserSignupRequestDto requestDto) throws DuplicateKeyException {

        boolean duplicated = this.userRepository.findByEmail(requestDto.getEmail()).isPresent();

        if (duplicated) {
            throw new DuplicateKeyException("중복된 이메일입니다.");
        }

        User user = new User(
                requestDto.getEmail(),
                requestDto.getNickname(),
                passwordEncoder.encode(requestDto.getPassword()),
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
}