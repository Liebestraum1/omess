package org.sixback.omess.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sixback.omess.common.BaseTimeEntity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 30, unique = true, nullable = false)
    private String nickname;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

    public Member(Long id, String nickname, String email, String password) {
        this(nickname, email, password);
        this.id = id;
    }
}
