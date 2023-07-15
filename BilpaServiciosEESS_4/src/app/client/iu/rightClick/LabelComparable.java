package app.client.iu.rightClick;

import com.google.gwt.user.client.ui.Label;

@SuppressWarnings("serial")
public class LabelComparable extends Label implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Object>{

	private Entero labelPosition;
	
	public Entero getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(Entero labelPosition) {
		this.labelPosition = labelPosition;
	}

	public int compareTo(Object o) {
		if (o.getClass().equals(LabelComparable.class))
		{
			LabelComparable lc = (LabelComparable)o;
			return lc.compareTo(this.getText());//TODO CAMBIE, ANTES DECIA this.getPosition()			
		}
		return 0;
	}

	public LabelComparable(Entero labelPosition, String texto) {
		super(texto);
		this.labelPosition = labelPosition;
	}
}
