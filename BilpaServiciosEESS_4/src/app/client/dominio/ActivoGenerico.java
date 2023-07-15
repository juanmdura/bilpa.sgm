package app.client.dominio;

import java.util.Date;

public class ActivoGenerico extends Activo implements com.google.gwt.user.client.rpc.IsSerializable{
	private TipoActivoGenerico tipoActivoGenerico;

	private MarcaActivoGenerico marca;
	private ModeloActivoGenerico modelo;
	private String serie;
	private String descripcion;
	private Date fecha;
	
	public ActivoGenerico() {
		super(6);
	}
	
	public TipoActivoGenerico getTipoActivoGenerico() {
		return tipoActivoGenerico;
	}

	public void setTipoActivoGenerico(TipoActivoGenerico tipoActivoGenerico) {
		this.tipoActivoGenerico = tipoActivoGenerico;
	}

	public MarcaActivoGenerico getMarca() {
		return marca;
	}

	public void setMarca(MarcaActivoGenerico marca) {
		this.marca = marca;
	}

	public ModeloActivoGenerico getModelo() {
		return modelo;
	}

	public void setModelo(ModeloActivoGenerico modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		String marcaModelo = "";
		String descripcionStr = "";
		if (marca != null){
			marcaModelo += " - " + marca.getNombre();
		
			if (modelo != null){
				marcaModelo += " - " + modelo.getNombre();
			}
			
			if (descripcion != null){
				descripcionStr = " - " + descripcion;
			}
		}
		
		return getSerie() + marcaModelo + descripcionStr;
	}

	@Override
	public boolean equals(Activo a) {
		if (a.getTipo() == 6)
		{
			ActivoGenerico g = (ActivoGenerico) a;
			return (g.getSerie().equals(this.getSerie()));		
		}
		else
		{
			return false;
		}
	}

	@Override
	public Activo copiar() {
		ActivoGenerico copia = new ActivoGenerico();
		copiarPropiedades(copia);
		return copia;
	}

	private void copiarPropiedades(ActivoGenerico copia) {
		copia.setEmpresa(getEmpresa().copiar());
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
		copia.setInicioGarantia(getInicioGarantia());
		copia.setFinGarantia(getFinGarantia());
		if (getQr() != null)copia.setQr(getQr().copiar());
		
		copia.setTipoActivoGenerico(getTipoActivoGenerico().copiar());
		copia.setSerie(getSerie());
		copia.setDescripcion(getDescripcion());
		copia.setFecha(getFecha());
		copia.setAnioFabricacion(getAnioFabricacion());
		copia.setDisplay(this.toStringLargo());
		if (getMarca() != null) copia.setMarca(getMarca());
		if (getModelo() != null) copia.setModelo(getModelo());
	}

	@Override
	public Activo copiarTodo() {
		return copiar();
	}

	@Override
	public String toStringLargo() {
		return toString();
	}
}
