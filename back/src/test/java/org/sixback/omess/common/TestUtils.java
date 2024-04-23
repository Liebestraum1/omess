package org.sixback.omess.common;

import org.sixback.omess.domain.member.model.entity.Member;

import static org.sixback.omess.common.RandomUtils.generateRandomEmail;
import static org.sixback.omess.common.RandomUtils.generateRandomString;

public class TestUtils {
    public static Member makeMember() {
        return new Member(generateRandomString(8), generateRandomEmail(), generateRandomString(20));
    }

    public static Member makeMember(String nickname, String email, String password) {
        return new Member(nickname, email, password);
    }
}
