package com.example.restcontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BoardMainDTO;
import com.example.dto.BoardOneReplyDto;
import com.example.dto.BoardSelectOneDto;
import com.example.dto.BoardSelectOneHashtagDTO;
import com.example.dto.BoardSelectOneImgDTO;
import com.example.dto.BoardTopDto;
import com.example.dto.BoardViewDTO;
import com.example.dto.HashtagDTO;
import com.example.dto.HashtagMappingDto;
import com.example.entity.Board;
import com.example.entity.Hashtag;
import com.example.entity.Likes;
import com.example.entity.Member;
import com.example.mapper.BoardImageMapper;
import com.example.mapper.BoardMapper;
import com.example.mapper.BoardReplyMapper;
import com.example.repository.HashtagRepository;

@RestController
@RequestMapping(value = "/api/board")
public class BoardRestController {

    @Autowired
    BoardMapper bMapper;

    @Autowired
    BoardReplyMapper brMapper;

    @Autowired
    BoardImageMapper biMapper;

    @Autowired
    HashtagRepository hRepository;



// 완료) ========= 메인페이지 게시글 해시태그 + 제목+ 내용 + 원하는 정렬순 + 12개 게시글 + 이미지url(썸네일) 가져오기 ============ 
    // 127.0.0.1:8080/ROOT/api/board/boardselectmain.json?hno=1&hno=2&hno=3&title=T&content=C&type=4&page=1
    
    @GetMapping(value = "/boardselectmain.json")
    public Map<String, Object> boardselectmainGET(
        @RequestParam(name = "hno", required = false) ArrayList<Long> hno,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "title", required = false) String title,
        @RequestParam(name = "type", defaultValue = "1") int type,
        HttpServletRequest request
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            Map<String , Object> map1 = new HashMap<>();
            // if(hno.isEmpty() || hno.get(0).equals(0L)){
            if(hno.get(0).equals(0L)){
                Long[] listHno = {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L};
                ArrayList<Long> listAll = new ArrayList<>(Arrays.asList(listHno));
                map1.put("hno", listAll);
            } else{
                map1.put("hno", hno);
            }
            page = (page-1)*12 +1;
            map1.put("page", page);
            map1.put("title", title);
            if(type == 1){ // 최신순
                map1.put("type", " "); // XML로 값 전달 안해줌
            } else if( type == 2 ) { // 조회수 순
                map1.put("type", "B.HIT DESC,"); // B.HIT,
            } else if( type == 3 ) { // 좋아요 순
                map1.put("type", "R.LIKECOUNT DESC, "); // R.LIKECOUNT,
            } else if( type == 4 ) { // 댓글수 순
                map1.put("type", "RR.REPLYCOUNT DESC, "); // RR.REPLYCOUNT DESC,
            }

            // 검색결과 게시글 COUNT개수 구하기
            Long count = bMapper.boardSelectCount(map1);

            // 검색된 게시글 목록 
            List<BoardMainDTO> boardlist = bMapper.boardSelectMain(map1);
            
            for(int i=0; i<boardlist.size(); i++){
                // boardlist의 게시글 중 한개의 게시글 번호
                Long bno = boardlist.get(i).getBno();
                
                // 게시글에 해당되는 해시태그 목록
                List<String> hnamelist = bMapper.boardSelectOneHashtagName(bno);

                boardlist.get(i).setHname(hnamelist);

                // 메인페이지 이미지 URL번호 가져오기
                // Long bno = boardlist.get(i).getBno();
                Long imgno = bMapper.boardImageNoSelect(bno);
                if(imgno == null){
                    imgno = 1L;
                }
                // 가져온 imgno로 imgurl 생성
                String imgurl = request.getContextPath() 
                + "/api/board/image?imgno=" + imgno;
                
                
                boardlist.get(i).setImgurl(imgurl);
            }
                
            if(boardlist != null){
                    map.put("status", 200);
                    map.put("result", boardlist);
                    map.put("count", count);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

// =========  조회수순 - 인기 게시물 4개 조회 (메인페이지/게시판메인상단)  =========
    // 127.0.0.1:8080/ROOT/api/board/boardHitselect.json
    @GetMapping(value = "/boardHitselect.json")
    public Map<String, Object> boardHitselectGET(
        HttpServletRequest request
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            // 검색된 게시글 목록 
            List<BoardTopDto> boardTopList = bMapper.boardHitSelect();

            // 메인페이지 이미지 URL번호 가져오기
            for(int i=0; i<boardTopList.size(); i++){

                Long bno = boardTopList.get(i).getBno();
                Long imgno = bMapper.boardImageNoSelect(bno);

                // 가져온 imgno로 imgurl 생성
                String imgurl = request.getContextPath() 
                + "/api/board/image?imgno=" + imgno;

                boardTopList.get(i).setImgurl(imgurl);
            }
                
            if(boardTopList != null){
                    map.put("status", 200);
                    map.put("result", boardTopList);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

// =========  좋아요순 - 인기 게시물 4개 조회 (메인페이지/게시판메인상단)  =========
    // 127.0.0.1:8080/ROOT/api/board/boardLikeselect.json
    @GetMapping(value = "/boardLikeselect.json")
    public Map<String, Object> boardLikeselectGET(
        HttpServletRequest request
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            // 검색된 게시글 목록 
            List<BoardTopDto> boardTopList = bMapper.boardLikeSelect();

            // 메인페이지 이미지 URL번호 가져오기
            for(int i=0; i<boardTopList.size(); i++){

                Long bno = boardTopList.get(i).getBno();
                Long imgno = bMapper.boardImageNoSelect(bno);

                // 가져온 imgno로 imgurl 생성
                String imgurl = request.getContextPath() 
                + "/api/board/image?imgno=" + imgno;

                boardTopList.get(i).setImgurl(imgurl);
            }
                
            if(boardTopList != null){
                    map.put("status", 200);
                    map.put("result", boardTopList);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 게시글 1개 조회
    // 127.0.0.1:8080/ROOT/api/board/boardselectone.json?bno=1
    @GetMapping(value = "/boardselectone.json")
    public Map<String, Object> boardSelectOneGET(
        @RequestParam(name = "bno") Long bno,
        HttpServletRequest request
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            // 게시글 정보 + 사용자 닉네임 + 게시글 좋아요 수 COUNT
            BoardSelectOneDto boardSelectOne = bMapper.boardSelectOne(bno);

            // 게시글 1개 조회시 게시글에 해당하는 이미지 URL 전부
            List<BoardSelectOneImgDTO> BoardImageList = bMapper.boardSelectOneImg(bno);

            List<String> imgurllist = new ArrayList<>();
            for(int i=0; i<BoardImageList.size(); i++){
                
                Long boardno = BoardImageList.get(i).getBno();
                List<BoardSelectOneImgDTO> imgno = bMapper.boardSelectOneImg(boardno);

                // 가져온 imgno로 imgurl 생성
                String imgurl = request.getContextPath() 
                + "/api/board/image?imgno=" + imgno.get(i).getBimageno();


                imgurllist.add(imgurl);
                boardSelectOne.setImgurllist(imgurllist);
            }
            
            // 게시글 1개 조회시 해당 게시글의 해시태그 목록
            List<BoardSelectOneHashtagDTO> BoardSelectOneHashtag = bMapper.boardSelectOneHashtag(bno);

            // 이전글 + 다음글
            Long Prevno = bMapper.selectBoardPrev(bno);
            Long Nextno = bMapper.selectBoardNext(bno);

            if(boardSelectOne != null){ // boardSelectOne 조회 결과가 있는경우 = 게시글이 있는경우
                map.put("status", 200);
                map.put("hashtag", BoardSelectOneHashtag);
                map.put("result", boardSelectOne);
                map.put("Prevno", Prevno);
                map.put("Nextno", Nextno);
            } else { // boardSelectOne 조회 결과가 없는경우 = 게시글이 없는경우
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 게시글 1개 조회시 해당 게시글에 등록된 댓글 목록 조회(8개씩 가져오기) + 게시글에 작성된 댓글 개수 COUNT
    // 127.0.0.1:8080/ROOT/api/board/boardselectonereply.json
    @GetMapping(value = "/boardselectonereply.json")
    public Map<String, Object> boardReplyGET(
        @RequestParam(name = "bno") Long bno,
        @RequestParam(name = "page") Long page
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            Map<String , Object> map1 = new HashMap<>();
            map1.put("bno", bno);
            map1.put("page", page);

            // 게시글 1개 조회시 해당 게시글에 등록된 댓글 목록 조회(8개씩 가져오기) + 정렬(오래된 순)
            List<BoardOneReplyDto> replyList = brMapper.selectBoardReplyList(map1);
            
            // 게시글 1개 조회시 해당 게시글에 작성된 댓글 개수 COUNT (페이지네이션 COUNT)
            Long count = brMapper.boardReplyCount(bno);

            map.put("status", 200);
            map.put("result", replyList);
            map.put("count", count);
            
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // // 로그인한 사용자 정보 조회
    // // 127.0.0.1:8080/ROOT/api/board/compareid.json
    @GetMapping(value = "/compareid.json")
    public Map<String, Object> boardCompareIdGET(
        HttpServletRequest httpServletRequest
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            
            map.put("status", 200);
            map.put("userid", userid);
            
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }



// ============ 해시태그 정보 가져오기 ==================
    @GetMapping(value = "/hashtagdataget.json")
    // 127.0.0.1:8080/ROOT/api/board/hashtagdataget.json
    public Map<String, Object> hashtagDataGET(
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            List<Hashtag> hashtaglist = hRepository.findAll();
            
            if(hashtaglist != null){
            List<HashtagDTO> list =  new ArrayList<>();

            for(int i=0; i<hashtaglist.size(); i++){
                HashtagDTO hashtag = new HashtagDTO();
                hashtag.setHno(hashtaglist.get(i).getHno());
                hashtag.setName(hashtaglist.get(i).getName());
                hashtag.setHcolor(hashtaglist.get(i).getHcolor());

                list.add(hashtag);
            }
                map.put("list", list);
                map.put("status", 200);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }







   
// ===========================================기본(해시태그) 검색 ===================================================

    // 버튼형의 해시태그 다중 검색
    // 127.0.0.1:8080/ROOT/api/board/hashtagsearch.json?hno=2&hno=3
    @GetMapping(value = "/hashtagsearch.json")
    public Map<String, Object> hashtagsearchGET(
        @RequestParam(name = "hno") List<Long> hno
    ){
        Map<String , Object> map = new HashMap<>();
        try {
            Map<String , Object> map1 = new HashMap<>();
            map1.put("hno", hno);

            List<BoardViewDTO> list = bMapper.hashtagSearch(map1);
            if(list != null){
                map.put("status", 200);
                map.put("result",list);
            }
            else{
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }






    // 최신순 + 검색 + 페이지네이션 목록 조회
    // 127.0.0.1:8080/ROOT/api/board/selectboardlist.json?text=&start=1
    @GetMapping(value = "/selectboardlist.json")
    public Map<String, Object> selectBoardListDESCGET(
            @RequestParam(name = "text") String text,
            @RequestParam(name = "start") int start) {
        Map<String, Object> map = new HashMap<>();

        try {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("text", text);
            map1.put("start", start);

            List<Board> list = bMapper.selectBoardListDESC(map1);
            long count = bMapper.countList(text);

            if (list != null) {
                map.put("result", list);
                map.put("count", count);
                map.put("status", 200);
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

// ========================================== 상세(해시태그 필터링을 거치고) 검색 ==================================================

    // 해시태그가 필터링된 제목 검색
    // 127.0.0.1:8080/ROOT/api/board/hashtagtitlesearch.json?text=카&start=1&hno=3&hno=4
    @GetMapping(value = "hashtagtitlesearch.json")
    public Map<String, Object> hashtagTitleSearchGET(
            @RequestParam(name = "text") String text,
            @RequestParam(name = "start") int start,
            @RequestParam(name = "hno") List<Long> hno
            ) {
        Map<String, Object> map = new HashMap<>();

        try {
            
            Map<String, Object> map1 = new HashMap<>();

            map1.put("text", text);
            map1.put("start", start);
            map1.put("hno", hno);

            List<BoardViewDTO> list = bMapper.hashtagTitleSearch(map1);
            
            if (list != null) {
                map.put("result", list);
                map.put("status", 200);
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }



    // 해시태그가 필터링된 내용 검색
    // 127.0.0.1:8080/ROOT/api/board/hashtagcontentsearch.json?text=&start=1
    @GetMapping(value = "hashtagcontentsearch.json")
    public Map<String, Object> hashtagContentSearchGET(
        @RequestParam(name = "text") String text,
        @RequestParam(name = "start") int start,
        @RequestParam(name = "hno") List<Long> hno) {
        Map<String, Object> map = new HashMap<>();

        try {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("text", text);
            map1.put("start", start);
            map1.put("hno", hno);


            List<BoardViewDTO> list = bMapper.hashtagContentSearch(map1);
            if (list != null) {
                map.put("result", list);
                map.put("status", 200);
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 해시태그가 필터링된 제목 + 내용 검색
    // 127.0.0.1:8080/ROOT/api/board/hashtagtitlecontentsearch.json?text=&start=1
    @GetMapping(value = "hashtagtitlecontentsearch.json")
    public Map<String, Object> hashtagTitleContentSearchGET(
        @RequestParam(name = "text") String text,
        @RequestParam(name = "start") int start,
        @RequestParam(name = "hno") List<Long> hno    
        ) {
        Map<String, Object> map = new HashMap<>();

        try {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("text", text);
            map1.put("start", start);
            map1.put("hno", hno);

            List<BoardViewDTO> list = bMapper.hashtagTitleContentSearch(map1);
            if (list != null) {
                map.put("result", list);
                map.put("status", 200);
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    



    // 완료 - 조회수 증가
    // 127.0.0.1:8080/ROOT/api/board/updatehit.json?bno=1
    @PutMapping(value = "/updatehit.json")
    public Map<String, Object> updatehitPUT(@RequestParam(name = "bno") Long bno) {
        Map<String, Object> map = new HashMap<>();
        int ret = bMapper.UpdateHit(bno);
        if (ret == 1) {
            map.put("status", 200);
        } else {
            map.put("status", 0);
        }
        return map;
    }

    // 정렬 기능은 view로 만들었기때문에 위에 있는거 참고해서 뽑아쓰면 됨

    

    // 완료 - 좋아요 누르면 좋아요 됨. 한번 더 누르면 좋아요 취소
    // Filter 통과후 좋아요 기능 구현 FilterConfig url 설정 해야 로그인 한 사용자만 좋아요를 누르는 기능 구현가능
    // 127.0.0.1:8080/ROOT/api/board/postlike.json
    // body에 {"board":{"bno": 57}} + TOKEN
    @PostMapping(value = "/postlike.json")
    public Map<String, Object> postlikePOST(
            HttpServletRequest httpServletRequest, @RequestBody Likes likes) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) httpServletRequest.getAttribute("username");
            Member member = new Member();
            member.setUserid(username);
            likes.setMember(member);

            int Count = bMapper.selectLike(likes);
            if (Count == 1) {
                int ret1 = bMapper.DeleteLike(likes);
                if (ret1 == 1) {
                    map.put("status", 200);
                    map.put("likestatus", 'B'); // 좋아요 해제됨
                } else {
                    map.put("status", 0);
                }
                
            } else if (Count == 0) {
                int ret = bMapper.PostLike(likes);
                if (ret == 1) {
                    map.put("status", 200);
                    map.put("likestatus", 'A'); // 좋아요 등록됨
                } else {
                    map.put("status", 0);
                }

            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }

        return map;
    }

    // 카운팅으로 좋아요를 눌렀는지 확인

            // 좋아요 테이블에는 SEQ, BNO, USERID
            // 상세페이지에서 좋아요 버튼이 있음
            // 좋아요 버튼을 눌렀을때 db 조회
            // 이버튼을 눌렀을떄 => BNO, USERID 파람으로 받아오겠지
            // 이걸 바탕으로 DB 조회 (BNO AND USERID) mapper
            // COUNT(*)를 했을때 0이나오면 좋아요 안한 상태, 1이 나오면 좋아요 한 상태 <- 데이터 형태 확인
            // 눌렀을때 0이 나오면 좋아요 안누를 상태니까 INSERT로 BNO, USERID를 DB저장


// ----------------------------------- 게시판 글쓰기 ------------------------------------------

    // 완료 - 글쓰기 (DB저장)
    // 127.0.0.1:8080/ROOT/api/board/insertboardone.json
    // {"title":"ABCDEFG","content":"123213" }
    // 토큰도 필요함 headers에 TOKEN
    @PostMapping(value = "/insertboardone.json")
    public Map<String, Object> insertBoardOne(
            @RequestBody Board board,
            HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            String username = (String) request.getAttribute("username");
            Member member = new Member();
            member.setUserid(username);
            board.setMember(member);
            
            int ret = bMapper.insertBoardOne(board);
            
            map.put("userid", username);
            map.put("title", board.getTitle());
            map.put("content", board.getContent());
            Long bno = bMapper.selectBoardBno(map);
            
            if(ret == 1){
                retMap.put("status", 200);
                retMap.put("bno", bno);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("status", -1);
        }
        return retMap;
    }


    // 완료 - 게시글 등록시 해시태그 여러개 추가
    // 127.0.0.1:8080/ROOT/api/board/hashtagmapping.json
    // POSTMAN => [{"bno": 136,"hno":1}, {"bno": 136,"hno":6}, {"bno": 136,"hno":9}, {"bno": 136,"hno":10}, {"bno": 136,"hno":11}]
    @PostMapping(value = "/hashtagmapping.json")
    public Map<String,Object> hashtagMAPPING(@RequestBody List<BoardViewDTO> boardviewDTO){
        Map<String, Object> map = new HashMap<>();
        try {

            // 본인이 작성한 게시글이어야함 ? => 게시글 등록할때 이루어질거니까 본인확인 안해도 될거같음

            int ret = bMapper.insertHashtagMapping(boardviewDTO);
            if(ret < 0){
                map.put("status", 0);
            } else {
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }


    // 완료 - 등록된 해시태그 여러개 삭제
    // 127.0.0.1:8080/ROOT/api/board/hashtagmappingdelete.json
    // POSTMAN => [{"bno": 133,"hno":11},{"bno": 133,"hno":11},{"bno": 133,"hno":11}]
    @PostMapping(value = "/hashtagmappingdelete.json")
        // boardviewDTO로 받아온 정보를 DB에서 조회하여 일치하면 삭제
        public Map<String,Object> hashtagDelete(
            @RequestBody List<HashtagMappingDto> HashtagMappingDto, 
            HttpServletRequest request){
            Map<String, Object> map = new HashMap<>();
            try {
                // 본인이 작성한 게시글인지 확인해야함
                // 로그인 한 사용자의 id
                String loginuserid = (String) request.getAttribute("username");

                Long bno = HashtagMappingDto.get(0).getBno();
                Board board = bMapper.selectBoardOne(bno);
                // xml에서 resultMap사용하여 하위 엔티티 가져옴
                // 원글 작성자 조회
                String WriterUserid = board.getMember().getUserid();

                if(loginuserid.equals(WriterUserid)){ //로그인 한 사용자의 id == 원글 작성자
                    int ret = bMapper.deleteHashtagMapping(HashtagMappingDto);
                    
                    if(ret > 0){ //삭제 완료시 반환 결과는 1보다 크다
                        map.put("status", 200);
                        
                    } else {
                        map.put("status", 1);
                    }
                } else {
                    map.put("status", 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", -1);
            }
            return map;
        }


    // 완료 - 게시글 수정
    // 127.0.0.1:8080/ROOT/api/board/updateoneboard.json
    // 토큰도 필요함 headers에 TOKEN
    // 이미지 추가 및 수정은 ImageRestController
    // {"bno":33, "title":"변경1", "content":"변경2"}
    @PutMapping(value = "/updateoneboard.json")
    public Map<String, Object> updateoneboardPUT(
        @RequestBody Board board,
        HttpServletRequest request
        ){
        Map<String, Object> map = new HashMap<>();
        try {
        // 본인이 작성한 게시글인지 확인해야함
        // 로그인 한 사용자의 id
        String loginuserid = (String) request.getAttribute("username");
        
        Board selectBoard = bMapper.selectBoardOne(board.getBno());
        
        if( loginuserid.equals(selectBoard.getMember().getUserid())){ // 수정하려는 사용자와 게시글 작성자가 일치하는 경우
            Member member = new Member();
            member.setUserid(loginuserid);
            board.setMember(member);

            int ret = bMapper.updateoneBoard(board);
                if(ret > 0){
                    map.put("status", 200);
                }
                else{
                    map.put("status", 1);
                }
        } else { // 수정하려는 사용자와 게시글 작성자가 일치하지 않는 경우
            map.put("status", 0); 
        }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }


    // 완료 - 게시글 삭제
    // 127.0.0.1:8080/ROOT/api/board/deleteoneboard.json?bno=
    @PostMapping(value = "/deleteoneboard.json")
    public Map<String, Object> deleteoneboardPUT(
        @RequestParam(name = "bno") Long bno,
        HttpServletRequest request
        ){
        Map<String, Object> map = new HashMap<>();
        try {
        // 본인이 작성한 게시글인지 확인해야함
        // 로그인 한 사용자의 id
        String loginuserid = (String) request.getAttribute("username");

        Board selectBoard = bMapper.selectBoardOne(bno);
        
        if( loginuserid.equals(selectBoard.getMember().getUserid())){ // 수정하려는 사용자와 게시글 작성자가 일치하는 경우

            // 게시글 하위 이미지 삭제
            biMapper.deleteBoardImage(bno);

            // 게시글 하위 댓글 삭제
            brMapper.deleteOneReply(bno);
            
            // 게시글 하위 좋아요 삭제
            bMapper.deleteOneLikes(bno);
            
            // 게시글 하위 해시태그 매핑 삭제
            bMapper.deleteHashtagOneBoard(bno);
            
            // 게시글 삭제
            int ret = bMapper.deleteOneBoard(bno);
                if (ret > 0) {
                    map.put("status", 200);
                } else {
                    map.put("status", 0);
                }
            } 
            else { // 수정하려는 사용자와 게시글 작성자가 일치하지 않는 경우
                    map.put("status", 0); 
                    System.out.println("사용자 불일치");
        }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    
    
    
}

// 사용 안함
// ------------------------------------------------------------------------------------
    // // 글 1개 조회(1개의 게시글 내용, 댓글목록전부, 이미지 url 전부, 좋아요 개수, 조회수 개수)
    // // 127.0.0.1:8080/ROOT/api/board/selectoneboard.json?bno=
    // @GetMapping(value = "/selectoneboard.json")
    // public Map<String, Object> selectoneboardGET( @RequestParam(name="bno") Long bno ){
    //     System.out.println(bno);
    //     Map<String, Object> map = new HashMap<>();
    //     Board board = bMapper.selectBoardOne(bno);

    //     if(board != null){
    //         map.put("status", 200);
    //         map.put("result", board);

    //     List<Reply> replylist = brMapper.selectAllReply(bno);

    //     System.out.println("replylist => " + replylist);

    //         map.put("status", 200);
    //         map.put("board", board);
    //         map.put("replylist", replylist);
    // }
    // return map;
    // }

        // 게시글 상세 수정
    // @PutMapping(value = "/updateoneboardView.json")
    // public Map<String, Object> updateoneboardViewPUT(@RequestBody Board board){
    //     // System.out.println(board.toString());
    //     Map<String, Object> map = new HashMap<>();
    //     int ret = bMapper.updateoneBoard(board);
    //     if(ret >0){
    //         map.put("status", 200);
    //         map.put("result", ret);
    //     }
    //     else{
    //         map.put("status", 0);
    //     }
    //     return map;
    // }


    // 게시글 상세 내용 조회
    // 게시글을 눌렀을때
    // 127.0.0.1:8080/ROOT/api/board/selectboardviewone.json?bno=
    // 이미지 url 따로 조회 해야함
    // @GetMapping(value = "/selectboardviewone.json")
    // public Map<String, Object> selectBoardViewOneGET(@RequestParam(name = "bno") Long bno){
    //     Map<String,Object> map = new HashMap<>();
    //     try {
    //         BoardViewDTO boardview = bMapper.selectBoardViewOne(bno);
    //         if(boardview != null){
    //             map.put("result", boardview);
    //             map.put("status", 200);
    //         }
    //         else{
    //             map.put("status", 0);
    //         }
    //     } catch (Exception e) {
    //         map.put("status", -1);
    //     }
    //     return map;
    // }

// ===========================================(페이지 하단) 게시글 목록 조회 ===================================================
    // 게시글 제목, 유저아이디 + 이미지 + 해시태그 + 작성일자 + 댓글수 + 조회수 + 좋아요 수
    // 127.0.0.1:8080/ROOT/api/board/selectboardrowpagelist.json?start=1
    // @GetMapping(value = "/selectboardrowpagelist.json")
    // public Map<String, Object> selectBoardrowPagelistGET(
    //         @RequestParam(name = "start") int start) {
    //     Map<String, Object> map = new HashMap<>();  
    //     try {
    //         Map<String, Object> map1 = new HashMap<>();
    //         map1.put("start", start);
    //         System.out.println("----------------------------------");
    //         System.out.println(map1.toString());

    //         List <BoardFullListView> list = bMapper.selectBoardrowPagelist(map1);
    //         System.out.println(list);
    //         System.out.println("abvsdsaadsfsadf");
    //         if (list != null) {
    //             System.out.println(list.toString());
    //             map.put("result", list);
    //             map.put("status", 200);
    //         } else {
    //             map.put("status", 0);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         map.put("status", -1);
    //     }
    //     return map;
    // }    

    // ============================================(페이지 상단) 인기 조회수 높은 게시글 ===============================================

    // 좋아요순 + 페이지네이션 정렬
    // 127.0.0.1:8080/ROOT/api/board/countinglike.json?text=&start=1
    // cnt가 좋아요 카운팅 개수 내림차순으로 정렬(좋아요 개수가 제일 많은게 위에서 부터 차례로 내려옴)
    // @GetMapping(value = "/countinglike.json")
    // public Map<String, Object> CountingLikeDESCGET(
    //     @RequestParam(name = "text") String text,
    //     @RequestParam(name = "page") int start) {
    //     Map<String, Object> map = new HashMap<>();

    //     try {
    //         Map<String, Object> map1 = new HashMap<>();
    //         map1.put("text", text);
    //         map1.put("start", start);
    //         System.out.println("----------------------------------");
    //         System.out.println(map1.toString());

    //         // map1 안에는 사용자 , start , 좋아요번호,게시판번호가 있음
    //         List <LikeCountViewDTO> list = bMapper.CountingLikeDESC(map1);
    //         if(list != null  ){   // 좋아요 개수가 있다면
    //             map.put("status", 200);
    //             map.put("result", list);
    //         } else{
    //             map.put("status", 0);
    //         }
    //     } catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }

    // // 조회순 + 검색 + 페이지네이션 완료 
    // // 127.0.0.1:8080/ROOT/api/board/selectboardlisthit.json?text=&start=1
    // @GetMapping(value = "/selectboardlisthit.json")
    // public Map<String, Object> selectBoardListHITDESCGET(
    //         @RequestParam(name = "text") String text,
    //         @RequestParam(name = "start") int start) {
    //     Map<String, Object> map = new HashMap<>();

    //     try {
    //         Map<String, Object> map1 = new HashMap<>();
    //         map1.put("text", text);
    //         map1.put("start", start);

    //         List<BoardViewDTO> list = bMapper.selectBoardListHITDESC(map1);
    //         System.out.println(list);
    //         if (list != null) {
    //             map.put("result", list);
    //             map.put("status", 200);
    //         } else {
    //             map.put("status", 0);
    //         }
    //     } catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }

    // // 댓글순 + 검색 + 페이지네이션 
    // // 127.0.0.1:8080/ROOT/api/board/selectboardlistreply.json?text=&start=1
    // @GetMapping(value = "/selectboardlistreply.json")
    // public Map<String, Object> selectBoardListREPLYDESCGET(
    // @RequestParam(name = "text") String text,
    // @RequestParam(name = "start") int start, String string) {
    // Map<String, Object> map = new HashMap<>();
    //     try {
    //         Map<String, Object> map1 = new HashMap<>();
    //         map1.put("text", text);
    //         map1.put("start", start);

    //         List<ReplyCountViewDTO> list = bMapper.selectBoardListREPLYDESC(map1);
    //         if (list != null) {
    //         map.put("result", list);
    //         map.put("status", 200);
    //         } 
    //         else {
    //         map.put("status", 0);
    //         }
    //     } 
    //     catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }
