package com.project.devblog.handler;

import com.project.devblog.exception.JwtAuthenticationException;
import com.project.devblog.exception.NonUniqueValueException;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.handler.apierror.ApiError;
import static com.project.devblog.handler.apierror.ApiError.buildResponseEntity;
import static com.project.devblog.handler.apierror.ApiError.toResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    @Override
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType())
                .append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        String businessMessage = builder.substring(0, builder.length() - 2);
        return buildResponseEntity(new ApiError(UNSUPPORTED_MEDIA_TYPE, businessMessage, ex));
    }

    @Override
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    @Override
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        return buildResponseEntity(new ApiError(BAD_REQUEST, "Malformed JSON request", ex));
    }

    @Override
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing JSON output", ex));
    }

    @Override
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s",
                ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex,
                                                                  HttpServletRequest request) {
        log.error("BadCredentialsException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(CONFLICT);
        apiError.setMessage(ex.getMessage());
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(VerificationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiError> handleVerificationException(VerificationException ex,
                                                                HttpServletRequest request) {
        log.error("VerificationException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<ApiError> handleConstraintViolation(javax.validation.ConstraintViolationException ex,
                                                                 HttpServletRequest request) {
        log.error("ConstraintViolationException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(BAD_REQUEST, "Validation error", ex);
        apiError.addValidationErrors(ex.getConstraintViolations());
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(NonUniqueValueException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<ApiError> handleNonUniqueResultException(NonUniqueValueException ex,
                                                                      HttpServletRequest request) {
        log.error("NonUniqueValueException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex);
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        log.error("NotFoundException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("EntityNotFoundException {}\n", request.getRequestURI(), ex);

        return toResponseEntity(new ApiError(NOT_FOUND, ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                    HttpServletRequest request) {
        log.error("DataIntegrityViolationException {}\n", request.getRequestURI(), ex);

        if (ex.getCause() instanceof ConstraintViolationException) {
            return toResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return toResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                        HttpServletRequest request) {
        log.error("MethodArgumentTypeMismatchException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ApiError> handleJwtAuthenticationException(HttpServletRequest request, AccessDeniedException ex) {
        log.error("JwtAuthenticationException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(UNAUTHORIZED, "Unauthorized!", ex);
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ApiError> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
        log.error("AccessDeniedException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(FORBIDDEN, "Access denied!", ex);
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ApiError> handleLockedException(HttpServletRequest request, LockedException ex) {
        log.error("LockedException {}\n", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(UNAUTHORIZED, "Unauthorized", ex);
        return toResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ApiError> handleAllException(Exception ex) {
        return toResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, ex));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return buildResponseEntity(new ApiError(status, ex));
    }
}

