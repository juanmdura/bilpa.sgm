package app.client.dominio;

import app.client.dominio.data.PrecintoData;
import app.client.dominio.json.PrecintoJson;

public class Precinto implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;

	private Pico pico;
	private ValorSiNoNa remplazado;
	private String numero;
	private String numeroViejo;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Pico getPico() {
		return pico;
	}

	public void setPico(Pico pico) {
		this.pico = pico;
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

	
	public Precinto(){
		super();
	}


	public void merge(PrecintoJson precinto, Pico pico) {
		this.setId(precinto.getId());
		this.setNumero(precinto.getNumero());
		this.setNumeroViejo(precinto.getNumeroViejo());
		this.setRemplazado(precinto.getRemplazado());
		this.setPico(pico);
	}

	public PrecintoData getPrecintoData() {
		
		PrecintoData pd = new PrecintoData();
		pd.setId(this.getId());
		pd.setNumero(this.getNumero());
		pd.setNumeroViejo(this.getNumeroViejo());
		pd.setRemplazado(this.getRemplazado());
		return pd;
		
	}
	
	public boolean cambioPrecinto(){
		return getNumero() != null && !getNumero().isEmpty() && getRemplazado().equals(ValorSiNoNa.SI);
	}

	public Precinto copiar() {
		Precinto precinto = new Precinto();
		precinto.setId(getId());
		precinto.setNumero(getNumero());
		precinto.setNumeroViejo(getNumeroViejo());
		precinto.setPico(getPico().copiar());
		precinto.setRemplazado(getRemplazado());
		return precinto;
	}

}
