package com.openai.jeju.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dao.SvcenterDao;
import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NoticeDto;
import com.openai.jeju.util.PagingUtil;

import lombok.extern.java.Log;

@Service
@Log
public class SvcenterService {

	private ModelAndView mv;
	private int listCnt = 5;
	private String listName = "";

	@Autowired
	private HttpSession session;
	@Autowired
	private SvcenterDao svdao;

	public ModelAndView selectMlist(ListDto list) {
		log.info("SvcenterService - selectMlist()");

		mv = new ModelAndView();

		if (list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<NoticeDto> nList = svdao.noticeSelect(list);

		mv.addObject("nList", nList);

		// 페이징 처리
		list.setPageNum(num);
		listName = "Notice_List?";
		String pageHtml = getPaging(list);
		mv.addObject("paging", pageHtml);

		// 세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
		// 돌아 갈 때 사용할 페이지 번호를 저장)

		mv.setViewName("Notice_List");

		return mv;
	} // 공지사항

	private String getPaging(ListDto list) {
		String pageHtml = null;

		// 전체 글개수 구하기
		int maxNum = svdao.noticeCntSelect(list);
		// 한 페이지 당 보여질 페이지 번호의 개수
		int pageCnt = 5;

		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName);

		pageHtml = paging.makePaging();

		return pageHtml;
	}

	public ModelAndView getList(Integer n_pk_num) {
		log.info("getList()");
		mv = new ModelAndView();

		// 조회수 증가
		svdao.viewUpdate(n_pk_num);

		NoticeDto nDto = svdao.NoticeList(n_pk_num);

		mv.addObject("nDto", nDto);

		mv.setViewName("Notice");

		return mv;
	}

	public ModelAndView selectqalist(ListDto list) {
		mv = new ModelAndView();

		// Dao에 보내는 데이터를 만들자.(검색 기능 추가로 코드 사용 안함)
		// Map<String, Integer> pmap = new HashMap<String, Integer>();
		// pmap.put("pnum", (pageNum - 1) * listCnt);
		// pmap.put("lcnt", listCnt);

		if (list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<ComplaintDto> cList = svdao.qaListSelect(list);

		ComplaintDto comDto = new ComplaintDto();

		for (int i = 0; i < cList.size(); i++) {
			switch (cList.get(i).getCo_check()) {
			case "0":
				comDto = cList.get(i);
				comDto.setCo_check("N");
				cList.set(i, comDto);
				break;
			case "1":
				comDto = cList.get(i);
				comDto.setCo_check("Y");
				;
				cList.set(i, comDto);
				break;
			}
		}

		mv.addObject("cList", cList);

		// 페이징 처리
		list.setPageNum(num);
		listName = "QA_List?";
		String pageHtml = getPaging(list);
		mv.addObject("paging", pageHtml);

		// 세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
		// 돌아 갈 때 사용할 페이지 번호를 저장)

		mv.setViewName("QA_List");

		return mv;
	}

	public String qawrite(ComplaintDto plaint, RedirectAttributes rttr) {
		String view = null;
		String msg = null;

		MemberDto mdto = (MemberDto) session.getAttribute("user");
		plaint.setCo_fk_id(mdto.getM_pk_id());

		log.info("아이디 : " + plaint.getCo_fk_id());
		log.info("제목 : " + plaint.getCo_title());
		log.info("컨텐츠 : " + plaint.getCo_contents());

		try {
			svdao.qaInsert(plaint);

			view = "redirect:QA_List?pageNum=1";
			msg = "건의사항이 등록되었습니다.";

		} catch (Exception e) {
			e.printStackTrace();

			view = "redirect:QA_Write";
			msg = "건의사항 등록에 실패하였습니다.";
		}

		rttr.addFlashAttribute("msg", msg);

		return view;
	}

	public ModelAndView Myqa_List(ListDto list, HttpSession session2) {
		mv = new ModelAndView();

		// Dao에 보내는 데이터를 만들자.(검색 기능 추가로 코드 사용 안함)
		// Map<String, Integer> pmap = new HashMap<String, Integer>();
		// pmap.put("pnum", (pageNum - 1) * listCnt);
		// pmap.put("lcnt", listCnt);

		if (list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		MemberDto mdto = (MemberDto) session.getAttribute("user");
		String id = mdto.getM_pk_id();

		list.setId(id);

		List<ComplaintDto> cList = svdao.MyQaListSelect(list);

		ComplaintDto comDto = new ComplaintDto();

		for (int i = 0; i < cList.size(); i++) {
			switch (cList.get(i).getCo_check()) {
			case "0":
				comDto = cList.get(i);
				comDto.setCo_check("N");
				cList.set(i, comDto);
				break;
			case "1":
				comDto = cList.get(i);
				comDto.setCo_check("Y");
				;
				cList.set(i, comDto);
				break;
			}
		}

		mv.addObject("cList", cList);

		// 페이징 처리
		list.setPageNum(num);
		listName = "Myqa_List?";
		String pageHtml = getPaging(list);
		mv.addObject("paging", pageHtml);

		// 세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
		// 돌아 갈 때 사용할 페이지 번호를 저장)

		mv.setViewName("Myqa_List");

		return mv;

	}

	public ModelAndView geqatList(Integer co_pk_conum) {
		log.info("getList()");
		mv = new ModelAndView();

		ComplaintDto coDto = svdao.qaList(co_pk_conum);
		ComplaintDto plaint = svdao.qaList(co_pk_conum);

		mv.addObject("coDto", coDto);
		mv.addObject("plaint", plaint);

		mv.setViewName("Myqa");

		return mv;
	}

	public String qaDelete(String co_pk_conum, HttpSession session2, RedirectAttributes rttr) {
		String msg = null;

		try {
			svdao.qaDelect(co_pk_conum);
			msg = "삭제 되었습니다.";

		} catch (Exception e) {
			e.printStackTrace();
			msg = "삭제 실패...관리자에게 문의하세요!";
		}

		return msg;

	} // qadelect end
	
	public String checkDel(String co_pk_conum) {
		String view = null;

		svdao.qaDelect(co_pk_conum);

		return view;
	}
}
