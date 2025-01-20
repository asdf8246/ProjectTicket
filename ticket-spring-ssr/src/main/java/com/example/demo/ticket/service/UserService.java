package com.example.demo.ticket.service;

import java.util.List;

import com.example.demo.ticket.exception.PasswordInvalidException;
import com.example.demo.ticket.exception.UserNotFoundException;
import com.example.demo.ticket.model.dto.UserDto;


public interface UserService {
	List<UserDto> findAll();
	
	void appendUser(UserDto userDto);
	
	void deleteUser(String userId);
	
	UserDto getUserByPhonenumber(String phonenumber) throws UserNotFoundException;
	
	UserDto getUserByEmail(String email) throws UserNotFoundException;
	
	UserDto getUser(Integer userId);
	
	void updateUser(String userId, String username, String phonenumber, String email, String role);
	
	void updatePassword(Integer userId, String phonenumber, String oldPassword, String newPassword) throws UserNotFoundException, PasswordInvalidException;
}
