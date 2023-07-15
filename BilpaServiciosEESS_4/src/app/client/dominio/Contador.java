package app.client.dominio;


public class Contador implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	private Pico pico;
	private float inicio;
	private float fin;
	private ReparacionSurtidor reparacion;
	private Solucion solucion;

	public Solucion getSolucion() {
		return solucion;
	}
	public void setSolucion(Solucion solucion) {
		this.solucion = solucion;
	}
	public ReparacionSurtidor getReparacion() {
		return reparacion;
	}
	public void setReparacion(ReparacionSurtidor reparacion) {
		this.reparacion = reparacion;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getInicio() {
		return inicio;
	}
	public void setInicio(float inicio) {
		this.inicio = inicio;
	}
	public Pico getPico() {
		return pico;
	}
	public void setPico(Pico pico) {
		this.pico = pico;
	}

	public float getFin() {
		return fin;
	}
	public void setFin(float fin) {
		this.fin = fin;
	}

	public float diferencia()
	{
		return fin - inicio;
	}

	public Contador(){}

	public Contador(ReparacionSurtidor reparacionSurtidor, Solucion sol, Pico _pico) {
		reparacion = reparacionSurtidor;
		solucion = sol;
		pico = _pico;
	}	

	public Contador copiarMinimo() {
		Contador copia = new Contador();

		copia.setId(getId());
		copia.setPico(getPico().copiar());
		copia.getPico().setSurtidor(null);
		copia.setInicio(getInicio());
		copia.setFin(getFin());
		
		return copia;
	}
	
	public Contador copiar() {
		Contador copia = new Contador();
		copiarPropiedades(copia);
		return copia;
	}

	public Contador copiarTodo() {
		Contador copia = new Contador();
		copiarPropiedades(copia);
		return copia;
	}

	private void copiarPropiedades(Contador copia) {
		copia.setId(getId());
		copia.setPico(getPico().copiar());
		copia.setInicio(getInicio());
		copia.setFin(getFin());
		copia.setReparacion(getReparacion().copiar());
		copia.setSolucion(getSolucion().copiar());
	}
	public boolean validarInicioYFin(String _inicio, String _fin) {
		try
		{
			Float.valueOf(_inicio );
			Float.valueOf(_fin);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
	

}

