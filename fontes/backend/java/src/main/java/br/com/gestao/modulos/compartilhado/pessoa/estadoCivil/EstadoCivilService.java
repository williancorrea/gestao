package br.com.gestao.modulos.compartilhado.pessoa.estadoCivil;

import br.com.gestao.config.propriedades.GestaoApiProperties;
import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EstadoCivilService {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    @Transactional
    public EstadoCivil novo(EstadoCivil obj) {
        return criarOuAtualizar(obj);
    }

    @Transactional
    public EstadoCivil atualizar(String key, EstadoCivil objNovo) {
        EstadoCivil objEncontrado = buscarPor(key);
        BeanUtils.copyProperties(objNovo, objEncontrado, "id", "uuid");
        return criarOuAtualizar(objNovo);
    }

    private EstadoCivil criarOuAtualizar(EstadoCivil obj) {
        return estadoCivilRepository.saveAndFlush(obj);
    }

    @Transactional
    public void excluir(String key) {
        EstadoCivil obj = buscarPor(key);
        excluir(obj);
    }

    private void excluir(EstadoCivil obj) {
        estadoCivilRepository.deleteById(obj.getId());
        estadoCivilRepository.flush();
    }

    public EstadoCivil buscarPor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<EstadoCivil> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = estadoCivilRepository.findById(Long.parseLong(key));
                } else {
                    obj = estadoCivilRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.estado-civil-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }
}
