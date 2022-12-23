package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "STORETBL")
@SequenceGenerator(name = "STORESEQ", sequenceName = "SEQ_STORETBL_NO", initialValue = 1, allocationSize = 1)
public class AirportStoreInfo {
    @Id // 기본키 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STORESEQ")
    private Long no;

    String airportCode; // 공항 코드 ex.PUS, CJU, GMP
    String airportName; // 공항 이름(국문)
    String contractEndDate; // 계약종료일
    String contractStartDate; // 계약시작일
    String contractStatus; // 계약상태 ex.진행중
    String leaseLocation; // 임대위치
    String rental; //연 임대료
    String space; // 면적(㎡)
    String storeName; //업체명
    String typeOfBusiness; //업종
}
