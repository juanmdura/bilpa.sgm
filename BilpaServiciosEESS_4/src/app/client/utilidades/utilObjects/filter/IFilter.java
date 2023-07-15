package app.client.utilidades.utilObjects.filter;

/**
 * Created by IntelliJ IDEA.
 * User: a.kuchuk
 * Date: 23.01.12
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public interface IFilter<T> {
    boolean isValid(T value, String filter);
}