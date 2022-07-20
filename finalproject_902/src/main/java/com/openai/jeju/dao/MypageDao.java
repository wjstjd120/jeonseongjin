package com.openai.jeju.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.BasketDto;
import com.openai.jeju.dto.BimgDto;
import com.openai.jeju.dto.BlogDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.EventDto2;
import com.openai.jeju.dto.IamBasketDto;
import com.openai.jeju.dto.LastBlogDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.NowTripDto;
import com.openai.jeju.dto.PastTripBlogDto;
import com.openai.jeju.dto.RevDto;
import com.openai.jeju.dto.TravelrouteDto;

@Mapper
public interface MypageDao {

	//마이페이지에 최근거래 목록 가져오기
	public List<IamBasketDto> mypageListSelect(IamBasketDto list);//목록을 묶어서 가져오겠다.
	
	//게시글 목록 가져오기
	public List<IamBasketDto> DealListSelect(IamBasketDto list);//목록을 묶어서 가져오겠다.
	
	//전체 글 갯수 구하기
	public int bcntSelect(IamBasketDto list);

	//마이페이지에 지난 여행 목록 가져오기
	public List<PastTripBlogDto> mypageBlogSelect(PastTripBlogDto list);
	
	//Review_Lodgment 에 숙박 리뷰 출력하기
	public List<RevDto> LodgmentListSelect(RevDto list);//목록을 묶어서 가져온다.

	//Review_Lodgment 에 해당 리스트 삭제
	public void delreview(Integer rv_pk_num);

	//Review_Activiry 에 레저 리뷰 출력하기
	public List<RevDto> ActiviryListSelect(RevDto rlist);

	//Review_Food 에 식당 리뷰 출력하기
	public List<RevDto> FoodListSelect(RevDto rlist);

	//NowTrip 페이지에 여행 리스트 출력
	public List<TravelrouteDto> NowTripListSelect(String tr_fk_id);
	//NowTrip 페이지에 여행 리스트 출력
	public List<TravelrouteDto> pastTripListSelect(String tr_fk_id);
	
	//달력 테스트 이벤트
	public List<EventDto2> eListSelect(EventDto2 edto);
	
	//NowTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보를 페이지에 출력
	public TravelrouteDto nowtripinfo(Integer tr_pk_num);
	
	//NowTrip 페이지실행시 달력에 정보 최신업데이트를위한 insert 작업.
//	public void cListInsert(NowTripDto ndto);
	
	//NowTrip 페이지에 여행리스트 삭제 버튼 클릭시 발동
	public void nowtripDel(Integer tr_pk_num);
	
	public void nowtripbasketDel(Integer tr_pk_num);
	
	//NowTrip 페이지에 여행리스트 여행완료 버튼 클릭시 발동
	public void nowtripCheck(Integer tr_pk_num);

	//Mypage_Plan 페이지에 게시판 리스트 출력
	public List<BlogDto> PlanList(ListDto list);
	
	//Mypage_Plan 페이지 게시판 리스트 개수 구하기
	public int PlancntSelect(ListDto list);
	
	//Mypage_plan에 해당정보와 일치한 상세내용 팝업창에 출력
	public LastBlogDto planPopup(Integer b_pk_num);
	
	//Mypage_plan 페이지 팝업창에 삭제버튼 실행시 삭제
	public void plandel(Integer b_pk_num);

	

	//PastTrip 페이지에 여행완료된 리스트 출력
	public List<NowTripDto> pastTripSelect(NowTripDto ndto);

	//PastTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보 페이지 출력
	public TravelrouteDto pasttripinfo(Integer tr_pk_num);
	
	
	
	public BlogDto bnumblogselect(Integer tr_pk_num);
	//PastTrip 페이지에 삭제버튼 클릭시 발동
	public void pasttripDel(Integer tr_pk_num);
	
	public void pasttripbasketDel(Integer tr_pk_num);
	
	public void pasttripbimgDel(Integer bnum);
	
	public void pasttripblogDel(Integer tr_pk_num);

	//PastTrip 페이지에 달력출력
	public List<EventDto2> pasteListSelect(EventDto2 edto);

	
	/*
	//Plan_Update 페이지에 수정 버튼 클릭시 발동
	public void blogUpdate(BlogDto bdto);

	//Plan_Update 페이지에 수정 버튼 클릭시 발동
	public void bimgUpdate(BimgDto bidto);
	 */
	
	
	//Plan_Update 페이지에 수정버튼 클릭시 발동
	public void blogUpdate(Integer bnum);
	
	//Plan_Update 페이지에 수정버튼 클릭시 발동
	public void bimgUpdate(BimgDto bidto);

	public List<BasketDto> TnumSelectBasketList(Integer bk_fk_tnum);

	public List<CompanyDto> getFavorites(CompanyDto cdto);
	
	public void jjimDel(CompanyDto cdto);
	
	
	
	
	public void blogUpdate(BlogDto blogdto);

	public TravelrouteDto travelSelect(int trnum);



}
