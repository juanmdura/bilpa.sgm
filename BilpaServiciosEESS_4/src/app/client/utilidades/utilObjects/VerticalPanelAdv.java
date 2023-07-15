package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.VerticalPanel;

public class VerticalPanelAdv extends VerticalPanel{

	private int id;
	private int tipo;
	
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public VerticalPanelAdv() {}
	
	public VerticalPanelAdv(int id) 
	{
		super();
		this.id = id;
	}
}
