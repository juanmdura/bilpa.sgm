package app.client.dominio;

public interface Copiable {

	
	//T copiarTodo();
	
	public Object copiar();
	
	public Object copiarTodo();
	
	public void copiarPropiedades(Object copia);
	
	public void copiarColecciones(Object copia);
	
}
