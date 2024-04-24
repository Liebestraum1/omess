package org.sixback.omess.common;

import net.bytebuddy.utility.RandomString;

import java.util.Random;

public class RandomUtils {
    public static String generateRandomString(int length) {
        return RandomString.make(length);
    }

    public static String generateRandomEmail() {
        String userName = generateRandomString(10); // 위의 랜덤 문자열 생성 메서드 사용
        String domainName = generateRandomString(5);
        return userName + "@" + domainName + ".com";
    }

    public static int generateRandomNumber(int limit) {
        int start = limit == 1 ? 0 : 1;
        Random random = new Random();
        return random.nextInt(start, limit);
    }
}
