package app.client.dominio;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.data.ItemChequeadoData;

public class ItemChequeado implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private ItemChequeo itemChequeo;
	private String valor;
	private boolean pendiente;

	private Set<Pendiente> listaDePendientes = new HashSet<Pendiente>();
	private Set<Corregido> listaDeCorregidos = new HashSet<Corregido>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ItemChequeo getItemChequeo() {
		return itemChequeo;
	}
	public void setItemChequeo(ItemChequeo itemChequeo) {
		this.itemChequeo = itemChequeo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Set<Pendiente> getListaDePendientes() {
		return listaDePendientes;
	}
	public void setListaDePendientes(Set<Pendiente> listaDePendientes) {
		this.listaDePendientes = listaDePendientes;
	}
	public Set<Corregido> getListaDeCorregidos() {
		return listaDeCorregidos;
	}
	public void setListaDeCorregidos(Set<Corregido> listaDeCorregidos) {
		this.listaDeCorregidos = listaDeCorregidos;
	}
	public boolean isPendiente() {
		return pendiente;
	}
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
	
	
	public ItemChequeado() {
		super();
	}
	
	
	public ItemChequeado(int id2) {
		setId(id2);
	}
	
	public ItemChequeadoData getItemChequeadoData() {
		ItemChequeadoData icd = new ItemChequeadoData();
		icd.setId(this.getId());
		icd.setNombre(this.getItemChequeo().getNombre());
		icd.setTexto(this.getItemChequeo().getTexto());
		icd.setValor(this.getValor());
		icd.setPendiente(this.isPendiente());
		icd.setCountCorregidos(this.getListaDeCorregidos().size());
		icd.setCountPendientes(this.getListaDePendientes().size());
		return icd;
	}
	
}
