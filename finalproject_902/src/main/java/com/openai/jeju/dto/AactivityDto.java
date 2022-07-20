package com.openai.jeju.dto;

import lombok.Data;

@Data
public class AactivityDto {
	private int at_pk_num;
	private String at_fk_cnum;
	private String at_name;
	private String at_img;
	private String at_contents;
	private int at_price;
	private String at_in;
	private String at_out;
	private String at_rtch;
	private String at_hidden;
	private String filecheck;
}
