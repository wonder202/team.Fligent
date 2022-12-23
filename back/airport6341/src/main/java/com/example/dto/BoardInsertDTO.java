package com.example.dto;


import java.util.Date;

import lombok.Data;

@Data
public class BoardInsertDTO {
    Long BNO ;
    String TITLE ;
    String CONTENT ;
    Long HIT ;
    Date REGDATE ;
    String USERID;
}
