package com.openai.jeju.dto;



import java.sql.Timestamp;

import lombok.Data;

@Data
public class JejuspotreviewDto {
	private String sv_pk_num;
	private String sv_fk_id;
	private String sv_fk_num;
	private String sv_img;
	private String sv_contents;
	private String sv_star;
	private Timestamp sv_date;
}
