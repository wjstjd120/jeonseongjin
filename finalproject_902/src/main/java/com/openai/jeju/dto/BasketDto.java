package com.openai.jeju.dto;

import lombok.Data;

@Data
public class BasketDto {
	private int bk_pk_num;
	private int bk_fk_tnum;
	private String bk_fk_cnum;
	private String bk_fk_id;
	private int bk_fk_num;
	private String bk_goods;
	private int bk_price;
	private String bk_in;
	private String bk_out;
	private String bk_name;
	private String bk_number;
	private String bk_carch;
	private String bk_paych;
	private String bk_paytime;
	private int bk_people;
	private String c_name;
}
 