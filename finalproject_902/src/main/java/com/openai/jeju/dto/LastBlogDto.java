package com.openai.jeju.dto;

import lombok.Data;

@Data
public class LastBlogDto {
	private int b_pk_num; //해당 컬럼과 bi_fk_num 컬럼은 데이터값을 공유하는 사이
	private int b_fk_tnum;
	private String b_fk_id;
	private String b_img;
	private String b_date;
	private String b_title;
	private int b_cost;
	private String b_contents;
	
	private int bi_pk_num;
	private int bi_fk_num; // 해당 컬럼과  b_pk_num 컬럼은 데이터값을 공유하는사이
	private String bi_oriname;
	private String bi_sysname;
}
