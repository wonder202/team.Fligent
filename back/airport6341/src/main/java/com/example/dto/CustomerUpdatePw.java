package com.example.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter // mybatis Mapper 에서 사용함
@Setter // view에서 사용
@ToString // 현재 객체의 내용 확인용
@NoArgsConstructor // 생성자
public class CustomerUpdatePw {
    private Date birth;
    private String phone;
    private String userpw; 
    private String userpw1; 
}
