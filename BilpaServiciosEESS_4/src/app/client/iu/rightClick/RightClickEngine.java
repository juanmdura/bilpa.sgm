package app.client.iu.rightClick;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class RightClickEngine implements AdvClickListener {

	protected final PopupPanel popupPanel = new PopupPanel(true);
	protected MenuBar popupMenuBar = new MenuBar(true);
	protected AdvLabel advLabel;

	public RightClickEngine() {
		super();		
	}

	public AdvLabel configureAdvLabel(AdvLabel advLabel) 
	{
		this.advLabel = advLabel;
		advLabel.addClickListener(this);

		return advLabel;
	}

	protected void createPopupMenu(Object o, Command command1, Command command2, String text1, String text2) {
		popupMenuBar.removeFromParent();
		popupMenuBar.clearItems();
		popupPanel.removeFromParent();
		
		MenuItem menuItemModificar = new MenuItem(text1, true, command1);
		popupMenuBar.addItem(menuItemModificar);

//		MenuItem menuItemEliminar = new MenuItem(text2, true, commandEliminar);
//		popupMenuBar.addItem(menuItemEliminar);		
		
		popupMenuBar.setVisible(true);
		popupPanel.add(popupMenuBar);

	}
	
	protected void launchPopUp(Event event) {
		int x = event.getClientX();
		int y = event.getClientY();
		popupPanel.setPopupPosition(x, y + Window.getScrollTop());
		popupPanel.show();
	}

	public abstract void onClick(Widget sender, Event event, Object o);
	public abstract void onRightClick(Widget sender, Event event, Object o);
}


