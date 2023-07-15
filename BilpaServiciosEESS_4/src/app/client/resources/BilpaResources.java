package app.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface BilpaResources extends ClientBundle {
	public static final BilpaResources INSTANCE = GWT
			.create(BilpaResources.class);

	/*
	 * @NotStrict
	 * 
	 * @Source("BilpaResources.css") public CssResource css();
	 */

	@Source("img/menos.png")
	public ImageResource getMenosResource();

	@Source("img/advertencia.png")
	public ImageResource getAdvertenciaResource();
}
