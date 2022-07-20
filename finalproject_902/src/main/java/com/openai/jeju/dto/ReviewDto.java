package com.openai.jeju.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReviewDto {
	private int rv_pk_num;
	private String rv_fk_cnum;
	private String rv_fk_id;
	private String rv_img;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", 
			timezone = "Asia/Seoul")
	private Timestamp rv_date;
	private String rv_contents;
	private int rv_star;
	private String rv_answer;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", 
			timezone = "Asia/Seoul")
	private Timestamp rv_updatedate; 
	private String rv_check;
	
	private String filecheck;
}
