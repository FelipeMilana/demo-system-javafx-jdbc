package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	//Attributes
	private Connection conn;

	// constructor with arguments.
	public  DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	//methods concretes from SellerDao

	/*This method receive a Department as an argument
	 * and insert this department in database.
	 */
	@Override
	public void insert(Department obj) {
		// Variables from JDBC
		PreparedStatement st = null;

		try {
			/*Create a table with instructions. Use return_generated 
			 * to find a id of insert a department.
			 */
			st = conn.prepareStatement(""
					+ "INSERT INTO department " 
					+ "(Name)  "
					+ "VALUES " 
					+ "(?)", 
					Statement.RETURN_GENERATED_KEYS);
			
			// add values to "?"
			st.setString(1, obj.getName());
			
			// number of insertions
			int rowsAffected = st.executeUpdate();
			
			// if an insertion ocurred..
			if (rowsAffected > 0) {
				// add a table with id's
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					// add id of the table to id seller
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			/*we need make an insertion, so if an insertion not ocurred...
			 */
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	/*This method receive a department as an argument
	 * and update this department in database
	 */
	@Override
	public void update(Department obj) {
		// Variables from JDBC
		PreparedStatement st = null;
		
		try {
			/*Create a table with instructions.We use id as 
			 * an restriction because each department has an 
			 * exclusive id.
			 */
			st = conn.prepareStatement(
					"UPDATE department " 
					+ "SET Name = ? "
					+ "WHERE " 
					+ "(id = ?)");
			
			// add values to "?"
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
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
	 * and delete this department from database
	 */
	@Override
	public void deleteById(Integer id) {
		// Variables from JDBC
		PreparedStatement st = null;
		
		try {
			/*Create a table with instructions.We use id as an 
			 * restriction because each department has an exclusive id.
			 */
			st = conn.prepareStatement(
					"DELETE from department " 
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

	/*This method receive an Id as an argument
	 * and return a department.
	 */
	@Override
	public Department findByName(String name) {
		// Variables from JDBC
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			// Create a table with instructions, exists only one department for each name
			st = conn.prepareStatement(
					"SELECT * " 
					+ "FROM department "
					+ "WHERE "
					+ "(Name = ?)"
					+ "Order by Id");
			
			st.setString(1, name);
			
			// attributes to the rs variable the table created
			rs = st.executeQuery();
			
			// verify if rs have or not datas, if yes..
			if (rs.next()) {
				// create a department with rs datas
				Department dep = instantiateDepartment(rs);
				
				return dep;
			}
			
			// if not...
			return null;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	/*this private method return a department receiving a resultSet
	 * as an argument. We decidde propagate the exception, because 
	 * in findById method we catch exceptions.
	 */
	private Department instantiateDepartment(ResultSet rs) throws SQLException  {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}
	
	/*This method return a list of all departments from 
	 * database. 
	 */
	@Override
	public List<Department> findAll() {
		// Variables from JDBC
		Statement st = null;
		ResultSet rs = null;
		
		try {
			/*Create a statement. We use Statement, because don't need to use "?" in sql
			 * commands.
			 */
			st = conn.createStatement();
			
			// attributes to the rs variable the table created
			rs = st.executeQuery(
					"SELECT * "
					+ "FROM department "
					+ "ORDER BY Id");
			
			// create a empty department list
			List<Department> departments = new ArrayList<Department>();
			
			// verify if rs have or not datas, if yes..
			while (rs.next()) {
				
				// create a department with rs datas
				Department dep = instantiateDepartment(rs);
				
				// add to list
				departments.add(dep);
			}
			
			return departments;
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
