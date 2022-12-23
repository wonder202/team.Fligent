package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.LikeDTO;
import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.entity.Reply;


@Mapper
public interface CustomerMapper {

    // 회원탈퇴
    public int deleteMember(Member member);

    // 암호변경
    public int updatePassword(Member member);

    // // 정보수정
    public int updateMember(Member member);

    // 닉네임 중복확인
    public int nicknamecheck(String nickname);

    // 작성한 게시글 가져오기
    public List<Board> myboardlist(Map<String,Object> map);

    // 작성한 총 게시글 갯수 count
    public Long myboardlistcount(String userid);

    // 작성한 댓글 가져오기
    public List<Reply> myboardreplylist(Map<String,Object> map);

    // 작성한 댓글 게시물 조회
    public Reply boardreplyone (Map<String,Object> map);

    // 작성한 댓글 총 갯수 count
    public Long myboardreplycount(String userid);

    // 작성한 댓글 수정 하기
    public int updateoneReply ( Reply reply );

    // 작성한 댓글 삭제 하기
    public int deleteoneReply ( Long rno);

    // 좋아요한 가져오기
    public List<LikeDTO> myboardlikelist(Map<String,Object> map);

    // 작성한 좋아요 총 갯수 count
    public Long myboardlikecount(String userid);

    // 게시판 selectone
    public Board myboardone(Long bno);

    // // 여기를 생략했기 때문에 xml에서 찾아서 자동으로 수행됨.
    // public int joinMember(MemberDTO member);

    // 공항 즐겨찾기 1개 변경
    public int updateAirportname (Map<String,Object> map);
    
    // 공항 즐겨찾기 1개 해제
    public int updatenullAirportname(Map<String,Object> map);

    public Member selectMemberOne(String userid);

    public MemberImg selectApiImageurl(String apiimageno);

    public MemberImg selectmemberimage(Long mimageno);

    public List<MemberImg> selectmemberimageList(String userid);

    // db에 회원 정보가 있는지 조회
    public Member selectPasswordMemberOne(Map<String,Object> map);

    // 임시 문자로 암호 변경
    public int updateMemberPassword(Member member);
    
    // 아이디 찾기
    public Member selectMemberidbirthandphone(Map<String, Object> map);
}
