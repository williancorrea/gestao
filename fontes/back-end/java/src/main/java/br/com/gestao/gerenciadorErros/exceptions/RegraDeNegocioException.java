package br.com.gestao.gerenciadorErros.exceptions;

public class RegraDeNegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RegraDeNegocioException(String message) {
        super(message);
    }
}
