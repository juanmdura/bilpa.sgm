package app.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableSmallResource extends CellTable.Resources {
	public static final CellTableSmallResource INSTANCE =  GWT.create(CellTableSmallResource.class);
	
	@NotStrict
	@Source({CellTable.Style.DEFAULT_CSS, "BilpaResources.css"})
	TableStyle cellTableStyle();

	
	interface TableStyle extends CellTable.Style {}
}
