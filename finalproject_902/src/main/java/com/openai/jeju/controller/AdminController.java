package com.openai.jeju.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NoticeDto;
import com.openai.jeju.dto.SimgDto;
import com.openai.jeju.dto.SpotDto;
import com.openai.jeju.service.AdminService;

import lombok.extern.java.Log;

@Controller
@Log
public class AdminController {
   @Autowired
      private AdminService aServ;
      
      private ModelAndView mv;
      
      @GetMapping("/Admin_Company_List")
      public ModelAndView Admin_Company_List(ListDto list, HttpSession session) {
         log.info("Admin_Company_List()");
         
         mv = new ModelAndView();
         
         mv = aServ.selectCompanylist(list, session);
         
         return mv;
      }

      @GetMapping("/Admin_Member_List")
      public ModelAndView  AdminMsearch(ListDto list, HttpSession session) {
         log.info("AdminMsearch");
         mv = new ModelAndView();
         
         mv = aServ.selectMlist(list, session);
         
         return mv;
      }
      
      @GetMapping("/Admin_Bmember_List")
      public ModelAndView Admin_Bmember_List(ListDto list, HttpSession session) {
         log.info("Admin_Bmember_List()");
         
         mv = new ModelAndView();
         
         mv = aServ.selectBMlist(list, session);
         
         return mv;
      }
      
      @GetMapping("/Admin_Event_List")
      public ModelAndView Admin_Event_List(ListDto list, HttpSession session) {
         log.info("Admin_Event_List()");
         
         mv = new ModelAndView();
         
         mv = aServ.selectEventlist(list, session);
         
         return mv;
      }
      
      @GetMapping("/Admin_Spot_List")
      public ModelAndView Admin_Spot_List(ListDto list, HttpSession session) {
         log.info("Admin_Company_List()");
         
         mv = new ModelAndView();
         
         mv = aServ.selectSpotlist(list, session);
         
         return mv;
      }
      
      @GetMapping("/Admin_Notice_List")
      public ModelAndView Admin_Notice_List(ListDto list, HttpSession session) {
         log.info("Admin_Notice_List()");
         
         mv = new ModelAndView();
         
         mv = aServ.selectNoticelist(list, session);
         
         return mv;
      }
      
      @GetMapping("/Admin_Complaint_List")
      public ModelAndView Admin_Complaint_List(ListDto list, HttpSession session) {
         log.info("Admin_Complaint_List()");
         
         mv = new ModelAndView();
         
         mv = aServ.selectComplaintlist(list, session);
         
         return mv;
      }
      
      
      @PostMapping(value = "MemberIdSelect",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, MemberDto> MemberIdSelect(String id){
         
         //화면에 보낼 결과 데이터 저장용 맵 생성
         Map<String, MemberDto> rmap = new HashMap<String, MemberDto>();
         
         System.out.println(id);
         
         MemberDto member = aServ.MemberIdSelect(id);
         
         rmap.put("member", member);
         
         
         return rmap;//jackson 라이브러리 클래스가 map을 json으로 변환.
         //{res:"처리성공", tt:"책제목", wr:"저자", ms:"내용"}
      }
      
      @PostMapping(value = "Admin_Member_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Member_Update (MemberDto mDto) {
         String msg = null;
         
         log.info(mDto.getM_addr());
         
         msg = aServ.Admin_Member_Update(mDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Member_Delete",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Member_Delete (String id) {
         
         String msg = aServ.Admin_Member_Delete(id);
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Bmember_Change_Category",
      produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Bmember_Change_Category (String id) {

         String msg = aServ.Bmember_Change_Category(id);

         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);

         return rmap;
      }
      
      @PostMapping(value = "Member_Change_Category",
      produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Member_Change_Category (String id) {
         
         log.info(id);
         
         String msg = aServ.Member_Change_Category(id);

         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);

         return rmap;
      }
      
      @PostMapping(value = "CompanyCnumSelect",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, Object> CompanyCnumSelect(String cnum){
         log.info("CompanyCnumSelect");
         //화면에 보낼 결과 데이터 저장용 맵 생성
         Map<String, Object> rmap = new HashMap<String, Object>();
         
//         Map<String, CompanyDto> cmap = new HashMap<String, CompanyDto>();
//         Map<String, MemberDto> mmap = new HashMap<String, MemberDto>();
         
         System.out.println(cnum);
         
         CompanyDto company = aServ.CompanyCnumSelect(cnum);
         MemberDto member = aServ.MemberIdSelect(company.getC_fk_id());
         
         rmap.put("company", company);
         rmap.put("member", member);
         
         return rmap;//jackson 라이브러리 클래스가 map을 json으로 변환.
         //{res:"처리성공", tt:"책제목", wr:"저자", ms:"내용"}
      }
      
      @PostMapping(value = "Admin_Company_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Company_Update (CompanyDto cDto) {
         String msg = null;
         
         log.info(cDto.getC_addr());
         
         msg = aServ.Admin_Company_Update(cDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      
      @PostMapping(value = "Admin_Company_Delete",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Company_Delete (String cnum) {
         
         String msg = aServ.Admin_Company_Delete(cnum);
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Company_file_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, CompanyDto> Admin_Company_file_Update (MultipartHttpServletRequest multi) {
         
         Map<String, CompanyDto> cmap = new HashMap<String, CompanyDto>();
         
         CompanyDto company = null;
         
         try {
             company = aServ.CompanyfileUpload(multi);
            
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         log.info("여기까지 왔는데...");
         
         cmap.put("company", company);
         
         return cmap;
      }
      
      @PostMapping(value = "EventEnumSelect",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, EventDto> EventEnumSelect(String e_num){
         log.info("CompanyCnumSelect");
         //화면에 보낼 결과 데이터 저장용 맵 생성
         Map<String, EventDto> rmap = new HashMap<String, EventDto>();
         
         System.out.println(e_num);
         
         EventDto event = aServ.EventEnumSelect(e_num);
         
         rmap.put("event", event);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Event_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Event_Update (EventDto eDto) {
         String msg = null;
         
         msg = aServ.Admin_Event_Update(eDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Spot_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Spot_Update (SpotDto sDto) {
         String msg = null;
         
         msg = aServ.Admin_Spot_Update(sDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Notice_Insert",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Notice_insert (NoticeDto nDto) {
         String msg = null;
         
         msg = aServ.Admin_Notice_Insert(nDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      
      @PostMapping(value = "Admin_Notice_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Notice_Update (NoticeDto nDto) {
         String msg = null;
         
         msg = aServ.Admin_Notice_Update(nDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Notice_Delete",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Notice_Delete (String nnum) {
         
         String msg = aServ.Admin_Notice_Delete(nnum);
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Complaint_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Complaint_Update (ComplaintDto coDto) {
         String msg = null;
         
         msg = aServ.Admin_Complaint_Update(coDto);
         
         log.info("여기까지 왔는데...");
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Complaint_Delete",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Complaint_Delete (String conum) {
         
         String msg = aServ.Admin_Complaint_Delete(conum);
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @GetMapping("/filedelete")
      public String deleteFile(String filename) {
         log.info("삭제()");
         String view = "CompanyFrm";
         
         aServ.deleteFile(filename);
         
         return view;
      }
      
      @PostMapping(value = "Admin_Event_file_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, EventDto> Admin_Event_file_Update (MultipartHttpServletRequest multi) {
         
         Map<String, EventDto> emap = new HashMap<String, EventDto>();
         
         EventDto event = null;
         
         try {
             event = aServ.fileUpload(multi);
            
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         log.info("여기까지 왔는데...");
         
         emap.put("event", event);
         
         System.out.println(event.getE_addr());
         
         return emap;
      }
      
      @PostMapping(value = "Admin_Event_Delete",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Event_Delete (String e_num) {
         
         String msg = aServ.Admin_Event_Delete(e_num);
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "SpotSnumSelect",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, Object> SpotSnumSelect(String snum){
         log.info("SpotSnumSelect");
         //화면에 보낼 결과 데이터 저장용 맵 생성
         Map<String, Object> rmap = new HashMap<String, Object>();
         System.out.println(snum);
         
         SpotDto spot = aServ.SpotSnumSelect(snum);
         List<SimgDto> simg = aServ.selectSimglist(snum);
         
         rmap.put("spot", spot);
         rmap.put("simg", simg);
         
         return rmap;
      }
      
      @PostMapping(value = "NoticeNnumSelect",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, NoticeDto> NoticeNnumSelect(String nnum){
         log.info("NoticeNnumSelect");
         //화면에 보낼 결과 데이터 저장용 맵 생성
         Map<String, NoticeDto> rmap = new HashMap<String, NoticeDto>();
         
         System.out.println(nnum);
         
         NoticeDto notice = aServ.NoticeNnumSelect(nnum);
         
         rmap.put("notice", notice);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Spot_file_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, SpotDto> Admin_Spot_file_Update (MultipartHttpServletRequest multi) {
         
         Map<String, SpotDto> smap = new HashMap<String, SpotDto>();
         
         SpotDto spot = null;
         
         try {
             spot = aServ.SpotfileUpload(multi);
            
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         log.info("여기까지 왔는데...");
         
         smap.put("spot", spot);
         
         System.out.println(spot.getS_addr());
         
         return smap;
      }
      
      @PostMapping(value = "Admin_Spot_Multi_file_Update",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, Object> Admin_Spot_Multi_file_Update (MultipartHttpServletRequest multi) {
         
         Map<String, Object> smap = new HashMap<String, Object>();
         
         SpotDto spot = null;
         
         try {
             spot = aServ.SpotMultifileUpload(multi);
            
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         log.info("여기까지 왔는데...");
         
         List<SimgDto> simg = aServ.selectSimglist(multi.getParameter("s_pk_num"));
         
         smap.put("spot", spot);
         smap.put("simg", simg);
         
         System.out.println(spot.getS_addr());
         
         return smap;
      }
      
      @PostMapping(value = "Admin_Spot_Insert",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Spot_Insert (MultipartHttpServletRequest multi) {
         
         Map<String, String> rmap = new HashMap<String, String>();
         
         String res = null;
         
         
         try {
            aServ.Admin_Spot_Insert(multi);
            res = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            res = "no";
         }
         
         
         log.info("여기까지 왔는데...");
         
         rmap.put("res", res);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Event_Insert",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Event_Insert (MultipartHttpServletRequest multi) {
         
         Map<String, String> rmap = new HashMap<String, String>();
         
         String res = null;
         
         
         try {
            aServ.Admin_Event_Insert(multi);
            res = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            res = "no";
         }
         
         
         log.info("여기까지 왔는데...");
         
         rmap.put("res", res);
         
         return rmap;
      }
      
      @PostMapping(value = "Admin_Spot_Delete",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, String> Admin_Spot_Delete (String snum) {
         
         String msg = aServ.Admin_Spot_Delete(snum);
         
         Map<String, String> rmap = new HashMap<String, String>();
         rmap.put("res", msg);
         
         return rmap;
      }
      
      @PostMapping(value = "ComplaintConumSelect",
            produces = "application/json; charset=UTF-8")
      @ResponseBody
      public Map<String, ComplaintDto> ComplaintConumSelect(String conum){
         log.info("NoticeNnumSelect");
         //화면에 보낼 결과 데이터 저장용 맵 생성
         Map<String, ComplaintDto> rmap = new HashMap<String, ComplaintDto>();
         
         System.out.println(conum);
         
         ComplaintDto complaint = aServ.ComplaintConumSelect(conum);
         
         rmap.put("complaint", complaint);
         
         return rmap;
      }
}