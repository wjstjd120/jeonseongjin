package com.openai.jeju.dto;

import lombok.Data;

@Data
public class BlogDto {
	private int b_pk_num; //후기번호
	private int b_fk_tnum; //여행번호
	private String b_fk_id;//회원아이디
	private String b_img;//대표이미지
	private String b_date;//작성날짜
	private String b_title;//제목
	private int b_cost;//총경비
	private String b_contents;//내용
}
