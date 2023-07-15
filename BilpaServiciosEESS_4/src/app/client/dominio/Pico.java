package app.client.dominio;

import app.client.dominio.data.PicoData;



public class Pico implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	private int numeroPico;
	private int numeroEnLaEstacion;
	
	private Producto producto;
	
	private Surtidor surtidor;

	private QR qr;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Surtidor getSurtidor() {
		return surtidor;
	}

	public void setSurtidor(Surtidor surtidor) {
		this.surtidor = surtidor;
	}

	public int getNumeroPico() {
		return numeroPico;
	}

	public void setNumeroPico(int numeroPico) {
		this.numeroPico = numeroPico;
	}

	public int getNumeroEnLaEstacion() {
		return numeroEnLaEstacion;
	}

	public void setNumeroEnLaEstacion(int numeroEnLaEstacion) {
		this.numeroEnLaEstacion = numeroEnLaEstacion;
	}

	public String toString(){
		return "Nro " + this.getNumeroPico() + " - " + this.getProducto().getNombre();
	}

	public Pico() {

	}

	public Pico(int idPico) {
		setId(idPico);
	}

	public Pico copiarMinimo() {
		Pico copia = new Pico();
		copia.setNumeroPico(getNumeroPico());
		copia.setNumeroEnLaEstacion(getNumeroEnLaEstacion());
		copia.setId(getId());
		return copia;
	}
	
	public Pico copiar() {
		Pico copia = new Pico();
		copiarPropiedades(copia);
		return copia;
	}

	public Pico copiarTodo() {
		Pico copia = new Pico();
		copiarPropiedades(copia);
		return copia;
	}
	private void copiarPropiedades(Pico copia) {
		copia.setProducto(getProducto().copia());
		copia.setNumeroPico(getNumeroPico());
		copia.setNumeroEnLaEstacion(getNumeroEnLaEstacion());
		copia.setId(getId());
		copia.setSurtidor(getSurtidor().copiar());
		if (getQr() != null)copia.setQr(getQr().copiar());
	}

	public QR getQr() {
		return qr;
	}

	public void setQr(QR qr) {
		this.qr = qr;
	}

	public boolean equals(Pico copia)
	{
		return getSurtidor().equals(copia.getSurtidor()) && getNumeroPico() == copia.getNumeroPico();	
	}

	@Override
	public int hashCode() {
		return getNumeroPico();
	}

	public PicoData getPicoData() {
		
		PicoData pd = new PicoData();
		pd.setId(getId());
		if(getSurtidor()!=null)pd.setIdSurtidor(getSurtidor().getId());
		pd.setNumeroEnLaEstacion(getNumeroEnLaEstacion());
		pd.setNumeroPico(getNumeroPico());
		if(getProducto()!=null)pd.setTipoCombusitble(getProducto().getNombre());
		if (getQr()!= null) pd.setCodigoQR(getQr().getCodigo());
		return pd;
		
	}
}
