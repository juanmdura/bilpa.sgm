package app.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResource extends CellTable.Resources {
	public static final CellTableResource INSTANCE =  GWT.create(CellTableResource.class);
	
	@NotStrict
	@Source({CellTable.Style.DEFAULT_CSS, "BilpaResources.css"})
	TableStyle cellTableStyle();

	
	interface TableStyle extends CellTable.Style {}
}
