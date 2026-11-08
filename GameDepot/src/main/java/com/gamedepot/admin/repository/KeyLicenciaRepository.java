package com.gamedepot.admin.repository;

import com.gamedepot.admin.model.KeyLicencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeyLicenciaRepository extends JpaRepository<KeyLicencia, Long> {

    List<KeyLicencia> findByVarianteIdAndAsignadaFalse(Long varianteId);

    long countByVarianteIdAndAsignadaFalse(Long varianteId);

    Optional<KeyLicencia> findFirstByVarianteIdAndAsignadaFalse(Long varianteId);

    Optional<KeyLicencia> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
