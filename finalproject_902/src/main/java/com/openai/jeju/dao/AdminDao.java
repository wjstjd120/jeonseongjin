package com.openai.jeju.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.dto.ComplaintDto;
import com.openai.jeju.dto.EventDto;
import com.openai.jeju.dto.ListDto;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.NoticeDto;
import com.openai.jeju.dto.SimgDto;
import com.openai.jeju.dto.SpotDto;

@Mapper
public interface AdminDao {
   public List<MemberDto> selectMlist(ListDto list);
      
      public int Mcntselect(ListDto list);

      public List<MemberDto> allMselect();
      
      public MemberDto MemberIdSelect(String id);

      public void Admin_Member_Update(MemberDto mDto);

      public void Admin_Member_Delete(String id);

      public void Bmember_Change_Category(String id);

      public void Member_Change_Category(String id);

      public List<CompanyDto> selectClist(ListDto list);
      
      public int selectCntClist(ListDto list);

      public CompanyDto CompanyCnumSelect(String cnum);

      public void Admin_Company_Update(CompanyDto cDto);
      
      public void Admin_Company_File_Update(CompanyDto sDto);

      public List<EventDto> selectElist(ListDto list);
      
      public int selectCntElist(ListDto list);

      public EventDto EventEnumSelect(String e_num);

      public void Admin_Event_Update(EventDto eDto);

      public void Admin_Event_File_Update(EventDto eDto);

      public List<SpotDto> selectSlist(ListDto list);
      
      public int selectCntSlist(ListDto list);

      public SpotDto SpotSnumSelect(String snum);

      public List<SimgDto> selectSimglist(String snum);

      public void Admin_Spot_File_Update(SpotDto sDto);

      public void SimgDel(String s_num);

      public void insertSimg(SimgDto siDto);

      public void Admin_Spot_Update(SpotDto sDto);

      public List<NoticeDto> selectNlist(ListDto list);

      public int selectCntNlist(ListDto list);

      public NoticeDto NoticeNnumSelect(String nnum);

      public void Admin_Notice_Update(NoticeDto nDto);

      public List<ComplaintDto> selectColist(ListDto list);

      public int selectCntColist(ListDto list);

      public ComplaintDto ComplaintConumSelect(String conum);

      public void Admin_Complaint_Update(ComplaintDto coDto);

      public void Admin_Event_Delete(String e_num);

      public void Admin_Company_Delete(String cnum);

      public void Admin_Notice_Delete(String nnum);

      public void Admin_Complaint_Delete(String conum);

      public void Admin_Complaint_Mid_Delete(String id);

      public void Admin_Company_Mid_Delete(String id);

      public void Admin_Spot_Delete(String snum);

      public void Admin_Simg_Snum_Delete(String snum);

      public CompanyDto CompanyMidSelect(String id);

      public void Admin_Notice_Insert(NoticeDto nDto);

      public void Admin_Spot_Insert(SpotDto sDto);

      public void Admin_Event_Insert(EventDto eDto);
   
}