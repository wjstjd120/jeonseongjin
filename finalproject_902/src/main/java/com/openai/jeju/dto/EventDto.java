package com.openai.jeju.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class EventDto {
   private String e_pk_enum;
   private Timestamp e_date;
   private String e_title;
   private String e_addr;
   private String e_eday;
   private String e_img;
   private String e_info;
   private String e_eventing;
}