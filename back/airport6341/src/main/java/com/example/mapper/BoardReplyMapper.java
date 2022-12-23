package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.BoardOneReplyDto;
import com.example.entity.Reply;

@Mapper
public interface BoardReplyMapper {

    // 게시글 1개 조회시 해당 게시글에 등록된 댓글 목록 조회(8개씩 가져오기) + 정렬(오래된 순)
    public List<BoardOneReplyDto> selectBoardReplyList(Map<String,Object> map);

    // 게시글 1개 조회시 해당 게시글에 작성된 댓글 개수 COUNT (페이지네이션 COUNT)
    public Long boardReplyCount(Long bno);

    // 게시글 하위 댓글 삭제
    public int deleteOneReply(Long bno);

    // 게시글 댓글 수정시 댓글 정보 1개 불러오기
    public BoardOneReplyDto boardSelectOneReply(Long bno);


    

    
    // 댓글 달기 
    public int insertBoardReplyOne(Reply reply);

    // 댓글 1개 조회
    public Reply selectReplyOne(Long rno);

    public BoardOneReplyDto selectReplyDTOOne(Long rno);

    // 댓글 수정
    public int updateoneReply ( Reply reply );

    // 댓글 삭제
    public int deleteoneReply (Long rno);

    // 게시글에 해당하는 댓글 전체 조회
    public List<Reply> selectAllReply(Long bno);
}
