package app.client.iu.rightClick;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public interface AdvClickListener extends ClickListener {
	void onClick(Widget sender, Event event, Object o, Composite iu);
	void onRightClick(Widget sender, Event event, Object o, Composite iu);
}
