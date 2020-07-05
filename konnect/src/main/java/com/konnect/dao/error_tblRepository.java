package com.konnect.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.konnect.model.error_tbl;

// This will be AUTO IMPLEMENTED by Spring into a Bean called error_tblRepository
// CRUD refers Create, Read, Update, Delete

public interface error_tblRepository extends CrudRepository<error_tbl, Integer> {
	@Query(
			  value = "SELECT * FROM error_tbl u where u.JIRA_FLAG=0;", 
			  nativeQuery = true)
			Collection<error_tbl> findAllErrorsJiraFalse();
	@Query(
			value = "SELECT * FROM error_tbl u where u.SOLUTION_FLAG=0 and u.JIRA_FLAG=1;", 
			nativeQuery = true)
			Collection<error_tbl> findAllErrorsSolutionFalseJiraTrue();
	@Query(
			value = "SELECT * FROM error_tbl u where u.SOLUTION_FLAG=1 and u.JIRA_FLAG=1;", 
			nativeQuery = true)
			Collection<error_tbl> findAllErrorsSolutionTrueJiraTrue();

}