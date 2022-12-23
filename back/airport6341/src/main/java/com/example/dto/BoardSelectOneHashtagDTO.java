package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSelectOneHashtagDTO {
    Long hno; // 해시태그 번호
    String name; //해시태그 이름
}
