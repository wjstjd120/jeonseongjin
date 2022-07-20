package com.openai.jeju.service;

import java.io.File;
import java.io.IOException;
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

import com.openai.jeju.dao.JejuplanDao;
import com.openai.jeju.dto.BimgDto;
import com.openai.jeju.dto.BlogDto;
import com.openai.jeju.dto.BreplyDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.JejusimgDto;
import com.openai.jeju.dto.JejuspotDto;
import com.openai.jeju.dto.JejuspotreviewDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.TravelrouteDto;
import com.openai.jeju.dto.jejuplanDto;
import com.openai.jeju.util.PagingUtil;

import lombok.extern.java.Log;

@Log
@Service
public class jejuService {
	@Autowired
	private HttpSession session;

	@Autowired
	private JejuplanDao cdao;

	private ModelAndView mv;
	

	private int listCnt = 6;
	private String listName = "";

	
	@Transactional
	public String jejuplanInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		jejuplanDto dto = new jejuplanDto();
		CompanyDto cdto = new CompanyDto();
		MemberDto mdto = new MemberDto();
		TravelrouteDto tdto = new TravelrouteDto();
		BimgDto bdto = new BimgDto();
		String view = null;
		String msg = null;
		String sysname = null;
		
		
		
		dto.setB_fk_id((String)session.getAttribute("M_pk_id"));
		dto.setB_pk_num(multi.getParameter("b_pk_num"));
		dto.setB_fk_tnum(String.valueOf(tdto.getTr_pk_num()));
		dto.setB_img(multi.getParameter("b_img"));
		dto.setB_title(tdto.getTr_title());
		dto.setB_cost(multi.getParameter("b_cost"));
		dto.setB_contents(multi.getParameter("b_contents"));
		//dto.setB_date(multi.getParameter("b_date"));
		
		
		bdto.setBi_fk_num(multi.getParameter("bi_fk_num"));
		bdto.setBi_pk_num(dto.getB_pk_num());
		bdto.setBi_oriname(multi.getParameter("bi_oriname"));
		bdto.setBi_sysname(multi.getParameter("bi_sysname"));
		try {
			sysname = fileupload(multi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setB_img(sysname);
		
		try {
			cdao.JejuplanInsert(dto);
			view = "redirect:/";
			msg = "신청완료";
			
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:jejuplanwrite";
			msg = "후기 작성 실패";
		}
		try {
			cdao.BimgInsert(bdto);
		} catch (Exception e) {
			e.printStackTrace();
		}
			rttr.addFlashAttribute("msg", msg);

			return view;
	}


	private String fileupload(MultipartHttpServletRequest multi) throws IllegalStateException, IOException {
		String sysname = null;

		Iterator<String> files = multi.getFileNames();

		while (files.hasNext()) {
			String fn = files.next();

			// multiple 선택 파일 처리 -> 파일 목록 가져오기
			List<MultipartFile> fList = multi.getFiles(fn);

			// 각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
			for (int i = 0; i < fList.size(); i++) {
				MultipartFile mf = fList.get(i);

				// 파일명 추출
				String oriname = mf.getOriginalFilename();

				// 변경할 이름 생성
				sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

				// upload 폴더 파일 저장
				File ff = new File(sysname);
				mf.transferTo(ff);
			}
		}
		return sysname;
	}


	public ModelAndView jejuplanInfo(int jnum) {
		String str = null;
		
		mv = new ModelAndView();

		mv.setViewName("jejuplan");

		List<jejuplanDto> jList = new ArrayList<>();
		List<BimgDto> bList = new ArrayList<>();
		List<BreplyDto> rList = cdao.replySelect(jnum);

		jList = cdao.jejuplanInfo(jnum);
		bList= cdao.jejuplanImg(jnum);
		
		jejuplanDto dto = new jejuplanDto();
		BimgDto bto = new BimgDto();
		
		mv.addObject("jList", jList);
		mv.addObject("bList", bList);
		mv.addObject("rList", rList);
		mv.addObject("jnum", jnum);
		return mv;
			
		
	}
		
	public ModelAndView planList(ListDto list) {
		mv = new ModelAndView();
		
				
		if(list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<jejuplanDto> pList = cdao.planList(list);// 리턴된값은 pList로 다시 넘겨 받는다

		mv.addObject("pList", pList);

		list.setPageNum(num);
		
		listName = "jejuplanList?";
		String pageHtml = getPaging(list);
		mv.addObject("paging", pageHtml);

		// 세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록화면으로
		// 돌아 갈 때 사용할 페이지 번호를 저장)
		session.setAttribute("pageNum", list.getPageNum()); // 해당 페이지 37번째줄 pageNum과 같은 이름으로 작성!

		if (list.getColname() != null) {
			session.setAttribute("list", list);
		}

		mv.setViewName("jejuplanList");

	      
	      return mv;
	   } //공지사항
	   
	private String getPaging(ListDto list) {
		String pageHtml = null;

		// 한 페이지 당 보여질 페이지 번호의 개수
		int pageCnt = 5;
		int maxNum = cdao.jejuplancnt(list);
		// 검색 용 컬럼명과 검색어를 추가
		if (list.getColname() != null) {
			listName += "colname=" + list.getColname() + "&keywoard=" + list.getKeyword() + "&";
		}

		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName); // 클래스 임포트!!

		pageHtml = paging.makePaging();

		return pageHtml; // 여기서 리턴값 받은것이 해당 클래스 45 번째줄로 간다!!
	}


	public Map<String, List<BreplyDto>> replyInsert(BreplyDto reply) {
		Map<String, List<BreplyDto>> rmap = null;

		try {
			// 댓글 삽입
			cdao.replyInsert(reply);
			// 댓글 목록 불러오기 및 rmap에 추가(getR_bnum() 어떤글의 댓글이 들어오는알아야하기때문에? 리플라이안에 꼭 필요함)
			List<BreplyDto> rList = cdao.replySelect(reply.getBr_fk_num());

			rmap = new HashMap<String, List<BreplyDto>>();
			rmap.put("rList", rList);
		} catch (Exception e) {
			e.printStackTrace();
			rmap = null;
		}

		return rmap;
	}


	public ModelAndView getspotlist(ListDto list) {
		mv = new ModelAndView();
		
		
		if(list.getPageNum() == 0) {
			list.setPageNum(1);
		}
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);

		List<JejuspotDto> spList = cdao.spotList(list);// 리턴된값은 pList로 다시 넘겨 받는다

		mv.addObject("spList", spList);

		list.setPageNum(num);
		
		listName = "jejutourList?";
		String pageHtml = getPaging2(list);
		mv.addObject("paging", pageHtml);

		// 세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록화면으로
		// 돌아 갈 때 사용할 페이지 번호를 저장)
		session.setAttribute("pageNum", list.getPageNum()); // 해당 페이지 37번째줄 pageNum과 같은 이름으로 작성!

		if (list.getColname() != null) {
			session.setAttribute("list", list);
		}

		mv.setViewName("jejutourList");

	      
	      return mv;
	   } //공지사항


	private String getPaging2(ListDto list) {
		String pageHtml = null;

		// 한 페이지 당 보여질 페이지 번호의 개수
		int pageCnt = 5;
		int maxNum = cdao.spotcnt(list);
		// 검색 용 컬럼명과 검색어를 추가
		if (list.getColname() != null) {
			listName += "colname=" + list.getColname() + "&keywoard=" + list.getKeyword() + "&";
		}

		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName); // 클래스 임포트!!

		pageHtml = paging.makePaging();

		return pageHtml; // 여기서 리턴값 받은것이 해당 클래스 45 번째줄로 간다!!

	}


	public ModelAndView spotinfo(int jnum) {
	String str = null;
		
		mv = new ModelAndView();

		mv.setViewName("jejutour");

		List<JejusimgDto> siList = cdao.spotimgList(jnum);
		List<JejuspotDto> sList = cdao.spotInfo(jnum);
		List<JejuspotreviewDto> rList = cdao.spotreview(jnum);
		List<JejusimgDto> siList2 = cdao.spotimgList2(jnum);
	    List<JejusimgDto> siList3 = cdao.spotimgList3(jnum);
	    List<JejusimgDto> siList4 = cdao.spotimgList4(jnum);
		List<JejuspotreviewDto> riList = cdao.spotreview(jnum);

	    JejusimgDto sito = new JejusimgDto();
		JejuspotDto sto = new JejuspotDto();
		JejuspotreviewDto rdto = new JejuspotreviewDto();
	  
		
	    mv.addObject("siList2", siList2);
	    mv.addObject("siList3", siList3);
	    mv.addObject("siList4", siList4);
	    mv.addObject("riList", riList);

		mv.addObject("siList", siList);
		mv.addObject("sList", sList);
		mv.addObject("rList", rList);
		mv.addObject("jnum", jnum);
		//mv.addObject("si2List", si2List);
		return mv;
			
	}
	public void reviewInsert(MultipartHttpServletRequest multi) throws Exception {
	      String path = multi.getRealPath("/upload/");
	      JejuspotreviewDto sDto = new JejuspotreviewDto();
	      JejuspotDto jsdto = new JejuspotDto();
	      
	      String filecheck = (String)multi.getParameter("FileCheck");
	      
	         File upFolder = new File(path);
	         if(!upFolder.isDirectory()) {
	            upFolder.mkdir();
	         }
	         sDto.setSv_star(multi.getParameter("sv_star"));
	      sDto.setSv_fk_id(multi.getParameter("sv_fk_id"));
	      sDto.setSv_fk_num(multi.getParameter("sv_fk_num"));
		  //sDto.setSv_fk_nickname(mdto.getM_nickname());
	      sDto.setSv_contents(multi.getParameter("sv_contents"));
	     // sDto.setSv_date(multi.getParameter("sv_date"));
	     

	      Iterator<String> files = multi.getFileNames();
	      
	      
	      if(filecheck.equals("1")) {
	    	  while(files.hasNext()) {
	 	         String fn = files.next();
	 	         
	 	         //multiple 선택 파일 처리 -> 파일 목록 가져오기
	 	         List<MultipartFile> fList = multi.getFiles(fn);
	 	         
	 	         //각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
	 	         for(int i = 0; i < fList.size(); i++) {
	 	            MultipartFile mf = fList.get(i);
	 	            
	 	            //파일명 추출
	 	            String oriname = mf.getOriginalFilename();
	 	            
	 	            //변경할 이름 생성
	 	            String sysname = System.currentTimeMillis()
	 	               + oriname.substring(oriname.lastIndexOf("."));
	 	            
	 	            //upload 폴더 파일 저장
	 	            File ff = new File(path + sysname);
	 	            sDto.setSv_img(sysname);
	 	            mf.transferTo(ff);
	 	         }
	 	      }
	      }
	      
	      cdao.spotreviewInsert(sDto);
	   }


		   public String breDelete(String sv_pk_num, HttpSession session) {
		      String msg = null;

		      try {
		         cdao.breDelete(sv_pk_num);
		         msg = "yes";

		         
		      } catch (Exception e) {
		         e.printStackTrace();
		         msg = "no";
		      }

		      return msg;

		   } // qadelect end


		public Map<String, JejuspotreviewDto> replynumselect(String renum) {
			Map<String, JejuspotreviewDto> rmap = new HashMap<String, JejuspotreviewDto>();
			JejuspotreviewDto reDto = cdao.replynumselect(renum);
			
			rmap.put("reply", reDto);
			
			return rmap;
		}


		public void reviewUpdate(MultipartHttpServletRequest multi) throws Exception {
			String path = multi.getRealPath("/upload/");
		      JejuspotreviewDto sDto = new JejuspotreviewDto();
		      JejuspotDto jsdto = new JejuspotDto();
		      
		      String filecheck = (String)multi.getParameter("FileCheck");
		      
		         File upFolder = new File(path);
		         if(!upFolder.isDirectory()) {
		            upFolder.mkdir();
		         }
		      String pknum = multi.getParameter("sv_pk_num");
		      JejuspotreviewDto reDto = cdao.replynumselect(pknum);
		      deleteFile(reDto.getSv_img());
		      sDto.setSv_pk_num(pknum);   
		      sDto.setSv_star(multi.getParameter("sv_star"));
		      sDto.setSv_fk_id(multi.getParameter("sv_fk_id"));
		      sDto.setSv_fk_num(multi.getParameter("sv_fk_num"));
			  //sDto.setSv_fk_nickname(mdto.getM_nickname());
		      sDto.setSv_contents(multi.getParameter("sv_contents"));
		     // sDto.setSv_date(multi.getParameter("sv_date"));
		     

		      Iterator<String> files = multi.getFileNames();
		      
		      
		      if(filecheck.equals("1")) {
		    	  while(files.hasNext()) {
		 	         String fn = files.next();
		 	         
		 	         //multiple 선택 파일 처리 -> 파일 목록 가져오기
		 	         List<MultipartFile> fList = multi.getFiles(fn);
		 	         
		 	         //각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
		 	         for(int i = 0; i < fList.size(); i++) {
		 	            MultipartFile mf = fList.get(i);
		 	            
		 	            //파일명 추출
		 	            String oriname = mf.getOriginalFilename();
		 	            
		 	            //변경할 이름 생성
		 	            String sysname = System.currentTimeMillis()
		 	               + oriname.substring(oriname.lastIndexOf("."));
		 	            
		 	            //upload 폴더 파일 저장
		 	            File ff = new File(path + sysname);
		 	            sDto.setSv_img(sysname);
		 	            mf.transferTo(ff);
		 	         }
		 	      }
		      }
		      
		      cdao.spotreviewUpdate(sDto);
		}
		
		public void deleteFile(String filename) {
	        
	        // 파일의 경로 + 파일명
	        String path = session.getServletContext().getRealPath("/");
	        path += "/upload/" + filename;
	        
	        File deleteFile = new File(path);
	        
	        System.out.println(path);
	 
	        // 파일이 존재하는지 체크 존재할경우 true, 존재하지않을경우 false
	        if(deleteFile.exists()) {
	            
	            // 파일을 삭제합니다.
	            deleteFile.delete(); 
	            
	            System.out.println("파일을 삭제하였습니다.");
	            
	        } else {
	            System.out.println("파일이 존재하지 않습니다.");
	        }
	    }
		
		
		
		
		
		public ModelAndView eventList(ListDto list) {
			log.info("eventList()");
			int listCnt = 4;
			mv = new ModelAndView();

			if (list.getPageNum() == 0) {
				list.setPageNum(1);
			}
			int num = list.getPageNum();
			list.setPageNum((num - 1) * listCnt);
			list.setListCnt(listCnt);

			List<EventDto> edto = cdao.eventListselect(list);

			mv.addObject("edto", edto);
			mv.setViewName("JejuEvent_List");

			list.setPageNum(num);
			listName = "JejuEvent_List?";
			String pageHtml = geteventPaging(list);
			mv.addObject("paging", pageHtml);

			return mv;
		}

		private String geteventPaging(ListDto list) {
			String pageHtml = null;

			// 전체 글개수 구하기
			int maxNum = cdao.eventCntList(list);
			// 한 페이지 당 보여질 페이지 번호의 개수
			int pageCnt = 5;

			PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), list.getListCnt(), pageCnt, listName);

			pageHtml = paging.makePaging();

			return pageHtml;
		}
		
		public ModelAndView eventInfoList(Integer e_pk_enum) {
			log.info("eventInfoList()");
			mv = new ModelAndView();
			
			EventDto edto = cdao.eventList(e_pk_enum);
			
			mv.addObject("edto", edto);
			
			mv.setViewName("JejuEvent");
			
			return mv;
			
		}
}

	

