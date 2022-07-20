package com.openai.jeju.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.jeju.dao.MemberDao;
import com.openai.jeju.dto.MemberDto;
import com.openai.jeju.dto.MessagesDto;
import com.openai.jeju.dto.SmsRequest;
import com.openai.jeju.dto.SmsResponse;

import lombok.extern.java.Log;

@Service
@Log
public class MemberService {
	
	private String serviceId = "ncp:sms:kr:286122150986:smsapi_test";
	private String secretKeyId = "kC5ebfdcXUxO2e1Up1ulOr43KySt9GEo8ymg6MAt";
	private String accessKeyId = "JAn45SSlUTiGdnAvHCXS";
	
	@Autowired
	private MemberDao mdao;
	
	private BCryptPasswordEncoder pwdEncoder;
	
	@Autowired
	private HttpSession session;
	
	@Transactional
	public String memberInsert(MemberDto dto,RedirectAttributes rttr) {
		log.info("service - memberInsert()");
		String msg = null;
		String view = null;
		log.info("아이디 : " + dto.getM_pk_id());
		log.info("비번 : " + dto.getM_pass());
		
		pwdEncoder = new BCryptPasswordEncoder();
		
		String encpwd = pwdEncoder.encode(dto.getM_pass());
		log.info("encpwd : " + encpwd);
		dto.setM_pass(encpwd);
		
		try {
			mdao.memberInsert(dto);
			view = "redirect:/";
			msg = "회원가입 성공!";
		}catch (Exception e) {
			e.printStackTrace();
			view = "redirct:joinfrm";
			msg = "회원가입에 실패했습니다. 관리자에게 문의해주세요";
		}
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}
	
	public String loginProc(MemberDto dto, RedirectAttributes rttr) {
		log.info("MemberService - loginProc()");
		String view = null;
		String msg = null;
		
		pwdEncoder = new BCryptPasswordEncoder();
		
		if(dto.getM_pk_id().equals("admin") && dto.getM_pass().equals("990907")) {
			session.setAttribute("Admin", "admin");
			
			return "redirect:Admin_Member_List";
		}
		
		log.info("아이디 : " + dto.getM_pk_id());
		String encPwd = mdao.pwdSelect(dto.getM_pk_id());
		log.info("가져온 비밀번호 : " + encPwd);
		
		if(encPwd != null) {
			//존재하는 아이디
			//나중에 변경하겠음.(암호화 처리로 변경할 예정임)
			if(pwdEncoder.matches(dto.getM_pass(), encPwd)) {
				//로그인 성공 -> 세션에 로그인 정보를 저장
				//회원 정보(이름, 포인트, 등급이름) : mem 재활용
				dto = mdao.memberSelect(dto.getM_pk_id());
				//세션에 DTO 저장.
				if(dto.getM_category().equals("블랙리스트")) {
					msg = "로그인 실패 관리자에게 문의하세요.";
					view = "redirect:/login";
						
				} else {
				
				log.info("로그인 성공!");
				session.setAttribute("user", dto);
				
				//로그인 성공 후 게시판의 목록 페이지로 이동.
				view = "redirect:/";
				}
			}
			else {
				//로그인 실패(비번 오류)
				view = "redirect:/login";
				msg = "아이디와 비밀번호를 다시 확인해주세요.";
			}
		}
		else {
			//아이디가 존재하지 않는 경우(회원이 아닌 경우)
			view = "redirect:/login";
			msg = "아이디와 비밀번호를 다시 확인해주세요.";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}
	
	public String logout() {
		log.info("MemberService - logout()");
		
		session.removeAttribute("user");
		session.removeAttribute("tdto");
		
		return "redirect:/";
	}
	
	public Map<String, String> idCheck(String m_pk_id){
		log.info("MemberService - idCheck()");
		String msg = null;
		Map<String, String> smap = new HashMap<String, String>();
		
		int result = mdao.IdCheck(m_pk_id);
		
		if(result == 0) {
			msg = "ok";
		}else {
			msg = "no";
		}
		
		smap.put("msg", msg);
		
		return smap;
	}
	
	public Map<String, String> nickCheck(String m_nickname){
		log.info("MemberService - idCheck()");
		String msg = null;
		Map<String, String> smap = new HashMap<String, String>();
		
		int result = mdao.NickCheck(m_nickname);
		
		if(result == 0) {
			msg = "ok";
		}else {
			msg = "no";
		}
		
		smap.put("msg", msg);
		
		return smap;
	}
	
	public Map<String, String> sendSms(String recipientPhoneNumber) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
    	String rcnum = "";
    	Random num = new Random();
    	
    	for(int i = 0; i<6; i++) {
    		int a = num.nextInt(10);
    		rcnum+=a;
    	}
    	String text = "인증번호는 " + rcnum + " 입니다.";
    	
        Long time = System.currentTimeMillis();
        List<MessagesDto> messages = new ArrayList<MessagesDto>();
        messages.add(new MessagesDto(recipientPhoneNumber, text));

        SmsRequest smsRequest = new SmsRequest("SMS", "COMM", "82", "01083703435", "내용", messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequest);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKeyId);
        String sig = makeSignature(time); //암호화
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponse smsResponse = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), body, SmsResponse.class);

        String scode = smsResponse.getStatusCode();
        String sname = smsResponse.getStatusName();
        
        Map<String, String> rmap = new HashMap<String, String>();
        rmap.put("scode", scode);
        rmap.put("sname", sname);
        
        session.setAttribute("CheckNum", rcnum);
        
        return rmap;
    }
	
	public String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + serviceId + "/messages";
        String timestamp = time.toString();
        String accessKey = accessKeyId;
        String secretKey = secretKeyId;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
	
	 public Map<String, String> CheckProc(String CheckNum) {
	    	String msg = null;
	    	
	    	Map<String, String> rmap = new HashMap<String, String>();
	    	
	    	String comparisonNum = (String)session.getAttribute("CheckNum");
	    	
	    	if(comparisonNum.equals(CheckNum)) {
	    		msg = "인증성공";
	    		session.removeAttribute("CheckNum");
	    	}else {
	    		msg = "인증번호가 맞지 않습니다.";
	    	}
	    	
	    	log.info(msg);
	    	
	    	rmap.put("msg", msg);
	    	
	    	
	    	return rmap;
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	//개인정보 업데이트
		public String memberUpdate(MemberDto mem, RedirectAttributes rttr) {
			log.info("memberUpdate 서비스!!!!!!!!!!!!!!!!");
			
			String view = null;
			String msg = null;
			
			System.out.println(mem.getM_pk_id());
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			try {
				mdao.memberUpdate(mem);
				
			//	MemberDto mdto = new MemberDto();
				
				mem = mdao.memberSelect(mem.getM_pk_id());
				
				session.setAttribute("user", mem);
				System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
				System.out.println(session.getAttribute("id"));
		        System.out.println(session.getAttribute("pwd"));
				
				
				session.setAttribute("mb", mem);
				//System.out.println(session.getAttribute("mb"));																											
				
				
				view = "redirect:/Mypage_Update";
				msg = "수정성공";
			} catch (Exception e) {
				e.printStackTrace();
				view = "redirect:/Mypage_Update";
				msg = "수정실패";
			}
			rttr.addFlashAttribute("msg", msg);
			return view;
		}
	 
		
		//mypage 회원 탈퇴
		public String memberDelete(String id) {
			String res = null;
			
			try {
				mdao.memberBlack(id);
				session.invalidate();//회원 삭제와 같이 로그아웃 처리도 함께 한다.
				res = "ok";
			} catch (Exception e) {
				e.printStackTrace();
				res = "fail";
			}
			
			
			return res;
		}
	 
		
		//신규 비밀번호 업데이트 
		public String pwUpdate(String pw) {
			MemberDto dto = (MemberDto)session.getAttribute("user");
			String res = null;
			
			pwdEncoder = new BCryptPasswordEncoder();
			
			String encpwd = pwdEncoder.encode(pw);
			
			dto.setM_pass(encpwd);
			
			try {
				mdao.pwUpdate(dto);
				
				res = "ok";
			} catch (Exception e) {
				// TODO: handle exception
				
				res = "no";
			}
			
			return res;
		}
	 
		
		
		   //비밀번호 확인
		   
		public String PwdCheck(MemberDto mem, UserSha256 sha, RedirectAttributes rttr) {
			log.info("PwdCheck()s");
			String view = null;
			String msg = null;
			
			System.out.println("로그인 첫번째:" + mem.getM_pass());
		    String encryPassword = sha.encrypt(mem.getM_pass());
		    mem.setM_pass(encryPassword);
		    System.out.println("로그인 두번째:" + mem.getM_pass());
		    
		    int result = mdao.PwdCheck(mem.getM_pass());
		    
		    if(result == 1) {
		    	//msg = "확인 완료!";   //이거  주석처리하면 반짝거림 사리짐 
		    	view = "redirect:/Mypage_PwdUpdate";
		    }else {
		    	//msg = "일치하는 비밀번호가 없습니다.";  //이거  주석처리하면 반짝거림 사리짐 
		    	view = "redirect:/Mypage_PwdUpdate";
		    }
			
		    rttr.addFlashAttribute("msg", msg);
			
			return view;
		}
		
		
		
		//비밀번호 대조 후 체크!! Mypage_PwdUpdate 페이지
		public String PwdCheck_2(String pw, RedirectAttributes rttr) {
			MemberDto dto = (MemberDto)session.getAttribute("user");
			String res= null;
			
			System.out.println(dto.getM_pk_id());
			
			pwdEncoder = new BCryptPasswordEncoder();
			
			String encPwd = mdao.pwdSelect(dto.getM_pk_id());
			
			System.out.println(encPwd);
			
			if(pwdEncoder.matches(pw, encPwd)) {
				//로그인 성공 -> 세션에 로그인 정보를 저장
				//회원 정보(이름, 포인트, 등급이름) : mem 재활용
				dto = mdao.memberSelect(dto.getM_pk_id());
				//세션에 DTO 저장.
				log.info("로그인 성공!");
				
				//로그인 성공 후 게시판의 목록 페이지로 이동.
				res = "ok";
			}
			else {
				//로그인 실패(비번 오류)
				res = "no";
			}
			
			return res;
		}
}
