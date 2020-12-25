package com.app.entity;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiException {
	private String errorMessage;
	private Throwable throwable;
	private HttpStatus httpStatus;
	private ZonedDateTime timestamp;
}
