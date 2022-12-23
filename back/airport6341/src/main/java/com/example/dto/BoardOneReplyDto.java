package com.example.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BoardOneReplyDto {
    // List<reply> 댓글 목록을 vue에 따로 리턴

    Long rno; // 댓글번호
    String content; // 댓글 내용

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yy-MM-dd", timezone="Asia/Seoul") //날짜 포멧 바꾸기
    Date regdate; // 작성일자
    String nickname; // 사용자 닉네임
    String userid; //사용자아이디
}
