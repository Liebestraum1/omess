package com.sixback.omesschat.common.utils;

import java.util.Base64;

public class Base64Util {
    public static String decode(String encodedString) {
        // Base64 디코더 인스턴스 생성
        Base64.Decoder decoder = Base64.getDecoder();

        // 인코딩된 문자열을 바이트 배열로 디코딩
        byte[] decodedBytes = decoder.decode(encodedString);

        // 바이트 배열을 문자열로 변환
        return new String(decodedBytes);
    }
}
