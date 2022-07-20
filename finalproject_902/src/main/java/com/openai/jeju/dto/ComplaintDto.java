package com.openai.jeju.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ComplaintDto {
   private int co_pk_conum;
   private String co_fk_id;
   private String co_title;
   private String co_contents;
   private Timestamp co_date;
   private String co_check;
   private String co_reply;
   private Timestamp co_re_date;
}