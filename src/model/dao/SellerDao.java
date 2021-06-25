package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {

	//abstracts methods
	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Integer id);
	List<Seller> findByName(String name);
	List<Seller> findByEmail(String email);
	List<Seller> findByBirthDate(Date birthDate);
	List<Seller> findByDepartment(Department department);
	List<Seller> findByBaseSalary(Double baseSalary);
	List<Seller> findAll();
}
