package br.com.gestao.gerenciadorErros;

import br.com.gestao.gerenciadorErros.manipulador.ManipuladrExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class FinanceiroException extends ManipuladrExceptions {

    @Autowired
    private MessageSource messageSource;

}
