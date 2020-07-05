package com.konnect;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.konnect.dao.error_tblRepository;
import com.konnect.model.error_tbl;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
	
	@Autowired
	private error_tblRepository error_tblRepository;
	
    @GetMapping("/konnecterror")
    public String error(final Model model) {
		 List<error_tbl> error_tbl = (List<error_tbl>)error_tblRepository.findAllErrorsJiraFalse();
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg", "This lists all the errors tabulated from customer audio");
		 System.out.println("Done");
		 return "konnecterror";
    }	 
    
    @GetMapping("/fixed")
    public String fixed(final Model model) {
		 List<error_tbl> error_tbl = (List<error_tbl>)error_tblRepository.findAllErrorsSolutionFalseJiraTrue();
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg","This lists all the errors integrated with Jira.If fixed,please provide the solution");
		 System.out.println("Done");
		 return "fixed";
   }
  
  @GetMapping("/solution")
  public String solution(final Model model) {
	  	List<error_tbl> error_tbl = (List<error_tbl>)error_tblRepository.findAllErrorsSolutionTrueJiraTrue();
		 model.addAttribute("error_tbls", error_tbl);
		 model.addAttribute("msg","This lists all the Fixed Errors with Solution. Use it if you want to change the solution");
		 System.out.println("Done");
		 return "solution";
  }
  
 
 @PostMapping("/updatesolutionfixed") 
 public String updatesolutionfixed( 
     @RequestParam(value = "error_id", required = true) int error_id,
     @RequestParam(value = "Solution", required = true) String solution,Model model)
  {    
	System.out.println(error_id);
	
	error_tbl error_tbl = error_tblRepository.findById(error_id).get(); 
	error_tbl.solution = solution; 
	error_tbl.solution_flag=1;
	error_tblRepository.save(error_tbl);
	
	List<error_tbl> error_tbl1 = (List<error_tbl>)error_tblRepository.findAllErrorsSolutionFalseJiraTrue();
	model.addAttribute("error_tbls", error_tbl1);
	 
    model.addAttribute("msg", "Successful updated Solution for eror_id:" + error_id + " solution:" + solution);
    
    return "fixed";
  }
 
 @PostMapping("/updatejiraerror") 
 public String updatejiraerror( 
     @RequestParam(value = "error_id", required = true) int error_id,
     Model model)
  {    
	System.out.println(error_id);
	error_tbl error_tbl = error_tblRepository.findById(error_id).get(); 
	error_tbl.jira_flag=1;
	error_tblRepository.save(error_tbl);
    model.addAttribute("msg", "Successful updated Jira for error_id:" + error_id);
    
    List<error_tbl> error_tbl1 = (List<error_tbl>)error_tblRepository.findAllErrorsJiraFalse();
	model.addAttribute("error_tbls", error_tbl1);
		 
    return "konnecterror";
  }
 
 @PostMapping("/updatesolutionsolution") 
 public String updatesolutionsolution( 
     @RequestParam(value = "error_id", required = true) int error_id,
     @RequestParam(value = "Solution", required = true) String solution,Model model)
  {    
	System.out.println(error_id);
	
	error_tbl error_tbl = error_tblRepository.findById(error_id).get(); 
	error_tbl.solution = solution; 
	error_tbl.solution_flag=1;
	error_tblRepository.save(error_tbl);
	
	List<error_tbl> error_tbl1 = (List<error_tbl>)error_tblRepository.findAllErrorsSolutionTrueJiraTrue();
	model.addAttribute("error_tbls", error_tbl1);
	 
    model.addAttribute("msg", "Successful updated Solution for error_id:" + error_id + " solution:" + solution);
    
    return "solution";
  }
 
 @GetMapping("/about")
 public String about() {
	return "about";
 }	 
}

