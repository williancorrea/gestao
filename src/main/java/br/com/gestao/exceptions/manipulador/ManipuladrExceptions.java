package br.com.gestao.exceptions.manipulador;

import br.com.gestao.exceptions.EntidadeNaoEncontradaException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ManipuladrExceptions extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Cria a lista de erros
     *
     * @param bindingResult BindingResult
     * @return Errors list
     */
    public List<ApiError> createErrorList(BindingResult bindingResult, HttpStatus httpStatus) {
        List<ApiError> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String userMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String developerMessage = fieldError.toString();
            errors.add(new ApiError(userMessage, developerMessage, httpStatus));
        }
        return errors;
    }

    /**
     * Entidade não encontrada
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({EntidadeNaoEncontradaException.class})
    public ResponseEntity<Object> handlerEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
        String userMessage = ex.getMessage();
        String developerMessage = ex.toString();
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.NOT_FOUND));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Violação de integridade do banco de dados - relacionamento entre tabelas
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("recurso.violacao-de-integridade", null, LocaleContextHolder.getLocale());
        String developerMessage = ExceptionUtils.getRootCauseMessage(ex);
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.CONFLICT));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Manipula mensagens de erro ilegíveis
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return handleExceptionInternal
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String userMessage = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());
        String developerMessage = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.BAD_REQUEST));
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }
//

    /**
     * Manipula mensagens de erro de validação para atributos de objetos
     *
     * @param ex      MethodArgumentNotValidException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return handleExceptionInternal
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ApiError> errors = createErrorList(ex.getBindingResult(), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Manipula mensagens de erro quando o recurso não é encontrado
     *
     * @param ex      EmptyResultDataAccessException
     * @param request WebRequest
     * @return handleExceptionInternal
     */
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
        String developerMessage = ex.toString();
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.NOT_FOUND));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Manipula mensagens de erro de conversão de tipo
     *
     * @param ex      MethodArgumentTypeMismatchException
     * @param request WebRequest
     * @return handleExceptionInternal
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("recurso.tipo-atributo-incorreto", null, LocaleContextHolder.getLocale());
        String developerMessage = ex.toString();
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.BAD_REQUEST));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    /**
     * Erro ao manipular o objeto
     *
     * @param ex      DataIntegrityViolationException
     * @param request WebRequest
     * @return handleExceptionInternal
     */
    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ResponseEntity<Object> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("recurso.construcao-do-objeto-esta-incorreta", null, LocaleContextHolder.getLocale());
        String developerMessage = ExceptionUtils.getRootCauseMessage(ex);
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.BAD_REQUEST));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Método não suportado
     *
     * @param ex      HttpRequestMethodNotSupportedException
     * @param request WebRequest
     * @return handleExceptionInternal
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String userMessage = messageSource.getMessage("recurso.metodo-nao-suportado", null, LocaleContextHolder.getLocale());
        String developerMessage = ex.toString();
        List<ApiError> errors = Arrays.asList(new ApiError(userMessage, developerMessage, HttpStatus.METHOD_NOT_ALLOWED));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
    }
}
