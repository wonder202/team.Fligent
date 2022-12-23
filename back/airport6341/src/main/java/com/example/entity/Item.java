package com.example.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name="ITEM")
@SequenceGenerator(name = "SEQ_ITEM", sequenceName = "SEQ_ITEM_NO", initialValue = 1, allocationSize = 1)   
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ITEM")
    @Column(name = "INO")
    private Long ino;
    
    @Column(length = 200) 
    private String name;

    @Lob //타입이 CLOB, varchar2(255)
    private String content;

    private Long price;

    private Long quantity;

    // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul") //날짜 포멧 바꾸기
    @Column(name = "REGDATE", updatable = false)  // updatable => 수정시에도 날짜 갱신/변경여부
    private Date regdate = null;

    @ToString.Exclude
    // @JsonBackReference(value = "")
    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ICATENO", referencedColumnName = "ICATENO")
    ItemCate itemcate;     

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    Member member; 

    @ToString.Exclude
    // @JsonIgnore
    // @JsonBackReference(value = "")
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE) 
    List<ItemImg> itemimg; 
    
    @ToString.Exclude
    // @JsonBackReference(value = "")
    // @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE) 
    List<Cart> cno; 
    
    @ToString.Exclude
    // @JsonBackReference(value = "")
    // @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE) 
    List<Orders> ono; 

    // 테이블 컬럼과 상관없는 임시변수
    // 이미지 url용 임시변수
    @Transient
    String imageurl;   
}
