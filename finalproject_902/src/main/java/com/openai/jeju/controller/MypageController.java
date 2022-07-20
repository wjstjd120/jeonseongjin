package com.openai.jeju.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dto.BimgDto;
import com.openai.jeju.dto.BlogDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.EventDto2;
import com.openai.jeju.dto.IamBasketDto;
import com.openai.jeju.dto.LastBlogDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NowTripDto;
import com.openai.jeju.dto.PastTripBlogDto;
import com.openai.jeju.dto.RevDto;
import com.openai.jeju.service.MemberService;
import com.openai.jeju.service.MypageService;
import com.openai.jeju.service.UserSha256;

import lombok.extern.java.Log;

@Controller
@Log
public class MypageController {
	
	 @Autowired
	   private MemberService msv;
	 @Autowired
	   private MypageService myServ;
	 
	   private ModelAndView mv;

	   

	   	   
@GetMapping("mypage")
public ModelAndView mypage(IamBasketDto list, PastTripBlogDto blist , HttpSession session) {
//	mv = myServ.getmypage(list, session);
	log.info("CCCCCCCCCCCC");
	mv = myServ.getmypage(list, blist, session);
	return mv;
}



@GetMapping("Mypage_Update")
public String Mypage_Update() {
	return "Mypage_Update";
}



@GetMapping("Mypage_DealList")
public ModelAndView Mypage_DealList(IamBasketDto list, HttpSession session) {
	
	log.info("VVVVVVVVVVVVVVVVVVVVVVVV");
	mv=myServ.getDealList(list, session);
	
	return mv;
}


@GetMapping("Mypage_Resign")
public String Mypage_Resign() {
	return "Mypage_Resign";
}

@GetMapping("Mypage_PwdUpdate")
public String Mypage_PwdUpdate() {
	return "Mypage_PwdUpdate";
}

//@GetMapping("Mypage_NowTrip")
//public ModelAndView Mypage_NowTrip(TravelrouteDto tdto,HttpSession session) {
//	mv=myServ.getmyNowTrip(tdto, session);
//	log.info("AAA");
//	return mv;
//}

//@GetMapping("Mypage_PastTrip")
//public String Mypage_PastTrip() {
//	return "Mypage_PastTrip";
//}

@GetMapping("Favorites_Lodgment")
public ModelAndView Favorites_Lodgment(CompanyDto cdto) {
	
	mv = myServ.favoritesList(cdto);
	
	return mv;
}

@GetMapping("Favorites_Food")
public ModelAndView favoritesFood(CompanyDto cdto) {
   log.info("favoritesFood()");
   
   mv = myServ.favoritesFoodList(cdto);
   
   return mv;
}
@GetMapping("Favorites_Activity")
public ModelAndView favoritesActivityFood(CompanyDto cdto) {
   log.info("favoritesFood()");
   
   mv = myServ.favoritesActivityList(cdto);
   
   return mv;
}

@GetMapping("Review_Lodgment")
public ModelAndView Review_Lodgment(RevDto rlist,HttpSession session) {
	log.info("Review_Lodgment");
	mv=myServ.getLodgmentList(rlist  ,session);
	
	return mv;
}

@GetMapping("Review_Activiry")
public ModelAndView Review_Activiry(RevDto rlist, HttpSession session) {
	log.info("Review_Activiry");
	mv=myServ.getActiviryList(rlist, session);
	
	return mv;
}

@GetMapping("Review_Food")
public ModelAndView Review_Food(RevDto rlist, HttpSession session) {
	log.info("Review_Food");
	mv=myServ.getFoodList(rlist, session);
	
	return mv;
}

@GetMapping("Mypage_Plan")
public ModelAndView Mypage_Plan(BlogDto blist, ListDto list ,HttpSession session) {
	log.info("Mypage_Plan()");
	mv=myServ.getPlanList(blist,list,session);
	
	return mv;
}


@GetMapping("Mypage_Plan_Update")
public ModelAndView Mypage_Plan_Update(String bnum) {
	log.info("Mypage_Plan_Update()");
	
	mv = myServ.Mypage_Plan_Update(bnum);
	
	log.info(bnum);
	
	return mv;
}


//Plan_Update 페이지 수정버튼 클릭시 발동
@PostMapping("PlanUpdate")
public String planUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
	log.info("PlanUpdate()");
	
	String view = myServ.PlanUpdate(multi , rttr);
	
	return view;
}








/*
//Plan_Update 페이지 수정버튼 클릭시 발동
@PostMapping("PlanUpdate")
public String planUpdate(MultipartHttpServletRequest multi, Integer b_pk_num, BlogDto bdto, BimgDto bidto, RedirectAttributes rttr) {
	log.info("PlanUpdate()");
	
	String view = myServ.PlanUpdate(multi ,b_pk_num,bdto, bidto, rttr);
	
	return view;
}
*/



//개인정보 업데이트
@PostMapping("memberUpdate")
public String memberUpdate(MemberDto mem,RedirectAttributes rttr) {
	log.info("memberUpdate 컨트롤러!!!!!!!!!!!!!!!!");
	
	String view = msv.memberUpdate(mem,rttr);
	
	return view;
}



//비밀번호 확인
@PostMapping("PwdCheck")
public String PwdCheck(MemberDto mem,UserSha256 sha, RedirectAttributes rttr) {
	log.info("PwdCheck");
	
	String view = msv.PwdCheck(mem,sha,rttr);
	
	return view;
}


//Mypage_Resign 페이지 memberDelete 실행
//ajax 활용
@GetMapping(value = "memberDelete", produces = "application/text; charset=UTF-8")
@ResponseBody
public String memberDelete(String id) {
	log.info("memberDelete");
	
	String res = msv.memberDelete(id);
	return res;
}


//Mypage_PwdUpdate 페이지 비밀번호 대조
//ajax 활용 
@PostMapping(value = "pwCheck", produces = "application/text; charset=UTF-8")
@ResponseBody
public String pwCheck(String pw, RedirectAttributes rttr) {
	log.info("pwCheck");
	
	String res = msv.PwdCheck_2(pw, rttr);
		
	return res;
}

//비밀번호 업데이트
//ajax 활용 
@PostMapping(value="pwUpdate", produces = "application/text; charset=UTF-8")
@ResponseBody
public String pwUpdate(String pw) {
	log.info("MypageController-pwUpdate()");
	
	String res = msv.pwUpdate(pw);
	
	return res;
}

//Review_Lodgment 에 삭제버튼을 눌렀을때 해당 리스트 삭제
//ajax json 사용
@PostMapping(value = "delereview", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, String> reviewDel(Integer rv_pk_num,HttpSession session, RedirectAttributes rttr){
	log.info("reviewDel()");
	
	String msg = myServ.reviewDel(rv_pk_num, session, rttr);
	Map<String, String> rmap = new HashMap<String, String>();
	rmap.put("res", msg);
	
	return rmap;
}

//Mypage_Plan 페이지에 팝업 띄울때 클릭한 해당 정보가 출력
//ajax json 사용
@PostMapping(value = "planPopUp", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, LastBlogDto> planPopup(Integer b_pk_num, LastBlogDto bdto , HttpSession session, RedirectAttributes rttr){
	log.info("popuplist()");
	
	LastBlogDto blist = myServ.planPopup(b_pk_num, bdto, session, rttr);
	Map<String, LastBlogDto> rmap = new HashMap<String, LastBlogDto>();
	
	rmap.put("blist", blist);
	
	return rmap;
}

//Mypage_Plan 페이지 팝업 창에서 삭제버튼 눌렀을떄
//ajax json 사용
@PostMapping(value = "planDelete", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, String> planDel(Integer b_pk_num , HttpSession session, RedirectAttributes rttr){
	log.info("planDel()");
	
	String msg = myServ.planDel(b_pk_num, session, rttr);
	Map<String, String> rmap = new HashMap<String, String>();
	rmap.put("res", msg);
			
	return rmap;
}

//NowTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보를 페이지에 출력
//ajax json 사용
@PostMapping(value = "nowtripinfo", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, Object> nowtripinfo (Integer tr_pk_num, HttpSession session){
	log.info("tr_pk_num()");
	
	Map<String, Object> rmap = myServ.nowtripinfo(tr_pk_num, session);
	
	return rmap;
}


//NowTrip 페이지에 여행리스트 삭제 버튼 클릭시 발동
//ajax json 사용
@PostMapping(value = "nowtripDelete", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, String> nowtripDel(Integer tr_pk_num, HttpSession session){
	log.info("nowtripDel()");
	String msg = myServ.nowtripDel(tr_pk_num, session);
	Map<String, String> rmap = new HashMap<String, String>();
	rmap.put("res", msg);

	return rmap;
}


//NowTrip 페이지에 여행리스트 여행완료 버튼 클릭시 발동
//ajax json 사용
@PostMapping(value = "nowtripCheck", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, String> nowtripCheck (Integer tr_pk_num, HttpSession session){
	log.info("nowtripCheck()");
	String msg = myServ.nowtripCheck(tr_pk_num, session);
	Map<String, String> rmap = new HashMap<String, String>();
	rmap.put("res", msg);
	
	return rmap;
}


//PastTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보 페이지 출력
//ajax json 사용
@PostMapping(value = "pasttripinfo", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, Object> pasttripinfo(Integer tr_pk_num, NowTripDto ntdto, HttpSession session){
	log.info("pasttripinfo()");
	
	Map<String, Object> rmap = myServ.pasttripinfo(tr_pk_num,ntdto,session);
	
	return rmap;
}


//PastTrip 페이지에 여행리스트 삭제버튼 클릭시 발동
//ajax json 사용
@PostMapping(value = "pasttripDelete", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, String> pasttripDel(Integer bk_fk_tnum, HttpSession session){
	log.info("pasttripDel()");
	String msg = myServ.pasttripDel(bk_fk_tnum, session);
	Map<String, String> rmap = new HashMap<String, String>();
	rmap.put("res", msg);
	
	return rmap;
}


//Plan_Update 페이지에 수정버튼 클릭시 발동
//ajax json 사용
@PostMapping(value = "updatePlan", produces = "application/json; charset=UTF-8")
@ResponseBody
public String updatePlan(Integer bnum, BlogDto bdto, BimgDto bidto, String data){
	log.info("updatePlan()");

	String res = myServ.updatePlan(bnum, bdto, bidto, data);
	
	return res;
}





// 달력 테스트용!!!!!!!!!!

@GetMapping("Mypage_NowTrip")
public ModelAndView YoungsuFrm(HttpSession session) {
	
	mv = myServ.eventList(session);
	
	return mv;
}


@GetMapping("Mypage_PastTrip")
public ModelAndView Mypage_PastTrip(HttpSession session) {
	
	mv = myServ.pastList(session);
	
	return mv;
}



//@GetMapping("Mypage_NowTrip")
//public ModelAndView Mypage_NowTrip(TravelrouteDto tdto,HttpSession session) {
//	mv=myServ.getmyNowTrip(tdto, session);
//	log.info("AAA");
//	return mv;
//}



@PostMapping(value = "jjimDelete", produces = "application/json; charset=UTF-8")
@ResponseBody
public Map<String, String> jjimDelete(CompanyDto cdto,HttpSession session, RedirectAttributes rttr){
   log.info("jjimDelete()");
   
   String msg = myServ.jjimDelet(cdto, session, rttr);
   Map<String, String> jmap = new HashMap<String,String>();
   jmap.put("msg", msg);
   
   return jmap;
}

@GetMapping("/nowTripUpdate")
public String nowTripUpdate(int trnum, HttpSession session) {
	
	String view = myServ.nowTripUpdate(trnum, session);
	
	return view;
}




}//class end



