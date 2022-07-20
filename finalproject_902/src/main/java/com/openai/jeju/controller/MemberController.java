package com.openai.jeju.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.service.MemberService;

import lombok.extern.java.Log;

@Controller
@Log
public class MemberController {
	
	private ModelAndView mv;
	
	@Autowired
	private MemberService msv;
	
	@GetMapping("joinFrm")
	public String joinFrm() {
		log.info("MemberController - joinFrm()");
		
		return "join_i";
	}
	
	@PostMapping("memberInsert")
	public String memberInsert(MemberDto dto, RedirectAttributes rttr) {
		log.info("MemberController - memberInsert()");
		String view = null;
		
		view = msv.memberInsert(dto, rttr);
		
		return view;
	}
	
	@GetMapping("login")
	public String loginFrm() {
		log.info("MemberController - loginFrm()");
		
		return "loginFrm";
	}
	
	@PostMapping("loginProc")
	public String loginProc(MemberDto dto, RedirectAttributes rttr) {
		log.info("MemberController - loginProc()");
		
		String view = msv.loginProc(dto, rttr);
		
		return view;
	}
	
	@GetMapping("logout")
	public String logout() {
		log.info("MemberController - logout()");
		
		String view = msv.logout();
		
		return view;
	}
	
	@PostMapping(value="idCheck", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, String> idCheck(String m_pk_id){
		log.info("MemberController - idCheck()");
		log.info("넘어온 정보 : " + m_pk_id);
		Map<String, String> smap = new HashMap<String, String>();
		
		smap = msv.idCheck(m_pk_id);
		
		return smap;
	}
	
	@PostMapping(value="nickCheck", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, String> nickCheck(String m_nickname){
		log.info("MemberController - idCheck()");
		log.info("넘어온 정보 : " + m_nickname);
		Map<String, String> smap = new HashMap<String, String>();
		
		smap = msv.nickCheck(m_nickname);
		
		return smap;
	}
	
	@PostMapping(value = "user", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, String> sms(String pnum) throws NoSuchAlgorithmException, URISyntaxException,
			UnsupportedEncodingException, InvalidKeyException, JsonProcessingException {
		log.info("MemberController - sms()");
		log.info("pnum : " + pnum);
		
		
		Map<String, String> rmap = msv.sendSms(pnum);

		return rmap;
	}
	
	@PostMapping(value = "confirmFrm", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, String> check(String checkNum) {
		log.info("MemberController - check()");
		log.info("check() - checkNum : " + checkNum);

		Map<String, String> rmap = msv.CheckProc(checkNum);

		return rmap;
	}
}
