package app.client.dominio.data;

import app.client.dominio.ValorSiNoNa;

public class PrecintoData {
	
	private int id;

	private ValorSiNoNa remplazado;
	private String numero;
	private String numeroViejo;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ValorSiNoNa getRemplazado() {
		return remplazado;
	}
	public void setRemplazado(ValorSiNoNa remplazado) {
		this.remplazado = remplazado;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNumeroViejo() {
		return numeroViejo;
	}
	public void setNumeroViejo(String numeroViejo) {
		this.numeroViejo = numeroViejo;
	}
	
	
	public PrecintoData() {
		super();
	}

}
