package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.ItemImg;

@Mapper
public interface AdminItemImageMapper {
    
    public int deleteImage(Map<String,Object> map);

    public int insertItembatchImage(List<ItemImg> list);

}
