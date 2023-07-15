package app.client.dominio;


public class ReparacionCano extends Reparacion{

	public ReparacionCano()
	{
		super(3);
	}

	public ReparacionCano copiar() {
		return (ReparacionCano)super.copiar();
	}

	public ReparacionCano copiarTodo() {
		return (ReparacionCano)super.copiarTodo();
	}
}
