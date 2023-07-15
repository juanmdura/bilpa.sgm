package app.server.persistencia;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Este objeto demarca las transacciones de solicitudes de acceso a entidades.
 * 
 * @author Alfonso Odriozola
 */
public class DaoTransaction {
	
	private Transaction transaction;
	private Session session;
	private boolean closed = false;
	
	/**
	 * Comienza una transacción.
	 */
	public void begin() {
		assertNotInsideTransaction();
		this.transaction = currentSession().beginTransaction();
	}

	/**
	 * Comienza una nueva transacción. Esto sirve para hacer operaciones obre
	 * transacciones ya abiertas.
	 */
	public void beginNew() {
		assertNotInsideTransaction();		
		this.session = getSessionFactory().openSession();
		this.transaction = session.beginTransaction();
	}
	
	/**
	 * Hace un commit de la transacción.
	 */
	public void commit() {
		transaction.commit();
	}
	
	/**
	 * Hace un rollback de la transacción.
	 */
	public void rollback() {
		transaction.rollback();
	}
	
	/**
	 * Cierra el objeto liberando los recursos.
	 */
	public void close() {
		if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
			transaction.rollback();
		}
		transaction = null;
		session = null;
		closed = true;
	}
	
	public Session getSession() {
		if (session != null) {
			return session;
		}
		return currentSession();
	}
	
	private Session currentSession() {
		return getSessionFactory().getCurrentSession();
	}
	
	private SessionFactory getSessionFactory() {
		return HibernateConfiguration.getSessionFactory();
	}
	
	private void assertNotInsideTransaction() {
		if(closed){
			throw new HibernateException("Transaction already closed");
		}
		if (transaction != null) {
			throw new HibernateException("Transaction already begun");
		}
	}

}
