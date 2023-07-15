package app.client.dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Surtidor extends Activo {

	private String numeroSerie;
	private Set<Pico> picos = new HashSet<Pico>();
	private ModeloSurtidor modelo;

	public ModeloSurtidor getModelo() {
		return modelo;
	}

	public void setModelo(ModeloSurtidor modelo) {
		this.modelo = modelo;
		
	}

	public Set<Pico> getPicos() {
		return picos;
	}

	public void setPicos(Set<Pico> picos) {
		this.picos = picos;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public Surtidor() {
		super(1);
	}
	public Surtidor(String nroSerie) {
		this.numeroSerie = nroSerie;
	}

	public String toStringLargo(){
		return "Surtidor " + getModelo().getNombre() + " | " + getNumeroSerie();	
	}

	public String toString(){
		return this.modelo.getNombre() + " - " + this.getNumeroSerie(); 
	}

	@Override
	public boolean equals(Activo a) {
		if (a.getTipo() == 1)
		{
			Surtidor surtidor = (Surtidor) a;
			return (surtidor.getNumeroSerie().equals(this.getNumeroSerie()));
		}
		else
		{
			return false;
		}
	}

	public int obtenerCantidadDePicos(){
		return this.modelo.getCantidadDePicos();
	}

	public Pico buscarPico(int id){
		for(Pico p: this.getPicos()){
			if (p.getId()==id)
				return p;
		}
		return null;
	}

	public Pico buscarPicoPorNumero(int numero){
		for(Pico p: this.getPicos()){
			if (p.getNumeroPico() == numero)
				return p;
		}
		return null;
	}

	public Surtidor copiar() {
		Surtidor copia = new Surtidor();
		copia.setPicos(new HashSet<Pico>());
		copiarPropiedades(copia);
		return copia;
	}

	public Surtidor copiarTodo() {
		Surtidor copia = new Surtidor();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Surtidor copia) {
		copia.setNumeroSerie(getNumeroSerie());
		copia.setModelo(getModelo().copiarTodo());
		if (getEmpresa() != null)
		{
			copia.setEmpresa(getEmpresa().copiar());			
		}
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setPicos(new HashSet<Pico>());
		copia.setInicioGarantia(getInicioGarantia());
		copia.setFinGarantia(getFinGarantia());
		copia.setEstado(getEstado());
		copia.setAnioFabricacion(getAnioFabricacion());
		copia.setDisplay("Modelo " + getModelo().getNombre() + " | Serie " + getNumeroSerie() );
		if (getQr() != null)copia.setQr(getQr().copiar());
	}

	public void copiarColecciones(Surtidor copia) {
		for (Pico p : picos) {
			copia.getPicos().add(p.copiar());
		}
	}

	public ArrayList<Pico> getPicosOrdenados(){
		ArrayList<Pico> retorno = new ArrayList<Pico>();
		return retorno;
	}

	public void agregarPico(Pico pico) {
		this.picos.add(pico);
	}

	@Override
	public int hashCode() {
		return getId();
	}
	
	public Surtidor (int id){
		setId(id);
	}

	
}
