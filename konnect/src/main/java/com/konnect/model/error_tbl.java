package com.konnect.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity		
public class error_tbl{
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
		 public Integer error_id;
		 public String  error_description;
		 public String error_category;
		 public String reported_by;
		 public String customer_name;
		 public String reported_date;
	     public String solution;
	     public int solution_flag;
	     public int jira_flag;
	}
	
