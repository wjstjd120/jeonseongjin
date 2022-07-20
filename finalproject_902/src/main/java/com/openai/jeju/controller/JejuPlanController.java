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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dto.BreplyDto;
import com.openai.jeju.dto.JejuspotreviewDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.service.jejuService;

import lombok.extern.java.Log;

@Log
@Controller
public class JejuPlanController {

	private ModelAndView mv;

	@Autowired
	private jejuService csv;

	@GetMapping("jejuplanList")
	public ModelAndView jejuplanList(ListDto list) {

		mv = csv.planList(list);

		return mv;
	}

	@GetMapping("jejuplanWrite")
	public String jejuplanWrite() {

		return "jejuplanWrite";
	}

	@PostMapping("jejuplanInsert")
	public String jejuplanInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {

		String view = csv.jejuplanInsert(multi, rttr);

		return view;
	}

	@GetMapping("jejuplan")
	public ModelAndView jejuplan(int jnum) {

		mv = csv.jejuplanInfo(jnum);

		return mv;
	}

	@GetMapping("jejuplanimginsert")
	public String jejuplanimginsert() {

		return "jejuplanimginsert";
	}

	@PostMapping(value = "/replyIns", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, List<BreplyDto>> replyInsert(BreplyDto reply) {

		Map<String, List<BreplyDto>> rmap = csv.replyInsert(reply);

		return rmap; // map -> json변환은 jackson 라이브러리 객체가 처리.
	}

	
	@GetMapping("jejutourList")
	public ModelAndView jejutourList(ListDto list) {

		mv = csv.getspotlist(list);

		return mv;
	}

	@GetMapping("jejutour")
	public ModelAndView jejutour(int jnum) {

		mv = csv.spotinfo(jnum);

		return mv;
	}
	@GetMapping("spotreviewwrite")
	public String spotreviewwrite() {



		return "spotreviewwrite";
	}
	@PostMapping(value = "reviewwrite",
	         produces = "application/json; charset=UTF-8")
	   @ResponseBody
	   public Map<String, String> reviewwrite (MultipartHttpServletRequest multi) {
	      
	      Map<String, String> rmap = new HashMap<String, String>();
	      
	      String res = null;
	      
	      
	      try {
	         csv.reviewInsert(multi);
	         res = "ok";
	      } catch (Exception e) {
	         e.printStackTrace();
	         res = "no";
	      }
	      
	      
	      rmap.put("res", res);
	      
	      return rmap;
	}
	@PostMapping(value = "redeletedelete", produces = "application/json; charset=UTF-8")
	   @ResponseBody
	   public Map<String, String> breDelete(String sv_pk_num, HttpSession session) {
	      
	      String msg = csv.breDelete(sv_pk_num, session);
	      Map<String, String> rmap = new HashMap<String, String>();
	      rmap.put("res", msg);
	      
	      return rmap;
	      
	   }//게시글 일반 삭제
	
	
	
	
	@PostMapping(value = "replynumselect",
	         produces = "application/json; charset=UTF-8")
	   @ResponseBody
	   public Map<String, JejuspotreviewDto> replynumselect (String renum) {
	      
	      Map<String, JejuspotreviewDto> rmap = csv.replynumselect(renum);
	      
	      return rmap;
	}
	
	@PostMapping(value = "reviewupdate",
	         produces = "application/json; charset=UTF-8")
	   @ResponseBody
	   public Map<String, String> reviewupdate (MultipartHttpServletRequest multi) {
	      
	      Map<String, String> rmap = new HashMap<String, String>();
	      
	      String res = null;
	      
	      
	      try {
	         csv.reviewUpdate(multi);
	         res = "ok";
	      } catch (Exception e) {
	         e.printStackTrace();
	         res = "no";
	      }
	      
	      
	      rmap.put("res", res);
	      
	      return rmap;
	}
	
	@GetMapping("JejuEvent_List")
	public ModelAndView jejuEventList(ListDto list) {
		log.info("jejuEventList()");
		
		mv = csv.eventList(list);
		
		return mv;
	}
	
	@GetMapping("JejuEvent")
	public ModelAndView jejuEventinfo(Integer e_pk_enum) {
		log.info("jejuEventinfo()");
		
		mv = csv.eventInfoList(e_pk_enum);
		
		return mv;
	}
}
