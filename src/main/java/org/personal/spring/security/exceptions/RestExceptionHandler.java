package org.personal.spring.security.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ApiException> handleAuthenticationException(AuthenticationException authenticationException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, authenticationException.getMessage(), authenticationException);
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ApiException> handleMalformedJwtException(MalformedJwtException malformedJwtException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, malformedJwtException.getMessage(), malformedJwtException);
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ApiException> handleExpiredJwtException(ExpiredJwtException expiredJwtException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, expiredJwtException.getMessage(), expiredJwtException);
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ApiException> handleSignatureException(SignatureException signatureException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, signatureException.getMessage(), signatureException);
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    protected ResponseEntity<ApiException> handleUnsupportedJwtException(UnsupportedJwtException unsupportedJwtException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, unsupportedJwtException.getMessage(), unsupportedJwtException);
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiException> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage(), illegalArgumentException);
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiException> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, accessDeniedException.getMessage(), accessDeniedException);
        return buildResponseEntity(apiException);
    }

    private ResponseEntity<ApiException> buildResponseEntity(ApiException apiException) {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

}
