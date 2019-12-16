package br.com.gestao.modulos.compartilhado.pessoa;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Pessoa> findAll(@PageableDefault(size = 5) Pageable pageable, PessoaRepositoryFiltro filtro) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);
        root.fetch("pessoaJuridica");
        root.fetch("pessoaFisica");
        root.fetch("cidade");

        //Criterios da pesquisa
        Predicate[] predicates = criarRestricoes(filtro, criteriaBuilder, root);
        criteria.where(predicates);

        //Paginacao
        Query consultaRegistro = manager.createQuery(criteria);
        consultaRegistro.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        consultaRegistro.setMaxResults(pageable.getPageSize());

        //Consulta paginada
        return new PageImpl<>(consultaRegistro.getResultList(), pageable, total(filtro));
    }

    /**
     * MONTA OS CRITERIOS PARA A PESQUISA
     */
    private Predicate[] criarRestricoes(PessoaRepositoryFiltro filtro, CriteriaBuilder criteriaBuilder, Root root) {
        List<Predicate> predicates = new ArrayList<>();


        //Filtrar somente os Motoristas
        if (filtro.isMotorista()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("pessoaFisica").get("motoristaInativo")), false));
        }

        // Filtrar somente as pessoas que tem a cnh cadastrada
        if (filtro.isCnh()) {
            predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.isNotNull(criteriaBuilder.lower(root.get("pessoaFisica").get("cnhNumero"))),
                            criteriaBuilder.isNotEmpty(criteriaBuilder.lower(root.get("pessoaFisica").get("cnhNumero")))
                    )
            );
        }

        //Filtrar somente empresa do Grupo Rosinha Transportes
        if (filtro.isEmpresaPrincipalOuFilial()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("empresaPrincipaOulFilial")), true));
        }

        //Filtrar somente empresa do Grupo Rosinha Transportes
        if (filtro.isRepresentanteComercial()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("empresaRepresentanteComercial")), true));
        }

        //SOMENTE CADASTROS ATIVOS
        if (filtro.isSomenteAtivo()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("inativo")), false));
        }

        if (StringUtils.isNotBlank(filtro.getFiltroGlobal())) {
            predicates.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("fantasia")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("endereco")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("bairro")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("cidade").get("nome")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("cep")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%")
                    )
            );
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    /**
     * TOTAL DE REGISTROS - SEM PAGINACAO
     */
    private Long total(PessoaRepositoryFiltro filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        Predicate[] predicates = criarRestricoes(filtro, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    @Override
    public Boolean verificarCPFJaCadastrado(String cpf, Long id) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        criteria.where(criteriaBuilder.equal(root.get("pessoaFisica").get("cpf"), cpf));
        Query consulta = manager.createQuery(criteria);

        if (consulta.getResultList().size() == 0 || ((Pessoa) consulta.getSingleResult()).getId() == id) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean verificarCNPJJaCadastrado(String cnpj, Long id) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        criteria.where(criteriaBuilder.equal(root.get("pessoaJuridica").get("cnpj"), cnpj));
        Query consulta = manager.createQuery(criteria);

        if (consulta.getResultList().size() == 0 || ((Pessoa) consulta.getSingleResult()).getId() == id) {
            return false;
        }
        return true;
    }
}
