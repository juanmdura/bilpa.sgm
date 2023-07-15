package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class SuggestBoxAdv extends SuggestBox{
	private String placeHolderText = "";

	  public SuggestBoxAdv(MultiWordSuggestOracle oracle) {
		super(oracle);
	}

	public String getPlaceHolderText() {
	   return placeHolderText;
	  }

	  public void setPlaceHolderText(String text) {
	    placeHolderText = text;
	    getStyleElement().setPropertyString("placeholder", placeHolderText);
	  }

}
