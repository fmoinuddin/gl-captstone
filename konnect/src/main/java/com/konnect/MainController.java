package com.konnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.konnect.dao.error_tblRepository;
import com.konnect.model.error_tbl;
import org.springframework.stereotype.Controller;
import com.konnect.integration.JiraClient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
public class MainController {
	
	@Autowired
	private error_tblRepository error_tblRepository;
	private JiraClient jiraclient = new JiraClient("jira","kamisama123","http://3.236.18.232:8080");
	int size=3;
	 
    @GetMapping("/konnecterror")
    public String error(final Model model) {
		 int page_num=0;
         Page<error_tbl> error_tbl = (Page<com.konnect.model.error_tbl>)error_tblRepository.findAllErrorsJiraFalse(PageRequest.of(page_num,size));
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg", "This lists all the errors tabulated from customer audio");
		 model.addAttribute("page_num",page_num);
		 return "konnecterror";
    }	 
    
    @PostMapping("/konnecterror")
    public String errorPage(@RequestParam(value = "page_num", required = true) int page_num,final Model model) {
    	if (page_num<0)
    		page_num=0;
		
         Page<error_tbl> error_tbl = (Page<com.konnect.model.error_tbl>)error_tblRepository.findAllErrorsJiraFalse(PageRequest.of(page_num,size));
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg", "This lists all the errors tabulated from customer audio");
		 model.addAttribute("page_num",page_num);
		 return "konnecterror";
    }
    
    @GetMapping("/fixed")
    public String fixed(final Model model) {
       	 int page_num=0;
		 Page<error_tbl> error_tbl = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionFalseJiraTrue(PageRequest.of(page_num,size));
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg","This lists all the errors integrated with Jira.If fixed,please provide the solution");
		 model.addAttribute("page_num",page_num);
		 return "fixed";
   }
    
    @PostMapping("/fixed")
    public String fixed(@RequestParam(value = "page_num", required = true) int page_num,final Model model) {
    	if (page_num<0)
    		page_num=0;
    	
		 Page<error_tbl> error_tbl = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionFalseJiraTrue(PageRequest.of(page_num,size));
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg","This lists all the errors integrated with Jira.If fixed,please provide the solution");
		 model.addAttribute("page_num",page_num);
		 return "fixed";
   }
    @GetMapping("/solution")
    public String solution(final Model model) {
  	  	 int page_num=0;
  	  	 Page<error_tbl> error_tbl = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionTrueJiraTrue(PageRequest.of(page_num,size));
  		 model.addAttribute("error_tbls", error_tbl);
  		 model.addAttribute("msg","This lists all the Fixed Errors with Solution. Use it if you want to change the solution");
  		 model.addAttribute("page_num",page_num);
  		 return "solution";
    }
    
  @PostMapping("/solution")
  public String solution(@RequestParam(value = "page_num", required = true) int page_num,final Model model) {
	  	if (page_num<0)
	  		page_num=0;
	  	 Page<error_tbl> error_tbl = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionTrueJiraTrue(PageRequest.of(page_num,size));
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg","This lists all the Fixed Errors with Solution. Use it if you want to change the solution");
		 model.addAttribute("page_num",page_num);
		 return "solution";
  }
  
 @PostMapping("/updatesolutionfixed") 
 public String updatesolutionfixed( 
     @RequestParam(value = "error_id", required = true) int error_id,@RequestParam(value = "page_num", required = true) int page_num,
     @RequestParam(value = "Solution", required = true) String solution,Model model)
  {    
	error_tbl error_tbl = error_tblRepository.findById(error_id).get(); 
	error_tbl.solution = solution; 
	error_tbl.solution_flag=1;
	error_tblRepository.save(error_tbl);
	Page<error_tbl> error_tbl1 = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionFalseJiraTrue(PageRequest.of(page_num,size));
	model.addAttribute("error_tbls", error_tbl1);
	model.addAttribute("msg", "Successful updated Solution for eror_id:" + error_id + " solution:" + solution);
	model.addAttribute("page_num",page_num);
    return "fixed";
  }
 
 @PostMapping("/updatejiraerror") 
 public String updatejiraerror( 
     @RequestParam(value = "error_id", required = true) int error_id, @RequestParam(value = "page_num", required = true) int page_num,
     @RequestParam(value = "error_description", required = true) String error_description,
     Model model) throws Exception
  {    

    String error_idS=Integer.toString(error_id);
    String result = jiraclient.createIssue("SMARTKONNE",10100L,error_idS,error_description);
    if(result!=null){
    	error_tbl error_tbl = error_tblRepository.findById(error_id).get(); 
    	error_tbl.jira_flag=1;
    	error_tblRepository.save(error_tbl);
    	model.addAttribute("msg", "Successful updated Jira for error_id: " + error_id + " & Jira_id:" + " " + result);
    }else{
    	model.addAttribute("msg", "UnSuccessful updated Jira for error_id:" + error_id);
    }
    Page<error_tbl> error_tbl1 = (Page<error_tbl>)error_tblRepository.findAllErrorsJiraFalse(PageRequest.of(page_num,size));
	model.addAttribute("error_tbls", error_tbl1);
	model.addAttribute("page_num",page_num);
	return "konnecterror";
  }
 
 @PostMapping("/updatesolutionsolution") 
 public String updatesolutionsolution( 
     @RequestParam(value = "error_id", required = true) int error_id,@RequestParam(value = "page_num", required = true) int page_num,
     @RequestParam(value = "Solution", required = true) String solution,Model model)
  {    
	error_tbl error_tbl = error_tblRepository.findById(error_id).get(); 
	error_tbl.solution = solution; 
	error_tbl.solution_flag=1;
	error_tblRepository.save(error_tbl);
	Page<error_tbl> error_tbl1 = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionTrueJiraTrue(PageRequest.of(page_num,size));
	model.addAttribute("error_tbls", error_tbl1);
	model.addAttribute("msg", "Successful updated Solution for error_id:" + error_id + " solution:" + solution);
	model.addAttribute("page_num",page_num);
    return "solution";
  }
 
 @PostMapping("/deletesolution") 
 public String deletesolution(@RequestParam(value = "page_num", required = true) int page_num, 
	     @RequestParam(value = "error_id", required = true) int error_id,
	     Model model)
{    
	error_tblRepository.deleteById(error_id);
	Page<error_tbl> error_tbl1 = (Page<error_tbl>)error_tblRepository.findAllErrorsSolutionTrueJiraTrue(PageRequest.of(page_num,size));
	model.addAttribute("error_tbls", error_tbl1);
	model.addAttribute("msg", "Successful deleted error_id:" + error_id);
	model.addAttribute("page_num",page_num);
    return "solution";
 }
 
 @GetMapping("/about")
 public String about() {
	return "about";
 }	 
}

