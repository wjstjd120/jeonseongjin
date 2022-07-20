package com.openai.jeju.dto;

import lombok.Data;

@Data
public class CompanyDto {
	private String c_pk_cnum;
	private String c_fk_id;
	private String c_name;
	private String c_phone;
	private String c_category;
	private String c_addr;
	private String c_contents;
	private String c_condition;
	private String c_check;
	private String c_img;
	private String c_type;
	
	private String fa_pk_num;
	private String fa_fk_id;
	private String fa_fk_cnum;
}
