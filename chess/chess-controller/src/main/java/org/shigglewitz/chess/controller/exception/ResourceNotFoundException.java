package org.shigglewitz.chess.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends WebException{
	private static final long serialVersionUID = 4458438573347248731L;

	public ResourceNotFoundException(String string) {
		super(string);
	}

}
