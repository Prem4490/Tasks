package com.email.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.email.constants.EmailClientConstants;

@Component
public class EmailCryptoUtil {

	Logger log = LoggerFactory.getLogger(EmailCryptoUtil.class);

	private SecretKey secretKey;

	private Cipher encryptedCipher;

	/* Implementation of DES Encryption */
	public String convertMessageWithDESEncryption(String message) throws Exception {
		try {
			secretKey = KeyGenerator.getInstance(EmailClientConstants.DES_ALGORITHM).generateKey();
			encryptedCipher = Cipher.getInstance(EmailClientConstants.DES_ALGORITHM);
			encryptedCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return encryptMessage(message);
		} catch (NoSuchAlgorithmException exp) {
			log.error("No Such Algorithm:", exp.getMessage());
			throw exp;
		} catch (NoSuchPaddingException exp) {
			log.error("No Such Padding:", exp.getMessage());
			throw exp;
		} catch (InvalidKeyException exp) {
			log.error("Invalid Key:", exp.getMessage());
			throw exp;
		}

	}

	/* Implementation of AES Encryption */
	public String convertMessageWithAESEncryption(String message) throws Exception {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(EmailClientConstants.AES_ALGORITHM);
			keyGenerator.init(256);
			SecretKey secretKey = keyGenerator.generateKey();
			encryptedCipher = Cipher.getInstance(EmailClientConstants.AES_ALGORITHM);
			encryptedCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return encryptMessage(message);
		} catch (NoSuchAlgorithmException exp) {
			log.error("No Such Algorithm:", exp.getMessage());
			throw exp;
		} catch (NoSuchPaddingException exp) {
			log.error("No Such Padding:", exp.getMessage());
			throw exp;
		} catch (InvalidKeyException exp) {
			log.error("Invalid Key:", exp.getMessage());
			throw exp;
		}

	}

	private String encryptMessage(String message) {

		try {
			byte[] messageArray = message.getBytes("UTF8");
			byte[] encoded = encryptedCipher.doFinal(messageArray);
			return Base64.getEncoder().encodeToString(encoded);
		} catch (Exception e) {
			log.error("Problem encrypting message:", e.getMessage());
		}
		return null;
	}
}
