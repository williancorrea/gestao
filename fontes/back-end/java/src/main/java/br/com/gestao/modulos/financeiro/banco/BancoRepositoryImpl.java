package br.com.gestao.modulos.financeiro.banco;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BancoRepositoryImpl implements BancoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Banco> findAll(BancoRepositoryFiltro filtro, Pageable pageable) {

        //Criterios da pesquisa
        Criteria criteria = criarCriteriaParaFiltro(filtro);

        //Paginacao
        criteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        criteria.setMaxResults(pageable.getPageSize());

        //Ordenacao
        if (filtro.getCampoOrdenacao() != null && filtro.getOrdemClassificacao() != null) {
            if (filtro.getOrdemClassificacao().equalsIgnoreCase("ASC")) {
                criteria.addOrder(Order.asc(filtro.getCampoOrdenacao()));
            } else {
                criteria.addOrder(Order.desc(filtro.getCampoOrdenacao()));
            }
        }

        //Consulta paginada
        return new PageImpl<>(criteria.list(), pageable, quantidadeRegistrosFiltrados(filtro));
    }

    /**
     * RECUPERA A QUANTIDADE DE REGISTRO
     *
     * @param filtro
     * @return
     */
    public int quantidadeRegistrosFiltrados(BancoRepositoryFiltro filtro) {
        Criteria criteria = criarCriteriaParaFiltro(filtro);
        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.uniqueResult()).intValue();
    }

    /**
     * MONTA OS CRITERIOS PARA A PESQUISA
     *
     * @param filtro
     * @return
     */
    private Criteria criarCriteriaParaFiltro(BancoRepositoryFiltro filtro) {

        //TODO: ALTERAR A CONSULTA PARA UTILIZAR O CRITERIA BUILDER - UTILIZAR O EXEMPLO DE RESTAURANTE
        //https://github.com/algaworks/curso-especialista-spring-rest/blob/master/05.20-estendendo-o-jpa-repository-para-customizar-o-repositorio-base/algafood-api/src/main/java/com/algaworks/algafood/infrastructure/repository/RestauranteRepositoryImpl.java

        Session session = manager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Banco.class);

        if (StringUtils.isNotBlank(filtro.getFiltroGlobal())) {
            Disjunction disjunction = Restrictions.disjunction(); // Restricao com OR
            disjunction.add(Restrictions.ilike("codigo", filtro.getFiltroGlobal(), MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("nome", filtro.getFiltroGlobal(), MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("url", filtro.getFiltroGlobal(), MatchMode.ANYWHERE));
            criteria.add(disjunction);

            return criteria;
        }

        if (StringUtils.isNotBlank(filtro.getCodigo())) {
            criteria.add(Restrictions.ilike("codigo", filtro.getCodigo(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotBlank(filtro.getNome())) {
            criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotBlank(filtro.getUrl())) {
            criteria.add(Restrictions.ilike("url", filtro.getUrl(), MatchMode.ANYWHERE));
        }

        return criteria;
    }
}
