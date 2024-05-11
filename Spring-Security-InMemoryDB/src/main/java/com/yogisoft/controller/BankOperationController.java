package com.yogisoft.controller;


import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.yogisoft.dto.ProjectHighlight;

@Controller
public class BankOperationController {
	
	
	
	@GetMapping("/")
	public String getHomePage() 
	{
		
		return "home";
	}
	
	
	@GetMapping("/offers")
	public String getOffers() 
	{
		
		return "offer";
		
	}
	
	@GetMapping("/loanApprove")
	public String approvedLoan(Map<String, Object> map) 
	{
	
		Integer loanAmount = new Random().nextInt(1000000);
		
		
		map.put("amount", loanAmount);
		
		
		return "loan";
		
		
	}
	
	
	@GetMapping("/balance")
	public String cheakBalance(Map<String,Object> map) 
	{
		Integer currentBalance = new Random().nextInt(1000);
		
		map.put("balance", currentBalance);
		
		
		return "show_balance";
		
		
		
	}
	
	@GetMapping("/denied")
	public String access_denied() 
	{
		
		
		
		return "access_denied";
		
		
		
	}
	
	@GetMapping("/logout")
	public String logout() 
	{
		
		
		
		return "logout";
		
		
		
	}
	
	
	@GetMapping("/registerhighlights")
	public  String  addHighlightPage(@ModelAttribute("highlight") ProjectHighlight highlight ) {
	
		//return LVN
		return  "addHighlights";
		
	}
	
	@PostMapping("/registerhighlights")
	public  String  addHighlights(HttpSession ses, Map<String, Object> map, @ModelAttribute("highlight") ProjectHighlight highlight) {
		
		
		
		String url = "http://localhost:1000/projecthighlight/addHighlight";
		
		Gson gson = new Gson();
		
		String json = gson.toJson(highlight); 
		
		HttpHeaders header = new org.springframework.http.HttpHeaders();
		
		header.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<String>(json,header);
		
		//Send Request using RestTemplate 
		
		RestTemplate template = new RestTemplate();
		
		
		ResponseEntity<String> response = template.postForEntity(url, entity, String.class);
		
		String msg = response.getBody();
		
		
		ses.setAttribute("resultMsg", msg);
		
		
		
		
		//return LVN
		return "redirect:/";
	}
	
	@GetMapping("/highlights")
	public   String   showHighlights(Map<String,Object> map) throws JsonMappingException, JsonProcessingException {
		
		
		RestTemplate template = new RestTemplate();
		
		String url = "http://localhost:1000/projecthighlight/getAllHighlights";
		
		ResponseEntity<String> allHighlights= template.exchange(url,HttpMethod.GET,null,String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		
		Iterable<ProjectHighlight> result = mapper.readValue(allHighlights.getBody(), new TypeReference<Iterable<ProjectHighlight>>() {
		});
		
		
		//invoke the service class method
		// keep the result in the Model attribute
		map.put("projectHighlights", result);
		//return LVN
		return  "show_highlights";
	}

}
