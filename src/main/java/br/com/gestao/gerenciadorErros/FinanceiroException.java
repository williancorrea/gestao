package br.com.gestao.gerenciadorErros;

import br.com.gestao.gerenciadorErros.manipulador.ManipuladorExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class FinanceiroException extends ManipuladorExceptions {

    @Autowired
    private MessageSource messageSource;

}
