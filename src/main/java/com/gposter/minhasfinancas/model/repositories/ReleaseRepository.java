package com.gposter.minhasfinancas.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gposter.minhasfinancas.model.entities.Release;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

}
