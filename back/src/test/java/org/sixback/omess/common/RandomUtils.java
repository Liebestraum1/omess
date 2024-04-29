package org.sixback.omess.common;

import net.bytebuddy.utility.RandomString;

import java.util.stream.LongStream;

public class RandomUtils {
    public static Long generateNaturalNumber(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("limit는 0보다 커야한다");
        return LongStream.generate(() -> (int) (Math.random() * limit)).limit(1).sum();
    }

    public static String generateRandomString(int length) {
        return RandomString.make(length);
    }

    public static String generateRandomEmail() {
        String userName = generateRandomString(10); // 위의 랜덤 문자열 생성 메서드 사용
        String domainName = generateRandomString(5);
        return userName + "@" + domainName + ".com";
    }
}
