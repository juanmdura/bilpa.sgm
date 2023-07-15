package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.ListBox;

public class ListBoxAdv extends ListBox{

	public void setItemSelectedByValue(String value){
		for(int i = 0 ; i < this.getItemCount() ; i++)
		{
			if (getValue(i).equals(value))
			{
				setSelectedIndex(i);
				break;
			}
		}
	}
	
	public void setItemSelectedByText(String text){
		for(int i = 0 ; i < this.getItemCount() ; i++)
		{
			if (getItemText(i).equals(text))
			{
				setSelectedIndex(i);
				break;
			}
		}
	}
}
