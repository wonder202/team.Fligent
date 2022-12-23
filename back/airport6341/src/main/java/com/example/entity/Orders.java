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
@Table(name="ORDERS")
@SequenceGenerator(name = "SEQ_ORDERS", sequenceName = "SEQ_ORDERS_NO", initialValue = 1, allocationSize = 1)  
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDERS")
    @Column(name = "ONO")
    private Long ono;

    private Long cnt;

    // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    // updatable => 수정시에도 날짜 갱신/변경여부
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;


    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "INO", referencedColumnName = "INO")
    Item item;

    @Column(name = "ORDERCODE")
    Long orderCode;
    
    
    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    Member member; 
}
