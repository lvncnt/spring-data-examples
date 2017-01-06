package com.spring.start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.spring.start.model.Employee;

@Service("customerService")
public class EmployeeService {
	
	private static List<Employee> employees; 
	static {
		employees = new ArrayList<>(); 
		employees.add(new Employee(12, "Jelly", 23, 6090.90));
		employees.add(new Employee(29, "Carrie", 24, 7090.90));
		employees.add(new Employee(43, "Jenny", 25, 5090.90));
	}
	
	public List<Employee> findAllEmployee(){
		return employees; 
	}
	
	public Employee findEmployeeById(long id){
		for(Employee employee: employees){
			if(employee.getId() == id){
				return employee; 
			}
		}
		return null;
	}
	
	public boolean isEmployeeExist(Employee employee){
		return findByName(employee.getName()) != null; 
	}
	private Employee findByName(String name) {
        for(Employee employee: employees){
            if(employee.getName().equalsIgnoreCase(name)){
                return employee;
            }
        }
        return null;
    }
	
	public void addEmployee(Employee employee){
		employee.setId(new Random().nextInt(100) + 1);
		employees.add(employee);
	}
	
	public void updateEmployee(long id, Employee employee){
		for(int i = 0; i < employees.size(); i ++){
            if(employees.get(i).getId() == id){
                employees.set(i, employee);
            }
        }
	}
	
	public void deleteEmployee(long id){
		for(int i = 0; i < employees.size(); i ++){
            if(employees.get(i).getId() == id){
                employees.remove(i);
            }
        }
	}

}
