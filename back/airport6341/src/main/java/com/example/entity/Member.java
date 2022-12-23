package com.example.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name="MEMBER")
public class Member {
    @Id
    @Column(name = "USERID", length = 200)
    private String userid; 

    @Column(length = 200)
    private String userpw;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 200)
    private String nickname;

    private Long birth; 
    
    @Column(length = 20)
    private String role;  // 권한 (회원 = user /관리자 = admin)
    
    private int block = 0; // 회원상태 [ 활동중 = 0, 탈퇴함 = 1, 관리자가 회원차단 = 2 ]
    
    @Column(length = 200)
    private String airportname;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss.SSS")
    @CreationTimestamp
    // updatable => 수정시에도 날짜 갱신/변경여부
    @Column(name = "REGDATE", updatable = false)
    private Date regdate = null;    

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    List<MemberImg> memberimg; // 엔티티 만들때는 객체로 잡고 있지만 DB에는 memberImg엔티티의 기본키값인 imgno만 들어감

    @ToString.Exclude
    // @JsonBackReference(value = "")
    @OneToMany(mappedBy = "member" , cascade = CascadeType.REMOVE)
    List<Address> address;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<Likes> like;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @JsonBackReference(value = "")
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<Item> item;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<Board> board;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE) 
    @ToString.Exclude
    // @OrderBy(value = "no desc") // 정렬하기 no를 기준으로 내림차순
    List<Token> token;

    public CharSequence get(String string) {
        return null;
    }
    
}
