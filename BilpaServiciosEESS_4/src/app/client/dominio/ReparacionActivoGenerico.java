package app.client.dominio;

public class ReparacionActivoGenerico extends Reparacion{

	public ReparacionActivoGenerico()
	{
		super(6);
	}

	public ReparacionActivoGenerico copiar() {
		return (ReparacionActivoGenerico)super.copiar();
	}

	public ReparacionActivoGenerico copiarTodo() {
		return (ReparacionActivoGenerico)super.copiarTodo();
	}
}
