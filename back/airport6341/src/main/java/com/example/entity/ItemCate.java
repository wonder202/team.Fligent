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
@Table(name="ITEMCATE")
@SequenceGenerator(name = "SEQ_ITEMCATE", sequenceName = "SEQ_ITEMCATE_NO", initialValue = 1, allocationSize = 1)   
public class ItemCate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ITEMCATE")
    @Column(name = "ICATENO")
    private Long icateno; //댓글번호

    private String catename;

    @ToString.Exclude
    @JsonIgnore //순환참조 방지
    // @JsonBackReference(value = "")
    @OneToMany(mappedBy = "itemcate", cascade = CascadeType.REMOVE) 
    List<Item> item; 
}
