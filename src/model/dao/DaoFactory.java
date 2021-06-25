package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	/*static methods, return the classes with methods
	 * concretes. This methods makes downCasting of 
	 * SellerDao to SellerDaoJDBC or DepartmentDao to
	 * DepartmentDaoJDBC. This way, when we instantiate 
	 * SellerDao or DepartmentDao we can use this method,
	 * whithout word "new". Argument of constructor is 
	 * DB.getConnection() because we need conn != null, 
	 * to connect to database, this way, we don't need
	 * open connection all the time.
	 */
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
