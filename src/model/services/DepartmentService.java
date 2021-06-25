package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	//association with DepatmentDao, so dao has every methods from database
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	//acess findAll method from database
	public List<Department> findAll(){
		return dao.findAll();
	}
	
	//save or update database
	public void saveOrUpdate(Department obj) {
		//new department
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		//department already exists
		else {
			dao.update(obj);
		}
	}
	
	//remove department from database
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
	
	//find department by name 
	public Department findDepartmentByName(Department obj) {
		return dao.findByName(obj.getName());
	}
}
