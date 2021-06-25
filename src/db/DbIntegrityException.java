package db;

public class DbIntegrityException extends RuntimeException{

	/*É necessária pois não pode apagar um dado sabendo que existe
	 * outro dado que faz referencia a ele.
	 */
	private static final long serialVersionUID = 1L;

	public DbIntegrityException(String msg) {
		super(msg);
	}
}
