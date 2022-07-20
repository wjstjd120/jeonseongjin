package com.openai.jeju.dto;

import lombok.Data;

@Data
public class RoomDto {
	private int r_pk_num;
	private String r_fk_cnum;
	private String r_name;
	private String r_img;
	private String r_contents;
	private int r_price;
	private String r_in;
	private String r_out;
	private String r_hidden;
	private String filecheck;
}
