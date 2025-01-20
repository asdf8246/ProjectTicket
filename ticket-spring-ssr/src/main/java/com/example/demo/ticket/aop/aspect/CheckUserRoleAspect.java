package com.example.demo.ticket.aop.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.ticket.exception.UserInsufficientPermissionsException;
import com.example.demo.ticket.model.dto.UserCert;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class CheckUserRoleAspect {
	
	@Autowired
	private HttpSession session;
	
	@Before("@annotation(com.example.demo.ticket.aop.CheckUserRole)")
	public void checkUserRole() throws UserInsufficientPermissionsException {
		UserCert userCert = (UserCert)session.getAttribute("userCert");
		if (!userCert.getRole().equals("ROLE_ADMIN")) {
			throw new UserInsufficientPermissionsException();
		}
	}
}
