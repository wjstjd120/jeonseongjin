package com.openai.jeju.dao;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.TravelrouteDto;

@Mapper
public interface TravelrouteDao {
   
   // 여행 테이블 insert
   public void traInsert(TravelrouteDto tra);
   // 여행 테이블 tr_pk_num 으로 select
   public TravelrouteDto traSelect(String trnum);

}