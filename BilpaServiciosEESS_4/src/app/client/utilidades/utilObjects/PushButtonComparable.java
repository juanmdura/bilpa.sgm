package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class PushButtonComparable extends PushButton implements Comparable<String>{

	public PushButtonComparable(Image imgPDF) 
	{
		super(imgPDF);
	}

	public int compareTo(String o) {
		return 0;
	}

}
