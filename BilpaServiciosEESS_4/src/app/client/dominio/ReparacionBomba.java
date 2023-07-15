package app.client.dominio;

public class ReparacionBomba extends Reparacion{

	public ReparacionBomba()
	{
		super(4);
	}

	public ReparacionBomba copiar() {
		return (ReparacionBomba)super.copiar();
	}

	public ReparacionBomba copiarTodo() {
		return (ReparacionBomba)super.copiarTodo();
	}
}
