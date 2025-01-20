package ticket.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator {
	// 電子郵件正則表達式
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    // 手機號碼正則表達式（可根據具體需求調整）
    private static final String PHONE_PATTERN = "^(\\+?\\d{1,4}[- ]?)?\\(?\\d{1,4}\\)?[- ]?\\d{1,4}[- ]?\\d{1,4}$";

    
    // 判斷是否是電子郵件格式
    public static boolean isEmail(String account) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(account);
        return matcher.matches();
    }

    // 判斷是否是手機號碼格式
    public static boolean isPhoneNumber(String account) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(account);
        return matcher.matches();
    }
}
