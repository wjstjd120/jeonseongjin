package com.openai.jeju.dto;

import lombok.Data;

@Data
public class IamBasketDto {
	private String bk_fk_cnum;
	private int bk_fk_num;
	private String bk_goods;
	private int bk_price;
	private String bk_name;
	private boolean bk_paych;
	private String bk_paytime;
	private String bk_fk_id;
	
	
	private String c_category;
	private String c_name;
	
	private String colname; // bk_paytime 또는 c_name  저장
	private String keyword; //검색단어.
	private int pageNum;
	private int listCnt;
}
