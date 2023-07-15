package app.client.iu.rightClick;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

public abstract class AdvLabel extends LabelComparable implements AdvClickNotifier {

	protected AdvClickListener listener = null;
	protected Composite iu;

	public AdvLabel(Entero entero, String text, Composite iu) {
		super(entero, text);
		this.iu = iu;
		sinkEvents(Event.ONMOUSEUP | Event.ONCONTEXTMENU | Event.ONDBLCLICK); 
	}
	
	public void addClickListener(AdvClickListener listener) {
		this.listener = listener;
	}

	public void removeClickListener(AdvClickListener listener) {
		this.listener = null;
	}
	
	@SuppressWarnings("deprecation")
	public void onBrowserEvent(Event event, Object o) {
		GWT.log("onBrowserEvent", null);
        event.cancelBubble(true);//This will stop the event from being propagated to parent elements.
        event.preventDefault();
        switch (DOM.eventGetType(event)) {
	        case Event.ONMOUSEUP:
	        	if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
	        		GWT.log("Event.BUTTON_RIGHT", null);
	        		listener.onRightClick(this, event, o, iu);
	        	}
	        	break;
	        case Event.ONDBLCLICK:
	        	GWT.log("Event.DOUBLECLICK", null);
	        	listener.onClick(this, event, o, iu);
	        	break;
	        default: 
	                // Do nothing
        }//end switch
	}

	
}
