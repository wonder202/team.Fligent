package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.BoardImg;

@Mapper
public interface BoardImageMapper {

    // 글쓰기 이미지 여러개 등록
    public int insertBoardbatchImage(List<BoardImg> list);
    
    // 글쓰기 이미지 한개 등록
    public int insertBoardImage(BoardImg boardImage);
    
    // 게시글 image 파일 불러오기
    public BoardImg selectboardimage( Long bimageno );

    
    public List<BoardImg> selectboardimageList(Long bno);

    
    public BoardImg selectImageBnoOne(Long bno);


    public int updateImage( BoardImg boardimage );

    // 게시글 하위 이미지 삭제
    public int deleteBoardOneImage(Map<String,Object> map);

    public int deleteBoardImage(Long bno);

    
}
