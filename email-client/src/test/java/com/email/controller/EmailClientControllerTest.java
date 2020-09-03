package com.email.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.email.constants.EmailClientConstants;
import com.email.model.EmailClientModel;
import com.jayway.jsonpath.JsonPath;

public class EmailClientControllerTest extends AbstractTest {

	@Before
	public void setUp() {
		super.setUp();
	}

	@Value("${junit.mailto.address}")
	private String toAddress;

	@Value("${junit.mailcc.address}")
	private String ccAddress;

	@Value("${junit.mailbcc.address}")
	private String bccAddress;

	@Value("${junit.mail.subject}")
	private String subject;

	@Value("${junit.mail.message}")
	private String message;

	@Test
	public void whenSendingUnenryptedPlainTextEmailToExternal_success() throws Exception {
		String uri = "/sendemail/external/plaintext";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo(toAddress);
		clientModel.setEmailCc(ccAddress);
		clientModel.setEmailCc(bccAddress);
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		clientModel.setEncryptMessage(false);

		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		String status = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.status");
		assertEquals(EmailClientConstants.EMAIL_SUCCESS, status);
	}

	@Test
	public void whenSendingEncryptedPlainTextEmailToExternal_success() throws Exception {
		String uri = "/sendemail/external/plaintext";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo(toAddress);
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		clientModel.setEncryptMessage(true);

		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		String status = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.status");
		assertEquals(EmailClientConstants.EMAIL_SUCCESS, status);
	}

	@Test
	public void whenSendingPlainTextEmailWithEmptyData_failure() throws Exception {
		String uri = "/sendemail/external/plaintext";
		EmailClientModel clientModel = new EmailClientModel();
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingPlainTextEmailWithoutToAddress_failure() throws Exception {
		String uri = "/sendemail/external/plaintext";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingPlainTextEmailInvalidToAddress_failure() throws Exception {
		String uri = "/sendemail/external/plaintext";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo("dummytoaddress");
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingPlainTextEmailWithIncorrectURI_failure() throws Exception {
		String uri = "/sendemail/internal/plaintext";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo(toAddress);
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToInternal_success() throws Exception {
		String uri = "/sendemail/internal/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo(toAddress);
		clientModel.setEmailCc(ccAddress);
		clientModel.setEmailCc(bccAddress);
		clientModel.setSubject(subject);
		clientModel.setMessage(message);

		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		String status = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.status");
		assertEquals(EmailClientConstants.EMAIL_SUCCESS, status);
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToInternalWithEmptyData_failure() throws Exception {
		String uri = "/sendemail/internal/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToInternalWithoutToAddress_failure() throws Exception {
		String uri = "/sendemail/internal/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToInternalInvalidToAddress_failure() throws Exception {
		String uri = "/sendemail/internal/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo("dummytoaddress");
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToInternalWithIncorrectURI_failure() throws Exception {
		String uri = "/sendemail/internal/encryptedhtml";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo(toAddress);
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToExternal_success() throws Exception {
		String uri = "/sendemail/external/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo(toAddress);
		clientModel.setEmailCc(ccAddress);
		clientModel.setEmailCc(bccAddress);
		clientModel.setSubject(subject);
		clientModel.setMessage(message);

		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		String status = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.status");
		assertEquals(EmailClientConstants.EMAIL_SUCCESS, status);
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToExternalWithEmptyData_failure() throws Exception {
		String uri = "/sendemail/external/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToExternalWithoutToAddress_failure() throws Exception {
		String uri = "/sendemail/external/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToExternalInvalidToAddress_failure() throws Exception {
		String uri = "/sendemail/external/encrypted-html";
		EmailClientModel clientModel = new EmailClientModel();
		clientModel.setEmailTo("dummytoaddress");
		clientModel.setSubject(subject);
		clientModel.setMessage(message);
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void whenSendingEncryptedHTMLEmailToExternalWithIncorrectURI_failure() throws Exception {
		String uri = "/sendemail/external/encryptedhtml";
		EmailClientModel clientModel = new EmailClientModel();
		String inputJson = super.mapToJson(clientModel);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
	}

}
