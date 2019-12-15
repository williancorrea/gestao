package br.com.gestao.modulos.financeiro.bancoAgencia;

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

public class BancoAgenciaRepositoryImpl implements BancoAgenciaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<BancoAgencia> findAll(@PageableDefault(size = 5) Pageable pageable, BancoAgenciaRepositoryFiltro filtro) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<BancoAgencia> criteria = criteriaBuilder.createQuery(BancoAgencia.class);
        Root<BancoAgencia> root = criteria.from(BancoAgencia.class);
        root.fetch("banco");

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
    private Predicate[] criarRestricoes(BancoAgenciaRepositoryFiltro filtro, CriteriaBuilder criteriaBuilder, Root root) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(filtro.getFiltroGlobal())) {
//            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("url")), "%" + filtro.getFiltroGlobal().toLowerCase() + "%"));

            List<Predicate> predicatesOR = new ArrayList<Predicate>();
            predicatesOR.add(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

            return predicatesOR.toArray(new Predicate[predicatesOR.size()]);
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    /**
     * TOTAL DE REGISTROS - SEM PAGINACAO
     */
    private Long total(BancoAgenciaRepositoryFiltro filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<BancoAgencia> root = criteria.from(BancoAgencia.class);

        Predicate[] predicates = criarRestricoes(filtro, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
