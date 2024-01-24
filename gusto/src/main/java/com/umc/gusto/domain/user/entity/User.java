package com.umc.gusto.domain.user.entity;

import com.umc.gusto.global.common.BaseTime;
import com.umc.gusto.global.common.PublishStatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User extends BaseTime {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID userid;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Age age;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "publishReview", nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'PUBLIC'")
    private PublishStatus publishReview;

    @Enumerated(EnumType.STRING)
    @Column(name = "publishPin", nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'PUBLIC'")
    private PublishStatus publishPin;

    @Column(nullable = false)
    private Long follower;

    @Column(nullable = false)
    private Integer pinCnt;

    @Column(nullable = false)
    private Integer reviewCnt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
    private MemberStatus memberStatus;

    public enum Gender {
        FEMALE, MALE, NONE
    }

    public enum Age {
        TEEN, TWENTIES, THIRTIES, FOURTIES, FIFTIES, OLDER, NONE
    }

    public enum MemberStatus {
        ACTIVE, INACTIVE, PAUSE
    }

}