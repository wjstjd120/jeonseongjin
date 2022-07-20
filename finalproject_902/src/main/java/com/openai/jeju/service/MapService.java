package com.openai.jeju.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openai.jeju.dao.MapDao;
import com.openai.jeju.dto.CompanyDto;

import lombok.extern.java.Log;

@Service
@Log
public class MapService {
   
   @Autowired
   private MapDao mapdao;
   
   
   public Map<String, List<CompanyDto>> selectCompany(String c_category) {
      log.info("MapService메서드 selectCompany : " + c_category);
      String category = "";
      Map<String, List<CompanyDto>> fmap = new HashMap<String, List<CompanyDto>>();
      List<CompanyDto> clist = new ArrayList<CompanyDto>();
      switch(c_category) {
      case "숙박" :
         category = "숙박";
         clist = mapdao.getCategoryList(category);
         break;
         
      case "레저" :
         category = "레저";
         clist = mapdao.getCategoryList(category);
         break;
         
      case "식당" :
         category = "식당";
         clist = mapdao.getCategoryList(category);
         break;
      }
      
      fmap.put("cList",clist);
      
      for (int i=0; i<clist.size(); i++) {
         System.out.println("리스트 : " + clist.get(i).getC_addr());
      }
      
      return fmap;
      
   }

}//class end
