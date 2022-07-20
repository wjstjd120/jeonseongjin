package com.openai.jeju.service;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.openai.jeju.dao.AdminDao;
import com.openai.jeju.dao.CompanyDao;
import com.openai.jeju.dao.MemberDao;
import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NoticeDto;
import com.openai.jeju.dto.SimgDto;
import com.openai.jeju.dto.SpotDto;
import com.openai.jeju.util.PagingUtil;

import lombok.extern.java.Log;

@Service
@Log
public class AdminService {
   @Autowired
      private AdminDao aDao;
      
      private ModelAndView mv;
      
      @Autowired
      private MemberDao mdao;
      @Autowired
      private CompanyDao cdao;
      
      @Autowired
      private HttpSession session;
      
      private int listCnt = 10;//페이지 당 출력할 게시글 개수
      
      String listName = "";
      
      int maxNum = 0;
      
      public ModelAndView selectMlist(ListDto list, 
            HttpSession session) {
         mv = new ModelAndView();
         
         //Dao에 보내는 데이터를 만들자.(검색 기능 추가로 코드 사용 안함)
         //Map<String, Integer> pmap = new HashMap<String, Integer>();
         //pmap.put("pnum", (pageNum - 1) * listCnt);
         //pmap.put("lcnt", listCnt);
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         if(list.getSage() == null) {list.setSage("1900-01-01");}
         if(list.getEage() == null) {list.setEage("2100-12-31");}
         if(list.getSage().equals("")) {list.setSage("1900-01-01");}
         if(list.getEage().equals("")) {list.setEage("2100-12-31");}
         
         List<MemberDto> mList = aDao.selectMlist(list);
         
         mv.addObject("mList", mList);
         
         MemberDto dto = new MemberDto();
         
         for (int i = 0; i < mList.size(); i++) {
               switch (mList.get(i).getM_gender()) {
               case "M":
                  dto = mList.get(i);
                  dto.setM_gender("남");
                  mList.set(i, dto);
                  break;
               case "F":
                  dto = mList.get(i);
                  dto.setM_gender("여");
                  mList.set(i, dto);
                  break;
               }
          }
         
         //페이징 처리
         list.setPageNum(num);
         maxNum = aDao.Mcntselect(list);
         listName = "Admin_Member_List?";
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Member_List");
         
         return mv;
      }
      
      public ModelAndView selectBMlist(ListDto list, 
            HttpSession session) {
         mv = new ModelAndView();
         
         
         list.setBl("블랙리스트");
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         if(list.getSage() == null) {list.setSage("1900-01-01");}
         if(list.getEage() == null) {list.setEage("2100-12-31");}
         if(list.getSage().equals("")) {list.setSage("1900-01-01");}
         if(list.getEage().equals("")) {list.setEage("2100-12-31");}
         
         List<MemberDto> mList = aDao.selectMlist(list);
         
         mv.addObject("mList", mList);
         
         MemberDto dto = new MemberDto();
         
         for (int i = 0; i < mList.size(); i++) {
               switch (mList.get(i).getM_gender()) {
               case "M":
                  dto = mList.get(i);
                  dto.setM_gender("남");
                  mList.set(i, dto);
                  break;
               case "F":
                  dto = mList.get(i);
                  dto.setM_gender("여");
                  mList.set(i, dto);
                  break;
               }
          }
         
         //페이징 처리
         list.setPageNum(num);
         listName = "Admin_Bmember_List?";
         maxNum = aDao.Mcntselect(list);
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Bmember_List");
         
         return mv;
      }
      
      public ModelAndView selectCompanylist(ListDto list, 
            HttpSession session) {
         mv = new ModelAndView();
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         List<CompanyDto> cList = aDao.selectClist(list);
         
         mv.addObject("cList", cList);
         
         
         CompanyDto dto = new CompanyDto();
         
         for (int i = 0; i < cList.size(); i++) {
               switch (cList.get(i).getC_check()) {
               case "1":
                  dto = cList.get(i);
                  dto.setC_check("승인");
                  cList.set(i, dto);
                  break;
               case "0":
                  dto = cList.get(i);
                  dto.setC_check("미승인");
                  cList.set(i, dto);
                  break;
               }
          }
         
         for (int i = 0; i < cList.size(); i++) {
               switch (cList.get(i).getC_condition()) {
               case "1":
                  dto = cList.get(i);
                  dto.setC_condition("영업중");
                  cList.set(i, dto);
                  break;
               case "0":
                  dto = cList.get(i);
                  dto.setC_condition("영업준비중");
                  cList.set(i, dto);
                  break;
               }
          }
         
         //페이징 처리
         list.setPageNum(num);
         listName = "Admin_Company_List?";
         maxNum = aDao.selectCntClist(list);
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Company_List");
         
         return mv;
      }
      
      public ModelAndView selectEventlist(ListDto list, 
            HttpSession session) {
         mv = new ModelAndView();
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         List<EventDto> eList = aDao.selectElist(list);
         
         mv.addObject("eList", eList);
         
         
         EventDto dto = new EventDto();
         
         for (int i = 0; i < eList.size(); i++) {
               switch (eList.get(i).getE_eventing()) {
               case "1":
                  dto = eList.get(i);
                  dto.setE_eventing("진행중");
                  eList.set(i, dto);
                  break;
               case "0":
                  dto = eList.get(i);
                  dto.setE_eventing("종료");
                  eList.set(i, dto);
                  break;
               }
          }
         
         //페이징 처리
         list.setPageNum(num);
         listName = "Admin_Event_List?";
         maxNum = aDao.selectCntElist(list);
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Event_List");
         
         return mv;
      }
      
      public ModelAndView selectSpotlist(ListDto list, 
            HttpSession session) {
         mv = new ModelAndView();
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         List<SpotDto> sList = aDao.selectSlist(list);
         
         mv.addObject("sList", sList);
         
         //페이징 처리
         list.setPageNum(num);
         listName = "Admin_Spot_List?";
         maxNum = aDao.selectCntSlist(list);
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Spot_List");
         
         return mv;
      }
      
      public ModelAndView selectNoticelist(ListDto list, 
            HttpSession session) {
         mv = new ModelAndView();
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         
         if(list.getColname() == null) {
            list.setColname("n_title");
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         List<NoticeDto> nList = aDao.selectNlist(list);
         
         mv.addObject("nList", nList);
         
         //페이징 처리
         list.setPageNum(num);
         listName = "Admin_Notice_List?";
         maxNum = aDao.selectCntNlist(list);
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Notice_List");
         
         return mv;
      }
      
      private String getPaging(ListDto list) {
         String pageHtml = null;
         
         //전체 글개수 구하기
         //한 페이지 당 보여질 페이지 번호의 개수
         int pageCnt = 5;
         
         
         //검색 용 컬럼명과 검색어를 추가
         if(list.getKeyword() != null) {
            listName += "colname="+ list.getColname()
               + "&keyword=" + list.getKeyword() + "&";
         } if(list.getCo() != null) {
            listName += "co="+ list.getCo() + "&";
         } if(list.getPr() != null) {
            listName += "pr="+ list.getPr() + "&";
         } if(list.getBl() != null) {
            listName += "bl="+ list.getBl() + "&";
         } if(list.getSage() != null) {
            listName += "sage="+ list.getSage() + "&";
         } if(list.getEage() != null) {
            listName += "eage="+ list.getEage() + "&";
         } if(list.getSelgender() != null) {
            listName += "selgender=" + list.getSelgender() + "&";
         } if(list.getAt() != null) {
            listName += "at=" + list.getAt() + "&";
         } if(list.getFo() != null) {
            listName += "fo=" + list.getFo() + "&";
         } if(list.getLod() != null) {
            listName += "lod=" + list.getLod() + "&";
         } if(list.getLod() != null) {
            listName += "condition=" + list.getConditionOn() + "&";
         } if(list.getLod() != null) {
            listName += "condition=" + list.getConditionOff() + "&";
         } if(list.getLod() != null) {
            listName += "check=" + list.getCheckOn() + "&";
         } if(list.getLod() != null) {
            listName += "check=" + list.getCheckOff() + "&";
         }   
         
         PagingUtil paging = new PagingUtil(maxNum, 
               list.getPageNum(),
               listCnt, pageCnt, listName);
         
         pageHtml = paging.makePaging();
         
         return pageHtml;
      }
      
      public MemberDto MemberIdSelect(String id) {
         MemberDto member = new MemberDto();
         
         member = aDao.MemberIdSelect(id);
         
         if(member.getM_gender().equals("M")) {
            member.setM_gender("남자");
         } else {
            member.setM_gender("여자");
         }
         
         return member;
      }

      
      @Transactional
      public String Admin_Member_Update(MemberDto mDto) {
         String msg = null;
         
         
         try {
            aDao.Admin_Member_Update(mDto);
            
            msg = "ok";
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            msg = "no";
         }   
         return msg;
      }

      @Transactional
      public String Admin_Member_Delete(String id) {
         String msg = null;
         
         try {
            Admin_Member_Company_Delete(id);
            
            aDao.Admin_Complaint_Mid_Delete(id);
            aDao.Admin_Member_Delete(id);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }
      
      @Transactional
      public void Admin_Member_Company_Delete(String id) {
         CompanyDto cDto =  aDao.CompanyMidSelect(id);
         
         String cimg = cDto.getC_img();
         
         deleteFile(cimg);
         
         try{
            aDao.Admin_Company_Mid_Delete(id);
         } catch (Exception e) {
            e.printStackTrace();
         }
            
      }
      
      @Transactional
      public String Bmember_Change_Category(String id) {
         String msg = null;

         try {
            aDao.Bmember_Change_Category(id);

            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();

            msg = "no";
         }

         return msg;
      }

      public String Member_Change_Category(String id) {
         String msg = null;
         
         try {
            aDao.Member_Change_Category(id);

            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();

            msg = "no";
         }

         return msg;
      }
      
      public EventDto fileUpload(MultipartHttpServletRequest multi) throws Exception {
         //request의 서버 정보에서 프로젝트 폴더의 절대 위치 정보 구하기.
         String path = multi.getRealPath("/upload/");
         
         EventDto eDto = new EventDto();
         String e_num = (String)multi.getParameter("e_pk_enum");
         String e_img = (String)multi.getParameter("e_img");
         
         eDto.setE_pk_enum(e_num);
         
         System.out.println();
         
            log.info("경로 : " + path);
            
            File upFolder = new File(path);
            if(!upFolder.isDirectory()) {
               upFolder.mkdir();
            }
         
          
            
         Iterator<String> files = multi.getFileNames();
         
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
               log.info(sysname);
               eDto.setE_img(sysname);
               mf.transferTo(ff);
               
               aDao.Admin_Event_File_Update(eDto);
            }
         }
         
         
         
         deleteFile(e_img);
         
         eDto = aDao.EventEnumSelect(e_num);
         
         return eDto;
      }
      
      public SpotDto SpotfileUpload(MultipartHttpServletRequest multi) throws Exception {
         //request의 서버 정보에서 프로젝트 폴더의 절대 위치 정보 구하기.
         String path = multi.getRealPath("/upload/");
         
         SpotDto sDto = new SpotDto();
         String snum = (String)multi.getParameter("s_pk_num");
         String s_img = (String)multi.getParameter("s_img");
         
         sDto.setS_pk_num(snum);
         
         System.out.println();
         
            log.info("경로 : " + path);
            
            File upFolder = new File(path);
            if(!upFolder.isDirectory()) {
               upFolder.mkdir();
            }
         
          
            
         Iterator<String> files = multi.getFileNames();
         
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
               log.info(sysname);
               sDto.setS_img(sysname);
               mf.transferTo(ff);
               
               aDao.Admin_Spot_File_Update(sDto);
            }
         }
         
         
         
         deleteFile(s_img);
         
         sDto = aDao.SpotSnumSelect(snum);
         
         return sDto;
      }
      
      public SpotDto SpotMultifileUpload(MultipartHttpServletRequest multi) throws Exception {
         //request의 서버 정보에서 프로젝트 폴더의 절대 위치 정보 구하기.
         String path = multi.getRealPath("/upload/");
         
         String snum = (String)multi.getParameter("s_pk_num");
         
         
            log.info("경로 : " + path);
            
            File upFolder = new File(path);
            if(!upFolder.isDirectory()) {
               upFolder.mkdir();
            }
            
          List<SimgDto> siList = aDao.selectSimglist(snum);
          
          for(int i = 0; i < siList.size(); i++) {
             
             deleteFile(siList.get(i).getSi_sysname());
          }
          
          aDao.SimgDel(snum);
            
          SimgDto siDto = new SimgDto();
          siDto.setSi_fk_num(snum);
          
         Iterator<String> files = multi.getFileNames();
         
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
               log.info(sysname);
               mf.transferTo(ff);
               
               siDto.setSi_oriname(oriname);
               siDto.setSi_sysname(sysname);
               
               aDao.insertSimg(siDto);
            }
         }
         
         
         SpotDto sDto = aDao.SpotSnumSelect(snum);
         
         return sDto;
      }
      
      public void Admin_Spot_Insert (MultipartHttpServletRequest multi) throws Exception {
         //request의 서버 정보에서 프로젝트 폴더의 절대 위치 정보 구하기.
         String path = multi.getRealPath("/upload/");
         String spkNum = null;
         SpotDto sDto = new SpotDto();
         SimgDto siDto = new SimgDto();
         
         String filecheck = (String)multi.getParameter("FileCheck");
         String multifilecheck = (String)multi.getParameter("MultiFileCheck");
         
         log.info(filecheck);
         log.info(multifilecheck);
         
         
         
            log.info("경로 : " + path);
            
            File upFolder = new File(path);
            if(!upFolder.isDirectory()) {
               upFolder.mkdir();
            }
            
          String stitle = (String)multi.getParameter("s_tittle");
         String saddr = (String)multi.getParameter("s_addr");
         String sinfo = (String)multi.getParameter("s_info");
         String sguide = (String)multi.getParameter("s_guide");
         
         sDto.setS_tittle(stitle);
         sDto.setS_addr(saddr);
         sDto.setS_info(sinfo);
         sDto.setS_guide(sguide);
         
         aDao.Admin_Spot_Insert(sDto);
         
         siDto.setSi_fk_num(sDto.getS_pk_num());
         log.info(sDto.getS_pk_num());
          
         Iterator<String> files = multi.getFileNames();
         
         
         if(multifilecheck.equals("1") && filecheck.equals("1")) {
            while(files.hasNext()) {
               String fn = files.next();

               //multiple 선택 파일 처리 -> 파일 목록 가져오기
               List<MultipartFile> fList = multi.getFiles(fn);
               
               if(fn.equals("titlefile")) {
                  fList = multi.getFiles(fn);
               //각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
                     MultipartFile mf = fList.get(0);
                     
                     //파일명 추출
                     String oriname = mf.getOriginalFilename();
                     
                     //변경할 이름 생성
                     String sysname = System.currentTimeMillis()
                        + oriname.substring(oriname.lastIndexOf("."));
                     
                     //upload 폴더 파일 저장
                     File ff = new File(path + sysname);
                     log.info(sysname);
                     mf.transferTo(ff);
                     
                     sDto.setS_img(sysname);
                     
                     aDao.Admin_Spot_File_Update(sDto);
               }
               
               if(fn.equals("titlefileMulti")) {
                  fList = multi.getFiles(fn);
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
                     log.info(sysname);
                     mf.transferTo(ff);

                     siDto.setSi_oriname(oriname);
                     siDto.setSi_sysname(sysname);

                     aDao.insertSimg(siDto);
                  }

               }
            }

         }
         
         if(multifilecheck.equals("1")) {
            while(files.hasNext()) {
               String fn = files.next();

               //multiple 선택 파일 처리 -> 파일 목록 가져오기
               List<MultipartFile> fList = multi.getFiles(fn);
               
               if(fn.equals("titlefileMulti")) {
                  fList = multi.getFiles(fn);
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
                     log.info(sysname);
                     mf.transferTo(ff);

                     siDto.setSi_oriname(oriname);
                     siDto.setSi_sysname(sysname);

                     aDao.insertSimg(siDto);
                  }

               }
            }

         }
         
         if(filecheck.equals("1")) {
            
            while(files.hasNext()) {
               String fn = files.next();
               
               //multiple 선택 파일 처리 -> 파일 목록 가져오기
               List<MultipartFile> fList = multi.getFiles(fn);
               
               if(fn.equals("titlefile")) {
                  fList = multi.getFiles(fn);
               //각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
                     MultipartFile mf = fList.get(0);
                     
                     //파일명 추출
                     String oriname = mf.getOriginalFilename();
                     
                     //변경할 이름 생성
                     String sysname = System.currentTimeMillis()
                        + oriname.substring(oriname.lastIndexOf("."));
                     
                     //upload 폴더 파일 저장
                     File ff = new File(path + sysname);
                     log.info(sysname);
                     mf.transferTo(ff);
                     
                     sDto.setS_img(sysname);
                     
                     aDao.Admin_Spot_File_Update(sDto);
               }
            }
         }
         
         
      }

      
      public CompanyDto CompanyfileUpload(MultipartHttpServletRequest multi) throws Exception {
         //request의 서버 정보에서 프로젝트 폴더의 절대 위치 정보 구하기.
         String path = multi.getRealPath("/upload/");
         
         CompanyDto cDto = new CompanyDto();
         String cnum = (String)multi.getParameter("c_pk_cnum");
         String c_img = (String)multi.getParameter("c_img");
         
         cDto.setC_pk_cnum(cnum);
         
         System.out.println();
         
            log.info("경로 : " + path);
            
            File upFolder = new File(path);
            if(!upFolder.isDirectory()) {
               upFolder.mkdir();
            }
         
          
            
         Iterator<String> files = multi.getFileNames();
         
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
               log.info(sysname);
               cDto.setC_img(sysname);
               mf.transferTo(ff);
               
               aDao.Admin_Company_File_Update(cDto);
            }
         }
         
         
         
         deleteFile(c_img);
         
         cDto = aDao.CompanyCnumSelect(cnum);
         
         return cDto;
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

      public CompanyDto CompanyCnumSelect(String cnum) {
         CompanyDto company = new CompanyDto();
         
         company = aDao.CompanyCnumSelect(cnum);
         
         if(company.getC_condition().equals("0")) {
            company.setC_condition("영업준비중");
         } else {
            company.setC_condition("영업중");
         }
         
         if(company.getC_check().equals("0")) {
            company.setC_check("승인대기중");
         } else {
            company.setC_check("승인");
         }
         
         return company;
      }

      public String Admin_Company_Update(CompanyDto cDto) {
         String msg = null;
         
         
         
         try {
            aDao.Admin_Company_Update(cDto);
            msg = "ok";
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            msg = "no";
         }
         
         cDto = cdao.getDto(cDto.getC_pk_cnum());
         
         if(cDto.getC_check().equals("1")) {
            try {
            mdao.upgradeMember(cDto.getC_fk_id());
         } catch (Exception e) {
            // TODO: handle exception
         }
         }
         
         return msg;
         
      }

      public EventDto EventEnumSelect(String e_num) {
         EventDto event = new EventDto();
         
         event = aDao.EventEnumSelect(e_num);
         
         if(event.getE_eventing().equals("1")) {
            event.setE_eventing("진행중");
         } else {
            event.setE_eventing("종료");
         }
         
         return event;
      }

      public String Admin_Event_Update(EventDto eDto) {
         String msg = null;
         
         
         try {
            aDao.Admin_Event_Update(eDto);
            
            msg = "ok";
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            msg = "no";
         }   
         return msg;
      }

      public SpotDto SpotSnumSelect(String snum) {
         SpotDto spot = new SpotDto();
         
         spot = aDao.SpotSnumSelect(snum);
         
         
         return spot;
      }

      public List<SimgDto> selectSimglist(String snum) {
         List<SimgDto> simg = aDao.selectSimglist(snum);
         
         return simg;
      }

      public String Admin_Spot_Update(SpotDto sDto) {
         String msg = null;
         
         
         try {
            aDao.Admin_Spot_Update(sDto);
            
            msg = "ok";
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            msg = "no";
         }   
         return msg;
      }

      public NoticeDto NoticeNnumSelect(String nnum) {
         NoticeDto notice = new NoticeDto();
         
         notice = aDao.NoticeNnumSelect(nnum);
         
         
         return notice;
      }

      public String Admin_Notice_Update(NoticeDto nDto) {
         String msg = null;
         
         
         try {
            aDao.Admin_Notice_Update(nDto);
            
            msg = "ok";
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            msg = "no";
         }   
         return msg;
      }

      public ModelAndView selectComplaintlist(ListDto list, HttpSession session2) {
         mv = new ModelAndView();
         
         if(list.getPageNum() == 0) {
            list.setPageNum(1);
         }
         
         if(list.getColname() == null) {
            list.setColname("co_title");
         }
         int num = list.getPageNum();
         list.setPageNum((num - 1) * listCnt);
         list.setListCnt(listCnt);
         
         List<ComplaintDto> coList = aDao.selectColist(list);
         
         mv.addObject("coList", coList);
         
         ComplaintDto dto = new ComplaintDto();
         
         for (int i = 0; i < coList.size(); i++) {
               switch (coList.get(i).getCo_check()) {
               case "1":
                  dto = coList.get(i);
                  dto.setCo_check("답변완료");
                  coList.set(i, dto);
                  break;
               case "0":
                  dto = coList.get(i);
                  dto.setCo_check("미답변");
                  coList.set(i, dto);
                  break;
               }
          }
         
         //페이징 처리
         list.setPageNum(num);
         listName = "Admin_Complaint_List?";
         maxNum = aDao.selectCntColist(list);
         String pageHtml = getPaging(list);
         mv.addObject("paging", pageHtml);
         
         //세션에 페이지번호 저장(글쓰기 또는 글 상세보기 화면에서 목록 화면으로
         //돌아 갈 때 사용할 페이지 번호를 저장)
         session.setAttribute("pageNum", list.getPageNum());
         if(list.getColname() != null) {
            session.setAttribute("list", list);
         }
         
         else {
            session.removeAttribute("list");
         }
         
         mv.setViewName("Admin_Complaint_List");
         
         return mv;
      }

      public ComplaintDto ComplaintConumSelect(String conum) {
         ComplaintDto complaint = new ComplaintDto();
         
         complaint = aDao.ComplaintConumSelect(conum);
         
         
         return complaint;
      }

      public String Admin_Complaint_Update(ComplaintDto coDto) {
         String msg = null;
         
         
         try {
            aDao.Admin_Complaint_Update(coDto);
            
            msg = "ok";
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            msg = "no";
         }   
         return msg;
      }

      public String Admin_Event_Delete(String e_num) {
         String msg = null;
         EventDto eDto =  aDao.EventEnumSelect(e_num);
         
         String eimg = eDto.getE_img();
         
         deleteFile(eimg);
         try {
            aDao.Admin_Event_Delete(e_num);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }
      
      public String Admin_Company_Delete(String cnum) {
         String msg = null;
         CompanyDto cDto =  aDao.CompanyCnumSelect(cnum);
         
         String cimg = cDto.getC_img();
         
         deleteFile(cimg);
         
         
         try {
            aDao.Admin_Company_Delete(cnum);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }

      public String Admin_Notice_Delete(String nnum) {
         String msg = null;
         
         try {
            aDao.Admin_Notice_Delete(nnum);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }

      public String Admin_Complaint_Delete(String conum) {
         String msg = null;
         
         try {
            aDao.Admin_Complaint_Delete(conum);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }

      public String Admin_Spot_Delete(String snum) {
         String msg = null;
         SpotDto sDto =  aDao.SpotSnumSelect(snum);
         List<SimgDto> siList = aDao.selectSimglist(snum);
         
         String simg = sDto.getS_img();
          
          for(int i = 0; i < siList.size(); i++) {
             
             deleteFile(siList.get(i).getSi_sysname());
          }
          
          aDao.SimgDel(snum);
         
         deleteFile(simg);
         
         try {
            aDao.Admin_Simg_Snum_Delete(snum);
            aDao.Admin_Spot_Delete(snum);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }

      public String Admin_Notice_Insert(NoticeDto nDto) {
         String msg = null;
         
         try {
            aDao.Admin_Notice_Insert(nDto);
            
            msg = "ok";
         } catch (Exception e) {
            e.printStackTrace();
            
            msg = "no";
         }
         
         return msg;
      }

      public void Admin_Event_Insert(MultipartHttpServletRequest multi) throws Exception {
         String path = multi.getRealPath("/upload/");
         EventDto eDto = new EventDto();
         
         String filecheck = (String)multi.getParameter("FileCheck");
         
         log.info(filecheck);
         
            log.info("경로 : " + path);
            
            File upFolder = new File(path);
            if(!upFolder.isDirectory()) {
               upFolder.mkdir();
            }
            
          String etitle = (String)multi.getParameter("e_title");
         String eaddr = (String)multi.getParameter("e_addr");
         String einfo = (String)multi.getParameter("e_info");
         String eeday = (String)multi.getParameter("e_eday");
         
         eDto.setE_title(etitle);
         eDto.setE_addr(eaddr);
         eDto.setE_info(einfo);
         eDto.setE_eday(eeday);
          
         Iterator<String> files = multi.getFileNames();
         
         
         if(filecheck.equals("1")) {
            while(files.hasNext()) {
               String fn = files.next();

               //multiple 선택 파일 처리 -> 파일 목록 가져오기
               List<MultipartFile> fList = multi.getFiles(fn);
               
               if(fn.equals("titlefile")) {
                  fList = multi.getFiles(fn);
               //각각의 파일을 처리(파일명 추출, 파일을 폴더에 저장)
                     MultipartFile mf = fList.get(0);
                     
                     //파일명 추출
                     String oriname = mf.getOriginalFilename();
                     
                     //변경할 이름 생성
                     String sysname = System.currentTimeMillis()
                        + oriname.substring(oriname.lastIndexOf("."));
                     
                     //upload 폴더 파일 저장
                     File ff = new File(path + sysname);
                     log.info(sysname);
                     mf.transferTo(ff);
                     
                     eDto.setE_img(sysname);
                     
                     aDao.Admin_Event_File_Update(eDto);
               }
               
               
               
            }
         }
         
         aDao.Admin_Event_Insert(eDto);
      }
}