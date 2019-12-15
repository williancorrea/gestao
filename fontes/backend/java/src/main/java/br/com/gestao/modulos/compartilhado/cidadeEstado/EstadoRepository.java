package br.com.gestao.modulos.compartilhado.cidadeEstado;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByUuid(String key);
}
