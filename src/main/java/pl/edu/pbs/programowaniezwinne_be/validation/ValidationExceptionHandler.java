package pl.edu.pbs.programowaniezwinne_be.validation;

import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ValidationExceptionHandler {
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstrainValidationException(ConstraintViolationException e){
		Set<Violation> violations = e.getConstraintViolations()
				.stream()
				.map(v-> new Violation(v.getPropertyPath().toString(), v.getMessage()))
				.collect(Collectors.toSet());
		return ResponseEntity
				.badRequest()
				.body(violations);
	}
}
