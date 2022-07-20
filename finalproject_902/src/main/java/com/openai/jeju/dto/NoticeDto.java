package com.openai.jeju.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class NoticeDto {
   private int n_pk_num;
   private String n_title;
   private String n_contents;
   private int n_views;
   private  Timestamp n_date;
}