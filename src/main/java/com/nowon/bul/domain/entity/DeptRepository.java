package com.nowon.bul.domain.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<DeptEntity, Long>{

	Optional<DeptEntity> findByName(String string);

}
