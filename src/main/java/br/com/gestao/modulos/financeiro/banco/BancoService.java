package br.com.gestao.modulos.financeiro.banco;

import br.com.gestao.exceptions.EntidadeNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BancoService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BancoRepository bancoRepository;

    public Banco atualizar(String chave, Banco banco) {
        Banco objEncontrado = buscarPor(chave);
        banco.setId(objEncontrado.getId());
        return atualizar(banco);
    }

    public Banco atualizar(Long id, Banco banco) {
        Banco objEncontrado = buscarPor(id);
        banco.setId(objEncontrado.getId());
        return atualizar(banco);
    }

    private Banco atualizar(Banco banco) {
        return bancoRepository.save(banco);
    }

    public void excluir(String chave) {
        Banco obj = buscarPor(chave);
        excluir(obj);
    }

    public void excluir(Long id) {
        Banco obj = buscarPor(id);
        excluir(obj);
    }

    private void excluir(Banco obj) {
        bancoRepository.deleteById(obj.getId());
    }

    public Banco buscarPor(Long id) {
        if (id != null) {
            Optional<Banco> obj = bancoRepository.findById(id);
            if (obj.isPresent()) {
                return obj.get();
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.banco-nao-encontrado", null, LocaleContextHolder.getLocale()));
    }

    public Banco buscarPor(String chave) {
        if (!chave.isEmpty()) {
            Optional<Banco> obj = bancoRepository.findByChave(chave);
            if (obj.isPresent()) {
                return obj.get();
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.banco-nao-encontrado", null, LocaleContextHolder.getLocale()));
    }
}
