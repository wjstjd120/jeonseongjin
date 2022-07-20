package com.openai.jeju.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.openai.jeju.dto.BasketDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.FavoritesDto;
import com.openai.jeju.service.MainService;

import lombok.extern.java.Log;

@Controller
@Log
public class MainController {
   
   private ModelAndView mv;
   
   @Autowired
   private MainService masv;
   
   
   @GetMapping("/")
   public ModelAndView home() {
      log.info("MainController - home()");
      
      mv = masv.homeList();
      return mv;
   }
   
   
   @GetMapping("Question_List")
      public String Question_List() {
         log.info("Question_List");
         
         return "Question_List";
      }
   
   @GetMapping("Lodgment_List")
   public ModelAndView Lodgment_List(String cate) {
      log.info("MainController - Lodgment_List()");
      mv = masv.selectLodList(cate);

      return mv;
   }
   
   @GetMapping("Lodgment")
   public ModelAndView Lodgment(String cnum) {
      log.info("MainController - LodgmentInfo()");
      log.info("넘어온 데이터 : " + cnum);
      
      mv = masv.LodgmentInfo(cnum);

      return mv;
   }
   
   @PostMapping(value="getRoomDto", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, Object> getRoomDto(String rnum, String cate){
      log.info("CompanyController - getRoomDto()");
      log.info("넘어온 데이터 : " + rnum); 
      
      Map<String,Object> omap = masv.getRoomDto(rnum,cate);
      
      return omap;
   }
   
   @PostMapping("LodInsert")
   public String LodInsert(BasketDto basdto, String cnum, String rname, RedirectAttributes rttr) {
      log.info("CompanyController - LodInsert()");
      
      
      String view = masv.BasketInsert(basdto,cnum,rname, rttr);

      return view;
   }
   
   @PostMapping(value="getAplan" , produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> getAplan(BasketDto bdto, String todayDate){
      log.info("MainController - getAplan()");
      
      Map<String, String> smap = masv.getAplan(bdto,todayDate);
      
      return smap;
   }
   
   @PostMapping(value="getFplan" , produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> getFplan(BasketDto bdto){
      log.info("MainController - getFplan");
      Map<String, String> smap = new HashMap<String, String>();
      
      smap = masv.getFplan(bdto);
      
      return smap;
   }
   
   @PostMapping(value = "lBasketDel", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<BasketDto>> lBasketDel(String bk_pk_num){
      log.info("MainController - lBasketDel()");
      Map<String, List<BasketDto>> bmap = new HashMap<String, List<BasketDto>>();
      
      bmap = masv.lBasketDel(bk_pk_num);
      
      return bmap;
   }
   
   @PostMapping(value = "aBasketDel", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<BasketDto>> aBasketDel(String bk_pk_num){
      log.info("MainController - aBasketDel()");
      Map<String, List<BasketDto>> bmap = new HashMap<String, List<BasketDto>>();
      
      bmap = masv.aBasketDel(bk_pk_num);
      
      return bmap;
   }
   
   @PostMapping(value = "fBasketDel", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<BasketDto>> fBasketDel(String bk_pk_num){
      log.info("MainController - fBasketDel()");
      Map<String, List<BasketDto>> bmap = new HashMap<String, List<BasketDto>>();
      
      bmap = masv.fBasketDel(bk_pk_num);
      
      return bmap;
   }
   
   @PostMapping(value = "payUpdate", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> payUpdate(BasketDto bdto){
      log.info("MainController - payUpdate()");
      Map<String, String> smap = new HashMap<String, String>();
      
      smap = masv.payUpdate(bdto);
      
      return smap;
   }
   
   @PostMapping(value = "optionCheck", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, Object> optionCheck(@RequestParam(value = "arrList[]") List<String> arrList, String cate){
      log.info("MainController - optionCheck()");
      log.info(cate);
      Map<String, Object> omap = new HashMap<String, Object>();
      
      omap = masv.optionCheck(arrList, cate);
      
      return omap;
   }
   
   @PostMapping("reviewInsert")
   public String reviewInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("MainController - reviewInsert()");
      log.info("넘어온 정보 : " + multi.getParameter("rv_contents"));
      String view = null;
      
      view = masv.reviewInsert(multi, rttr);
      
      return view;
   }
   
   @PostMapping(value = "favoritest", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> favoritestest(FavoritesDto fav) {
   	Map<String, String> rmap = masv.favoritInsert(fav);
   	
   	return rmap;
   }
   
  

}