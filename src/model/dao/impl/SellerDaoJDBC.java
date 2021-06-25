package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	//Attributes
	private Connection conn;
	
	//constructor with arguments.
	public  SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	//methods concretes from SellerDao
	
	/*This method receive a seller as an argument
	 * and insert this seller in database
	 */
	@Override
	public void insert(Seller obj) {
		//Variables from JDBC
		PreparedStatement st = null;
		
		try {
			/*Create a table with instructions. Use return_generated
			 * to find a id of insert seller.
			 */
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)  "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			//add values to "?"
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			//number of insertions
			int rowsAffected = st.executeUpdate();
			
			//if an insertion ocurred..
			if(rowsAffected > 0) {
				//add a table with id's
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					//add id of the table to id seller
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			/*we need make an insertion, so if an insertion not
			 * ocurred...
			 */
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	/*This method receive a seller as an argument
	 * and update this seller in database
	 */
	@Override
	public void update(Seller obj) {
		// Variables from JDBC
		PreparedStatement st = null;
		
		try {
			/*Create a table with instructions.We use id as an restriction
			 * because each seller has an exclusive id.
			 */
			st = conn.prepareStatement(
					"UPDATE seller " 
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE "
					+ "(id = ?)");
			
			// add values to "?"
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	/*This method receive an id as an argument
	 * and delete this seller from database
	 */
	@Override
	public void deleteById(Integer id) {
		// Variables from JDBC
		PreparedStatement st = null;
		
		try {
			/*Create a table with instructions.We use id as an restriction because each
			 * seller has an exclusive id.
			 */
			st = conn.prepareStatement(
					"DELETE from seller " 
					+ "WHERE " 
					+ "(id = ?)");
			
			// add values to "?"
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	/*this private method return a department receiving a resultSet
	 * as an argument. We decidde propagate the exception, because 
	 * in findById method we catch exceptions.
	 */
	private Department instantiateDepartment(ResultSet rs) throws SQLException  {
		Department dep = new Department();
		dep.setId(rs.getInt("departmentId"));
		dep.setName(rs.getString("depName"));
		return dep;
	}
	
	/*this private method return a seller receiving a resultSet
	 * and a department as an argument. We decidde propagate 
	 * the exception, because in findById method we catch
	 * exceptions.
	 */
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(dep);
		return seller;
	}
	
	/*This method receive a department as an argument
	 * and return a list of sellers that have this 
	 * department.
	 */
	@Override
	public List<Seller> findByDepartment(Department department){
		//Variables from JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Create a table with instructions
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "on seller.DepartmentID = department.Id "
					+ "WHERE seller.DepartmentId = ? "
					+ "ORDER BY Id");
			
			st.setInt(1, department.getId());
			
			//attributes to the rs variable the table created
			rs = st.executeQuery();
			
			//create a empty seller list
			List<Seller> sellers = new ArrayList<Seller>();
			
			//create a empty map, key = Integer, value = department.
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			
			//verify if rs have or not datas, if yes..
			while(rs.next()) {
				/*use get to return a value(department) associate a key.
				 * rs.getInt("DepatmentId") is only a number and we verify
				 * if with this key, the map have a value associate.
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					/*create a department with rs datas and
					 * add to map.
					 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//create a seller with rs and department datas
				Seller s = instantiateSeller(rs, dep);
				
				//add to list
				sellers.add(s);
			}
			
			//verify if sellers is empty
			if(sellers.isEmpty()) {
				System.out.println("No seller was found!");;
			}
			
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	/*This method receive a name as an argument
	 * and return a list of sellers that have this 
	 * name.
	 */
	@Override
	public List<Seller> findByName(String name){
		//Variables from JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Create a table with instructions
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "on seller.DepartmentID = department.Id "
					+ "WHERE seller.Name = ? "
					+ "ORDER BY Id");
			
			st.setString(1, name);
			
			//attributes to the rs variable the table created
			rs = st.executeQuery();
			
			//create a empty seller list
			List<Seller> sellers = new ArrayList<Seller>();
			
			//create a empty map, key = Integer, value = department.
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			
			//verify if rs have or not datas, if yes..
			while(rs.next()) {
				/*use get to return a value(department) associate a key.
				 * rs.getInt("DepatmentId") is only a number and we verify
				 * if with this key, the map have a value associate.
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					/*create a department with rs datas and
					 * add to map.
					 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//create a seller with rs and department datas
				Seller s = instantiateSeller(rs, dep);
				
				//add to list
				sellers.add(s);
			}
			
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	/*This method receive an email as an argument
	 * and return a list of sellers that have this 
	 * email.
	 */
	@Override
	public List<Seller> findByEmail(String email){
		//Variables from JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Create a table with instructions
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "on seller.DepartmentID = department.Id "
					+ "WHERE seller.Email = ? "
					+ "ORDER BY Id");
			
			st.setString(1, email);
			
			//attributes to the rs variable the table created
			rs = st.executeQuery();
			
			//create a empty seller list
			List<Seller> sellers = new ArrayList<Seller>();
			
			//create a empty map, key = Integer, value = department.
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			
			//verify if rs have or not datas, if yes..
			while(rs.next()) {
				/*use get to return a value(department) associate a key.
				 * rs.getInt("DepatmentId") is only a number and we verify
				 * if with this key, the map have a value associate.
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					/*create a department with rs datas and
					 * add to map.
					 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//create a seller with rs and department datas
				Seller s = instantiateSeller(rs, dep);
				
				//add to list
				sellers.add(s);
			}
			
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	/*This method receive a birthDate as an argument
	 * and return a list of sellers that have this 
	 * birthDate.
	 */
	@Override
	public List<Seller> findByBirthDate(java.util.Date birthDate){
		//Variables from JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Create a table with instructions
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "on seller.DepartmentID = department.Id "
					+ "WHERE seller.BirthDate = ? "
					+ "ORDER BY Id");
			
			st.setDate(1, new java.sql.Date(birthDate.getTime()));
			
			//attributes to the rs variable the table created
			rs = st.executeQuery();
			
			//create a empty seller list
			List<Seller> sellers = new ArrayList<Seller>();
			
			//create a empty map, key = Integer, value = department.
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			
			//verify if rs have or not datas, if yes..
			while(rs.next()) {
				/*use get to return a value(department) associate a key.
				 * rs.getInt("DepatmentId") is only a number and we verify
				 * if with this key, the map have a value associate.
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					/*create a department with rs datas and
					 * add to map.
					 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//create a seller with rs and department datas
				Seller s = instantiateSeller(rs, dep);
				
				//add to list
				sellers.add(s);
			}
			
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	/*This method receive a baseSalary as an argument
	 * and return a list of sellers that have this 
	 * baseSalary.
	 */
	@Override
	public List<Seller> findByBaseSalary(Double baseSalary){
		//Variables from JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Create a table with instructions
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "on seller.DepartmentID = department.Id "
					+ "WHERE seller.BaseSalary = ? "
					+ "ORDER BY Id");
			
			st.setDouble(1, baseSalary);
			
			//attributes to the rs variable the table created
			rs = st.executeQuery();
			
			//create a empty seller list
			List<Seller> sellers = new ArrayList<Seller>();
			
			//create a empty map, key = Integer, value = department.
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			
			//verify if rs have or not datas, if yes..
			while(rs.next()) {
				/*use get to return a value(department) associate a key.
				 * rs.getInt("DepatmentId") is only a number and we verify
				 * if with this key, the map have a value associate.
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					/*create a department with rs datas and
					 * add to map.
					 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//create a seller with rs and department datas
				Seller s = instantiateSeller(rs, dep);
				
				//add to list
				sellers.add(s);
			}
			
			return sellers;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	/*This method return a list of all sellers from 
	 * database. 
	 */
	@Override
	public List<Seller> findAll() {
		//Variables from JDBC
		Statement st = null;
		ResultSet rs = null;
		
		try {
			/* Create a statement. We use Statement, because
			 * don't need to use "?" in sql commands.
			 */
			st = conn.createStatement();
			
			// attributes to the rs variable the table created
			rs = st.executeQuery(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department " 
					+ "on seller.DepartmentID = department.Id "
					+ "ORDER BY Id");
			
			// create a empty seller list
			List<Seller> sellers = new ArrayList<Seller>();
			
			// create a empty map, key = Integer, value = department.
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			// verify if rs have or not datas, if yes..
			while (rs.next()) {
				/*
				 * use get to return a value(department) associate a key.
				 * rs.getInt("DepatmentId") is only a number and we verify if with this key, the
				 * map have a value associate.
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					/*create a department with rs datas and add to map.
					 */
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				// create a seller with rs and department datas
				Seller s = instantiateSeller(rs, dep);
				
				// add to list
				sellers.add(s);
			}
			
			return sellers;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
