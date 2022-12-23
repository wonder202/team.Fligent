package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name="TOKEN")
// @DynamicUpdate
public class Token {
    @Id
    @ToString.Exclude
    @Column(name = "TOKEN", length = 500)
    private String token;
    
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private Member member;
    

    @Column(name = "ROLE", length = 20)
    private String role;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss.SSS")
    @CreationTimestamp
    // updatable => 수정시에도 날짜 갱신/변경여부
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;
}
