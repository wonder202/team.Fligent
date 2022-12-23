package com.example.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSelectOneDto {

    Long bno;  // 게시글 번호
    String title;  // 게시글 제목
    String content;  // 게시글 내용
    Long hit;  // 조회수

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul") //날짜 포멧 바꾸기
    Date regdate;  // 작성일자
    String nickname;  // 사용자 닉네임
    String userid; //사용자 아이디
    Long lcnt;  // 좋아요 개수
    List<String> imgurllist; // 이미지url목록
    // List<BoardSelectOneHashtagDTO> hashtaglist;  // 해시태그 매핑목록
}
