package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {

	//abstracts methods
	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findByName(String name);
	List<Department> findAll();
}
