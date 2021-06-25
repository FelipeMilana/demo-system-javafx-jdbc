package model.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	//association with SellerDao, so dao has every methods from database
	private SellerDao dao = DaoFactory.createSellerDao();
	
	//acess findAll method from database
	public List<Seller> findAll(){
		return dao.findAll();
	}
	
	//save or update database
	public void saveOrUpdate(Seller obj) {
		//new seller
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		//seller already exists
		else {
			dao.update(obj);
		}
	}
	
	//remove seller from database
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
	
	// find seller by name
	public List<Seller> findSellers(Seller seller) {
		
		List<Seller> list = new ArrayList<Seller>();
		Set<Seller> set = new LinkedHashSet<Seller>();
		
		if(seller.getName() != null) {
			List<Seller> s = dao.findAll();
			
			String[] field = seller.getName().split(" ");
			
			if(field.length == 1) {
				set = s.stream().filter(x -> x.getName().startsWith(field[0])).collect(Collectors.toSet());
			}
			else {
				set.addAll(dao.findByName(seller.getName()));
			}
		}
		
		if(seller.getEmail() != null && !set.isEmpty()) {
			set.retainAll(dao.findByEmail(seller.getEmail()));
		}
		
		if(seller.getEmail() != null && set.isEmpty()) {
			set.addAll(dao.findByEmail(seller.getEmail()));
		}
		
		if(seller.getBirthDate() != null && !set.isEmpty()) {
			set.retainAll(dao.findByBirthDate(seller.getBirthDate()));
		}
		
		if(seller.getBirthDate() != null && set.isEmpty()) {
			set.addAll(dao.findByBirthDate(seller.getBirthDate()));
		}
		
		if(seller.getBaseSalary() != null && !set.isEmpty()) {
			set.retainAll(dao.findByBaseSalary(seller.getBaseSalary()));
		}
		
		if(seller.getBaseSalary() != null && set.isEmpty()) {
			set.addAll(dao.findByBaseSalary(seller.getBaseSalary()));
		}
		
		if(seller.getDepartment() != null && !set.isEmpty()) {
			set.retainAll(dao.findByDepartment(seller.getDepartment()));
		}
		
		if(seller.getDepartment() != null && set.isEmpty()) {
			set.addAll(dao.findByDepartment(seller.getDepartment()));
		}
		
		for(Seller s: set) {
			list.add(s);
		}
		
		return list;
	}
}
