package com.openai.jeju.dto;

import lombok.Data;

@Data
public class NowTripDto {
	private int bk_pk_num;
	private int bk_fk_tnum;
	private String bk_fk_cnum;
	private int bk_fk_num;
	private int bk_price;
	private String bk_in; //업체마다의 이용 날
	private String bk_out;
	private boolean bk_paych;
	private String bk_fk_id;
 	private int bk_people;
 	
	private String tr_fk_id;
	private String tr_title;
	private String tr_in; // 여행 총 시작일
	private String tr_out; // 여행 총 마지막 일 
	private int tr_pk_num; //여행 코스번호 
	private String tr_relationship; //대인관계
	private boolean tr_tf; //여행완료여부
	
	private String b_img;
	private int b_fk_tnum;
	private String b_fk_id;
}
