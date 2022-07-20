package com.openai.jeju.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.openai.jeju.dao.CompanyDao;
import com.openai.jeju.dao.MainDao;
import com.openai.jeju.dto.AactivityDto;
import com.openai.jeju.dto.AimgDto;
import com.openai.jeju.dto.AoptionDto;
import com.openai.jeju.dto.BasketDto;
import com.openai.jeju.dto.CimgDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.FavoritesDto;
import com.openai.jeju.dto.LoptionDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NoticeDto;
import com.openai.jeju.dto.ReviewDto;
import com.openai.jeju.dto.RoomDto;
import com.openai.jeju.dto.RoomImgDto;
import com.openai.jeju.dto.SpotDto;
import com.openai.jeju.dto.TravelrouteDto;

import lombok.extern.java.Log;

@Service
@Log
public class MainService {

   private ModelAndView mv;

   @Autowired
   private MainDao madao;
   @Autowired
   private CompanyDao cdao;
   @Autowired
   private HttpSession session;


   public ModelAndView selectLodList(String cate) {
      log.info("MainService - selectLodList()");
      mv = new ModelAndView();

      String trnum= "";

      switch(cate) {
      case "숙박":
         cate = "'숙박'";
         break;
      case "레저":
         cate = "'레저'";
         break;
      case "식당":
         cate = "'식당'";
         break;
      }
      TravelrouteDto tdto = (TravelrouteDto)session.getAttribute("tdto");

      

      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      
      Calendar cal = Calendar.getInstance();

      

      if(tdto != null) {
         String tr_in = tdto.getTr_in();
         String tr_out = tdto.getTr_out();
         trnum = String.valueOf(tdto.getTr_pk_num());

         log.info("세션 번호 :" + trnum);

         if(!(trnum.equals(""))) {
            TravelrouteDto trdto = madao.getTrdto(trnum);
            List<BasketDto> lList = madao.selectLodCompany(trnum);
            List<BasketDto> aList = madao.selectActCompany(trnum);
            List<BasketDto> fList = madao.selectFoodCompany(trnum);

            mv.addObject("trdto", trdto);
            mv.addObject("lList", lList);
            mv.addObject("aList", aList);
            mv.addObject("fList", fList);
         }
      }

      List<CompanyDto> cList = madao.cateSelectCompany(cate);
      mv.addObject("cList", cList);
      
      cate = cate.replace("'", "");
      log.info("cate : " + cate);
      mv.addObject("cate", cate);


      mv.setViewName("Lodgment_List");

      return mv;
   }

   public ModelAndView LodgmentInfo(String cnum) {
      log.info("MainService - LodgmentInfo()");


      String str = null;
      mv = new ModelAndView();

      CompanyDto cdto = cdao.getDto(cnum);
      List<CimgDto> cidto = cdao.getCimgList(cnum);

      switch(cdto.getC_category()) {
      case "숙박" :
         List<RoomDto> roomList = cdao.getRoomList(cnum);
         mv.addObject("roomList", roomList); // 방 상세정보 이미지 방번호와 비교하여(rnum) 방 이미지

         break;
      case "레저" :
         List<AactivityDto> aactivityList = cdao.getAactivityList(cnum);
         mv.addObject("aactivityList", aactivityList);
         break;
      }

      List<ReviewDto> rvList = cdao.getReviewList(cnum);


      mv.addObject("rvList", rvList);
      mv.addObject("cdto", cdto); // 업체 list 사진 클릭시 그 업체만 받아옴
      mv.addObject("cidto", cidto); // 업체 사진들 가져오기


      mv.setViewName("Lodgment");


      return mv;
   }

   public Map<String, Object> getRoomDto(String rnum, String cate) {
      log.info("CompanyService - getRoomDto()");
      Map<String, Object> omap = new HashMap<String, Object>();




      ArrayList<String> strarr = new ArrayList<String>();



      switch(cate) {
      case "숙박" :
         RoomDto rdto = cdao.getRoomDto(rnum);
         LoptionDto ldto = cdao.loptionSelect(rdto.getR_fk_cnum());
         if (ldto != null) {
            strarr = loptionGet(ldto);
         }
         List<RoomImgDto> riList = cdao.getRimgList(rnum);

         for (int i = 0; i < strarr.size(); i++) {
            log.info(strarr.get(i));
         }

         omap.put("riList", riList);
         omap.put("rdto", rdto);

         break;
      case "레저" :
         AactivityDto atdto = cdao.getAactivityDto(rnum);

         AoptionDto aodto = cdao.aoptionSelect(atdto.getAt_fk_cnum());

         strarr = aoptionGet(aodto);

         List<AimgDto> aiList = cdao.getAimgList(rnum);

         for (int i = 0; i < strarr.size(); i++) {
            log.info(strarr.get(i));
         }

         omap.put("aiList", aiList);
         omap.put("atdto", atdto);

         break;
      }

      omap.put("strarr", strarr);

      return omap;
   }

   private ArrayList<String> loptionGet(LoptionDto ldto) {
      ArrayList<String> strarr = new ArrayList<String>();

      if (ldto.getL_cityview().equals("1")) {
         strarr.add("시티뷰");
      }
      if (ldto.getL_seeview().equals("1")) {
         strarr.add("바다뷰");
      }
      if (ldto.getL_skyview().equals("1")) {
         strarr.add("하늘뷰");
      }
      if (ldto.getL_outdoor().equals("1")) {
         strarr.add("야외테라스");
      }
      if (ldto.getL_petok().equals("1")) {
         strarr.add("애견동반");
      }
      if (ldto.getL_bbq().equals("1")) {
         strarr.add("바베큐");
      }
      if (ldto.getL_cooking().equals("1")) {
         strarr.add("취사");
      }
      if (ldto.getL_pool().equals("1")) {
         strarr.add("수영장");
      }
      if (ldto.getL_spa().equals("1")) {
         strarr.add("스파");
      }
      if (ldto.getL_wallpool().equals("1")) {
         strarr.add("월풀");
      }
      if (ldto.getL_garaoke().equals("1")) {
         strarr.add("노래방");
      }

      return strarr;
   }

   private ArrayList<String> aoptionGet(AoptionDto aodto) {
      ArrayList<String> strarr = new ArrayList<String>();

      if (aodto.getA_waterpark().equals("1")) {
         strarr.add("워터파크");
      }
      if (aodto.getA_spa().equals("1")) {
         strarr.add("스파/온천");
      }
      if (aodto.getA_surfing().equals("1")) {
         strarr.add("서핑/카약");
      }
      if (aodto.getA_snowcooling().equals("1")) {
         strarr.add("스노쿨링");
      }
      if (aodto.getA_rafting().equals("1")) {
         strarr.add("래프팅/보트");
      }
      if (aodto.getA_aquarium().equals("1")) {
         strarr.add("아쿠아리움");
      }
      if (aodto.getA_flowers().equals("1")) {
         strarr.add("수목원");
      }
      if (aodto.getA_zoo().equals("1")) {
         strarr.add("동물원");
      }
      if (aodto.getA_shooting().equals("1")) {
         strarr.add("사격");
      }
      if (aodto.getA_horse().equals("1")) {
         strarr.add("승마");
      }
      if (aodto.getA_paragliding().equals("1")) {
         strarr.add("패러글라이딩");
      }
      if (aodto.getA_zipline().equals("1")) {
         strarr.add("짚라인");
      }
      if (aodto.getA_bike().equals("1")) {
         strarr.add("레일바이크");
      }
      if (aodto.getA_cart().equals("1")) {
         strarr.add("카트");
      }
      if (aodto.getA_atv().equals("1")) {
         strarr.add("ATV");
      }
      if (aodto.getA_tourpass().equals("1")) {
         strarr.add("투어패스");
      }
      if (aodto.getA_yort().equals("1")) {
         strarr.add("유람선/요트");
      }
      if (aodto.getA_cablecar().equals("1")) {
         strarr.add("케이블카");
      }
      if (aodto.getA_shipfishing().equals("1")) {
         strarr.add("선상낚시");
      }
      if (aodto.getA_fishinglounge().equals("1")) {
         strarr.add("낚시터");
      }
      if (aodto.getA_showpainting().equals("1")) {
         strarr.add("전시");
      }
      if (aodto.getA_show().equals("1")) {
         strarr.add("공연");
      }
      return strarr;
   }

   @Transactional
   public String BasketInsert(BasketDto basdto, String cnum, String rname, RedirectAttributes rttr) {
      CompanyDto cdto = null;
      MemberDto mdto = null;

      Map<String, Object> omap = new HashMap<String, Object>();

      String view = null;
      String msg = null;

      mdto = (MemberDto)session.getAttribute("user");
      cdto = cdao.getDto(cnum);

      TravelrouteDto trdto = (TravelrouteDto)session.getAttribute("tdto");

      String trnum = String.valueOf(trdto.getTr_pk_num());



      if(cdto.getC_category().equals("레저")) {
         AactivityDto atdto = cdao.getAactivityDto(String.valueOf(basdto.getBk_fk_num()));
         if(atdto.getAt_rtch().equals("1")) {
            omap.put("at_rtch", atdto.getAt_rtch());
         }

         basdto.setBk_price(basdto.getBk_price() * basdto.getBk_people());
      }

      switch (basdto.getBk_carch()) {
      case "도보" :
         basdto.setBk_carch("0");
         break;
      case "차량" :
         basdto.setBk_carch("1");
         break;
      }

      basdto.setBk_fk_id(mdto.getM_pk_id());
      basdto.setBk_fk_cnum(cnum);
      basdto.setBk_goods(rname);
      log.info("상품 이름 : " + basdto.getBk_goods());
      basdto.setBk_fk_tnum(Integer.parseInt(trnum));

      omap.put("basdto", basdto);
      omap.put("cate", cdto.getC_category());

      String cate = cdto.getC_category();

      switch(cate) {
      case "숙박" : 
         cate = "'숙박'";
         try {
            cate = URLEncoder.encode("'숙박'", "UTF-8");
         } catch (Exception e) {
            e.printStackTrace();
         }
         break;
      case "레저":
         cate = "'레저'";
         try {
            cate = URLEncoder.encode("'레저'", "UTF-8");
         } catch (Exception e) {
            e.printStackTrace();
         }
         break;
      case "식당":
         cate = "'식당'";
         try {
            cate = URLEncoder.encode("'식당'", "UTF-8");
         } catch (Exception e) {
            e.printStackTrace();
         }
         break;
      }



      try {
         madao.BasketInsert(omap);
         view = "redirect:Lodgment_List?cate=" + cate;
      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:Lodgment?cnum=" + cnum;
         msg = "담기 실패";
      }

      rttr.addFlashAttribute("msg", msg);

      return view;
   }

   @Transactional
   public Map<String, String> getAplan(BasketDto bdto, String todayDate) {
      log.info("MainService - getAplan()");
      Map<String, String> smap = new HashMap<String, String>();
      Map<String, Object> omap = new HashMap<String, Object>();
      String msg = null;
      
      if(todayDate.length()>1) {
         bdto.setBk_in(todayDate);
         bdto.setBk_out(todayDate);
      }
      
      CompanyDto cdto = cdao.getDto(bdto.getBk_fk_cnum());
      AactivityDto atdto = cdao.getAactivityDto(String.valueOf(bdto.getBk_fk_num()));
      log.info("종류 : " + cdto.getC_category());

      omap.put("at_rtch", atdto.getAt_rtch());
      omap.put("cate", cdto.getC_category());
      omap.put("basdto", bdto);

      try {
         madao.BasketInsert(omap);
         msg = "일정에 추가했습니다!";
      } catch (Exception e) {
         e.printStackTrace();
         msg = "일정 추가에 실패했습니다.";
      }

      smap.put("msg", msg);

      return smap;
   }

   public Map<String, String> getFplan(BasketDto bdto) {
      log.info("MainService - getFplan()");
      Map<String, String> smap = new HashMap<String, String>();
      Map<String, Object> omap = new HashMap<String, Object>();
      String msg = null;

      CompanyDto cdto = cdao.getDto(bdto.getBk_fk_cnum());

      omap.put("cate", cdto.getC_category());
      omap.put("basdto", bdto);

      try {
         madao.BasketInsert(omap);
         msg = "일정에 추가했습니다!";
      } catch (Exception e) {
         e.printStackTrace();
         msg = "일정 추가에 실패했습니다.";
      }

      smap.put("msg", msg);

      return smap;
   }

   @Transactional
   public Map<String, List<BasketDto>> lBasketDel(String bk_pk_num) {
      log.info("MainService - lBasketDel()");
      Map<String, List<BasketDto>> bmap = new HashMap<String, List<BasketDto>>();
      List<BasketDto> bList = new ArrayList<BasketDto>();
      TravelrouteDto tdto = (TravelrouteDto)session.getAttribute("tdto");
      
      try {
         madao.delBasket(bk_pk_num);
         bList = madao.selectLodCompany(String.valueOf(tdto.getTr_pk_num()));
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      
      return bmap;
   }
   
   @Transactional
   public Map<String, List<BasketDto>> aBasketDel(String bk_pk_num) {
      log.info("MainService - lBasketDel()");
      Map<String, List<BasketDto>> bmap = new HashMap<String, List<BasketDto>>();
      List<BasketDto> bList = new ArrayList<BasketDto>();
      TravelrouteDto tdto = (TravelrouteDto)session.getAttribute("tdto");
      
      try {
         madao.delBasket(bk_pk_num);
         bList = madao.selectActCompany(String.valueOf(tdto.getTr_pk_num()));
      } catch (Exception e) {
         e.printStackTrace();
      }
      
         
      
      return bmap;
   }
   
   @Transactional
   public Map<String, List<BasketDto>> fBasketDel(String bk_pk_num) {
      log.info("MainService - lBasketDel()");
      Map<String, List<BasketDto>> bmap = new HashMap<String, List<BasketDto>>();
      List<BasketDto> bList = new ArrayList<BasketDto>();
      TravelrouteDto tdto = (TravelrouteDto)session.getAttribute("tdto");
      
      try {
         madao.delBasket(bk_pk_num);
         bList = madao.selectFoodCompany(String.valueOf(tdto.getTr_pk_num()));
      } catch (Exception e) {
         e.printStackTrace();
      }
      
         
      
      return bmap;
   }
   @Transactional
   public Map<String, String> payUpdate(BasketDto bdto) {
      log.info("MainService - payUpdate()");
      Map<String, String> smap = new HashMap<String, String>();
      
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      
      String nowTime = now.format(format);
      bdto.setBk_paytime(nowTime);
      
      
      log.info("현 시간 : " + bdto.getBk_paytime());
      
      try {
         madao.payUpdate(bdto);
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return smap;
   }

   public Map<String, Object> optionCheck(List<String> arrList, String cate) {
      log.info("MainService - optionCheck()");
      Map<String, Object> omap = new HashMap<String, Object>();
      List<CompanyDto> cList = new ArrayList<CompanyDto>();
      ArrayList<String> sList = new ArrayList<>();
      
      switch(cate) {
      case "숙박" :
         LoptionDto lodto = new LoptionDto();
         lodto = loptionGet(arrList);
         
         sList = madao.loptionCheck(lodto);
         
         break;
      case "레저":
         AoptionDto aodto = new AoptionDto();
         aodto = aoptionGet(arrList);
         
         sList = madao.aoptionCheck(aodto);
         
         break;
      }
      
      for(int i = 0; i<sList.size(); i++) {
         CompanyDto cdto = cdao.getDto(sList.get(i));
         cList.add(cdto);
      }
      
      omap.put("cList", cList);
      log.info("리스트 숫자 : " + cList.size());
      return omap;
   }
   
   private LoptionDto loptionGet(List<String> arrList) {
      LoptionDto lpdto = new LoptionDto();
      
      for(int i = 0; i<arrList.size(); i++) {
         if(arrList.get(i).equals("l_cityview")) {
               lpdto.setL_cityview("1");
         } else if(arrList.get(i).equals("l_seeview")) {
               lpdto.setL_seeview("1");
         } else if(arrList.get(i).equals("l_skyview")) {
               lpdto.setL_skyview("1");
         } else if(arrList.get(i).equals("l_outdoor")) {
               lpdto.setL_outdoor("1");
         } else if(arrList.get(i).equals("l_petok")) {
               lpdto.setL_petok("1");
         } else if(arrList.get(i).equals("l_bbq")) {
               lpdto.setL_bbq("1");
         } else if(arrList.get(i).equals("l_cooking")) {
               lpdto.setL_cooking("1");
         } else if(arrList.get(i).equals("l_pool")) {
               lpdto.setL_pool("1");
         } else if(arrList.get(i).equals("l_wallpool")) {
               lpdto.setL_wallpool("1");
         } else if(arrList.get(i).equals("l_spa")) {
               lpdto.setL_spa("1");
         } else if(arrList.get(i).equals("l_garaoke")) {
               lpdto.setL_garaoke("1");
         } 
      }
      return lpdto;
   }
   
   private AoptionDto aoptionGet(List<String> arrList) {
      AoptionDto apdto = new AoptionDto();
      
      for(int i = 0; i<arrList.size(); i++) {
         
         if(arrList.get(i).equals("a_waterpark")) {
               apdto.setA_waterpark("1");
         } else if(arrList.get(i).equals("a_spa")){
               apdto.setA_spa("1");
         } else if(arrList.get(i).equals("a_surfing")){
               apdto.setA_surfing("1");
         } else if(arrList.get(i).equals("a_snowcooling")){
               apdto.setA_snowcooling("1");
         } else if(arrList.get(i).equals("a_rafting")){
               apdto.setA_rafting("1");
         } else if(arrList.get(i).equals("a_aquarium")){
               apdto.setA_aquarium("1");
         } else if(arrList.get(i).equals("a_flowers")){
               apdto.setA_flowers("1");
         } else if(arrList.get(i).equals("a_zoo")){
               apdto.setA_zoo("1");
         } else if(arrList.get(i).equals("a_shooting")){
               apdto.setA_shooting("1");
         } else if(arrList.get(i).equals("a_horse")){
               apdto.setA_horse("1");
         } else if(arrList.get(i).equals("a_paragliding")){
               apdto.setA_paragliding("1");
         } else if(arrList.get(i).equals("a_zipline")){
               apdto.setA_zipline("1");
         } else if(arrList.get(i).equals("a_bike")){
               apdto.setA_bike("1");
         } else if(arrList.get(i).equals("a_cart")){
               apdto.setA_cart("1");
         } else if(arrList.get(i).equals("a_atv")){
               apdto.setA_atv("1");
         } else if(arrList.get(i).equals("a_tourpass")){
               apdto.setA_tourpass("1");
         } else if(arrList.get(i).equals("a_yort")){
               apdto.setA_yort("1");
         } else if(arrList.get(i).equals("a_shipfishing")){
               apdto.setA_shipfishing("1");
         } else if(arrList.get(i).equals("a_cablecar")){
               apdto.setA_cablecar("1");
         } else if(arrList.get(i).equals("a_showpainting")){
               apdto.setA_showpainting("1");
         } else if(arrList.get(i).equals("a_fishinglounge")){
               apdto.setA_fishinglounge("1");
         } else if(arrList.get(i).equals("a_show")){
               apdto.setA_show("1");
         } 
      }
      return apdto;
   }
   
   @Transactional
   public String reviewInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("MainService - reviewInsert()");
      String view = null;
      String msg = null;
      
      ReviewDto rdto = new ReviewDto();
      MemberDto mdto = (MemberDto)session.getAttribute("user");
      
      rdto.setRv_fk_cnum(multi.getParameter("rv_fk_cnum"));
      rdto.setRv_contents(multi.getParameter("rv_contents"));
      rdto.setRv_star(Integer.parseInt(multi.getParameter("rv_star")));
      rdto.setRv_fk_id(mdto.getM_pk_id());
      log.info("qufwja : " + rdto.getRv_star());
      rdto.setFilecheck((String)multi.getParameter("filecheck"));
      
      try {
         madao.reviewInsert(rdto);
         if(rdto.getFilecheck().equals("1")) {
            log.info("파일 체크 : " + rdto.getFilecheck());
            cfileupload(multi,rdto.getRv_pk_num());
         }
         
         view = "redirect:Lodgment?cnum=" + rdto.getRv_fk_cnum();
         msg = "리뷰작성이 완료되었습니다!";
      } catch (Exception e) {
         e.printStackTrace();
         
         view = "redirect:Lodgment?cnum=" + rdto.getRv_fk_cnum();
         msg = "리뷰작성이 실패되었습니다. 누락된 사항이 없는지 확인해주세요";
      }
      
      rttr.addFlashAttribute("msg", msg);
      
      return view;
   }
   
   @Transactional
   private String cfileupload(MultipartHttpServletRequest multi ,int rv_pk_num) throws IllegalStateException, IOException {
      String sysname = null;

      String path = multi.getRealPath("/upload/");

      File upFolder = new File(path);
      if (!upFolder.isDirectory()) {
         upFolder.mkdir();
      }
      
      Map<String, String> fmap = null;

      Iterator<String> files = multi.getFileNames();

      while (files.hasNext()) {

         String fn = files.next();
         List<MultipartFile> fList = null;
         if (fn.equals("r_img")) {
            // 대문이미지 - update

            fmap = new HashMap<String, String>();

            fList = multi.getFiles(fn);
            int i = 0;

            MultipartFile ui = fList.get(i);

            if (ui.isEmpty()) {
               continue;
            }

            String oriname = ui.getOriginalFilename();
            sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
            
            fmap.put("rv_pk_num", String.valueOf(rv_pk_num));
            fmap.put("sysname", sysname);

            File fff = new File(path + sysname);
            ui.transferTo(fff);

            madao.ReviewR_imgU(fmap);

         } 
      }
      return sysname;
   }

   public ModelAndView homeList() {
	   log.info("homeqaList()");
		mv = new ModelAndView();

		List<ComplaintDto> pldto = madao.homeqaListSelect();
		List<NoticeDto> ndto = madao.homenoticeListSelect();
		List<EventDto> edto = madao.homeeventListSelect();
		List<CompanyDto> cdto = madao.homecompanyList();
		List<CompanyDto> adto = madao.homeactiveList();
		List<SpotDto> bigsdto = madao.spotbigList();
		List<SpotDto> sdto = madao.spotList();
		List<CompanyDto> mdl = madao.homemdcompanyList();
		List<CompanyDto> mda = madao.homemdactiveList();
	
		mv.addObject("mdl", mdl);
		mv.addObject("mda", mda);
		mv.addObject("bigsdto", bigsdto);
		mv.addObject("sdto", sdto);
		mv.addObject("adto", adto);
		mv.addObject("cdto", cdto);
		mv.addObject("edto", edto);
		mv.addObject("ndto", ndto);
		mv.addObject("pldto", pldto);
		
		mv.setViewName("home");
	return mv;
   }

	public Map<String, String> favoritInsert(FavoritesDto fav) {
		log.info("favoritInsert()");
		Map<String, String> rmap = new HashMap<>();
	
		try {
			madao.favoritesInsert(fav);
			rmap.put("res", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			rmap.put("res", "fail");
		}
	
		return rmap;
	}
	
	
}