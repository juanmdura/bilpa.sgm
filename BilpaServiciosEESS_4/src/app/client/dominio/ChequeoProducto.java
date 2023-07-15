package app.client.dominio;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.data.ChequeoProductoData;
import app.client.dominio.data.ItemChequeadoData;
import app.server.persistencia.ItemChequeadoDao;

public class ChequeoProducto extends Chequeo implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	
	private Preventivo preventivo;
	private Producto producto;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Preventivo getPreventivo() {
		return preventivo;
	}

	public void setPreventivo(Preventivo preventivo) {
		this.preventivo = preventivo;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	
	public ChequeoProducto(){
		super();
	}

	
	public ChequeoProducto(Producto producto2, Preventivo preventivo2) {
		setProducto(producto2);
		setPreventivo(preventivo2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChequeoProducto other = (ChequeoProducto) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public void merge(Chequeo chequeo) {
		super.merge(chequeo);
		ChequeoProducto producto = (ChequeoProducto) chequeo;
		this.setPreventivo(new Preventivo(producto.getPreventivo().getId()));
		//this.setProducto(producto.getProducto());
	}

	@Override
	public ChequeoProductoData getChequeoData() {
		ChequeoProductoData pd = new ChequeoProductoData();
		pd.setId(this.getId());
		pd.setProductoData(this.getProducto().getData());
		pd.setIdPreventivo(this.getPreventivo().getId());
		
		List<ItemChequeadoData> itemsChequeadosData = new ArrayList<ItemChequeadoData>();
		for(ItemChequeado ic : getItemsChequeados()) {
			itemsChequeadosData.add(ic.getItemChequeadoData());
		}
		pd.setItemsChequeados(itemsChequeadosData);
		
		return pd;
	}
	

	@Override
	public boolean tieneOtraData() {
		return false;
	}


}
