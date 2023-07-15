package app.client.iu.rightClick;


@SuppressWarnings("serial")
public class Entero implements com.google.gwt.user.client.rpc.IsSerializable{
	private int row;
	private int column;
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public Entero(int column, int row) {
		super();
		this.column = column;
		this.row = row;
	}
	public Entero() {
		super();
	}		
}