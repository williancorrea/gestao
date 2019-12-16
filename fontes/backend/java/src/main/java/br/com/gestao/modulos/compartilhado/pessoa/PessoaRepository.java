package br.com.gestao.modulos.compartilhado.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

    Optional<Pessoa> findByUuid(String key);

    Optional<Pessoa> findByPessoaFisicaCpf(String cpf);

    Optional<Pessoa> findByPessoaJuridicaCnpj(String cnpj);
}
