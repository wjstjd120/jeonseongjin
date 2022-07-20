package com.openai.jeju.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


import com.openai.jeju.dto.AoptionDto;
import com.openai.jeju.dto.BasketDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.FavoritesDto;
import com.openai.jeju.dto.LoptionDto;
import com.openai.jeju.dto.NoticeDto;
import com.openai.jeju.dto.ReviewDto;
import com.openai.jeju.dto.SpotDto;
import com.openai.jeju.dto.TravelrouteDto;

@Mapper
public interface MainDao {
   // cate에 따라 Company테이블 select
   public List<CompanyDto> cateSelectCompany(String cate);
   // basket 테이블 Insert
   public void BasketInsert(Map<String, Object> omap);
   // trnum으로 trdto select
   public TravelrouteDto getTrdto(String trnum);
   // trnum에 맞는 데이터중 c_category가 숙박인것만 select
   public List<BasketDto> selectLodCompany(String trnum);
   // trnum에 맞는 데이터중 c_category가 레저인것만 select
   public List<BasketDto> selectActCompany(String trnum);
   // trnum에 맞는 데이터중 c_category가 식당인것만 select
   public List<BasketDto> selectFoodCompany(String trnum);
   
   //bk_pk_num으로 basket 테이블 delete
   public void delBasket(String bk_pk_num);
   // 결제후 bk_paych 1, bk_paytime 현재 시간으로 업데이트
   public void payUpdate(BasketDto bdto);
   // lodto에 따른 Company select
   public ArrayList<String> loptionCheck(LoptionDto lodto);
   // aodto에 따른 Company select
   public ArrayList<String> aoptionCheck(AoptionDto lodto);
   
   public void reviewInsert(ReviewDto rdto);
   public void ReviewR_imgU(Map<String, String> fmap);
   
    //home QAlist
 	public List<ComplaintDto> homeqaListSelect();
 	
 	//home 공지사항List
 	public List<NoticeDto> homenoticeListSelect();
 	
 	//home 이벤트lsit
 	public List<EventDto> homeeventListSelect();
 	
 	//home 숙박이미지뿌리기
 	public List<CompanyDto> homecompanyList();
 	
 	//home md숙박이미지뿌리기
 	public List<CompanyDto> homemdcompanyList();
 	
 	//home 레저이미지뿌리기
 	public List<CompanyDto> homeactiveList();
 	
 	//home md레저이미지뿌리기
 	public List<CompanyDto> homemdactiveList();
 	//제주 관광지
 	public List<SpotDto> spotbigList();
 	public List<SpotDto> spotList();
	public void favoritesInsert(FavoritesDto fav);
	

   
}