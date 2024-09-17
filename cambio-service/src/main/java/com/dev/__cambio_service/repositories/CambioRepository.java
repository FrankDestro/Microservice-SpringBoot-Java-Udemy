package com.dev.__cambio_service.repositories;

import com.dev.__cambio_service.models.entities.Cambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CambioRepository extends JpaRepository <Cambio, Long> {

    Cambio findByFromAndTo(String from, String to);

}
