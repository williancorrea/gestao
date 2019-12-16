package br.com.gestao.modulos.compartilhado.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

    Optional<Pessoa> findByUuid(String key);

    Optional<Pessoa> findByPessoaFisicaCpf(String cpf);

    Optional<Pessoa> findByPessoaJuridicaCnpj(String cnpj);

//    @Query("from PESSOA p where p.pessoaFisica.cpf = :cpf")
//    Optional<Pessoa> findOneByCPF(String cpf);

//    @Query("from PESSOA p where p.pessoaJuridica.cnpj = :cnpj")
//    Optional<Pessoa> findOneByCNPJ(String cnpj);

    @Query("select (count(p.id) > 0) from PESSOA p where p.pessoaFisica.cpf = :cpf")
    Boolean verificarCPFJaCadastrado(String cpf, Long id);

    @Query("select (count(p.id) > 0) from PESSOA p where 1=1 and p.pessoaJuridica.cnpj = :cnpj")
    Boolean verificarCNPJJaCadastrado(String cnpj, Long id);

}
