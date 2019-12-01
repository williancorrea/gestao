package br.com.gestao.gerenciadorErros.manipulador;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ClasseDespesa responsavel por apresentar os erros ao usuario
 */
public class ApiError {
    @Getter
    private int status;

    @Getter
    private String error;

    @Getter
    private String userMessage;

    @Getter
    private String developerMessage;

    public ApiError(String userMessage, String developerMessage, HttpStatus httpStatus) {
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }
}
