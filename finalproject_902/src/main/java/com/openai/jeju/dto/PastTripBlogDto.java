package com.openai.jeju.dto;

import lombok.Data;

@Data
public class PastTripBlogDto {
	private int b_pk_num;
	private int b_fk_tnum;
	private String b_fk_id;
	private String b_img;
	private String tr_title;
	private String tr_in;
	private String tr_out;
	private boolean tr_tf;
}
