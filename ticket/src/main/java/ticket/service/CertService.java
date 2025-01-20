package ticket.service;

import ticket.exception.CertException;
import ticket.exception.PasswordInvalidException;
import ticket.exception.UserNotFoundException;
import ticket.model.dto.UserCert;
import ticket.model.entity.User;
import ticket.repository.UserDao;
import ticket.repository.UserDaoImpl;
import ticket.utils.AccountValidator;
import ticket.utils.Hash;


//憑證服務
public class CertService extends AccountValidator {
	private UserDao userDao = new UserDaoImpl();
	//登入成功後可以取得憑證
	public UserCert gerCert(String account,String password) throws CertException {
		
		User user = null;
		// 1.是否有此人
		if (isEmail(account)) {
			user = userDao.getUserByEmail(account);
		} else if (isPhoneNumber(account)) {
			user = userDao.getUser(account);
		} 

		if (user == null){
			throw new UserNotFoundException("查無此用戶");
		}
		
		// 2.比對密碼
		String passwordHash = Hash.getHash(password,user.getSalt());
		if (!passwordHash.equals(user.getPasswordHash())) {
			throw new PasswordInvalidException("密碼錯誤");
		}
		// 3.簽發憑證
		UserCert userCert = new UserCert(user.getId(), user.getPhonenumber(), user.getRole());
		return userCert;
	}
}
