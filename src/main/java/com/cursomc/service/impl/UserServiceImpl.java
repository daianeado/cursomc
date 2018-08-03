package com.cursomc.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;

import com.cursomc.security.UserSS;

public class UserServiceImpl {

	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
