package app.client.dominio;


public class CorregidoSurtidor extends Corregido{

	private Pico pico;

	public Pico getPico() {
		return pico;
	}

	public void setPico(Pico pico) {
		this.pico = pico;
	}
	
	
	public CorregidoSurtidor(){
		super();
	}

	
	@Override
	public void merge(Corregido corregidoParam) {
		super.merge(corregidoParam);
		setPico( ((CorregidoSurtidor)corregidoParam).getPico() );
	}
	
}
