package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
@Table(name="CART")
@SequenceGenerator(name = "SEQ_CART", sequenceName = "SEQ_CART_NO", initialValue = 1, allocationSize = 1) 
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART")
    @Column(name = "CNO")
    private Long cno; //장바구니번호

    private Long cnt;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss.SSS")
    @CreationTimestamp
    // updatable => 수정시에도 날짜 갱신/변경여부
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "INO", referencedColumnName = "INO")
    Item item; 

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne //many to one
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    Member member; 
    
}
