package com.concert.app.domain.user;

import com.concert.app.interfaces.api.exception.ApiException;
import com.concert.app.interfaces.api.exception.ExceptionCode;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "USERS")
@Component
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_mail", nullable = false)
    private String userMail;

    @Column(name = "user_amount", nullable = false)
    private Long userAmount;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Version
    private int version;

    public Users(Long userId, Long userAmount) {
        String randomEmail = generateRandomEmail();
        this.id = userId;
        this.userMail = randomEmail;
        this.userAmount = userAmount;
        this.createdDt = LocalDateTime.now();
        this.isDelete = false;
    }

    // 랜덤 이메일 생성 메서드
    private static String generateRandomEmail() {
        String uuid = UUID.randomUUID().toString();
        return uuid + "@gmail.com"; // 랜덤 UUID 기반 이메일 생성
    }

    // jwt 토큰 파싱으로 userId 추출
    public static Long extractUserIdFromJwt(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .get("userId", Long.class);
    }

    public void addAmount(Long amount) {
        if(0 >= amount) {
            throw new IllegalArgumentException("충전금액을 0 이상으로 설정해주세요.");
        }

        this.userAmount += amount;
    }

    public void checkConcertAmount(Long seatAmount) {
        if(this.userAmount < seatAmount) {
            throw new ApiException(ExceptionCode.E005, LogLevel.INFO);
        }
        this.userAmount -= seatAmount;
    }
}
