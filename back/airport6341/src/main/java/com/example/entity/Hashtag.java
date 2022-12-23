package com.example.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name="HASHTAG")
@SequenceGenerator(name = "SEQ_HASHTAG", sequenceName = "SEQ_HASHTAG_NO", initialValue = 1, allocationSize = 1)  
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HASHTAG")
    @Column(name = "HNO")
    private Long hno;

    @Column(length = 200)
    private String name;

    @Column(length = 50)
    private String hcolor;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<HashtagMapping> hashtagMapping;
}
