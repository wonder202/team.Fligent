package com.example.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class AddressListDTO {
    Long ano;
    String postcode;
    String address;
    String comment;
    Date regdate;
    String userid;
    String name;
    String phone;
    String detailaddress;
}
