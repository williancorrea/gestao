package br.com.gestao.gerenciadorErros.manipulador;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiErro {

    private int status;
    private String metodo;
    private LocalDateTime dataHora;
    private String erro;
    private String mensagem;
    private String detalhes;
    private String origem;


    public ApiErro(String mensagem, String detalhes, HttpStatus httpStatus, String origem, String metodo) {
        this.status = httpStatus.value();
        this.dataHora = LocalDateTime.now();
        this.erro = httpStatus.getReasonPhrase();
        this.mensagem = mensagem;
        this.detalhes = detalhes;
        this.origem = origem;
        this.metodo = metodo;
    }
}
