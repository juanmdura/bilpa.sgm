package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.DecoratorPanel;

public class DecoratorPanelAdv extends DecoratorPanel{

	private int id;

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public DecoratorPanelAdv(){}
	
	public DecoratorPanelAdv(int id) 
	{
		this.id = id;
	}
	
	
}
