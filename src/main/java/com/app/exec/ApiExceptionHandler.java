package com.app.exec;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.entity.ApiException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(value = ProductNotFoundException.class)
	public ResponseEntity<Object> handleApiRequestException(ProductNotFoundException ex) {
		ApiException apiException = new ApiException(ex.getMessage(), ex.getCause(), HttpStatus.NOT_FOUND, ZonedDateTime.now(ZoneId.systemDefault()));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiException);
	}
}
