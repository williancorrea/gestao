package br.com.gestao.gerenciadorErros.manipulador;

import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import br.com.gestao.gerenciadorErros.exceptions.RegraDeNegocioException;
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
import org.springframework.web.context.request.ServletWebRequest;
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
     */
    public List<ApiErro> createErrorList(BindingResult bindingResult, HttpStatus httpStatus, String uri, String metodo) {
        List<ApiErro> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagem = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String detalhes = fieldError.toString();
            errors.add(new ApiErro(mensagem, detalhes, httpStatus, uri, metodo));
        }
        return errors;
    }

    /**
     * Entidade não encontrada
     */
    @ExceptionHandler({EntidadeNaoEncontradaException.class})
    public ResponseEntity<Object> handlerEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
        String mensagem = ex.getMessage();
        String detalhes = ex.toString();
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.NOT_FOUND, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Regra de negocio da aplicacao
     */
    @ExceptionHandler({RegraDeNegocioException.class})
    public ResponseEntity<Object> handlerRegraDeNegocioException(RegraDeNegocioException ex, WebRequest request) {
        String mensagem = ex.getMessage();
        String detalhes = ex.toString();
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.BAD_REQUEST, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    /**
     * Violação de integridade do banco de dados - relacionamento entre tabelas
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.violacao-de-integridade", null, LocaleContextHolder.getLocale());
        String detalhes = ExceptionUtils.getRootCauseMessage(ex);
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.CONFLICT, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Manipula mensagens de erro ilegíveis
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagem = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());
        String detalhes = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.BAD_REQUEST, uri, metodo));
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Manipula mensagens de erro de validação para atributos de objetos
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = createErrorList(ex.getBindingResult(), HttpStatus.BAD_REQUEST, uri, metodo);
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Manipula mensagens de erro quando o recurso não é encontrado
     */
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
        String detalhes = ex.toString();
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.NOT_FOUND, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Manipula mensagens de erro de conversão de tipo
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.tipo-atributo-incorreto", null, LocaleContextHolder.getLocale());
        String detalhes = ex.toString();
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.BAD_REQUEST, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    /**
     * Erro ao manipular o objeto
     */
    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ResponseEntity<Object> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.construcao-do-objeto-esta-incorreta", null, LocaleContextHolder.getLocale());
        String detalhes = ExceptionUtils.getRootCauseMessage(ex);
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.BAD_REQUEST, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Método não suportado
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.metodo-nao-suportado", null, LocaleContextHolder.getLocale());
        String detalhes = ex.toString();
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        String metodo = ((ServletWebRequest) request).getRequest().getMethod();
        List<ApiErro> errors = Arrays.asList(new ApiErro(mensagem, detalhes, HttpStatus.METHOD_NOT_ALLOWED, uri, metodo));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
    }
}
