package tz.co.nezatech.apps.bulkpay.web.service;

public interface EmailService {
	void sendMail(String from, String to, String subject, String msg);
}
