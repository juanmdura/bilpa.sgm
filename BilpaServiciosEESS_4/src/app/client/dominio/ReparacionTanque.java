package app.client.dominio;

public class ReparacionTanque extends Reparacion{
	
	public ReparacionTanque() {
		super(2);
	}
	
	public ReparacionTanque copiar() {
		return (ReparacionTanque)super.copiar();
	}

	public ReparacionTanque copiarTodo() {
		return (ReparacionTanque)super.copiarTodo();
	}

}
