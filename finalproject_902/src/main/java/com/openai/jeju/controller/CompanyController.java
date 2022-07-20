package com.openai.jeju.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dto.AimgDto;
import com.openai.jeju.dto.CimgDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.ReviewDto;
import com.openai.jeju.dto.RoomDto;
import com.openai.jeju.dto.RoomImgDto;
import com.openai.jeju.service.CompanyService;

import lombok.extern.java.Log;

@Controller
@Log
public class CompanyController {

   private ModelAndView mv;

   @Autowired
   private CompanyService csv;

   @GetMapping("companyFrm")
   public String companyFrm() {
      log.info("CompanyController - companyFrm()");

      return "companyFrm";
   }

   @PostMapping("companyInsert")
   public String companyInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyController - companyInsert()");

      String view = csv.companyInsert(multi, rttr);

      return view;
   }

   @GetMapping("company")
   public ModelAndView CompanyPage(RedirectAttributes rttr) {
      log.info("CompanyController - companyPage()");

      mv = csv.getCompanyList(rttr);

      return mv;
   }

   @PostMapping(value = "roomPlus")
   public String roomInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyController - roomInsert()");

      String view = csv.roomInsert(multi, rttr);

      return view;
   }

   @PostMapping(value = "getRimgList", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<RoomImgDto>> getRimgList(String r_pk_num) {
      log.info("CompanyController - getRimgList()");

      Map<String, List<RoomImgDto>> rimap = csv.getRimgList(r_pk_num);

      return rimap;
   }

   @GetMapping("company_contents")
   public ModelAndView c_contents(String cnum) {
      log.info("CompanyController - c_contents()");

      mv = csv.c_contents(cnum);

      return mv;
   }

   @GetMapping("addGoodsFrm")
   public ModelAndView addGoodsFrm(String cnum) {
      log.info("CompanyController - addGoodsFrm()");

      mv = csv.addGoodsFrm(cnum);

      return mv;
   }

   @PostMapping("aactivityGoodsInsert")
   public String aactivityGoodsInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyController - aactivityGoodsInsert()");

      String view = csv.aGoodsInsert(multi, rttr);

      return view;
   }

   @GetMapping("updateAddGoods")
   public ModelAndView updateAddGoods(String c_category, String pknum) {
      log.info("CompanyController - updateAddGoods()");

      mv = csv.goupdateFrm(c_category, pknum);

      return mv;
   }

   @PostMapping(value = "delRimg", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<RoomImgDto>> delRimg(String sysname, String rnum) {
      log.info("CompanyController - delRimg()");
      String msg = null;

      Map<String, List<RoomImgDto>> rmap = csv.delRimg(sysname, rnum);

      return rmap;
   }

   @PostMapping("roomUpdate")
   public String roomUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyController - roomUpdate()");
      String view = null;

      view = csv.roomUpdate(multi, rttr);

      return view;
   }

   @PostMapping(value = "delAimg", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<AimgDto>> delAimg(String sysname, String atpnum) {
      log.info("CompanyController - delAimg()");

      Map<String, List<AimgDto>> amap = csv.delAimg(sysname, atpnum);

      return amap;
   }

   @PostMapping("aactivityGoodsUpdate")
   public String aactivityUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyController - aactivityGoodsUpdate()");
      String view = null;

      view = csv.aactivityUpdate(multi, rttr);

      return view;
   }

   @PostMapping(value = "delGoods", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> delGoods(String cate, String pknum) {
      log.info("CompanyController - delGoods()");

      Map<String, String> msvi = csv.delGoods(cate, pknum);

      return msvi;
   }

   @PostMapping(value = "searchCompany", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, Object> searchCompany(ListDto dto) {
      log.info("CompanyController - searchCompany()");

      Map<String, Object> cmap = csv.searchCompany(dto);

      return cmap;
   }

   @GetMapping("conCheck")
   public String updateCondition(String cnum, RedirectAttributes rttr) {
      log.info("CompanyController - conCheck()");

      String view = csv.updateCondition(cnum, rttr);

      return view;
   }

   @GetMapping("hidChancheck")
   public String hidChancheck(String category, String pknum, RedirectAttributes rttr) {
      log.info("CompanyController - hidChancheck()");
      String view = null;

      view = csv.hidChancheck(category, pknum, rttr);

      log.info(view);

      return view;
   }

   @GetMapping("companyDelete")
   public String companyDelete(String cnum, RedirectAttributes rttr) {
      log.info("CompanyController - companyDelete()");
      String view = null;

      view = csv.companyDelete(cnum, rttr);

      return view;
   }

   @GetMapping("companyOutFrm")
   public String companyOutFrm() {
      log.info("CompanyController - companyOutFrm()");
      String view = null;

      view = "companyoutFrm";

      return view;
   }

   @GetMapping(value = "solMemChange")
   public String solMemChange(String m_pk_id, RedirectAttributes rttr) {
      log.info("CompanyController - solMemChange()");
      String view = null;
      
      view = csv.solMemChange(m_pk_id, rttr);
      
      return view;
   }
   
   @GetMapping("memdelete")
   public String memdelete(String m_pk_id, RedirectAttributes rttr) {
      log.info("CompanyController - memdelete()");
      String view = null;
      
      view = csv.memdelete(m_pk_id, rttr);
      
      return view;
   }
   
   @PostMapping(value="cnumDeduplication" , produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> cnumDeduplication(String b_no){
      log.info("CompanyController - cnumDeduplication()");
      
      Map<String, String> smap = csv.cnumDeduplication(b_no);
      
      return smap;
    }
   
   @PostMapping(value="getReviewList", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<ReviewDto>> getReviewList(String cnum){
      log.info("CompanyController - getReviewList()");
      
      Map<String, List<ReviewDto>> rvmap = csv.getReviewList(cnum);
      
      return rvmap;
   }
   
   @PostMapping(value="reviewAnswer", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, String> reviewAnswer(ReviewDto dto){
      log.info("CompanyController - reviewAnswer()");
      Map<String, String> smap = new HashMap<String, String>();
      
      smap = csv.reviewAnswer(dto);
      
      return smap;
   }
   
   @PostMapping(value="getCimgList", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<CimgDto>> getCimgList(String cnum){
      log.info("CompanyController - getCimgList()");
      Map<String, List<CimgDto>> cimap = new HashMap<String, List<CimgDto>>();
      
      cimap = csv.getCimgList(cnum);
      
      return cimap;
   }
   
   @GetMapping("updateCompanyFrm")
   public ModelAndView updateCompanyFrm(String cnum) {
      log.info("CompanyController - updateCompanyFrm()");
      
      mv = csv.updateCompanyFrm(cnum);
      
      return mv;
   }
   
   @PostMapping(value="delCimg", produces = "application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<CimgDto>> delCimg(String cnum, String sysname){
      log.info("CompanyController - delCimg()");
      
      Map<String, List<CimgDto>> cimap = csv.delCimg(cnum, sysname);
      
      return cimap;
   }
   
   @PostMapping("companyUpdate")
   public String companyUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyController - companyUpdate()");
      String view = null;
      
      view = csv.companyUpdate(multi, rttr);
      
      return view;
   }
   
   @PostMapping(value = "optionSend", produces = "application/json; charset=utf-8")
   @ResponseBody
   public Map<String, Object> optionSend(@RequestParam(value = "arrList[]") List<String> arrList, String cate, String cnum){
      log.info(arrList.toString());
      
      Map<String, Object> rmap = csv.optionSend(arrList, cate, cnum);
            
      return rmap;
   }
   
   @GetMapping("CompanyreservationCheckFrm")
   public ModelAndView CompanyreservationCheckFrm(String num, String cate) {
      log.info("CompanyController - CompanyreservationCheckFrm()");
      
      mv = csv.reservationCheck(num,cate);
      
      return mv;
   }
   
   
}