package com.openai.jeju.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.openai.jeju.util.SessionInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer  {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionInterceptor())
		.addPathPatterns("/*")
		.excludePathPatterns("/")
		.excludePathPatterns("/joinFrm")
		.excludePathPatterns("/memberInsert")
		.excludePathPatterns("/login")
		.excludePathPatterns("/loginProc")
		.excludePathPatterns("/idCheck")
		.excludePathPatterns("/nickCheck")
		.excludePathPatterns("/user")
		.excludePathPatterns("/confirmFrm")
		.excludePathPatterns("/Question_List")
		.excludePathPatterns("/Notice_List")
		.excludePathPatterns("/QA_List")
		.excludePathPatterns("/jejuplanWrite")
		.excludePathPatterns("/upload/**");
		
		
		
		
	}
	
	
}
