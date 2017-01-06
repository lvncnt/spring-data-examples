package com.spring.start.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.spring.start.model.Employee;
import com.spring.start.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
	@Autowired
	private EmployeeService employeeService; 
	
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Employee> getAllEmployee() {
        return employeeService.findAllEmployee();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") long id){
    	Employee employee = employeeService.findEmployeeById(id);
    	if(employee == null){
    		return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND); 
    	}
    	return new ResponseEntity<Employee>(employee, HttpStatus.OK); 
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Void> addEmployee(@RequestBody Employee employee, 
    		UriComponentsBuilder ucBuilder){
    	if(employeeService.isEmployeeExist(employee)){
    		return new ResponseEntity<Void>(HttpStatus.CONFLICT); 
    	}
    	
    	employeeService.addEmployee(employee);
    	HttpHeaders headers = new HttpHeaders();
       
    	headers.setLocation(ucBuilder.path("/employee/{id}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id, 
    		@RequestBody Employee employee){
        if (employeeService.findEmployeeById(id)==null) {
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
        employeeService.updateEmployee(id, employee);;
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") long id) {
        if (employeeService.findEmployeeById(id) == null) {
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
        employeeService.deleteEmployee(id);;
        return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
    }
    
}
