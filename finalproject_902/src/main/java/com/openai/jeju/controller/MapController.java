package com.openai.jeju.controller;

import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.openai.jeju.dto.CompanyDto;
import com.openai.jeju.service.MapService;

import lombok.extern.java.Log;

@RestController
@Log
public class MapController {
   
   @Autowired
   private MapService mapsv;
   
   
   @PostMapping(value ="map", produces ="application/json; charset=UTF-8")
   @ResponseBody
   public Map<String, List<CompanyDto>> markerCompany(CompanyDto dto ){
      log.info("메서드 : markerCompany : " + dto.getC_category());
      
      Map<String, List<CompanyDto>> fmap = mapsv.selectCompany(dto.getC_category());
   
      return fmap;
   }
}
