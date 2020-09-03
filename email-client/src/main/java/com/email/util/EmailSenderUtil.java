package com.email.util;

import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import com.email.constants.EmailClientConstants;
import com.email.model.EmailClientModel;

@Component
public class EmailSenderUtil {

	Logger log = LoggerFactory.getLogger(EmailSenderUtil.class);

	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromAddress;

	public EmailSenderUtil(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/* Sending a mail without retry option */
	public String sendMailWithoutRetry(EmailClientModel email, String message) throws Exception {
		try {
			return sendMail(email, message, false);
		} catch (Exception ex) {
			log.error("Exception in sending email without retry: {}, error message: {}", email.getEmailTo(),
					ex.getMessage());
			throw ex;
		}
	}

	/*
	 * Sending an email with retry option. In case of exception, it will retry for 3
	 * times
	 */
	@Retryable(maxAttempts = 3, value = Exception.class, backoff = @Backoff(delay = 5000))
	public String sendMailWithRetries(EmailClientModel email, String message) throws Exception {
		try {
			return sendMail(email, message, true);
		} catch (Exception ex) {
			log.error("Exception in sending email with retries: {}, error message: {}", email.getEmailTo(),
					ex.getMessage());
			throw ex;
		}
	}

	/* Sending an email with all required parameters */
	private String sendMail(EmailClientModel mailProperties, String message, boolean isHtml) throws Exception {

		try {
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mail, true);
			messageHelper.setFrom(fromAddress);
			messageHelper.setTo(mailProperties.getEmailTo());
			if (mailProperties.getEmailCc() != null && !mailProperties.getEmailCc().isEmpty()) {
				messageHelper.setCc(mailProperties.getEmailCc());
			}
			if (mailProperties.getEmailBcc() != null && !mailProperties.getEmailBcc().isEmpty()) {
				messageHelper.setBcc(mailProperties.getEmailBcc());
			}
			messageHelper.setSubject(mailProperties.getSubject());
			messageHelper.setText(message, isHtml);
			mailSender.send(mail);
			log.info("Send email '{}' to: {}", mailProperties.getSubject(), mailProperties.getEmailTo());

			return EmailClientConstants.EMAIL_SUCCESS;

		} catch (Exception ex) {
			log.error("Exception while sending email to: {}, error message: {}", mailProperties.getEmailTo(),
					ex.getMessage());
			throw new Exception(EmailClientConstants.EMAIL_FAILURE);
		}

	}

}
