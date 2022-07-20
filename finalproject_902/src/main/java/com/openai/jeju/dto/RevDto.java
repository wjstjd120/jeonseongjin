package com.openai.jeju.dto;

import lombok.Data;

@Data
public class RevDto {
	private int rv_pk_num;
	private String rv_fk_cnum;
	private String rv_fk_id;
	private String rv_img;
	private String rv_date;
	private String rv_contents;
	private int rv_star;
	private String c_pk_cnum;
	private String c_name;
	private String c_category;
}
