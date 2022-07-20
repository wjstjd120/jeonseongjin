package com.openai.jeju.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.NoticeDto;

@Mapper
public interface SvcenterDao {
	// 공지사항 List 구하기
	public List<NoticeDto> noticeSelect(ListDto list);
	// 공지사항 table 정보 갯수 숫자 구하기
	public int noticeCntSelect(ListDto list);
	// Notice 테이블 view 1증가 업데이트
	public void viewUpdate(Integer n_pk_num);
	// n_pk_num으로 Notice 정보 select
	public NoticeDto NoticeList(Integer n_pk_num);
	
	
	
	// ComplaintDto select
	public List<ComplaintDto> qaListSelect(ListDto list);
	// Complaint insert
	public void qaInsert(ComplaintDto plaint);
	// session으로 얻은 co_fk_id로 select 
	public List<ComplaintDto> MyQaListSelect(ListDto list);
	// co_pk_num으로 dto select
	public ComplaintDto qaList(Integer co_pk_conum);
	// co_pk_num으로 delete
	public void qaDelect(String co_pk_conum);

}
