package com.openai.jeju.dto;

import lombok.Data;

@Data
public class FavoritesDto {
	private String fa_pk_num;
	private String fa_fk_id;
	private String fa_fk_cnum;
	private String c_name;
	private String c_img;
}
