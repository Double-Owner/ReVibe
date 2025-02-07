package com.doubleowner.revibe.domain.point;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserRepository userRepository;

    @Transactional
    public void addReviewPoint(User user, String image) {
        int point = PointPolicy.TEXT_ONLY_POINT.getPoint();
        if (image != null) {
            point = PointPolicy.TEXT_IMAGE_POINT.getPoint();
        }
        User findUser = userRepository.findByEmailOrElseThrow(user.getEmail());
        findUser.addPoint(point);


    }

    @Transactional
    public void handlePoint(User user, String preImage, MultipartFile afterImage) {

        int pointChange = 0;
        if (preImage != null && afterImage == null) {
            pointChange = PointPolicy.TEXT_ONLY_POINT.getPoint() - PointPolicy.TEXT_IMAGE_POINT.getPoint();
        } else if (preImage == null && afterImage != null) {
            pointChange = PointPolicy.TEXT_IMAGE_POINT.getPoint() - PointPolicy.TEXT_ONLY_POINT.getPoint();
        }
        if (pointChange != 0) {
            User findUser = userRepository.findByEmailOrElseThrow(user.getEmail());
            findUser.addPoint(pointChange);
        }
    }

    @Transactional
    public void deletePoint(User user, String image) {
        int point = (image != null)
                ? PointPolicy.TEXT_IMAGE_POINT.getPoint()
                : PointPolicy.TEXT_ONLY_POINT.getPoint();

        User findUser = userRepository.findByEmailOrElseThrow(user.getEmail());
        findUser.addPoint(-point);  // 포인트 차감
    }
}

