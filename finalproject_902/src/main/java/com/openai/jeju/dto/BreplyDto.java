package com.openai.jeju.dto;

import java.security.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class BreplyDto {
	private String br_pk_num;
	private int br_fk_num;
	private String br_fk_id;
	private Date br_date;
	private String br_contents;
}
