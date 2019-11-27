package br.com.gestao.modulos.financeiro.banco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    public Banco update(String key, Banco banco) {
        Long id = 0L;
//        try {
//            id = new Criptografia().getKey(key);
//        } catch (Exception e) {
//            throw new BancoExceptionNaoEncontrado();
//        }
        return update(id, banco);
    }

    public Banco update(Long id, Banco banco) {
        Banco updateFound = buscarPorId(id);
        banco.setId(updateFound.getId());
        return bancoRepository.save(banco);
    }

    public Banco buscarPorId(Long id) {
        if (id != null && id > 0) {
            Optional<Banco> obj = bancoRepository.findById(id);
            if (obj.isPresent()) {
                return obj.get();
            }
        }
        throw new BancoExceptionNaoEncontrado();
    }

//    public Banco buscarPorKey(String key) {
//        try {
//            return buscarPorId(new Criptografia().getKey(key));
//        } catch (Exception e) {
//            throw new BancoExceptionNaoEncontrado();
//        }
//    }
}
