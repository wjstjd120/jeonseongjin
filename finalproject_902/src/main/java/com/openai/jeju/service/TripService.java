package com.openai.jeju.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dao.TravelrouteDao;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.TravelrouteDto;

import lombok.extern.java.Log;

@Service
@Log
public class TripService {
   
   private ModelAndView mv;
   
   @Autowired
   private HttpSession session;
   @Autowired
   private TravelrouteDao tdao;
   
   @Transactional
   public String traInsert(TravelrouteDto tra, RedirectAttributes rttr) {
      log.info("traInsert()" + tra.getTr_in());
      
      String view = null;
      String msg = null;
      
      
      String tra_in = tra.getTr_in();
      String tra_out = tra.getTr_out();  
      
      tra_in = tra_in.replace("년 ", "-");
      tra_in = tra_in.replace("월", "-");
      tra_in = tra_in.replace("일", "");
      tra_out = tra_out.replace("년 ", "-");
      tra_out = tra_out.replace("월", "-");
      tra_out = tra_out.replace("일", "");
      
      tra.setTr_in(tra_in);
      tra.setTr_out(tra_out);
      
      log.info("tra_in" + tra_in);
      log.info("tra_out" + tra_out);
      
      
      MemberDto mdto = (MemberDto)session.getAttribute("user");
      String id = mdto.getM_pk_id();
      
      String cate = "'숙박'";
      
      try {
         cate = URLEncoder.encode("'숙박'", "UTF-8");
      } catch (UnsupportedEncodingException e1) {
         e1.printStackTrace();
      }
      
      try {
            tra.setTr_fk_id(id);
            tdao.traInsert(tra);
            
            tra = tdao.traSelect(String.valueOf(tra.getTr_pk_num()));
            
            log.info("tra_in : " + tra.getTr_in());
            
            session.setAttribute("tdto", tra);
            
            view="redirect:/Lodgment_List?cate="+ cate;
         
      } catch (Exception e) {
    	  e.printStackTrace();
         view="redirect:/";
         msg="일정담기 실패";
      }
      rttr.addFlashAttribute("msg", msg);
      
      return view;
   }

}