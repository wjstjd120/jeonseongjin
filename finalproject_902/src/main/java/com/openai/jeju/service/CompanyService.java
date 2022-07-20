package com.openai.jeju.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.openai.jeju.dao.MemberDao;
import com.openai.jeju.dto.AactivityDto;
import com.openai.jeju.dto.AimgDto;
import com.openai.jeju.dto.AoptionDto;
import com.openai.jeju.dto.CimgDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.LoptionDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.ReviewDto;
import com.openai.jeju.dto.RoomDto;
import com.openai.jeju.dto.RoomImgDto;

import lombok.extern.java.Log;

@Service
@Log
public class CompanyService {

   @Autowired
   private HttpSession session;

   @Autowired
   private CompanyDao cdao;
   @Autowired
   private MemberDao mdao;

   private ModelAndView mv;

   @Transactional
   public String companyInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyServcie - companyInsert()");
      CompanyDto dto = new CompanyDto();

      String view = null;
      String msg = null;
      String sysname = null;
      dto.setC_pk_cnum(multi.getParameter("c_pk_cnum"));
      dto.setC_fk_id(multi.getParameter("c_fk_id"));
      dto.setC_name(multi.getParameter("c_name"));
      dto.setC_phone(multi.getParameter("c_phone"));
      dto.setC_category(multi.getParameter("c_category"));
      dto.setC_addr(multi.getParameter("c_addr"));
      dto.setC_type(multi.getParameter("c_type"));
      
      log.info("타입! : " + dto.getC_type());
      
      try {
         cdao.CompanyInsert(dto);
         
         view = "redirect:/";
         msg = "신청완료";
      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:companyFrm";
         msg = "업체 신청 실패";
      }
      
      try {
         cfileupload(multi, dto.getC_pk_cnum());
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      
      switch(dto.getC_category()) {
      case "숙박" :
         try {
            cdao.loptionInsert(dto.getC_pk_cnum());
         } catch (Exception e) {
            // TODO: handle exception
         }
         break;
      case "레저" :
         try {
            cdao.aoptionInsert(dto.getC_pk_cnum());
         } catch (Exception e) {
            // TODO: handle exception
         }
         break;
      }
      
      rttr.addFlashAttribute("msg", msg);

      return view;
   }

   private String cfileupload(MultipartHttpServletRequest multi ,String c_pk_cnum) throws IllegalStateException, IOException {
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
         if (fn.equals("c_img")) {
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

            fmap.put("c_pk_cnum", c_pk_cnum);
            fmap.put("sysname", sysname);

            File fff = new File(path + sysname);
            ui.transferTo(fff);

            cdao.CompanyC_imgU(fmap);

         } else {
            fmap = new HashMap<String, String>();

            fList = multi.getFiles(fn);
            fmap.put("c_pk_cnum", c_pk_cnum);
            for (int i = 0; i < fList.size(); i++) {
               MultipartFile mf = fList.get(i);

               // 파일명 추출
               String oriname = mf.getOriginalFilename();

               // 변경할 이름 생성
               sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

               fmap.put("oriname", oriname);
               fmap.put("sysname", sysname);

               // upload 폴더 파일 저장
               File ff = new File(path + sysname);
               mf.transferTo(ff);

               // DB에 파일정보 저장
               cdao.CimgInsert(fmap);
            }
         }
      }
      return sysname;
   }

   public ModelAndView getCompanyList(RedirectAttributes rttr) {
      log.info("CompanyService - getCompanyList()");
      String str = null;
      mv = new ModelAndView();

      mv.setViewName("company2");

      List<CompanyDto> cList = new ArrayList<>();

      MemberDto mdto = (MemberDto) session.getAttribute("user");

      cList = cdao.getList(mdto.getM_pk_id());
      int count = cdao.getListCount(mdto.getM_pk_id());

      CompanyDto dto = new CompanyDto();

      for (int i = 0; i < cList.size(); i++) {
         switch (cList.get(i).getC_condition()) {
         case "0":// 영업상태 false
            str = "영업 준비중";
            dto = cList.get(i);
            dto.setC_condition(str);
            cList.set(i, dto);
            break;
         case "1": // 영업상태 true
            str = "영업 중";
            dto = cList.get(i);
            dto.setC_condition(str);
            cList.set(i, dto);
            break;
         }
      }

      mv.addObject("cList", cList);
      mv.addObject("Lcount", count);

      return mv;
   }

   public Map<String, CompanyDto> getDto(String data) {
      log.info("CompanyService - getDto()");

      Map<String, CompanyDto> fmap = new HashMap<String, CompanyDto>();

      CompanyDto cdto = cdao.getDto(data);

      fmap.put("cdto", cdto);

      return fmap;
   }

   public Map<String, List<RoomDto>> getRoomList(String data) {
      log.info("CompanyService - getRoomList()");

      Map<String, List<RoomDto>> fmap = new HashMap<String, List<RoomDto>>();

      List<RoomDto> rList = cdao.getRoomList(data);

      fmap.put("rList", rList);

      return fmap;
   }

//   @Transactional
//   public Map<String, String> roomInsert(MultipartHttpServletRequest multi){
//      log.info("CompanyService - rommInsert()");
//      Map<String, String> remap = new HashMap<String, String>();
//      
//      String result = null;
//      
//      String sysname = null;
//      
//      RoomDto rdto = new RoomDto();
//      
//      rdto.setR_fk_cnum(multi.getParameter("r_fk_cnum"));
//      rdto.setR_name(multi.getParameter("r_name"));
//      rdto.setR_contents(multi.getParameter("r_contents"));
//      rdto.setR_price(Integer.parseInt(multi.getParameter("r_price")));
//      rdto.setR_in(multi.getParameter("r_in"));
//      rdto.setR_out(multi.getParameter("r_out"));
//      
//      
//      
//      try {
//         cdao.roomInsert(rdto);
//         log.info("넘어온 번호 : " + rdto.getR_pk_num());
//         fileupload(multi, rdto.getR_pk_num());
//         result = "1";
//      }catch(Exception e) {
//         e.printStackTrace();
//         result = "0";
//      }
//      log.info("사업자 번호 : " + rdto.getR_fk_cnum());
//      log.info("방 이름 : " + rdto.getR_name());
//      log.info("주의사항 및 안내 : " + rdto.getR_contents());
//      log.info("가격 : " + rdto.getR_price());
//      log.info("입실 시간 : " + rdto.getR_in());
//      log.info("퇴실 시간 : " + rdto.getR_out());
//      
//      remap.put("result", result);
//      
//      return remap;
//   }

   @Transactional
   public String roomInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyService - rommInsert()");
      String view = null;
      String msg = null;

      RoomDto rdto = new RoomDto();

      rdto.setR_fk_cnum(multi.getParameter("r_fk_cnum"));
      rdto.setR_name(multi.getParameter("r_name"));
      rdto.setR_contents(multi.getParameter("r_contents"));
      rdto.setR_price(Integer.parseInt(multi.getParameter("r_price")));
      rdto.setR_in(multi.getParameter("r_in"));
      rdto.setR_out(multi.getParameter("r_out"));

      try {
         cdao.roomInsert(rdto);
         log.info("넘어온 번호 : " + rdto.getR_pk_num());
         fileupload(multi, rdto.getR_pk_num());
         view = "redirect:company_contents?cnum=" + rdto.getR_fk_cnum();
         msg = "방이 추가되었습니다!";

      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:addGoodsFrm?" + rdto.getR_fk_cnum();
         msg = "방추가에 실패했습니다. 관리자에게 문의 해주세요";
      }
      log.info("사업자 번호 : " + rdto.getR_fk_cnum());
      log.info("방 이름 : " + rdto.getR_name());
      log.info("주의사항 및 안내 : " + rdto.getR_contents());
      log.info("가격 : " + rdto.getR_price());
      log.info("입실 시간 : " + rdto.getR_in());
      log.info("퇴실 시간 : " + rdto.getR_out());

      rttr.addFlashAttribute("msg", msg);

      return view;
   }

   @Transactional
   private String fileupload(MultipartHttpServletRequest multi, int r_pk_num)
         throws IllegalStateException, IOException {
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

            fmap.put("r_pk_num", String.valueOf(r_pk_num));
            fmap.put("sysname", sysname);

            File fff = new File(path + sysname);
            ui.transferTo(fff);

            cdao.roomImgUpdate(fmap);

         } else {
            fmap = new HashMap<String, String>();

            fList = multi.getFiles(fn);
            fmap.put("r_pk_num", String.valueOf(r_pk_num));
            for (int i = 0; i < fList.size(); i++) {
               MultipartFile mf = fList.get(i);

               // 파일명 추출
               String oriname = mf.getOriginalFilename();

               // 변경할 이름 생성
               sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

               fmap.put("oriname", oriname);
               fmap.put("sysname", sysname);

               // upload 폴더 파일 저장
               File ff = new File(path + sysname);
               mf.transferTo(ff);

               // DB에 파일정보 저장
               cdao.rImgInsert(fmap);

            }

         }
      }
      return sysname;
   }

   public Map<String, List<RoomImgDto>> getRimgList(String r_pk_num) {
      log.info("CompanyService - getRimgList");

      List<RoomImgDto> riList = cdao.getRimgList(r_pk_num);

      Map<String, List<RoomImgDto>> rimap = new HashMap<String, List<RoomImgDto>>();

      rimap.put("riList", riList);

      return rimap;
   }

   public ModelAndView c_contents(String cnum) {
      log.info("CompanyService - c_contents()");
      String str = null;
      mv = new ModelAndView();

      CompanyDto cdto = cdao.getDto(cnum);
      ArrayList<String> option = null;
      
      
      switch (cdto.getC_category()) {
      case "숙박":
         List<RoomDto> rList = cdao.getRoomList(cnum);
         LoptionDto lodto = cdao.loptionSelect(cnum);
         option = loptionGet(lodto);
         
         mv.addObject("rList", rList);
         break;
      case "레저":
         List<AactivityDto> aList = cdao.getAactivityList(cnum);
         AoptionDto aodto = cdao.aoptionSelect(cnum);
         option = aoptionGet(aodto);
         
         mv.addObject("aList", aList);
         
         break;
      }

      switch (cdto.getC_condition()) {
      case "0":// 영업상태 false
         str = "영업 준비중";
         cdto.setC_condition(str);
         break;
      case "1": // 영업상태 true
         str = "영업 중";
         cdto.setC_condition(str);
         break;
      }

      switch (cdto.getC_check()) {
      case "0":// 영업상태 false
         str = "미승인";
         cdto.setC_check(str);
         break;
      case "1": // 영업상태 true
         str = "승인";
         cdto.setC_check(str);
         break;
      }
      
      
      mv.addObject("cdto", cdto);
      mv.addObject("option", option);
      mv.setViewName("company_contents");

      return mv;
   }

   public ModelAndView addGoodsFrm(String cnum) {
      log.info("CompanyService - addGoodsFrm()");

      CompanyDto cdto = cdao.getDto(cnum);

      mv.addObject("cdto", cdto);
      mv.setViewName("GoodsAddFrm");

      return mv;
   }

   @Transactional
   public String aGoodsInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyService - aGoodsInsert()");

      String view = null;
      String msg = null;

      AactivityDto rdto = new AactivityDto();

      rdto.setAt_fk_cnum(multi.getParameter("at_fk_cnum"));
      rdto.setAt_name(multi.getParameter("at_name"));
      rdto.setAt_contents(multi.getParameter("at_contents"));
      rdto.setAt_rtch(multi.getParameter("at_rtch"));

      rdto.setAt_price(Integer.parseInt(multi.getParameter("at_price")));

      log.info("at_rtch : " + rdto.getAt_rtch());

      rdto.setAt_in(multi.getParameter("at_in"));
      rdto.setAt_out(multi.getParameter("at_out"));

      if (rdto.getAt_rtch().equals("0")) {
         rdto.setAt_in("0");
         rdto.setAt_out("0");
      }

      log.info("사업자 번호 : " + rdto.getAt_fk_cnum());
      log.info("방 이름 : " + rdto.getAt_name());
      log.info("주의사항 및 안내 : " + rdto.getAt_contents());
      log.info("가격 : " + rdto.getAt_price());
      log.info("입실 시간 : " + rdto.getAt_in());
      log.info("퇴실 시간 : " + rdto.getAt_out());
      log.info("예약 여부 : " + rdto.getAt_rtch());

      try {
         cdao.AactivityInsert(rdto);
         log.info("넘어온 번호 : " + rdto.getAt_pk_num());
         afileupload(multi, rdto.getAt_pk_num());
         view = "redirect:company_contents?cnum=" + rdto.getAt_fk_cnum();
         msg = "상품이 추가되었습니다!";

      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:addGoodsFrm?cnum=" + rdto.getAt_fk_cnum();
         msg = "상품추가에 실패했습니다. 관리자에게 문의 해주세요";
      }
      log.info("사업자 번호 : " + rdto.getAt_fk_cnum());
      log.info("방 이름 : " + rdto.getAt_name());
      log.info("주의사항 및 안내 : " + rdto.getAt_contents());
      log.info("가격 : " + rdto.getAt_price());
      log.info("입실 시간 : " + rdto.getAt_in());
      log.info("퇴실 시간 : " + rdto.getAt_out());
      log.info("예약 여부2 : " + rdto.getAt_rtch());

      rttr.addFlashAttribute("msg", msg);

      return view;
   }

   @Transactional
   private String afileupload(MultipartHttpServletRequest multi, int r_pk_num)
         throws IllegalStateException, IOException {
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
         if (fn.equals("at_img")) {
            // 대문이미지 - update

            fmap = new HashMap<String, String>();

            fList = multi.getFiles(fn);
            int i = 0;
            MultipartFile ui = fList.get(i);

            if (ui.isEmpty()) {
               continue;
            }else {
               log.info("안비었음ㅋ");
            }

            String oriname = ui.getOriginalFilename();
            sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

            fmap.put("r_pk_num", String.valueOf(r_pk_num));
            fmap.put("sysname", sysname);

            File fff = new File(path + sysname);
            ui.transferTo(fff);

            cdao.aactivityImgUpdate(fmap);

         } else {
            fmap = new HashMap<String, String>();

            fList = multi.getFiles(fn);
            fmap.put("r_pk_num", String.valueOf(r_pk_num));
            for (int i = 0; i < fList.size(); i++) {
               MultipartFile mf = fList.get(i);

               // 파일명 추출
               String oriname = mf.getOriginalFilename();

               // 변경할 이름 생성
               sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

               fmap.put("oriname", oriname);
               fmap.put("sysname", sysname);

               // upload 폴더 파일 저장
               File ff = new File(path + sysname);
               mf.transferTo(ff);

               // DB에 파일정보 저장
               cdao.aImgInsert(fmap);

            }

         }
      }
      return sysname;
   }

   public ModelAndView goupdateFrm(String c_category, String pknum) {
      log.info("CompanyController - goupdateFrm()");
      mv = new ModelAndView();
      // 1. 상품 정보 - clear
      // 2. 사업자 정보
      // 3. 방 이미지 정보
      CompanyDto cdto = null;

      switch (c_category) {
      case "숙박":
         RoomDto rdto = cdao.getRoomDto(pknum);
         cdto = cdao.getDto(rdto.getR_fk_cnum());
         List<RoomImgDto> rimgList = cdao.getRimgList(String.valueOf(rdto.getR_pk_num()));

         mv.addObject("rdto", rdto);
         mv.addObject("cdto", cdto);
         mv.addObject("rimgList", rimgList);
         break;
      case "레저":
         AactivityDto adto = cdao.getAactivityDto(pknum);
         cdto = cdao.getDto(adto.getAt_fk_cnum());
         List<AimgDto> aimgList = cdao.getAimgList(String.valueOf(adto.getAt_pk_num()));

         mv.addObject("adto", adto);
         mv.addObject("cdto", cdto);
         mv.addObject("aimgList", aimgList);
         break;
      }

      mv.setViewName("updateAddGoods");

      return mv;
   }

   @Transactional
   public Map<String, List<RoomImgDto>> delRimg(String sysname, String rnum) {
      log.info("CompanyServcie - delRimg()");
      Map<String, List<RoomImgDto>> rmap = new HashMap<>();

      String path = session.getServletContext().getRealPath("/");

      path += "/upload/" + sysname;

      log.info("경로 : " + path);

      try {
         cdao.solDelrImg(sysname);
         File file = new File(path);
         if (file.exists()) {// 파일이 있을 경우
            if (file.delete()) {
               List<RoomImgDto> rList = cdao.getRimgList(rnum);
               rmap.put("rList", rList);

            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         rmap = null;
      }

      return rmap;
   }
   
   @Transactional
   public String roomUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyService - roomUpdate()");
      String view = null;
      String msg = null;

      String sysname = (String) multi.getParameter("sysname");

      RoomDto rdto = new RoomDto();
      
      
      rdto.setR_pk_num(Integer.parseInt(multi.getParameter("r_pk_num")));
      rdto.setR_fk_cnum(multi.getParameter("r_fk_cnum"));
      rdto.setR_name(multi.getParameter("r_name"));
      rdto.setR_contents(multi.getParameter("r_contents"));
      rdto.setR_price(Integer.parseInt(multi.getParameter("r_price")));
      rdto.setR_in(multi.getParameter("r_in"));
      rdto.setR_out(multi.getParameter("r_out"));
      rdto.setR_hidden(multi.getParameter("r_hidden"));

      log.info("PK 넘버 : " + rdto.getR_pk_num());
      log.info("FK 넘버 : " + rdto.getR_fk_cnum());
      log.info("방 이름 : " + rdto.getR_name());
      log.info("방 컨텐츠 : " + rdto.getR_contents());
      log.info("방 가격 : " + rdto.getR_price());
      log.info("입실시간 : " + rdto.getR_in());
      log.info("퇴실시간 : " + rdto.getR_out());

      CompanyDto cdto = cdao.getDto(rdto.getR_fk_cnum());

      String filech = multi.getParameter("filecheck");
      String multifilech = multi.getParameter("multifilecheck");
      log.info("과연 숫자는 : " + multifilech);

      if (filech.equals("1")) {
         String path = session.getServletContext().getRealPath("/");

         path += "/upload/" + sysname;
         log.info("파일체크 on");
         File file = new File(path);
         file.delete();
         try {
            fileupload(multi, rdto.getR_pk_num());

         } catch (Exception e) {
            e.printStackTrace();
         }
      }else if (multifilech.equals("1") && filech.equals("0")) {
         try {
            fileupload(multi, rdto.getR_pk_num());
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      String cate = null;
      try {
         cate = URLEncoder.encode(cdto.getC_category(), "UTF-8");
      } catch (UnsupportedEncodingException e1) {
         e1.printStackTrace();
      }

      // Room Img table 업데이트 완료
      try {
         cdao.roomUpdate(rdto);
         view = "redirect:company_contents?cnum=" + rdto.getR_fk_cnum();
         msg = "방 수정이 완료되었습니다!";
      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:updateAddGoods?c_category=" + cate + "&pknum=" + rdto.getR_pk_num();
         msg = "방 수정에 실패했습니다";
      }

      return view;
   }

   @Transactional
   public Map<String, List<AimgDto>> delAimg(String sysname, String atpnum) {
      log.info("CompanyServcie - delRimg()");
      Map<String, List<AimgDto>> rmap = new HashMap<>();

      String path = session.getServletContext().getRealPath("/");

      path += "/upload/" + sysname;

      log.info("경로 : " + path);

      try {
         cdao.solDelaImg(sysname);
         File file = new File(path);
         if (file.exists()) {// 파일이 있을 경우
            if (file.delete()) {
               List<AimgDto> aList = cdao.getAimgList(atpnum);
               rmap.put("aList", aList);

            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         rmap = null;
      }

      return rmap;

   }

   @Transactional
   public String aactivityUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyService - aactivityUpdate()");
      String view = null;
      String msg = null;

      String sysname = (String) multi.getParameter("asysname");
      log.info("sysname : " + sysname);

      AactivityDto adto = new AactivityDto();

      adto.setAt_pk_num(Integer.parseInt(multi.getParameter("at_pk_num")));
      adto.setAt_fk_cnum(multi.getParameter("at_fk_cnum"));
      adto.setAt_name(multi.getParameter("at_name"));
      adto.setAt_contents(multi.getParameter("at_contents"));
      adto.setAt_price(Integer.parseInt(multi.getParameter("at_price")));
      adto.setAt_rtch(multi.getParameter("at_rtch"));
      adto.setAt_in(multi.getParameter("at_in"));
      adto.setAt_out(multi.getParameter("at_out"));
      adto.setAt_hidden(multi.getParameter("at_hidden"));

      log.info("PK 넘버 : " + adto.getAt_pk_num());
      log.info("FK 넘버 : " + adto.getAt_fk_cnum());
      log.info("상품 이름 : " + adto.getAt_name());
      log.info("상품 컨텐츠 : " + adto.getAt_contents());
      log.info("상품 가격 : " + adto.getAt_price());
      log.info("예약 여부 : " + adto.getAt_rtch());
      log.info("입실시간 : " + adto.getAt_in());
      log.info("퇴실시간 : " + adto.getAt_out());

      CompanyDto cdto = cdao.getDto(adto.getAt_fk_cnum());

      String filech = multi.getParameter("filecheck");
      String multifilech = multi.getParameter("multifilecheck");

      log.info("단독 파일 : " + filech);
      log.info("다중 파일 : " + multifilech);

      if (filech.equals("1")) {
         String path = session.getServletContext().getRealPath("/");

         path += "/upload/" + sysname;
         File file = new File(path);
         file.delete();
         try {
            afileupload(multi, adto.getAt_pk_num());

         } catch (Exception e) {
            e.printStackTrace();
         }
      }else if (multifilech.equals("1") && filech.equals("0")) {
         try {
            afileupload(multi, adto.getAt_pk_num());
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      
      String cate = null;
      try {
         cate = URLEncoder.encode(cdto.getC_category(), "UTF-8");
      } catch (UnsupportedEncodingException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }

      try {
         cdao.aactivityUpdate(adto);
         view = "redirect:company_contents?cnum=" + adto.getAt_fk_cnum();
         msg = "상품 수정이 완료되었습니다!";
      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:updateAddGoods?c_category=" + cate + "&pknum=" + adto.getAt_pk_num();
         msg = "상품 수정에 실패했습니다";
      }

      return view;
   }
   
   @Transactional
   public Map<String, String> delGoods(String cate, String pknum){
      log.info("CompanyService - delGoods()");
      String path = null;
      String msg = null;
      String view = null;
      Map<String, String> msvi = new HashMap<String, String>();
      
      File file = null;
      
      switch(cate) {
      case "숙박" :
         List<RoomImgDto> riList = cdao.getRimgList(pknum);
         RoomDto rdto = cdao.getRoomDto(pknum);
         
         path = session.getServletContext().getRealPath("/");
         path += "/upload/" + rdto.getR_img();
         
         file = new File(path);
         file.delete();
         
         for(int i=0; i<riList.size(); i++) {
            path = session.getServletContext().getRealPath("/");
            path += "/upload/" + riList.get(i).getRi_sysname();
            file = new File(path);
            file.delete();
         }
         try {
            cdao.del_fk_delRImg(pknum);
            cdao.delPK_Room(pknum);
            msg = "삭제 완료 되었습니다!";
         } catch (Exception e) {
            e.printStackTrace();
            msg = "삭제가 실패 되었습니다. 관리자에게 문의해주세요.";
         }
         msvi.put("msg", msg);
         
         
         break;
      case "레저" :
         List<AimgDto>aiList = cdao.getAimgList(pknum);
         AactivityDto adto = cdao.getAactivityDto(pknum);
         
         path = session.getServletContext().getRealPath("/");
         path += "/upload/" + adto.getAt_img();
         
         file = new File(path);
         file.delete();
         
         for(int i=0; i<aiList.size(); i++) {
            path = session.getServletContext().getRealPath("/");
            path += "/upload/" + aiList.get(i).getAi_sysname();
            file = new File(path);
            file.delete();
         }
         try {
            cdao.del_fk_delAImg(pknum);
            cdao.delPK_Aactivity(pknum);
            msg = "삭제 완료 되었습니다!";
         } catch (Exception e) {
            e.printStackTrace();
            msg = "삭제가 실패 되었습니다. 관리자에게 문의해주세요.";
         }
         msvi.put("msg", msg);
         break;
      }
      return msvi;
   }
   
   public Map<String, Object> searchCompany(ListDto ldto){
      log.info("CompanyService - searchCompany()");
      String str = null;
      Map<String, Object> cmap = new HashMap<String, Object>();
      List<CompanyDto> cList = new ArrayList<CompanyDto>();
      CompanyDto dto = null;
      
      MemberDto mdto = (MemberDto)session.getAttribute("user");
      String id = mdto.getM_pk_id();
      
      ldto.setId(id);
      
      cList = cdao.searchCompany(ldto);
      
      for (int i = 0; i < cList.size(); i++) {
         switch (cList.get(i).getC_condition()) {
         case "0":// 영업상태 false
            str = "영업 준비중";
            dto = cList.get(i);
            dto.setC_condition(str);
            cList.set(i, dto);
            break;
         case "1": // 영업상태 true
            str = "영업 중";
            dto = cList.get(i);
            dto.setC_condition(str);
            cList.set(i, dto);
            break;
         }
      }
      
      int count = cList.size();
      
      log.info("데이터 개수 : " + count);
      cmap.put("cList", cList);
      cmap.put("count", count);
      
      return cmap;
   }
   
   @Transactional
   public String updateCondition(String cnum, RedirectAttributes rttr) {
      log.info("CompanyService - updateCondition()");
      String view = null;
      String str = null;
      String msg = null;
      
      CompanyDto cdto = cdao.getDto(cnum);
      
      if(cdto.getC_condition().equals("0")) {
         cdto.setC_condition("1");
      }else if(cdto.getC_condition().equals("1")) {
         cdto.setC_condition("0");
      }
      
      try {
         cdao.updateCondition(cdto);
         msg = "업데이트를 성공했습니다.";
      } catch (Exception e) {
         msg = "업데이트를 실패했습니다. 관리자에게 문의해주세요.";
      }
      
      rttr.addFlashAttribute("msg",msg);
      view = "redirect:company";
      
      return view;
   }
   
   @Transactional
   public String hidChancheck(String category, String pknum, RedirectAttributes rttr) {
      log.info("CompanyService - hidChancheck()");
      String view = null;
      String msg = null;
      
      switch (category) {
      case "숙박":
         RoomDto rdto = cdao.getRoomDto(pknum);
         
         if(rdto.getR_hidden().equals("1")) {
            rdto.setR_hidden("0");
         }else if(rdto.getR_hidden().equals("0")) {
            rdto.setR_hidden("1");
         }
         
         try {
            cdao.roomUpdate(rdto);
            msg = "업데이트에 성공했습니다!";
            view = "redirect:company_contents?cnum="+rdto.getR_fk_cnum();
         } catch (Exception e) {
            msg = "업데이트에 실패해습니다. 관리자에게 문의해주세요.";
         }
         
            
         break;
      case "레저":
         AactivityDto adto = cdao.getAactivityDto(pknum);
         
         if(adto.getAt_hidden().equals("1")) {
            adto.setAt_hidden("0");
         }else if(adto.getAt_hidden().equals("0")) {
            adto.setAt_hidden("1");
         }
         
         try {
            cdao.aactivityUpdate(adto);
            msg = "업데이트에 성공했습니다!";
            view = "redirect:company_contents?cnum="+adto.getAt_fk_cnum();
         } catch (Exception e) {
            msg = "업데이트에 실패해습니다. 관리자에게 문의해주세요.";
         }
         break;
      }
      
      rttr.addFlashAttribute("msg", msg);
      
      return view;
   }
   
   @Transactional
   public String companyDelete(String cnum, RedirectAttributes rttr) {
      log.info("CompanyService - companyDelete()");
      String view = null;
      String msg = null;
      String path = null;
      File file = null;
      
      CompanyDto cdto = cdao.getDto(cnum);
      
      
      
      switch(cdto.getC_category()) {
      case "숙박" :
         List<RoomDto> rList = cdao.getRoomList(cnum);
         
         for(int i = 0; i<rList.size(); i++) {
            RoomDto rdto = rList.get(i);
            delGoods(cdto.getC_category(), String.valueOf(rdto.getR_pk_num()));
         }
         
         path = session.getServletContext().getRealPath("/");
         path += "/upload/" + cdto.getC_img();
         
         file = new File(path);
         file.delete();
         
         try {
            cdao.loptionDelete(cnum);
         } catch (Exception e) {
            // TODO: handle exception
         }
         
         
         break;
      case "레저" :
         
         List<AactivityDto> aList = cdao.getAactivityList(cnum);
         
         for(int i = 0; i<aList.size(); i++) {
            AactivityDto adto = aList.get(i);
            delGoods(cdto.getC_category(), String.valueOf(adto.getAt_pk_num()));
         }
         
         
         try {
            cdao.aoptionDelete(cnum);
         } catch (Exception e) {
            // TODO: handle exception
         }
         break;
      
      }
      
      List<CimgDto> ciList = cdao.getCimgList(cnum);
      
      for(int i = 0; i < ciList.size(); i++) {
      path = session.getServletContext().getRealPath("/");
      path += "/upload/" + ciList.get(i).getCi_sysname();
      
      file = new File(path);
      file.delete();
      }
      
      try {
         cdao.reviewDelete(cnum);
         cdao.deleteCimg(cnum);
         cdao.deleteCompany(cnum);
         view = "redirect:company";
         msg = "업체가 삭제되었습니다.";
      } catch (Exception e) {
         view = "redirect:company_contents?cnum=" + cdto.getC_pk_cnum();
         msg = "업체삭제에 실패했습니다. 관리자에게 문의해주세요";
      }
      
      rttr.addFlashAttribute("msg", msg);
      
      return view;
   }
   
   @Transactional
   public String solMemChange(String m_pk_id, RedirectAttributes rttr) {
      log.info("CompanyService - solMemChange()");
      String view = null;
      String msg = null;
      String m_category ="개인";
      
      MemberDto mdto = (MemberDto)session.getAttribute("user");
      mdto.setM_category(m_category);
      
      try {
         cdao.companyOff(m_pk_id);
         cdao.solMemChange(mdto);
         mdto = mdao.memberSelect(m_pk_id);
         session.setAttribute("user", mdto);
         view = "redirect:/";
         msg = "개인회원 변경이 완료 되었습니다. 고생하셨습니다.";
      } catch (Exception e) {
         view = "redirect:companyOutFrm";
         msg = "개인회원 변경에 실패했습니다ㅋㅋ";
      }
      
      rttr.addFlashAttribute("msg", msg);
      
      return view;
   }
   
   @Transactional
   public String memdelete(String m_pk_id, RedirectAttributes rttr) {
      log.info("CompanyService - memdelete()");
      String view = null;
      String msg = null;
      
      List<CompanyDto> cList = cdao.getList(m_pk_id);
      
      try {
         
         if(cList != null) {
            for(int i = 0; i<cList.size(); i++) {
               companyDelete(cList.get(i).getC_pk_cnum(), rttr);
            }
         }
         
         cdao.reviewMemberDelete(m_pk_id);
         
         mdao.memberDelete(m_pk_id);
         session.removeAttribute("user");
         
         msg = "회원이 탈퇴 되었습니다.";
      } catch (Exception e) {
         e.printStackTrace();
         msg = "회원 탈퇴에 실패했습니다. 관리자에게 문의해주세요.";
      }
      
      view = "redirect:/";
      
      rttr.addFlashAttribute("msg",msg);
      
      
      return view;
   }
   
   public Map<String, String> cnumDeduplication(String cnum){
      log.info("CompanyService - cnumDeduplication()");
      Map<String, String> smap = new HashMap<String, String>();
      String msg = null;
      
      int res = cdao.cnumDeduplication(cnum);
      
      if(res >= 1) {
         msg = "no";
      }else {
         msg = "yes";
      }
      
      log.info("msg : " + msg);
      
      smap.put("msg", msg);
      
      return smap;
   }
   
   public Map<String, List<ReviewDto>> getReviewList(String cnum){
      log.info("CompanyService - getReviewList()");
      
      Map<String, List<ReviewDto>> rvmap = new HashMap<String, List<ReviewDto>>();
      
      List<ReviewDto> rvList = cdao.getReviewList(cnum);
      
      rvmap.put("rvList", rvList);
      
      return rvmap;
   }
   
   @Transactional
   public Map<String, String> reviewAnswer(ReviewDto dto) {
      log.info("CompanyService - reviewAnswer()");
      Map<String, String> smap = new HashMap<String, String>();
      
      
      
      try {
         cdao.reviewAnswer(dto);
      } catch (Exception e) {
         // TODO: handle exception
      }
      
      return smap;
   }

   public Map<String, List<CimgDto>> getCimgList(String cnum) {
      log.info("CompanyService - getCimgList()");
      Map<String, List<CimgDto>> cimap = new HashMap<String, List<CimgDto>>();
      
      List<CimgDto> ciList = cdao.getCimgList(cnum);
      
      cimap.put("ciList", ciList);
      
      return cimap;
   }

   public ModelAndView updateCompanyFrm(String cnum) {
      log.info("CompanyService - updateCompanyFrm()");
      mv = new ModelAndView();
      
      CompanyDto cdto = cdao.getDto(cnum);
      List<CimgDto> ciList = cdao.getCimgList(cnum);
      
      switch(cdto.getC_category()) {
      case "숙박" :
         LoptionDto lodto = cdao.loptionSelect(cnum);
         mv.addObject("lodto", lodto);
         break;
      case "레저" :
         AoptionDto aodto = cdao.aoptionSelect(cnum);
         mv.addObject("aodto", aodto);
         break;
      }   
      
      mv.addObject("cdto", cdto);
      mv.addObject("ciList", ciList);
      
      mv.setViewName("companyUpdateFrm");
      
      return mv;
   }
   
   @Transactional
   public Map<String, List<CimgDto>> delCimg(String cnum, String sysname) {
      log.info("CompanyService - delCimg()");
      Map<String, List<CimgDto>> cimap = new HashMap<String, List<CimgDto>>();
      
      String path = session.getServletContext().getRealPath("/");
      path += "/upload/" + sysname;
      
      File file = new File(path);
      
      if(file.delete()) {
         try {
            cdao.delCimg(sysname);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      
      List<CimgDto> ciList = cdao.getCimgList(cnum);
      
      cimap.put("ciList", ciList);
      
      
      return cimap;
   }
   
   @Transactional
   public String companyUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
      log.info("CompanyService - companyUpdate()");
      String view = null;
      String msg = null;
      
      CompanyDto cdto = new CompanyDto();
      
      cdto.setC_pk_cnum(multi.getParameter("c_pk_cnum"));
      cdto.setC_type(multi.getParameter("c_type"));
      cdto.setC_phone(multi.getParameter("c_phone"));
      
      String solfile = multi.getParameter("fileCheck");
      String mulfile = multi.getParameter("multifileCheck");
      
      log.info("사업자 번호 : " + cdto.getC_pk_cnum());
      log.info("업체 특징 : " + cdto.getC_type());
      log.info("전화번호 : " + cdto.getC_phone());
      log.info("대문 파일 체크 : " + solfile);
      log.info("파일 체크 : " + mulfile);
      
      if (solfile.equals("1")) {
         String path = session.getServletContext().getRealPath("/");

         path += "/upload/" + cdto.getC_img();
         log.info("파일체크 on");
         File file = new File(path);
         
         file.delete();
         try {
            cfileupload(multi, cdto.getC_pk_cnum());

         } catch (Exception e) {
            e.printStackTrace();
         }
      }else if (mulfile.equals("1") && solfile.equals("0")) {
         try {
            cfileupload(multi, cdto.getC_pk_cnum());
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      
      
      try {
         cdao.companyUpdate(cdto);
         view = "redirect:company_contents?cnum=" + cdto.getC_pk_cnum();
         msg = "업체 수정이 완료되었습니다!";
      } catch (Exception e) {
         e.printStackTrace();
         view = "redirect:updateCompanyFrm?cnum=" + cdto.getC_pk_cnum();
         msg = "업체 수정에 실패했습니다";
      }
      
      rttr.addFlashAttribute("msg", msg);
      
      return view;
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
   public Map<String, Object> optionSend(List<String> arrList, String cate, String cnum) {
      log.info("CompanyService - optionSend()");
      Map<String, Object> smap = new HashMap<String, Object>();
      String msg = null;
      
      switch(cate) {
      case "숙박" :
         
         LoptionDto lpdto = loptionGet(arrList,cnum);
         lpdto.setL_fk_cnum(cnum);
         
         try {
            cdao.loptionUpdate(lpdto);
            LoptionDto lodto = cdao.loptionSelect(cnum);
            smap.put("lodto", lodto);
            msg = "수정이 완료되었습니다.";
         } catch (Exception e) {
            msg = "수정에 실패했습니다. 관리자에게 문의해주세요"; 
         }
         break;
      case "레저" :
         
         AoptionDto apdto = aoptionGet(arrList,cnum);
         apdto.setA_fk_cnum(cnum);
         try {
            cdao.aoptionUpdate(apdto);
            AoptionDto aodto = cdao.aoptionSelect(cnum);
            smap.put("aodto", aodto);
            msg = "수정이 완료되었습니다.";
         } catch (Exception e) {
            msg = "수정에 실패했습니다. 관리자에게 문의해주세요";
         }
         break;
      }
      smap.put("msg", msg);
      
      return smap;
   }

   private LoptionDto loptionGet(List<String> arrList, String cnum) {
      LoptionDto lpdto = cdao.loptionSelect(cnum);
      
      for(int i = 0; i<arrList.size(); i++) {
         if(arrList.get(i).equals("l_cityview")) {
            if(lpdto.getL_cityview().equals("0")) {
               lpdto.setL_cityview("1");
            } else {
               lpdto.setL_cityview("0");
            }
         } else if(arrList.get(i).equals("l_seeview")) {
            if(lpdto.getL_seeview().equals("0")) {
               lpdto.setL_seeview("1");
            } else {
               lpdto.setL_seeview("1");
            }
         } else if(arrList.get(i).equals("l_skyview")) {
            if(lpdto.getL_skyview().equals("0")) {
               lpdto.setL_skyview("1");
            } else {
               lpdto.setL_skyview("0");
            }
         } else if(arrList.get(i).equals("l_outdoor")) {
            if(lpdto.getL_outdoor().equals("0")) {
               lpdto.setL_outdoor("1");
            } else {
               lpdto.setL_outdoor("0");
            }
         } else if(arrList.get(i).equals("l_petok")) {
            if(lpdto.getL_petok().equals("0")) {
               lpdto.setL_petok("1");
            } else {
               lpdto.setL_petok("0");
            }
         } else if(arrList.get(i).equals("l_bbq")) {
            if(lpdto.getL_bbq().equals("0")) {
               lpdto.setL_bbq("1");
            } else {
               lpdto.setL_bbq("0");
            }
         } else if(arrList.get(i).equals("l_cooking")) {
            if(lpdto.getL_cooking().equals("0")) {
               lpdto.setL_cooking("1");
            } else {
               lpdto.setL_cooking("0");
            }
         } else if(arrList.get(i).equals("l_pool")) {
            if(lpdto.getL_pool().equals("0")) {
               lpdto.setL_pool("1");
            } else {
               lpdto.setL_pool("0");
            }
         } else if(arrList.get(i).equals("l_wallpool")) {
            if(lpdto.getL_wallpool().equals("0")) {
               lpdto.setL_wallpool("1");
            } else {
               lpdto.setL_wallpool("0");
            }
         } else if(arrList.get(i).equals("l_spa")) {
            if(lpdto.getL_spa().equals("0")) {
               lpdto.setL_spa("1");
            } else {
               lpdto.setL_spa("0");
            }
         } else if(arrList.get(i).equals("l_garaoke")) {
            if(lpdto.getL_garaoke().equals("0")) {
               lpdto.setL_garaoke("1");
            } else {
               lpdto.setL_garaoke("0");
            }
         } 
      }
      
      return lpdto;
   }
   
   private AoptionDto aoptionGet(List<String> arrList, String cnum) {
      AoptionDto apdto = cdao.aoptionSelect(cnum);
      
      for(int i = 0; i<arrList.size(); i++) {
         
         if(arrList.get(i).equals("a_waterpark")) {
            if(apdto.getA_waterpark().equals("0")) {
               apdto.setA_waterpark("1");
            } else {
               apdto.setA_waterpark("0");
            }
            
         } else if(arrList.get(i).equals("a_spa")){
            if(apdto.getA_spa().equals("0")) {
               apdto.setA_spa("1");
            } else {
               apdto.setA_spa("0");
            }
         } else if(arrList.get(i).equals("a_surfing")){
            if(apdto.getA_surfing().equals("0")) {
               apdto.setA_surfing("1");
            } else {
               apdto.setA_surfing("0");
            }
         } else if(arrList.get(i).equals("a_snowcooling")){
            if(apdto.getA_snowcooling().equals("0")) {
               apdto.setA_snowcooling("1");
            } else {
               apdto.setA_snowcooling("1");
            }
         } else if(arrList.get(i).equals("a_rafting")){
            if(apdto.getA_rafting().equals("0")) {
               apdto.setA_rafting("1");
            } else {
               apdto.setA_rafting("0");
            }
         } else if(arrList.get(i).equals("a_aquarium")){
            if(apdto.getA_aquarium().equals("0")) {
               apdto.setA_aquarium("1");
            } else {
               apdto.setA_aquarium("0");
            }
         } else if(arrList.get(i).equals("a_flowers")){
            if(apdto.getA_flowers().equals("0")) {
               apdto.setA_flowers("1");
            } else {
               apdto.setA_flowers("0");
            }
         } else if(arrList.get(i).equals("a_zoo")){
            if(apdto.getA_zoo().equals("0")) {
               apdto.setA_zoo("1");
            } else {
               apdto.setA_zoo("0");
            }
         } else if(arrList.get(i).equals("a_shooting")){
            if(apdto.getA_shooting().equals("0")) {
               apdto.setA_shooting("1");
            } else {
               apdto.setA_shooting("0");
            }
         } else if(arrList.get(i).equals("a_horse")){
            if(apdto.getA_horse().equals("0")) {
               apdto.setA_horse("1");
            } else {
               apdto.setA_horse("0");
            }
         } else if(arrList.get(i).equals("a_paragliding")){
            if(apdto.getA_paragliding().equals("0")) {
               apdto.setA_paragliding("1");
            } else {
               apdto.setA_paragliding("0");
            }
         } else if(arrList.get(i).equals("a_zipline")){
            if(apdto.getA_zipline().equals("0")) {
               apdto.setA_zipline("1");
            } else {
               apdto.setA_zipline("0");
            }
         } else if(arrList.get(i).equals("a_bike")){
            if(apdto.getA_bike().equals("0")) {
               apdto.setA_bike("1");
            } else {
               apdto.setA_bike("0");
            }
         } else if(arrList.get(i).equals("a_cart")){
            if(apdto.getA_cart().equals("0")) {
               apdto.setA_cart("1");
            } else {
               apdto.setA_cart("0");
            }
         } else if(arrList.get(i).equals("a_atv")){
            if(apdto.getA_atv().equals("0")) {
               apdto.setA_atv("1");
            } else {
               apdto.setA_atv("0");
            }
         } else if(arrList.get(i).equals("a_tourpass")){
            if(apdto.getA_tourpass().equals("0")) {
               apdto.setA_tourpass("1");
            } else {
               apdto.setA_tourpass("0");
            }
         } else if(arrList.get(i).equals("a_yort")){
            if(apdto.getA_yort().equals("0")) {
               apdto.setA_yort("1");
            } else {
               apdto.setA_yort("0");
            }
         } else if(arrList.get(i).equals("a_shipfishing")){
            if(apdto.getA_shipfishing().equals("0")) {
               apdto.setA_shipfishing("1");
            } else {
               apdto.setA_shipfishing("0");
            }
         } else if(arrList.get(i).equals("a_cablecar")){
            if(apdto.getA_cablecar().equals("0")) {
               apdto.setA_cablecar("1");
            } else {
               apdto.setA_cablecar("0");
            }
         } else if(arrList.get(i).equals("a_showpainting")){
            if(apdto.getA_showpainting().equals("0")) {
               apdto.setA_showpainting("1");
            } else {
               apdto.setA_showpainting("0");
            }
         } else if(arrList.get(i).equals("a_fishinglounge")){
            if(apdto.getA_fishinglounge().equals("0")) {
               apdto.setA_fishinglounge("1");
            } else {
               apdto.setA_fishinglounge("0");
            }
         } else if(arrList.get(i).equals("a_show")){
            if(apdto.getA_show().equals("0")) {
               apdto.setA_show("1");
            } else {
               apdto.setA_show("0");
            }
         } 
      }
      return apdto;
   }

   public ModelAndView reservationCheck(String num, String cate) {
      log.info("CompanyService - reservationCheck()");
      mv = new ModelAndView();
      
      switch (cate) {
      case "숙박":
         RoomDto rdto = cdao.getRoomDto(num);
         
         mv.addObject("rdto", rdto);
         break;
      case "레저":
         AactivityDto atdto = cdao.getAactivityDto(num);
         
         mv.addObject("atdto", atdto);
         break;
      }
      
      mv.setViewName("companycal");
      
      return mv;
   }
   
}