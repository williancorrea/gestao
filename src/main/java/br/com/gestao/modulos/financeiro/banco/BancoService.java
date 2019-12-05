package br.com.gestao.modulos.financeiro.banco;

import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BancoService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BancoRepository bancoRepository;

    @Transactional
    public Banco atualizar(String uuid, Banco objNovo) {
        Banco objEncontradoBD = buscarPor(uuid);
        BeanUtils.copyProperties(objNovo, objEncontradoBD, "id", "uuid");
        return atualizar(objNovo);
    }

    private Banco atualizar(Banco banco) {
        return bancoRepository.save(banco);
    }

    @Transactional
    public void excluir(String uuid) {
        Banco obj = buscarPor(uuid);
        excluir(obj);
    }

    private void excluir(Banco obj) {
        bancoRepository.deleteById(obj.getId());
    }

    public Banco buscarPor(String uuid) {
        if (!uuid.isEmpty()) {
            Optional<Banco> obj = bancoRepository.findByUuid(uuid);
            if (obj.isPresent()) {
                return obj.get();
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.banco-nao-encontrado", new Object[]{uuid}, LocaleContextHolder.getLocale()));
    }
}
