package com.email.controller;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.email.model.EmailClientModel;
import com.email.service.EmailClientService;

@RestController
@RequestMapping(value = "/sendemail")
public class EmailClientController {

	Logger log = LoggerFactory.getLogger(EmailClientController.class);

	@Autowired
	EmailClientService service;

	/*
	 * Send Plain Text email to external resource with disclaimer, unencrypted and no retry option
	 * Send Plain Text email to external resource, encrypted with DES and then AES and no retry option
	 */
	@PostMapping("/external/plaintext")
	public String sendPlainTextEmailToExternal(@Valid @RequestBody EmailClientModel email) throws Exception {
		String emailStatus = null;
		try {
			if (email.isEncryptMessage()) {
				emailStatus = service.sendEncryptedPlainTextEmailToExternal(email);				
			} else {
				emailStatus = service.sendUnencryptedPlainTextEmailToExternal(email);				
			}			
			return sendStatus(emailStatus);
		} catch (Exception ex) {
			log.error("Exception in sending plain text email to external server", ex);
			throw ex;
		}
	}

	/*
	 * Send HTML email to internal server, without disclaimer, encrypted with DES and retry option
	 */
	@PostMapping("/internal/encrypted-html")
	public String sendInternalHTMLEmail(@Valid @RequestBody EmailClientModel email) throws Exception {
		try {
			String emailStatus =  service.sendEncryptedHTMLEmailToInternal(email);
			return sendStatus(emailStatus);
		} catch (Exception ex) {
			log.error("Exception in sending encrypted HTML email to internal server", ex);
			throw ex;
		}
	}

	/*
	 * Sends HTML email to outside resource, with disclaimer, encrypted with AES and retry option
	 */
	@PostMapping("/external/encrypted-html")
	public String sendExternalHTMLEmail(@Valid @RequestBody EmailClientModel email) throws Exception {
		try {
			String emailStatus = service.sendEncryptedHTMLEmailToExternal(email);
			return sendStatus(emailStatus);
		} catch (Exception ex) {
			log.error("Exception in sending encrypted HTML email to external server", ex);
			throw ex;
		}
	}
	
	private String sendStatus(String emailStatus) {
		JSONObject response = new JSONObject();
		response.put("status",emailStatus);
		return response.toString();
	}

}
