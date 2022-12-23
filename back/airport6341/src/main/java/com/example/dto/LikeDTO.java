package com.example.dto;

import lombok.Data;

@Data
public class LikeDTO {
    Long lno;    // 좋아요 식별
    Long bno ;      // 좋아요 게시물 식별 
    String userid;  // 좋아요 한 유저 식별
    BoardSelectOneDto board; // 게시글 내용
}
