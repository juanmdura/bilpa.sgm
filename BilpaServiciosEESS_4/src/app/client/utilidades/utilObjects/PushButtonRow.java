package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class PushButtonRow extends PushButton{
	private int row;

	public PushButtonRow(Image imbRemove) {
		super(imbRemove);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	

}
