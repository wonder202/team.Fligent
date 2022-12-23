package com.example.dto;

import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// 메인페이지 조회용 ItemDTO
public class ItemDTO {
    Long ino; //물품번호
    
    String name; //물품이름
    
    Long price; //물품가격

    Long quantity; //물품수량

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul") //날짜 포멧 바꾸기
    Date regdate; //물품등록일

    @Transient
    String imageurl; //이미지url
}
