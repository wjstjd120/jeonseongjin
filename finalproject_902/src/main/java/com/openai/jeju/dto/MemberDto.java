package com.openai.jeju.dto;

import lombok.Data;

@Data
public class MemberDto {
	private String m_pk_id;
	private String m_pass;
	private String m_name;
	private String m_gender;
	private String m_nickname;
	private String m_birth;
	private String m_phone;
	private String m_addr;
	private String m_email;
	private String m_category;
}
