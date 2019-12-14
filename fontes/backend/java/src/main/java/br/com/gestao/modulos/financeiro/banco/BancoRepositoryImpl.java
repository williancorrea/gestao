package br.com.gestao.modulos.financeiro.banco;

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

public class BancoRepositoryImpl implements BancoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Banco> findAll(@PageableDefault(size = 5) Pageable pageable, BancoRepositoryFiltro filtro) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Banco> criteria = criteriaBuilder.createQuery(Banco.class);
        Root<Banco> root = criteria.from(Banco.class);

//        utilizar fech para resolver o problema do N+1 se for necessario ex:
//        root.fetch("restaurante").root.fetch("cozinha ");
//        root.fetch("cliente");

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
    private Predicate[] criarRestricoes(BancoRepositoryFiltro filtro, CriteriaBuilder criteriaBuilder, Root root) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(filtro.getFiltroGlobal())) {
            predicates.add(criteriaBuilder.like(root.get("codigo"), "%" + filtro.getFiltroGlobal() + "%"));
            predicates.add(criteriaBuilder.like(root.get("nome"), "%" + filtro.getFiltroGlobal() + "%"));
            predicates.add(criteriaBuilder.like(root.get("url"), "%" + filtro.getFiltroGlobal() + "%"));

            List<Predicate> predicatesOR = new ArrayList<Predicate>();
            predicatesOR.add(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

            return predicatesOR.toArray(new Predicate[predicatesOR.size()]);
        }

        if (StringUtils.isNotBlank(filtro.getCodigo())) {
            predicates.add(criteriaBuilder.like(root.get("codigo"), "%" + filtro.getCodigo() + "%"));
        }
        if (StringUtils.isNotBlank(filtro.getNome())) {
            predicates.add(criteriaBuilder.like(root.get("nome"), "%" + filtro.getNome() + "%"));
        }
        if (StringUtils.isNotBlank(filtro.getUrl())) {
            predicates.add(criteriaBuilder.like(root.get("url"), "%" + filtro.getUrl() + "%"));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    /**
     * TOTAL DE REGISTROS - SEM PAGINACAO
     */
    private Long total(BancoRepositoryFiltro filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Banco> root = criteria.from(Banco.class);

        Predicate[] predicates = criarRestricoes(filtro, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
