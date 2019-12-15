package br.com.gestao.modulos.compartilhado.cidadeEstado;

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
public class CidadeService {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade novo(Cidade obj) {
        return criarOuAtualizar(obj);
    }

    @Transactional
    public Cidade atualizar(String key, Cidade objNovo) {
        Cidade objEncontrado = buscarCidadePor(key);
        BeanUtils.copyProperties(objNovo, objEncontrado, "id", "uuid");
        return criarOuAtualizar(objNovo);
    }

    private Cidade criarOuAtualizar(Cidade cidade) {
        Estado estado = buscarEstadoPor(cidade.getEstado().getKey());
        cidade.setEstado(estado);

        return cidadeRepository.saveAndFlush(cidade);
    }

    @Transactional
    public void excluir(String key) {
        Cidade obj = buscarCidadePor(key);
        excluir(obj);
    }

    private void excluir(Cidade obj) {
        cidadeRepository.deleteById(obj.getId());
        cidadeRepository.flush();
    }

    public Cidade buscarCidadePor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<Cidade> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = cidadeRepository.findById(Long.parseLong(key));
                } else {
                    obj = cidadeRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.cidade-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }

    public Estado buscarEstadoPor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<Estado> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = estadoRepository.findById(Long.parseLong(key));
                } else {
                    obj = estadoRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.estado-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }
}
