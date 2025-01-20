package com.example.demo.ticket.service;

import com.example.demo.ticket.exception.CertException;
import com.example.demo.ticket.model.dto.UserCert;

public interface CertService {
	
	UserCert gerCert(String account,String password) throws CertException;
}
