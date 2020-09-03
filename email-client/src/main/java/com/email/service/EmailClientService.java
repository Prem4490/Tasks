package com.email.service;

import com.email.model.EmailClientModel;

public interface EmailClientService {

	String sendUnencryptedPlainTextEmailToExternal(EmailClientModel email) throws Exception;

	String sendEncryptedHTMLEmailToInternal(EmailClientModel email) throws Exception;

	String sendEncryptedHTMLEmailToExternal(EmailClientModel email) throws Exception;

	String sendEncryptedPlainTextEmailToExternal(EmailClientModel email) throws Exception;
}
