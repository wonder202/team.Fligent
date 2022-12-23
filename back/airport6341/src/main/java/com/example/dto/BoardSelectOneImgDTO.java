package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSelectOneImgDTO {
    Long bno; //게시글 번호
    Long bimageno; // 게시글 이미지 번호
}
