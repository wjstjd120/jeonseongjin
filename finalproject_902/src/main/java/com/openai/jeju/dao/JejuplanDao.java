package com.openai.jeju.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.BimgDto;
import com.openai.jeju.dto.BreplyDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.JejusimgDto;
import com.openai.jeju.dto.JejuspotDto;
import com.openai.jeju.dto.JejuspotreviewDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.jejuplanDto;

@Mapper
public interface JejuplanDao {
		// blog테이블 Insert
		public void JejuplanInsert(jejuplanDto dto);
		//제주 blog 리스트
		public List<jejuplanDto> planList(ListDto list);
		//제주이미지 클릭시 보이는 창
		public List<jejuplanDto> jejuplanInfo(int jnum);
		//제주 이미지 테이블
		public List<BimgDto> jejuplanImg(int jnum);
		//전체 글 개수 구하기
		public int jejuplancnt(ListDto list);
		//bimg insert 하기
		public void BimgInsert(BimgDto bdto);
		//댓글 목록 가져오기
		public List<BreplyDto> replySelect(int jnum);
		//댓글 저장하기
		public void replyInsert(BreplyDto reply);
		//제주 spot 리스
		public List<JejuspotDto> spotInfo(int jnum);
		//제주 spot 이미지 리스트
		public List<JejusimgDto>spotimgList(int jnum);
		//제주 spot 리뷰 리스트
		public List<JejuspotreviewDto> spotreview(int jnum);
		//제주 페이지 리스트
		public List<JejuspotDto> spotList(ListDto list);
		//spot cnt
		public int spotcnt(ListDto list);
		//이미지 2
		public List<JejusimgDto>spotimgList2(int jnum);
		//이미지 3
		public List<JejusimgDto>spotimgList3(int jnum);
		//이미지 4
		public List<JejusimgDto>spotimgList4(int jnum);
		//리뷰 insert
		public void spotreviewInsert(JejuspotreviewDto sDto);
		//
		public void breDelete(String sv_pk_num);
		//
		public JejuspotreviewDto replynumselect(String renum);
		//
		public void spotreviewUpdate(JejuspotreviewDto sDto);
		
		public List<EventDto> eventListselect(ListDto list);
		public int eventCntList(ListDto list);
		
		//제주 이벤트 상세보기
		public EventDto eventList(Integer e_pk_enum);
				
}


