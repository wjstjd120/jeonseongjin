package com.openai.jeju.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.service.SvcenterService;

import lombok.extern.java.Log;

@Controller
@Log
public class SvcenterController {

	@Autowired
	private SvcenterService svsv;

	private ModelAndView mv;

	@GetMapping("Notice_List")
	public ModelAndView Notice_List(ListDto list) {
		log.info("Notice_List");

		mv = svsv.selectMlist(list);

		return mv;
	}

	@GetMapping("Notice")
	public ModelAndView Notice(Integer n_pk_num) {
		log.info("Notice()");

		mv = svsv.getList(n_pk_num);

		return mv;
	}

	@GetMapping("QA_List")
	public ModelAndView Qalist(ListDto list) {
		log.info("Qalist()");

		mv = svsv.selectqalist(list);

		return mv;
	}

	@GetMapping("QA_Write")
	public String Qawrite() {
		log.info("Qawrite()");

		return "QA_Write";
	}

	@PostMapping("qawrite")
	public String qawrite(ComplaintDto plaint, RedirectAttributes rttr) {
		log.info("qawrite()");

		String view = svsv.qawrite(plaint, rttr);

		return view;
	}

	@GetMapping("Myqa_List")
	public ModelAndView MyqaList(HttpSession session, RedirectAttributes rttr) {
		log.info("MyqaList()");

		mv = new ModelAndView();
		String msg = null;
		MemberDto mdto = (MemberDto) session.getAttribute("user");

		if (mdto.getM_pk_id() != null) {
			ListDto list = new ListDto();
			mv = svsv.Myqa_List(list, session);
		}
		
		rttr.addFlashAttribute("msg", msg);

		return mv;
	}

	@GetMapping("Myqa")
	public ModelAndView Myqa(Integer co_pk_conum) {
		log.info("Myqa");

		mv = svsv.geqatList(co_pk_conum);

		return mv;
	}

	@PostMapping(value = "deleteComplaint", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, String> qaDelete(String co_pk_conum, HttpSession session, RedirectAttributes rttr) {
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<");

		String msg = svsv.qaDelete(co_pk_conum, session, rttr);
		Map<String, String> rmap = new HashMap<String, String>();
		rmap.put("res", msg);

		return rmap;

	}// 게시글 일반 삭제

	@PostMapping(value = "checkDelete", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, String> checkDel(HttpServletRequest request, String co_pk_conum) {

		String[] ajaxMsg = request.getParameterValues("valueArr");
		int size = ajaxMsg.length;
		for (int i = 0; i < size; i++) {
			svsv.checkDel(ajaxMsg[i]);
		}

		Map<String, String> rmap = new HashMap<>();

		rmap.put("res", "1");

		return rmap;

	}
}
