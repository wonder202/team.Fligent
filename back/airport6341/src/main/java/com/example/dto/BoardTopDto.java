package com.example.dto;

import lombok.Data;

@Data
public class BoardTopDto {
    Long bno; // 글번호

    String content; // 글내용

    Long hit; // 조회수

    String regdate; // 작성일자

    String title; // 글제목

    String userid; // 사용자 아이디

    String nickname; // 사용자 닉네임

    Long lcnt; // 해당 게시글 좋아요 개수

    Long recnt; // 해당 게시글 댓글 개수

    String imgurl; // 해당 게시글 이미지 썸네일
}
