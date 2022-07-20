package com.openai.jeju.service;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dao.MypageDao;
import com.openai.jeju.dto.BasketDto;
import com.openai.jeju.dto.BimgDto;
import com.openai.jeju.dto.BlogDto;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.EventDto2;
import com.openai.jeju.dto.IamBasketDto;
import com.openai.jeju.dto.LastBlogDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NowTripDto;
import com.openai.jeju.dto.PastTripBlogDto;
import com.openai.jeju.dto.RevDto;
import com.openai.jeju.dto.TravelrouteDto;
import com.openai.jeju.util.PagingUtil;

import lombok.extern.java.Log;

@Service
@Log
public class MypageService {
	
	@Autowired
	private MypageDao myDao;
	
	@Autowired
	private HttpSession session;
	
	private ModelAndView mv;
	
	private MemberDto sessionmember;
	
	//최근 거래내역 뽑아오기(출력 개수 제한!)
	public ModelAndView getmypage(IamBasketDto list, PastTripBlogDto blist, HttpSession session) {
		sessionmember = (MemberDto) session.getAttribute("user");
		
		mv = new ModelAndView();
		
		//마이페이지 최근거래내역 보여주는곳
		list.setBk_fk_id(sessionmember.getM_pk_id());
		
		System.out.println(sessionmember.getM_pk_id());
		
		List<IamBasketDto> bList = myDao.mypageListSelect(list);//리턴된값은 bList로 다시 넘겨 받는다.
		
		System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFffff");
		
		//마이페이지 여행코스 보여주는곳
		blist.setB_fk_id(sessionmember.getM_pk_id());
		
		List<PastTripBlogDto> BlogList = myDao.mypageBlogSelect(blist);//리턴값은 tList로 다시 넘겨 받는다.
		
		
		mv.addObject("bList",bList);
		mv.addObject("BlogList",BlogList);
		
		System.out.println(bList);
		System.out.println(BlogList);
		
		mv.setViewName("mypage");
		
		return mv;
	}

	
	private int listCnt = 13; //페이지 당 게시글 개수
	private String listName = "";
	
	//최근 거래내역 뽑아오기(해당 아이디의 전체 출력)
	public ModelAndView getDealList(IamBasketDto list, HttpSession session) {
		
		mv = new ModelAndView();
		
		if(list.getPageNum() == 0) {
	         list.setPageNum(1);
	      }
		int num = list.getPageNum();
		list.setPageNum((num - 1) * listCnt);
		list.setListCnt(listCnt);
		list.setBk_fk_id(sessionmember.getM_pk_id());
		
		String cate = list.getC_category();  
		
		if(cate != null && cate.equals("c_all")) { //카테고리 옵션이 null이 아니고 c_all과 같을때  카테고리를 null로 바꿔줌
			list.setC_category(null);
		}
		
		List<IamBasketDto> bList = myDao.DealListSelect(list);//리턴된 값은 bList로 다시 넘겨 받는다.
		
		mv.addObject("bList", bList);
		
		System.out.println("NNNNNNNNNNNNn"+ bList);
		
		//페이징 처리
		list.setPageNum(num);
		listName = "Mypage_DealList?";
		String pageHtml = getPaging(list);
		mv.addObject("paging", pageHtml);
		
		
		System.out.println("ASASAS" + pageHtml);
		
		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", list.getPageNum());
		if(list.getColname() != null) {
			session.setAttribute("list", list);
		}
		else { 
			session.removeAttribute("list");
		}
		
		mv.setViewName("Mypage_DealList");
		

		
		return mv;
	}

	//페이징 도움
	private String getPaging(IamBasketDto list) {
		String pageHtml = null;
		list.setBk_fk_id(sessionmember.getM_pk_id());
		//전체 글개수 구하기
		int maxNum = myDao.bcntSelect(list);
		//한 페이지당 보여질 페이지 번호의 개수
		int pageCnt = 5;
		
		
		//검색시 페이징 처리
		if(list.getC_name() != null) {
			listName += "&c_name=" + list.getC_name() + "&";
			
			System.out.println("mMMMMm");
		}
		
		//업체 종류 옵션선택시 페이징처리
		if(list.getC_category() != null) {
			listName += "&c_category=" + list.getC_category() + "&";
		}
		
		
		System.out.println("@@@@@@");
		
		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName); //클래스 임포트!
		
		pageHtml = paging.makePaging();
		
		return pageHtml;
	}
	
	//Review_Lodgment 페이지에 숙박 리뷰 출력하기! 
	public ModelAndView getLodgmentList(RevDto rlist ,HttpSession session) {
		mv = new ModelAndView();
		
		//세션아이디와 동일한것만 뽑아오기
		rlist.setRv_fk_id(sessionmember.getM_pk_id());
		
		
		System.out.println(sessionmember.getM_pk_id());
		
		List<RevDto> rList = myDao.LodgmentListSelect(rlist);//리턴된값은 rList로 다시 념겨 받는다 
		
		session.setAttribute("rv", rList);
		
		System.out.println(session.getAttribute("rv") + "AAAAAAAAAAAAAAAa");
		
		mv.addObject("rList",rList);
		
		System.out.println(rList);
		
		mv.setViewName("Review_Lodgment");
		
		return mv;

	}

	//Review_Activiry에 레저 리뷰 출력
	public ModelAndView getActiviryList(RevDto rlist, HttpSession session2) {
		mv = new ModelAndView();
		
		//세션아이디와 동일한것만 뽑아오기
		rlist.setRv_fk_id(sessionmember.getM_pk_id());
		
		List<RevDto> rList = myDao.ActiviryListSelect(rlist);//리턴된값은 rList로 다시 념겨 받는다 
		
		session.setAttribute("rv", rList);
		
		System.out.println(session.getAttribute("rv") + "AAAAAAAAAAAAAAAa");
		
		mv.addObject("rList",rList);
		
		System.out.println(rList);
		
		mv.setViewName("Review_Activiry");
		
		return mv;
	}

	//Review_Food에 식당 리뷰 출력
	public ModelAndView getFoodList(RevDto rlist, HttpSession session2) {
		mv = new ModelAndView();
		
		//세션아이디와 동일한것만 뽑아오기
		rlist.setRv_fk_id(sessionmember.getM_pk_id());
		
		
		System.out.println(sessionmember.getM_pk_id());
		
		List<RevDto> rList = myDao.FoodListSelect(rlist);//리턴된값은 rList로 다시 념겨 받는다 
		
		session.setAttribute("rv", rList);
		
		System.out.println(session.getAttribute("rv") + "AAAAAAAAAAAAAAAa");
		
		mv.addObject("rList",rList);
		
		System.out.println(rList);
		
		mv.setViewName("Review_Food");
		
		return mv;
	}
	
	//Review_Lodgment , Activiry , Food 에 삭제버튼을 눌렀을때 해당 리스트 삭제
	public String reviewDel(Integer rv_pk_num,HttpSession session, RedirectAttributes rttr) {
		log.info("reviewDel()");
		String msg = null;
		
		try {
			myDao.delreview(rv_pk_num);
			msg = "ok";
		}catch (Exception e) {
			e.printStackTrace();
			msg = "no";
		}
		return msg;
	}
	
	//Mypage_NowTrip 페이지 진행중인 리스트 출력
//	public ModelAndView getmyNowTrip(NowTripDto ndto, HttpSession session) {
//		mv = new ModelAndView();
//		
//		ndto.setTr_fk_id(sessionmember.getM_pk_id());
//		
//		List<NowTripDto> nList = myDao.NowTripListSelect(ndto);
//		
//		mv.addObject("nList",nList);
//		
//		mv.setViewName("Mypage_NowTrip");
//		
//		return mv;
//	}



	//Mypage_Plan 에 게시글 리스트 출력
	public ModelAndView getPlanList(BlogDto blist, ListDto list, HttpSession session) {
		int listCnt = 10; //페이지 당 게시글 개수
		
		mv = new ModelAndView();
		
//		if(list.getPageNum()==0) {
//			list.setPageNum(1);
//		}
//			int num = list.getPageNum();
//			list.setPageNum((num-1)*listCnt);
//			list.setListCnt(listCnt);
//			list.setB_fk_id(sessionmember.getM_pk_id());
//		
//		List <BlogDto> bList = myDao.PlanList(list);
//		//List<BlogDto> bList = mDao.PlanList(blist);
//		mv.addObject("bList", bList);
//		
//		//페이징 처리
//		list.setPageNum(num);
//		String pageHtml = getPaging(list);
//		mv.addObject("paging", pageHtml);
//		
//		//세션에 페이지번호 저장 돌아갈 떄 사용할 페이지 번호를 저장
//		session.setAttribute("pageNum", list.getPageNum());
//		if(list.getColname() != null) {
//			session.setAttribute("list", list);
//		}
//		else { //검색이 아닐 경우는 세션의 ListDto를 제거
//			session.removeAttribute("list");
//		}
		
		String msg = "준비중인 게시판입니다.";
		
		mv.addObject("msg", msg);
		
		mv.setViewName("mypage");
		
		return mv;
	}
	
//	private String getPaging(ListDto list) {
//		String pageHtml = null;
//		
//		//Plan 게시글 개수 구하기
//		int maxNum = myDao.PlancntSelect(list);
//		//한 페이지 당 보여질 페이지 번호의 개수
//		int pageCnt = 5;
//		String listName = "Mypage_Plan?";
//		
//		//검색용 컬럼명과 검색어를 추가
//		if(list.getColname() != null) {
//			
//		}
//		
//		PagingUtil paging = new PagingUtil(maxNum, list.getPageNum(), listCnt, pageCnt, listName);
//		
//		pageHtml = paging.makePaging();
//		
//		return pageHtml; //리턴
//	}
	
	
	//Mypage_Plan 에 해당 정보 클릭시 클릭된 정보와 일치한 상세내용이 팝업창에 출력
	public LastBlogDto planPopup(Integer b_pk_num,LastBlogDto bdto ,HttpSession session, RedirectAttributes rttr) {
		log.info("planPopup()");
		LastBlogDto blist = new LastBlogDto();
		
		try {
			blist = myDao.planPopup(b_pk_num);
			System.out.println(blist);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blist;
	}
	
	
	//Mypage_Plan 페이지에 팝업창에 삭제버튼을 실행했을때
		public String planDel(Integer b_pk_num, HttpSession session, RedirectAttributes rttr) {
			log.info("planDel()");
			String msg = null;
			
			try {
				myDao.plandel(b_pk_num);
				msg = "ok";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "no";
			}
			
			return msg;
		}
		
		
		//Myapge_plan페이지에서 수정버튼을 눌렀을떄 나오는 페이지
		public ModelAndView Mypage_Plan_Update(String bnum) {
			mv = new ModelAndView();
			
			LastBlogDto bdto = myDao.planPopup(Integer.parseInt(bnum));
			
			mv.addObject("bdto", bdto);
			mv.setViewName("Mypage_Plan_Update");
			
			return mv;
		}
		
/*		
		//Plan_Update 페이지 수정버튼 클릭시 발동
		public String PlanUpdate(MultipartHttpServletRequest multi, Integer b_pk_num, BlogDto bdto, BimgDto bidto, RedirectAttributes rttr) {
			log.info("planUpdate()");
			
			String view = null;
			String msg = null;
			
//			BlogDto blogdto = new BlogDto();
//			
//			blogdto.setB_img(multi.getParameter("b_img"));
//			blogdto.setB_title(multi.getParameter("b_title"));
//			blogdto.setB_contents(multi.getParameter("b_contents"));
//			
//			BimgDto bimgdto = new BimgDto();
//			
//			bimgdto.setBi_oriname(multi.getParameter("bi_oriname"));
//			bimgdto.setBi_sysname(multi.getParameter("bi_sysname"));
			
			
			try {
				mDao.blogUpdate(bdto);
				mDao.bimgUpdate(bidto);
				
				
				view ="redirect:/Mypage_Plan";
				msg = "수정성공";
			} catch (Exception e) {
				e.printStackTrace();
				view ="redirect:/Mypage_Plan";
				msg = "수정실패";
			}
		
			rttr.addFlashAttribute("msg", msg);
			
			return view;
		}
*/		 
		
		
		//Plan_Update 페이지 수정하기 버튼 실행시 발동
				public String updatePlan(Integer bnum, BlogDto bdto, BimgDto bidto, String data) {
					log.info("updatePlan()");
					String res = null;
					
				
					
					try {
						myDao.blogUpdate(bnum);
						myDao.bimgUpdate(bidto);
						
						res = "ok";
					} catch (Exception e) {
						e.printStackTrace();
						res = "no";
					}
					
					return res;
				}
		
		
				
		//NowTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보를 페이지에 출력
		public Map<String, Object> nowtripinfo(Integer tr_pk_num, HttpSession session2) {
			log.info("nowtripinfo()");
			
			int price = 0;
			
			Map<String, Object> rmap = new HashMap<String, Object>();
			
			EventDto2 eDto = new EventDto2();
			
			List<EventDto2> eList = new ArrayList<EventDto2>();
			
			TravelrouteDto ntinfo = new TravelrouteDto();
			
			try {
				ntinfo = myDao.nowtripinfo(tr_pk_num);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<BasketDto> bList = myDao.TnumSelectBasketList(tr_pk_num);
			
			
			for(int i = 0; i<bList.size(); i++) {
				price += bList.get(i).getBk_price();
				eDto.setTitle(bList.get(i).getBk_goods());
				eDto.setStart(bList.get(i).getBk_in());
				eDto.setEnd(bList.get(i).getBk_out());
				String url = "Lodgment?cnum=" + bList.get(i).getBk_fk_cnum();
				eDto.setUrl(url);
				
				eList.add(eDto);
			}
			
			rmap.put("price", price);
			rmap.put("eList", eList);
			rmap.put("ntinfo", ntinfo);
			
			return rmap;
		}
		
		
		//NowTrip 페이지에 여행리스트 삭제 버튼 클릭시 발동
		public String nowtripDel(Integer tr_pk_num, HttpSession session) {
			log.info("nowtripDel()");
			String msg = null;
					
			try {
				myDao.nowtripbasketDel(tr_pk_num);
				myDao.nowtripDel(tr_pk_num);
				msg = "ok";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "no";
			}
					
			return msg;
		}
		
		
	//NowTrip 페이지에 여행리스트 여행완료 버튼 클릭시 발동
	public String nowtripCheck(Integer tr_pk_num, HttpSession session) {
		log.info("nowtripCheck()");
		String msg = null;
		
		try {
			myDao.nowtripCheck(tr_pk_num);
			msg = "ok";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "no";
		}
		
		return msg;
	}	
		
	
	
	//다렭 테스트 겸 여행 리스트 출력  진행중인여행
	public ModelAndView eventList(HttpSession session) {
		mv = new ModelAndView();
		sessionmember = (MemberDto) session.getAttribute("user");
		
//		ndto.setBk_fk_id((String)session.getAttribute("id"));
		
		List<TravelrouteDto> nList = myDao.NowTripListSelect(sessionmember.getM_pk_id());
		
//		mDao.cListInsert(ndto);
		
		mv = new ModelAndView();
		mv.addObject("nList",nList);
		mv.setViewName("Mypage_NowTrip");
		
		return mv;
	}

	//지난 여행
	public ModelAndView pastList(HttpSession session) {
		mv = new ModelAndView();
		
		sessionmember = (MemberDto) session.getAttribute("user");
		
//		ndto.setBk_fk_id((String)session.getAttribute("id"));
		
		List<TravelrouteDto> nList = myDao.pastTripListSelect(sessionmember.getM_pk_id());
		
//		mDao.cListInsert(ndto);
		
		mv = new ModelAndView();
		mv.addObject("nList",nList);
		mv.setViewName("Mypage_PastTrip");
		
		return mv;
	}

	
	//PastTrip 페이지에 여행리스트 정보 클릭시 해당 상세정보 페이지 출력
			public Map<String, Object> pasttripinfo(Integer tr_pk_num, NowTripDto ntdto, HttpSession session) {
				log.info("pasttripinfo()");

				int price = 0;
				
				Map<String, Object> rmap = new HashMap<String, Object>();
				
				EventDto2 eDto = new EventDto2();
				
				List<EventDto2> eList = new ArrayList<EventDto2>();
				
				TravelrouteDto ntinfo = new TravelrouteDto();
				
				try {
					ntinfo = myDao.pasttripinfo(tr_pk_num);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				List<BasketDto> bList = myDao.TnumSelectBasketList(tr_pk_num);
				
				
				for(int i = 0; i<bList.size(); i++) {
					price += bList.get(i).getBk_price();
					eDto.setTitle(bList.get(i).getBk_goods());
					eDto.setStart(bList.get(i).getBk_in());
					eDto.setEnd(bList.get(i).getBk_out());
					String url = "Lodgment?cnum=" + bList.get(i).getBk_fk_cnum();
					eDto.setUrl(url);
					
					eList.add(eDto);
				}
				
				rmap.put("price", price);
				rmap.put("eList", eList);
				rmap.put("ntinfo", ntinfo);
				
				return rmap;
			}

		
			//PastTrip 페이지에 삭제버튼 클릭시 발동
			public String pasttripDel(Integer bk_fk_tnum, HttpSession session) {
				log.info("pasttripDel()");
				String msg = null;
				BlogDto bDto = myDao.bnumblogselect(bk_fk_tnum);
				
				try {
					//myDao.pasttripbimgDel(bDto.getB_pk_num());
					myDao.pasttripblogDel(bk_fk_tnum);
					myDao.pasttripbasketDel(bk_fk_tnum);
					myDao.pasttripDel(bk_fk_tnum);
					msg = "ok";
				} catch (Exception e) {
					e.printStackTrace();
					msg = "no";
				}
				
				return msg;
			}
			
			
			
			   // 찜하기 숙박
			   public ModelAndView favoritesList(CompanyDto cdto) {
			      log.info("favoritesList()");
			      mv = new ModelAndView();

			      String fa_fk_id = (sessionmember.getM_pk_id());
			      cdto.setFa_fk_id(fa_fk_id);

			      String cate = cdto.getC_category();
			      if (cate == null) {
			         cdto.setC_category("숙박");
			      }

			      List<CompanyDto> cDto = myDao.getFavorites(cdto);

			      mv.addObject("cDto", cDto);
			      mv.addObject("ctype", cdto.getC_type());

			      mv.setViewName("Favorites_Lodgment");

			      return mv;
			   }

			   // 찜하기 식당
			   public ModelAndView favoritesFoodList(CompanyDto cdto) {
			      log.info("favoritesFoodList()");
			      mv = new ModelAndView();

			      String fa_fk_id = (sessionmember.getM_pk_id());
			      cdto.setFa_fk_id(fa_fk_id);

			      String cate = cdto.getC_category();
			      if (cate == null) {
			         cdto.setC_category("식당");
			      }

			      List<CompanyDto> cDto = myDao.getFavorites(cdto);

			      mv.addObject("cDto", cDto);
			      mv.addObject("ctype", cdto.getC_type());

			      mv.setViewName("Favorites_Food");

			      return mv;
			   }
			   
			   // 찜하기 레저
			   public ModelAndView favoritesActivityList(CompanyDto cdto) {
			      log.info("favoritesFoodList()");
			      mv = new ModelAndView();

			      String fa_fk_id = (sessionmember.getM_pk_id());
			      cdto.setFa_fk_id(fa_fk_id);

			      String cate = cdto.getC_category();
			      if (cate == null) {
			         cdto.setC_category("레저");
			      }

			      List<CompanyDto> cDto = myDao.getFavorites(cdto);

			      mv.addObject("cDto", cDto);
			      mv.addObject("ctype", cdto.getC_type());

			      mv.setViewName("Favorites_Activity");

			      return mv;
			   }

		
			   
			   
			   
			   
			   public String jjimDelet(CompanyDto cdto, HttpSession session, RedirectAttributes rttr) {
				      log.info("jjimDelet()");
				      String msg = null;

				      try {
				         myDao.jjimDel(cdto);
				         msg = "ok";
				      } catch (Exception e) {
				         e.printStackTrace();
				         msg = "no";
				      }
				      return msg;
				   }
			   
			   
			 //Plan_Update 페이지 수정버튼 클릭시 발동
				public String PlanUpdate(MultipartHttpServletRequest multi,  RedirectAttributes rttr) {
					log.info("planUpdate()");
					
					String view = null;
					String msg = null;
					
					//Integer bnum = b_pk_num;
					
					//int bnum = Integer.parseInt(multi.getParameter("b_pk_num"));
					//int b_pk_num =  (int) session.getAttribute("b_pk_num");
					//System.out.println("bbbbbbbbbbbbbbbbbbbbbb");
					//System.out.println(b_pk_num);
					
					//multi 에서 데이터 추출 및 Dto에 삽입
					BlogDto blogdto = new BlogDto();
					
					//blogdto.setB_pk_num((Integer.parseInt(multi.getParameter("b_pk_num"))));;
				//	blogdto.setB_pk_num(b_pk_num);
					blogdto.setB_pk_num(Integer.parseInt(multi.getParameter("b_pk_num")));
					blogdto.setB_img(multi.getParameter("b_img"));
					blogdto.setB_title(multi.getParameter("b_title"));
					blogdto.setB_cost((Integer.parseInt(multi.getParameter("b_cost"))));
					blogdto.setB_contents(multi.getParameter("b_contents"));
					
					BimgDto bimgdto = new BimgDto();
					
					bimgdto.setBi_fk_num(multi.getParameter("b_pk_num"));
					bimgdto.setBi_oriname(multi.getParameter("bi_oriname"));
					bimgdto.setBi_sysname(multi.getParameter("bi_sysname"));
					
					
					String check = multi.getParameter(msg);
					
					try {
						myDao.blogUpdate(blogdto);
						myDao.bimgUpdate(bimgdto);
						
						
						view ="redirect:/Mypage_Plan";
						msg = "수정성공";
					} catch (Exception e) {
				
						e.printStackTrace();
						view ="redirect:/Mypage_Plan";
						msg = "수정실패";
					}
				
					rttr.addFlashAttribute("msg", msg);
					
					return view;
				}

				public String nowTripUpdate(int trnum, HttpSession session) {
					TravelrouteDto tDto = myDao.travelSelect(trnum);
					
					String view = null;
					
					String cate = "'숙박'";
				      
				      try {
				         cate = URLEncoder.encode("'숙박'", "UTF-8");
				      } catch (UnsupportedEncodingException e1) {
				         e1.printStackTrace();
				      }
					
					session.setAttribute("tdto", tDto);
					
					view="redirect:/Lodgment_List?cate="+ cate;
					
					return view;
				}
		
			

	
}//class end







