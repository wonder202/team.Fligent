package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Setter
@SequenceGenerator(name = "SEQ_PAYMENT_INFO", sequenceName = "SEQ_PAYMENT_NO", initialValue = 1, allocationSize = 1)  
@ToString
public class PaymentInfo {
    @Id
    @Column(name = "PAYMENTNO")
    private Long PaymentNo; // 결제번호

    private String pg; // PG사 구분코드
    private String payMethod; // 결제수단 구분코드
    private String merchantUid; // 가맹점 주문번호
    private String name; // 결제대상 제품명
    private Long amount; // 결제금액
    private String buyer_email; // 주문자 이메일
    private String buyer_name; // 주문자명
    private String buyer_tel; // 주문자 연락처
    private String buyer_addr; // 주문자 주소
    private String buyer_postcode; // 주문자 우편번호
}
