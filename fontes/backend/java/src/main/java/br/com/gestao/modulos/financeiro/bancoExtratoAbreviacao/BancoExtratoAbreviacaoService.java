package br.com.gestao.modulos.financeiro.bancoExtratoAbreviacao;

import br.com.gestao.config.propriedades.GestaoApiProperties;
import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import br.com.gestao.modulos.financeiro.banco.BancoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BancoExtratoAbreviacaoService {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BancoExtratoAbreviacaoRepository bancoExtratoAbreviacaoRepository;

    @Autowired
    private BancoService bancoService;

    @Transactional
    public BancoExtratoAbreviacao novo(BancoExtratoAbreviacao obj) {
        return criarOuAtualizar(obj);
    }

    @Transactional
    public BancoExtratoAbreviacao atualizar(String key, BancoExtratoAbreviacao objNovo) {
        BancoExtratoAbreviacao objEncontrado = buscarPor(key);
        BeanUtils.copyProperties(objNovo, objEncontrado, "id", "uuid");
        return criarOuAtualizar(objNovo);
    }

    private BancoExtratoAbreviacao criarOuAtualizar(BancoExtratoAbreviacao obj) {
        obj.setBanco(bancoService.buscarPor(obj.getBanco().getKey()));
        return bancoExtratoAbreviacaoRepository.saveAndFlush(obj);
    }

    @Transactional
    public void excluir(String key) {
        BancoExtratoAbreviacao obj = buscarPor(key);
        excluir(obj);
    }

    private void excluir(BancoExtratoAbreviacao obj) {
        bancoExtratoAbreviacaoRepository.deleteById(obj.getId());
        bancoExtratoAbreviacaoRepository.flush();
    }

    public BancoExtratoAbreviacao buscarPor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<BancoExtratoAbreviacao> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = bancoExtratoAbreviacaoRepository.findById(Long.parseLong(key));
                } else {
                    obj = bancoExtratoAbreviacaoRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.banco-extrato-abreviacao-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }
}
