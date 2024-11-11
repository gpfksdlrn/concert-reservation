package com.concert.app.domain.payment.entity;

import com.concert.app.domain.payment.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "PAYMENT_HISTORY")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "amount_change", nullable = false)
    private Long amountCharge;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType type;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    public PaymentHistory(Long userId, Long amount, PaymentType paymentType) {
        this.userId = userId;
        this.paymentId = null;
        this.amountCharge = amount;
        this.type = paymentType;
        this.createdDt = LocalDateTime.now();
        this.isDelete = false;
    }

    public PaymentHistory(Long userId, Long amount, PaymentType paymentType, Long paymentId) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.amountCharge = amount;
        this.type = paymentType;
        this.createdDt = LocalDateTime.now();
        this.isDelete = false;
    }

    public static PaymentHistory enterPaymentHistory(Long userId, Long amount, PaymentType paymentType) {
        return new PaymentHistory(userId, amount, paymentType);
    }

    public static PaymentHistory enterPaymentHistory(Long userId, Long amount, PaymentType paymentType, Long paymentId) {
        return new PaymentHistory(userId, amount, paymentType, paymentId);
    }
}
