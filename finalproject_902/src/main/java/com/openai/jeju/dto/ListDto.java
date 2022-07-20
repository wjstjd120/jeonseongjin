package com.openai.jeju.dto;

import lombok.Data;

@Data
public class ListDto {
	private String id;
	private String colname;
	private String keyword;
	private int pageNum;
	private int listCnt;

	// member
	private String selgender;
	private String sage;
	private String eage;
	private String pr;
	private String co;
	private String bl;
	private String m_category;

	// company
	private String lod;
	private String at;
	private String fo;
	private String conditionOn;
	private String conditionOff;
	private String checkOn;
	private String checkOff;

	// event
	private String e_on;
	private String e_off;

	private String act;
	private String dir;

	private String working;
	private String c_check;
	
	
	
	// mypage
	private String b_fk_id;
	
	
	
	
	
	
	
}
