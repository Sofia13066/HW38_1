package telran.employee.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import telran.employee.model.Employee;
import telran.employee.model.SalesManager;

public class CompanyMapImpl implements Company {
	Map<Integer, Employee> employees;
	int capacity;

	public CompanyMapImpl(int capacity) {
		employees = new HashMap<Integer, Employee>();
		this.capacity = capacity;
	}

	@Override
	public boolean addEmployee(Employee employee) {
		if (employee == null || employees.size() == capacity) {
			return false;
		}
		return employees.putIfAbsent(employee.getId(), employee) == null;
	}

	// O(1)
	@Override
	public Employee removeEmployee(int id) {
		return employees.remove(id);
	}

	// O(1)
	@Override
	public Employee findEmployee(int id) {
		return employees.get(id);
	}

	// O(n)
	@Override
	public double totalSalary() {                               //change
		
        return employees.values().stream()
        .map(n -> n.calcSalary()) //Employee::calcSalary
        .reduce(0., (m, n) -> m + n);
	}

	@Override
	public double totalSales() {                             //change
		return employees.values().stream()
        .filter(n -> (n instanceof SalesManager))
		.map(n -> ((SalesManager) n).getSalesValue())
		.reduce(0., (m, l) -> m + l);
	}

	

	@Override
	public void printEmployees() {                                 //change
		employees.values().stream()
        .forEach(System.out::println);
	}

	@Override
	public int getSize() {
		return employees.size();
	}

	@Override
	public Employee[] findEmployeesHoursGreaterThan(int hours) {
		return findEmployeesByPredicate(e -> e.getHours() >= hours);
	}

	@Override
	public Employee[] findEmployeesSalaryBetween(double min, double max) {
		Predicate<Employee> predicate = new Predicate<>() {

			@Override
			public boolean test(Employee t) {
				return t.calcSalary() >= min && t.calcSalary() < max;
			}
		};
		return findEmployeesByPredicate(predicate);
	}

	private Employee[] findEmployeesByPredicate(Predicate<Employee> predicate) {  //change
		
        return employees.values().stream()
        .filter(predicate)
        .toArray(Employee[]::new);
    
	}

}