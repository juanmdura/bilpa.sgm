package app.client.dominio;

import app.client.dominio.data.ProductoData;

public class Producto implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private String codigo;
	private String nombre;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public Producto() {
		super();
	}
	
	public Producto(int i) {
		setId(i);
	}
	
	
	public Producto copia() {
		Producto producto = new Producto();
		producto.setId(getId());
		producto.setCodigo(getCodigo());
		producto.setNombre(getNombre());
		return producto;
	}
	
	public ProductoData getData() {
		ProductoData pd = new ProductoData();
		pd.setId(getId());
		pd.setCodigo(getCodigo());
		pd.setNombre(getNombre());
		return pd;
	}
	
	public Producto copiar() {
		Producto p = new Producto();
		p.setCodigo(getCodigo());
		p.setId(getId());
		p.setNombre(getNombre());
		return p;
	}
}
