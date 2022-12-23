package com.example.dto;

import java.sql.Date;

public class BoardListView {
    Long bno; // 게시물 번호
    Long hit; // 게시물 조회수
    Date regdate; // 게시물 날짜
    String title; // 게시물 제목
    String userid; // 작성자 아이디
    String nickname; // 작성자 닉네임 
    String imageurl; // 썸네일 이미지 url 임시 변수 
    Long likecnt; // 좋아요수 
}
