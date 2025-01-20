package com.example.demo.ticket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ticket.exception.CertException;
import com.example.demo.ticket.exception.PasswordInvalidException;
import com.example.demo.ticket.exception.UserNotFoundException;
import com.example.demo.ticket.model.dto.UserCert;
import com.example.demo.ticket.model.entity.User;
import com.example.demo.ticket.repository.UserRepository;
import com.example.demo.ticket.service.CertService;
import com.example.demo.ticket.util.AccountValidator;
import com.example.demo.ticket.util.Hash;

//憑證服務
@Service
public class CertServiceImpl extends AccountValidator implements CertService {
	
	@Autowired
	private UserRepository userRepository;
	
	//登入成功後可以取得憑證
	public UserCert gerCert(String account,String password) throws CertException {
		
		User user = null;
		// 1.是否有此人
		if (isEmail(account)) {
			System.out.println("isEmail");
			user = userRepository.findByEmail(account).orElse(null);
			if (user == null) {
			    throw new UserNotFoundException("使用者不存在");
			}
		} else if (isPhoneNumber(account)) {
			System.out.println("isPhoneNumber");
			user = userRepository.findByPhonenumber(account).orElse(null);
			if (user == null) {
			    throw new UserNotFoundException("使用者不存在");
			}
		} 
		
		// 2.比對密碼
		String passwordHash = Hash.getHash(password,user.getSalt());
		if (!passwordHash.equals(user.getPasswordHash())) {
			throw new PasswordInvalidException("密碼錯誤");
		}
		// 3.簽發憑證
		UserCert userCert = new UserCert(user.getUserId(), user.getPhonenumber(), user.getRole());
		return userCert;
	}
}
