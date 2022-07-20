package com.openai.jeju.dto;

import lombok.Data;

@Data
public class CalendarDto {
	private String title;
	private String start;
	private String end;
	private String url;
	
	private int e_num;
	private String e_id;
	private boolean e_pay;
	private String e_relationship;
}
