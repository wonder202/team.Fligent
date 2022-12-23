package com.example.dto;

import lombok.Data;

@Data
public class HashtagMappingDto {
  Long bno; // 게시글 번호
  Long hno; // 해시태그 번호
  String hname; // 해시태그 이름
}
