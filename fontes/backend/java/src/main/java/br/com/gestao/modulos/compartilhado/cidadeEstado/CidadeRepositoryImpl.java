package br.com.gestao.modulos.compartilhado.cidadeEstado;

import br.com.gestao.utils.jpa.QueryFiltroPadrao;
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

public class CidadeRepositoryImpl implements CidadeRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Cidade> findAll(@PageableDefault(size = 5) Pageable pageable, QueryFiltroPadrao filtro) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Cidade> criteria = criteriaBuilder.createQuery(Cidade.class);
        Root<Cidade> root = criteria.from(Cidade.class);

        // Fetch
        root.fetch("estado");

        // Todo: REMOVER
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
    private Predicate[] criarRestricoes(QueryFiltroPadrao filtro, CriteriaBuilder criteriaBuilder, Root root) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(filtro.getFiltroGlobal())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"));

            List<Predicate> predicatesOR = new ArrayList<Predicate>();
            predicatesOR.add(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    /**
     * TOTAL DE REGISTROS - SEM PAGINACAO
     */
    private Long total(QueryFiltroPadrao filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Cidade> root = criteria.from(Cidade.class);

        Predicate[] predicates = criarRestricoes(filtro, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
