package app.client.utilidades.utilObjects.filter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

public class TextBoxFilter extends TextBox {

	public TextBoxFilter() {
		super();
		configureHandlers();
	}

	private List<IStringValueChanged> subscribers = new ArrayList<IStringValueChanged>(1);

	protected String currentValue;

	public void addValueChangeHandler(IStringValueChanged valueChanged) {
		subscribers.add(valueChanged);
	}

	private void configureHandlers() {
		addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				onValueChanged();
			}
		});

		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onValueChanged();
			}
		});
	}

	private void onValueChanged() {
		String newValue = getValue() == null ? "" : getValue();
		if (newValue.equals(currentValue)){
			return;
		}

		currentValue = newValue;
		for (IStringValueChanged item : subscribers) {
			item.valueChanged(currentValue);
		}
	}
}

