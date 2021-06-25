package db;

public class DbIntegrityException extends RuntimeException{

	/*� necess�ria pois n�o pode apagar um dado sabendo que existe
	 * outro dado que faz referencia a ele.
	 */
	private static final long serialVersionUID = 1L;

	public DbIntegrityException(String msg) {
		super(msg);
	}
}
