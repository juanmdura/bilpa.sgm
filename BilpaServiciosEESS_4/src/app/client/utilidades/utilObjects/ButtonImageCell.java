package app.client.utilidades.utilObjects;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Image;

public class ButtonImageCell extends ButtonCell{

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
    	Image img = new Image(value);
    	img.setSize("20px", "20px");
    	
    	SafeHtml html = SafeHtmlUtils.fromTrustedString(img.toString());
        sb.append(html);
    }
}