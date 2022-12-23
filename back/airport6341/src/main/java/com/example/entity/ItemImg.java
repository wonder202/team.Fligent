package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name="ITEMIMG")
@SequenceGenerator(name = "SEQ_ITEMIMG", sequenceName = "SEQ_ITEMIMG_NO", initialValue = 1, allocationSize = 1)    
    public class ItemImg {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ITEMIMG")
        @Column(name = "IIMAGENO")
        private Long iimageno;
    
        // 컬럼 삭제함
        // private Long idx = 0L;

        // 변수명이 같으니 컬럼명 따로 지정하지 않음
        @Column(length = 200) //길이만 지정
        String imagename;
    
        @JsonIgnore //vue로 출력 되지 않게 함
        Long imagesize;

        @Column(length = 30)
        String imagetype;
    
        @ToString.Exclude
        @JsonIgnore //vue로 출력 되지 않게 함
        @Lob
        byte[] imagedata;
    
    
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss.SSS")
        @CreationTimestamp
        // updatable => 수정시에도 날짜 갱신/변경여부
        @Column(name = "REGDATE", updatable = false)
        private Date regdate = null;

        @ToString.Exclude
        @JsonIgnore
        // @JsonBackReference(value = "")
        @ManyToOne
        @JoinColumn(name = "INO", referencedColumnName = "INO")
        Item item;     
    
        // 테이블 컬럼과 상관없는 임시변수
        // 이미지 url용 임시변수
        @Transient
        String imageurl;   

}
