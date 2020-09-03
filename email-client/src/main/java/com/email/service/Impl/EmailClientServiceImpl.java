package com.email.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.email.model.EmailClientModel;
import com.email.service.EmailClientService;
import com.email.util.EmailCryptoUtil;
import com.email.util.EmailSenderUtil;

@Service
public class EmailClientServiceImpl implements EmailClientService {

	private EmailSenderUtil emailSender;

	private EmailCryptoUtil emailCrypto;

	private TemplateEngine templateEngine;
	
	@Value("${mail.disclaimer.message}")
    private String disclaimerMessage;

	@Autowired
	public EmailClientServiceImpl(TemplateEngine templateEngine, EmailSenderUtil emailSender,
			EmailCryptoUtil emailCrypto) {
		this.templateEngine = templateEngine;
		this.emailSender = emailSender;
		this.emailCrypto = emailCrypto;
	}

	/* Plain Text Email added with disclaimer */
	public String sendUnencryptedPlainTextEmailToExternal(EmailClientModel email) throws Exception {
		return emailSender.sendMailWithoutRetry(email, createMessageWithDisclaimer(email.getMessage()));
	}

	/* HTML Email message are encrypted with DES */
	public String sendEncryptedHTMLEmailToInternal(EmailClientModel email) throws Exception {
		Context context = new Context();
		context.setVariable("message", email.getMessage());
		String emailMessage = templateEngine.process("email-template-internal.html", context);
		String encryptedMessage = emailCrypto.convertMessageWithDESEncryption(emailMessage);
		return emailSender.sendMailWithRetries(email, encryptedMessage);
	}

	/* HTML Email message are encrypted with AES */
	public String sendEncryptedHTMLEmailToExternal(EmailClientModel email) throws Exception {
		Context context = new Context();
		context.setVariable("message", email.getMessage());
		String emailMessage = templateEngine.process("email-template-external.html", context);
		String encryptedMessage = emailCrypto.convertMessageWithAESEncryption(emailMessage);
		return emailSender.sendMailWithRetries(email, encryptedMessage);
	}

	/* Plain Text Email message are encrypted with DES and then AES */
	public String sendEncryptedPlainTextEmailToExternal(EmailClientModel email) throws Exception {
		String encryptedDESMessage = emailCrypto.convertMessageWithDESEncryption(email.getMessage());
		String encryptedAESMessage = emailCrypto.convertMessageWithAESEncryption(encryptedDESMessage);
		return emailSender.sendMailWithoutRetry(email, encryptedAESMessage);
	}

	/* Creating a message with disclaimer */
	private String createMessageWithDisclaimer(String plainTextMessage) {
		StringBuilder emailMessage = new StringBuilder();
		emailMessage.append(plainTextMessage).append(System.lineSeparator()).append(System.lineSeparator())
				.append(disclaimerMessage);
		return emailMessage.toString();

	}

}
