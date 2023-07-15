package app.server.persistencia;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 * <p>
 * Esta es clase base de los objetos de acceso a entidades. Para implementar un
 * caso concreto de DAO se tiene que extender esta clase.
 * </p>
 * <p>
 * Por ejemplo:
 * 
 * <pre>
 * <code>
 * public class IEObjectDao&lt;IEObject, Long&gt; {
 * 	// métodos de acceso
 * }
 * </code>
 * </pre>
 * 
 * Esta clase sirve para accedera a objetos del tipo <code>IEObject</code>
 * </p>
 * 
 * @author Gonzalo Chiappara
 * @author Alfonso Odrioola
 * 
 * @param <T>
 *            la tipo de entidad que sera accedida por el objeto de acceso a
 *            datos.
 * @param <ID>
 *            el tipo de datos que se usará como identificador de la entidad.
 */
public abstract class AbstractDao<T, ID extends Serializable> {

	private Class<T> persistentClass;
	private DaoTransaction transaction;

	/**
	 * Constructor de un objeto de acceso a datos.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractDao() {
		persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Este método es invocado para hacer que este objeto de acceso a datos se
	 * una a una transacción.
	 * 
	 * @param transaction
	 *            la transacción a la cual se tiene que unir.
	 */
	public void joinTransaction(DaoTransaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * Obtiene una referencia a la sesión activa.
	 * 
	 * @return una referencia a la sesión activa.
	 */
	public Session getSession() {
		if (transaction == null) {
			return getSessionFactory().getCurrentSession();
		}
		return transaction.getSession();
	}

	@SuppressWarnings("unchecked")
	public ID save(T entity) {
		return (ID) getSession().save(entity);
	}

	public void update(T entity) {
		getSession().update(entity);
	}

	@SuppressWarnings("unchecked")
	public T merge(T entity) {
		return (T) getSession().merge(entity);
	}

	public void refresh(T t) {
		getSession().refresh(t);
	}
	
	@SuppressWarnings("unchecked")
	public void delete(ID id) {
		T entity = (T) getSession().load(getPersistentClass(), id);
		getSession().delete(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public T load(ID id) {
		return (T) getSession().load(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return (T) getSession().get(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public List<T> list() {
		Criteria criteria = createCriteria();
		return criteria.list();
	}

	/**
	 * Realiza un listado de los elementos disponibles hasta un máximo de
	 * <code>maxResults</code> elementos.
	 * 
	 * @param maxResults
	 *            la cantidad máxima de resultados.
	 * @return una lista, con un largo máximo acotado, de los elementos
	 *         disponibles.
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(int maxResults) {
		Criteria criteria = createCriteria();
		criteria.setMaxResults(maxResults);
		return criteria.list();
	}

	/**
	 * Realiza un listado de los elementos disponibles hasta un máximo de
	 * <code>maxResults</code> elementos y comenzando en la posición
	 * <code>firstResult</code>. Este método es especialmente útil si
	 * queremos hacer paginación sobre los resultados obtenidos.
	 * 
	 * @param firstResult
	 *            el primer elemento a listar.
	 * @param maxResults
	 *            la cantidad máxima de resultados esperada.
	 * @return una lista, con un largo máximo acotado y comenzando en
	 *         firstResult, de los elementos disponibles.
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(int firstResult, int maxResults) {
		Criteria criteria = createCriteria();
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResults);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	protected T get(Criterion... criterion) {
		Criteria criteria = createCriteria();
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return (T) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	protected List<T> find(Criterion... criterion) {
		Criteria criteria = createCriteria();
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	protected List<T> find(Example example) {
		return createCriteria().add(example).list();
	}

	protected List<T> find(T entity, String... excludedProperties) {
		Example example = Example.create(entity);
		for (String property : excludedProperties) {
			example.excludeProperty(property);
		}
		return find(example);
	}

	/**
	 * Crea una nuevo criterio de búsqueda.
	 * 
	 * @return un criterio de búsqueda.
	 */
	protected Criteria createCriteria() {
		return getSession().createCriteria(getPersistentClass());
	}

	/**
	 * Retorna una referencia a la fábrica de sesiones usada por los objetos de
	 * acceso a datos.
	 * 
	 * @return la fabrica de sesiones.
	 */
	protected SessionFactory getSessionFactory() {
		return HibernateConfiguration.getSessionFactory();
	}

	/**
	 * Retorna la clase que representa el tipo de objetos persistidos por este
	 * objeto de acceso a datos.
	 * 
	 * @return la clase persistida por el objeto de acceso a datos.
	 */
	protected Class<T> getPersistentClass() {
		return persistentClass;
	}

}
