package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="HASHTAGMAPPING")
@SequenceGenerator(name = "SEQ_HASHTAGMAPPING", sequenceName = "SEQ_HASHTAGMAPPING_NO", initialValue = 1, allocationSize = 1)
public class HashtagMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HASHTAGMAPPING")
    @Column(name = "HMAPNO")
    private Long hmapno;
    
    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "BNO", referencedColumnName = "BNO")
    Board board; 

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "HNO", referencedColumnName = "HNO")
    Hashtag hashtag; 
}
