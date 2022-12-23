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
@Table(name="LIKES")
@SequenceGenerator(name = "SEQ_LIKES", sequenceName = "SEQ_LIKES_NO", initialValue = 1, allocationSize = 1) 
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LIKES")
    @Column(name = "LNO")
    private Long lno;

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
