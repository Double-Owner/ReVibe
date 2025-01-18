package com.doubleowner.revibe.domain.option.entity;

import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Size {
    S_A(220), S_B(225), S_C(230), S_D(235), S_E(240), S_F(245),
    S_G(250), S_H(255), S_I(260), S_J(265), S_K(270), S_L(275),
    S_M(280), S_N(285), S_O(290), S_P(295), S_Q(300), S_R(305),
    S_S(310), S_T(315), S_U(320);

    private final int value;

    public static Size of(int value) {

        for (Size size : Size.values()) {
            if (size.value == value) {
                return size;
            }
        }

        throw new CommonException(ErrorCode.ILLEGAL_ARGUMENT,"잘못된 사이즈 입력 입니다.");
    }

    public static int toValue(Size size) {
        return size.value;
    }

}
