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
public class BoardMainViewDTO {
    Long bno;
    String title;
    String content;
    Long hit;
    Date regdate;
    String userid;
    Long bimageno;
    String nickname;
    Long hmapno;
    Long hno;
    String hname;
    Long rno;
    Long lno;
    

}
