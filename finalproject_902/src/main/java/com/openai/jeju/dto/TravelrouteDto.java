package com.openai.jeju.dto;

import lombok.Data;

@Data
public class TravelrouteDto {
	private int tr_pk_num;
	private String tr_fk_id;
	private String tr_title;
	private String tr_in;
	private String tr_out;
	private String tr_relationship;
	private boolean tr_tf;
}
