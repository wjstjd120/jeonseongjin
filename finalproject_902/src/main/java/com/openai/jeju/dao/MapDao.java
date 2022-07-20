package com.openai.jeju.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.CompanyDto;
@Mapper
public interface MapDao {

	List<CompanyDto> getCategoryList(String c_category);

}
