package com.example.demo.ticket.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.exception.PasswordInvalidException;
import com.example.demo.ticket.exception.UserNotFoundException;
import com.example.demo.ticket.model.dto.UserDto;
import com.example.demo.ticket.model.entity.User;
import com.example.demo.ticket.repository.UserRepository;
import com.example.demo.ticket.service.UserService;
import com.example.demo.ticket.util.Hash;


//UserService 是給 UserServlet(Controller) 使用
@Transactional
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	// 所有使用者
	@Override
	public List<UserDto> findAll() {
		return userRepository.findAll().stream()
					.map(user -> modelMapper.map(user, UserDto.class))
					.collect(Collectors.toList());
	}

	// 新增使用者
	@Transactional
	@Override
	public void appendUser(UserDto userDto) {
		String salt = Hash.getSalt(); // 得到隨機鹽
		String passwordHash = Hash.getHash(userDto.getPassword(), salt); // 得到 hash
		// 根據上列參數封裝到 User 物件中
		
		User user = modelMapper.map(userDto, User.class);
		user.setSalt(salt);
		user.setPasswordHash(passwordHash);
		try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("手機或電郵已被使用!");
		}
	}

	// 刪除使用者
	@Override
	public void deleteUser(String userId) {
		userRepository.deleteById(Long.parseLong(userId));
	}

	// 取得指定使用者
	@Override
	public UserDto getUserByPhonenumber(String phonenumber) throws UserNotFoundException {
		return userRepository.findByPhonenumber(phonenumber)
						.map(user -> modelMapper.map(user, UserDto.class))
						.orElseThrow(() -> new UserNotFoundException());
	}
	
	@Override
	public UserDto getUserByEmail(String email) throws UserNotFoundException {
		return userRepository.findByEmail(email)
						.map(user -> modelMapper.map(user, UserDto.class))
						.orElseThrow(() -> new UserNotFoundException());
	}

	// 取得指定使用者 by userId
	@Override
	public UserDto getUser(Integer userId) {
		return userRepository.findById(Long.parseLong(userId.toString()))
						.map(user -> modelMapper.map(user, UserDto.class))
						.orElse(null);
	}

	// 修改使用者
	@Override
	public void updateUser(String userId, String username, String phonenumber, String email, String role) {
		if (!username.isEmpty()) {
			userRepository.updateName(Integer.parseInt(userId), username);
		}
		if (!email.isEmpty()) {
			userRepository.updateEmail(Integer.parseInt(userId), email);
		}
		if (!role.isEmpty()) {
			userRepository.updateUserRole(Integer.parseInt(userId), role);
		}
	}

	// 變更密碼
	@Override
	public void updatePassword(Integer userId, String phonenumber, String oldPassword, String newPassword)
			throws UserNotFoundException, PasswordInvalidException {
		User user = userRepository.findByPhonenumber(phonenumber).orElseThrow(() -> new UserNotFoundException("用戶不存在"));

		// 比對修改之前的 password 是否正確
		String oldPasswordHash = Hash.getHash(oldPassword, user.getSalt());
		if (!oldPasswordHash.equals(user.getPasswordHash())) {
			throw new PasswordInvalidException("密碼錯誤");
		}

		// 產生新密碼的 Hash
		String newPasswordHash = Hash.getHash(newPassword, user.getSalt());
		// 密碼 Hash 修改
		userRepository.updatePasswordHash(userId, newPasswordHash);
	}
}
