package com.example.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyDTO {
    Long rno; // 댓글번호
    String content; // 댓글내용
    Date regdate; // 댓글작성일
    String userid; // 댓글작성자 아이디
    String nickname; // 댓글작성자 닉네임
}
