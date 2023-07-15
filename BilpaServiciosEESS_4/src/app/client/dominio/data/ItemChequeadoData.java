package app.client.dominio.data;

public class ItemChequeadoData implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<ItemChequeadoData> {

	private int id;
	private String nombre;
	private String texto;
	private String valor;
	private boolean pendiente;
	private int countPendientes;
	private int countCorregidos;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCountPendientes() {
		return countPendientes;
	}
	public void setCountPendientes(int countPendientes) {
		this.countPendientes = countPendientes;
	}
	public int getCountCorregidos() {
		return countCorregidos;
	}
	public void setCountCorregidos(int countCorregidos) {
		this.countCorregidos = countCorregidos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public boolean isPendiente() {
		return pendiente;
	}
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public ItemChequeadoData() {
		super();
	}
	@Override
	public int compareTo(ItemChequeadoData o) {
		return getTexto().compareTo(o.getTexto());
	}
	
}
