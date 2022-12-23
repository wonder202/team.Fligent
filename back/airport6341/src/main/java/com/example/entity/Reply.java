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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name="REPLY")
@SequenceGenerator(name = "SEQ_REPLY", sequenceName = "SEQ_REPLY_NO", initialValue = 1, allocationSize = 1)  
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REPLY")
    @Column(name = "RNO")
    private Long rno;
    
    @Column(length = 200)
    private String content;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false) // updatable => 수정시에도 날짜 갱신/변경여부
    private Date regdate = null;

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    Member member; 
    
    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "BNO", referencedColumnName = "BNO")
    Board board; 
}
