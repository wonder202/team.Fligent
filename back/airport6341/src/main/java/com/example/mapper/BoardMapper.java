package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.BoardMainDTO;
import com.example.dto.BoardMainViewDTO;
import com.example.dto.BoardSelectOneDto;
import com.example.dto.BoardSelectOneHashtagDTO;
import com.example.dto.BoardSelectOneImgDTO;
import com.example.dto.BoardTopDto;
import com.example.dto.BoardViewDTO;
import com.example.dto.HashtagMappingDto;
import com.example.dto.LikeCountViewDTO;
import com.example.dto.ReplyCountViewDTO;
import com.example.entity.Board;
import com.example.entity.Likes;

@Mapper
public interface BoardMapper {

    // 메인페이지 게시글 해시태그 + 제목+ 내용 + 최신순12개 가져오기
    public List<BoardMainDTO> boardSelectMain(Map<String,Object> map);

    // 메인페이지 이미지 URL 번호 가져오기
    public Long boardImageNoSelect(Long bno);

    // 메인페이지 검색결과 게시물 개수(count)
    public Long boardSelectCount(Map<String,Object> map);

    // 좋아요순 - 인기 게시물 4개 조회 (메인페이지/게시판메인상단)
    public List<BoardTopDto> boardLikeSelect();
    
    // 조회수순 - 인기 게시물 4개 조회 (메인페이지/게시판메인상단)
    public List<BoardTopDto> boardHitSelect();

    // 게시글 정보 + 사용자 닉네임 + 게시글 좋아요 수 COUNT
    public BoardSelectOneDto boardSelectOne(Long bno);
    
    // 게시글 1개 조회시 해당 게시글의 해시태그 목록
    public List<BoardSelectOneHashtagDTO> boardSelectOneHashtag(Long bno);
    
    // 게시글 1개 조회시 게시글에 해당하는 이미지 URL 전부
    public List<BoardSelectOneImgDTO> boardSelectOneImg(Long bno);

    // 게시글 1개 조회시 해당 게시글의 해시태그 목록 이름만 조회
    public List<String> boardSelectOneHashtagName(Long bno);


//  =========================================기본검색(해시태그) ===================================================
    // public BnoListDTO hashtagSelect(Map<String,Object> map);

    // 버튼형의 해시태그 다중 검색
    public List<BoardViewDTO> hashtagSearch(Map<String,Object> map);

    // 최신순 페이지 네이션
    public List<Board> selectBoardListDESC(Map<String,Object> map);

    // 페이지네이션을 위한 게시글 전체개수 구하기
    public long countList(String text);



// ======================================== 상세검색(검색창에 검색어도 입력)============================================

    // 공항 해시태그 검색 포함 제목 검색
    public List<BoardViewDTO> hashtagTitleSearch(Map<String,Object> map);

    // 공항 해시태그 검색 포함 내용 검색
    public List<BoardViewDTO> hashtagContentSearch(Map<String,Object> map);

    // 공항 해시태그 검색 포함 제목 + 내용 검색
    public List<BoardViewDTO> hashtagTitleContentSearch(Map<String, Object> map);

// ============================================ 인기/조회수 높은 게시물================================================


    // 조회순 + 페이지네이션
    public List<BoardViewDTO> selectBoardListHITDESC(Map<String,Object> map);


    // 좋아요순 + 페이지네이션
    public List<LikeCountViewDTO> CountingLikeDESC(Map<String,Object> map);


    // 댓글순 + 페이지네이션
    public List<ReplyCountViewDTO> selectBoardListREPLYDESC(Map<String,Object> map);



//------------------------------------- 좋아요 기능 구현--------------------------

    // LIKETBL DB 조회
    public int selectLike(Likes likes);

    // LIKE 테이블에 집어 넣기
    public int PostLike( Likes likes);

    // LIKE 테이블 카운팅으로 좋아요 여부 확인
    public int CountingLike(Long bno);

    // LIKE 테이블에 좋아요 한 정보 지우기
    public int DeleteLike1(Map<String,Object> map);

    public int DeleteLike(Likes likes);

    // 게시글 하위 좋아요 삭제
    public Integer deleteOneLikes(Long bno);

// -------------------------------CRUD-------------------------------------------
    // 게시판 글쓰기
    public int insertBoardOne(Board board);

    // 게시판 글쓰기 - 이미지 등록을 위한 글번호 조회
    public Long selectBoardBno(Map<String,Object> map);

    // 게시물 조회수 증가 
    public int UpdateHit(Long bno);

    //  게시글 1개 조회
    public Board selectBoardOne(Long bno);
    
    // 마이페이지 좋아요 게시글 조회(시간 포맷 수정)
    public BoardSelectOneDto selectBoardOne1(Long bno);

    // 글 1개 수정 기능
    public int updateoneBoard( Board board );

    // 검색 + 페이지네이션
    // SELECT * FROM( SELECT B.*, ROW_NUMBER() OVER (ORDER BY BNO DESC) ROWN FROM BOARD B WHERE TITLE LIKE '%' || #{text} || '%' ) WHERE ROWN BETWEEN #{start} AND #{start}+9
    

    // 상세 이전글
    public Long selectBoardPrev(Long bno);

    // 상세 다음글
    public Long selectBoardNext(Long bno);

    // 게시글 삭제
    public int deleteOneBoard(Long bno);

// ----------------------------------- 해시태그 검색 구현------------------------------------

    // 공항 해시태그
    public List<BoardViewDTO> airporthashtag(Map<String,Object> map);
    
    //(완료) 해시태그 여러개 등록
    public int insertHashtagMapping(List<BoardViewDTO> boardviewDTO);

    // 해시태그 삭제
    public int deleteHashtagMapping(List<HashtagMappingDto> HashtagMappingDto);

    // 게시글 하위 해시태그 매핑 삭제
    public int deleteHashtagOneBoard(Long bno);

// -------------------------------------게시글 상세 구현------------------------------------------

    public BoardViewDTO selectBoardViewOne(Long bno);

    public BoardViewDTO updateOneboardView(Long bno);

// ------------------------------------- BOARDMAINVIEW ------------------------------------------

    // BOARDMAINVIEW에서 BNO에 해당하는 USERID 조회(게시글/해시태그/댓글 수정시 회원확인용)
    public BoardMainViewDTO selectOneUserBoardView(Long bno);


    
// --------------------------------------(페이지 하단) 게시글 목록 전체 조회----------------------------
//   게시글 제목, 이미지 , 해시태그 , 작성일자, 댓글수 , 조회수 , 좋아요 수
    // public List <BoardFullListView> selectBoardrowPagelist( Map<String,Object> map);

}
