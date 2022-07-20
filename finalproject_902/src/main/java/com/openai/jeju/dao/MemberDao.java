package com.openai.jeju.dao;

import org.apache.ibatis.annotations.Mapper;

import com.openai.jeju.dto.MemberDto;

@Mapper
public interface MemberDao {
	// Member테이블 insert
	public void memberInsert(MemberDto dto);
	
	// m_pk_id로 비밀번호 구하기
	public String pwdSelect(String m_pk_id);
	// m_pk_id로 dto 구하기
	public MemberDto memberSelect(String m_pk_id);
	// m_pk_id가 있으면 1을 반환
	public int IdCheck(String m_pk_id);
	// m_nickname이 있으면 1을반환
	public int NickCheck(String m_nickname);
	// m_pk_id 로 member테이블 delete
	public void memberDelete(String m_pk_id);
	public void upgradeMember(String id);
	
	
	// 개인정보 수정
	public void memberUpdate(MemberDto mem);
	//비밀번호 수정
	public void pwUpdate(MemberDto mem);
	
	public int PwdCheck(String m_pass);

	public void memberBlack(String id);
}
