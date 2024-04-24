package org.sixback.omess.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sixback.omess.common.TestUtils;
import org.sixback.omess.common.config.QuerydslConfig;
import org.sixback.omess.domain.member.model.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.sixback.omess.common.TestUtils.makeMember;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = NONE)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("email로 member를 조회 - 해당하는 member 존재함")
    void existsMemberByEmail_exist() {
        // given
        Member savedMember = memberRepository.save(new Member("nickname", "email@naver.com", "password"));

        // when
        boolean isExist = memberRepository.existsMemberByEmail("email@naver.com");

        // then
        Assertions.assertThat(isExist).isEqualTo(true);
    }

    @Test
    @DisplayName("email로 member를 조회 - 해당하는 member 존재하지 않음")
    void existsMemberByEmail_notExist() {
        // given

        // when
        boolean isExist = memberRepository.existsMemberByEmail("email@naver.com");

        // then
        Assertions.assertThat(isExist).isEqualTo(false);
    }

    @Test
    @DisplayName("member 조회 테스트 - 성공")
    void getMember_success() {
        // given
        Member savedMember = memberRepository.save(makeMember());

        // when
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        // then
        Assertions.assertThat(foundMember.get().getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("member 조회 테스트 - 실패")
    void getMember_fail() {
        // given

        // when
        Optional<Member> foundMember = memberRepository.findById(-1L);

        // then
        Assertions.assertThatThrownBy(() -> foundMember.get());
    }
}