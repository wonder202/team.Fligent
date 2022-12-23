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
@Table(name="BOARD")
@SequenceGenerator(name = "SEQ_BOARD", sequenceName = "SEQ_BOARD_NO", initialValue = 1, allocationSize = 1)    
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD")
    @Column(name = "BNO")
    private Long bno;
    
    @Column(length = 200) 
    private String title;

    @Lob //타입이 CLOB, varchar2(255)
    private String content;

    private Long hit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    // updatable => 수정시에도 날짜 갱신/변경여부
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;

    // @ToString.Exclude 
    // @JsonManagedReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    // Member member;
    Member member;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<Likes> like;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<Reply> reply;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<BoardImg> boardImg;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<HashtagMapping> hashtagMapping;

    // 날짜 포맷 변경을 위한 임시변수
    @Transient
    String date;   

    
}
