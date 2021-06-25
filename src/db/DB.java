package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*conecta com o banco de dados.
 */
public class DB {

	/*Objeto de conexao com o banco de dados
	 * do jdbc. Inicialmente nulo.
	 */
	private static Connection conn = null;
	
	/*se a conex�o for nula, iremos carregar o arquivo 
	 * propriedades na variavel props a partir do m�todo estatico
	 * loadProperties(), e pegamos a url do aquivo e salvamos
	 * na variavel url. Para fazer a conex�o com o banco de dados
	 * chama-se a classe DriverManager e o m�todo getConnection,
	 * passando a url do banco e as propriedades de conex�o. Dessa
	 * forma, a conex�o est� salva no m�todo conn. Lan�amos a nossa 
	 * exce��o pois � derivada da runtimeexception(n�o precisa tratar)
	 * ao inves de tratarmos a sqlexception, que � derivada da exception
	 * (que � obrigada a tratar).
	 */
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	/*M�todo para fechar a conex�o.
	 */
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	/*Programa pra carregar o arquivo 
	 * db.properties. Como ele est� na
	 * pasta raiz do projeto, basta es-
	 * crever com o " ". Usou o FileInputeStream
	 * com o intuito de ler o arquivo, pois o comando
	 *  loads, do objeto props recebe uma imputStream.
	 *  Assim, o props conter� as informa��es do 
	 *  arquivo.
	 */
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
		}
		catch(IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
