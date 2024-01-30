package com.nowon.bul.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dept")
@Entity
public class DeptEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long no;
	private String name;//부서이름

	@JoinColumn(name = "upperNo")
	@ManyToOne
	private DeptEntity upper;
	
	@JoinColumn(name = "mgrNo")
	@OneToOne
	private EmpEntity mgr;
}
