package app.client.iu.widgets;

import app.client.utilidades.utilObjects.GlassPopup;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * 
 * @author dfleitas
 *
 */
public class PopupDialogBox extends DialogBox {
	
	private final static String PANEL_STYLE = "dialogBox";
	
	protected GlassPopup glass;

	
	public GlassPopup getGlass() {
		return glass;
	}

	public void setGlass(GlassPopup glass) {
		this.glass = glass;
	}

	
	public PopupDialogBox() {
		super(true);
		this.setStyleName(PANEL_STYLE);
	}

	public PopupDialogBox(GlassPopup glass) {
		super(true);
		this.glass = glass;
		this.setStyleName(PANEL_STYLE);
	}
	
	public void close() {
		super.hide();
		this.glass.hide();
	}

	@Override
	public void show() {
		this.glass.show();
		super.show();
	}
	
	@Override
	public void hide(boolean autoClosed) {
		super.hide(autoClosed);
		this.glass.hide();
	}

	@Override
    protected void onPreviewNativeEvent(NativePreviewEvent event) {
        super.onPreviewNativeEvent(event);
        switch (event.getTypeInt()) {
            case Event.ONKEYDOWN:
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    close();
                }
                break;
        }
    }

}
