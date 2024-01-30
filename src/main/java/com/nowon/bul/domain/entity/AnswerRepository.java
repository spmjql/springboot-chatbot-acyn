package com.nowon.bul.domain.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>{

	Optional<Answer> findByKeyword(String string);

}
