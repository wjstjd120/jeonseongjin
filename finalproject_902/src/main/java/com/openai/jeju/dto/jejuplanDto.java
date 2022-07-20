package com.openai.jeju.dto;

import java.security.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class jejuplanDto {
	private String b_pk_num;
	private String b_fk_tnum;
	private String b_fk_id;
	private String b_img;
	private Date b_date;
	private String b_title;
	private String b_cost;
	private String b_contents;
}
