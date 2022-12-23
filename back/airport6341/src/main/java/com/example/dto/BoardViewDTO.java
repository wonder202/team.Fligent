package com.example.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardViewDTO {
    Long bno;
    String content;
    Long hit; 
    Date regdate;
    String title; 
    String userid; 
    Long hmapno; 
    Long hno; 
    String name; 
}
