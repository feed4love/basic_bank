package com.inrip.bank.controller.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import com.inrip.bank.common.Constants;
import com.inrip.bank.controller.exceptions.BadRequestException;
import com.inrip.bank.controller.exceptions.HttpAcceptException;
import com.inrip.bank.controller.exceptions.NotFoundException;

/**
 * @author Enrique AC
 *
 */
public abstract class HTTPResponseHandler {

	private static final Logger mLogger = LogManager.getLogger(HTTPResponseHandler.class);

	private static final String STATUS_CODE = "StatusCode";
	private static final String STATUS_INTERNAL_MESSAGE = "StatusInternal";
	private static final String STATUS_END_USER_MESSAGE = "StatusMessage";

	public HTTPResponseHandler() {
	}

	public void setStatusHeaders(HttpServletResponse response, int httpStatus, String code, String message) {
		response.setStatus(httpStatus);
		response.setHeader(STATUS_CODE, code);
		response.setHeader(STATUS_INTERNAL_MESSAGE, message);
	}

	public void setStatusHeaders(HttpServletResponse response, int httpStatus, String code, String message,
			String endUserMessage) {
		response.setStatus(httpStatus);
		response.setHeader(STATUS_CODE, code);
		response.setHeader(STATUS_INTERNAL_MESSAGE, message);
		response.setHeader(STATUS_END_USER_MESSAGE, endUserMessage);
	}

	public void setStatusHeaders(HttpServletResponse response, int httpStatus, String code, String message,
			String endUserMessage, String externalMessage) {
		response.setStatus(httpStatus);
		response.setHeader(STATUS_CODE, code);
		response.setHeader(STATUS_INTERNAL_MESSAGE, message);
		response.setHeader(STATUS_END_USER_MESSAGE, endUserMessage);

	}

	public void setStatusHeadersToSuccess(HttpServletResponse response) {
		int status = HttpStatus.OK.value();
		setStatusHeaders(response, status, "SUCCESS", "Success");
	}

	@ExceptionHandler(NotFoundException.class)
	public void handleNotFoundException(NotFoundException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_NOT_FOUND_STATUS;
		String message = ex.getStatusMessage();
		int httpStatus = HttpStatus.NOT_FOUND.value();
		String endUserMessage = ex.getEndUserMessage();
		String externalMessage = ex.getExternalMessage();

		mLogger.error(message, ex);

		constructHeaders(response, code, message, httpStatus, endUserMessage, externalMessage);

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_BAD_REQUEST_STATUS;

		String message = "Invalid character OR characters are specified in the request body.Please double check your request";
		String debugMessage = message + " More details >> " + ex.getMessage();
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		String endUserMessage = message;

		mLogger.error(message, ex);

		constructHeaders(response, code, debugMessage, httpStatus, endUserMessage, null);

	}

	@ExceptionHandler(BadRequestException.class)
	public void handleBadRequestException(BadRequestException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_BAD_REQUEST_STATUS;
		String message = ex.getStatusMessage();
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		String endUserMessage = ex.getEndUserMessage();
		String externalMessage = ex.getExternalMessage();

		constructHeaders(response, code, message, httpStatus, endUserMessage, externalMessage);

	}

	private void constructHeaders(HttpServletResponse response, String code, String message, int httpStatus,
			String endUserMessage, String externalMessage) {

		if (endUserMessage != null && externalMessage != null) {
			setStatusHeaders(response, httpStatus, code, message, endUserMessage, externalMessage);
		} else if (endUserMessage != null) {
			setStatusHeaders(response, httpStatus, code, message, endUserMessage);
		} else {
			setStatusHeaders(response, httpStatus, code, message);
		}
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public void handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_BAD_REQUEST_STATUS;
		String message = ex.getMessage();
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		String endUserMessage = ex.getMessage();

		constructHeaders(response, code, message, httpStatus, endUserMessage, null);

		mLogger.error(message, ex);

	}

	@ExceptionHandler(IllegalStateException.class)
	public void handleIllegalStateException(IllegalStateException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_BAD_REQUEST_STATUS;
		String message = ex.getMessage();
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		String endUserMessage = ex.getMessage();

		constructHeaders(response, code, message, httpStatus, endUserMessage, null);

		mLogger.error(message, ex);

	}

	@ExceptionHandler(HttpStatusCodeException.class)
	public void handleHttpStatusCodeException(HttpStatusCodeException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = "";
		String message = "";
		int status = ex.getStatusCode().value();

		try {
			code = ex.getResponseHeaders().get(STATUS_CODE).get(0);
			message = ex.getResponseHeaders().get(STATUS_INTERNAL_MESSAGE).get(0);
		} catch (NullPointerException e) {

		}

		mLogger.error(message, ex);

		setStatusHeaders(response, status, code, message);

	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public void handleMissingServletRequestParameterException(MissingServletRequestParameterException ex,
			HttpServletRequest request, HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_BAD_REQUEST_STATUS;
		String message = ex.getMessage();
		int status = HttpStatus.BAD_REQUEST.value();

		mLogger.error(message, ex);

		setStatusHeaders(response, status, code, message);

	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public void handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_BAD_REQUEST_STATUS;
		String message = ex.getMessage();
		int status = HttpStatus.BAD_REQUEST.value();

		mLogger.error(message, ex);

		setStatusHeaders(response, status, code, message);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request,
			HttpServletResponse response) {

		String code = HttpStatus.BAD_REQUEST.name();
		String message = HttpStatus.BAD_REQUEST.name();

		if (ex.getBindingResult() != null && ex.getBindingResult().hasErrors()) {
			List<FieldError> unmodifiableErrorList = ex.getBindingResult().getFieldErrors();
			List<FieldError> errorList = new ArrayList<FieldError>(unmodifiableErrorList);
			Collections.sort(errorList, new Comparator<FieldError>() {

				@Override
				public int compare(FieldError arg0, FieldError arg1) {
					return arg0.getDefaultMessage().compareTo(arg1.getDefaultMessage());
				}
			});

			message = errorList.get(0).getDefaultMessage();
		}

		int status = HttpStatus.BAD_REQUEST.value();

		setStatusHeaders(response, status, code, message);

		mLogger.error(message, ex);

	}

	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

		String code = HttpStatus.INTERNAL_SERVER_ERROR.name();
		String message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

		setStatusHeaders(response, status, code, message);

		mLogger.error(message, ex);

	}

	@ExceptionHandler(HttpAcceptException.class)
	public void handleHttpAcceptException(HttpAcceptException ex, HttpServletRequest request,
										  HttpServletResponse response) {

		String code = Constants.HTTP_STATUS_ACCEPT_STATUS;
		String message = ex.getStatusMessage();
		int httpStatus = HttpStatus.ACCEPTED.value();
		String endUserMessage = ex.getEndUserMessage();
		String externalMessage = ex.getExternalMessage();

		mLogger.error(message, ex);

		constructHeaders(response, code, message, httpStatus, endUserMessage, externalMessage);

	}


}
