package com.openai.jeju.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.jeju.dto.TravelrouteDto;
import com.openai.jeju.service.TripService;

import lombok.extern.java.Log;

@Controller
@Log
public class TripController {
   
   private ModelAndView mv;
   
   @Autowired
   private TripService tsv;
   
   @PostMapping("traInsert")
   public String traInsert(TravelrouteDto tra,  RedirectAttributes rttr) {
      log.info("traInsert");
      
      String view = tsv.traInsert(tra, rttr);
      
      return view;
   }
   
   
   
   
   
   
   
   
}