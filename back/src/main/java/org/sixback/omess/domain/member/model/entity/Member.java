package org.sixback.omess.domain.member.model.entity;

<<<<<<< Updated upstream
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
    @Column(length = 10, unique = true, nullable = false)
    private String nickname;
    @Column(length = 20, unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
}
