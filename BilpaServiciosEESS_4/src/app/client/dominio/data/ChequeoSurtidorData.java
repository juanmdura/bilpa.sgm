package app.client.dominio.data;


public class ChequeoSurtidorData extends ChequeoData {
	
	private String cabezalLimpiezaSellado;
	private String presetCara1;
	private String presetCara2;
	private String pruebaDisplayIluminacion;
	private String visualYLimpieza;
	private String parteElectrica;
	private String planSellado;
	
	//private Set<Producto> listaDeProductos = new HashSet<Producto>();
	//private Set<ChequeoPico> listaDeChequeosPicos = new HashSet<ChequeoPico>();
	
	
	public String getCabezalLimpiezaSellado() {
		return cabezalLimpiezaSellado;
	}
	public void setCabezalLimpiezaSellado(String cabezalLimpiezaSellado) {
		this.cabezalLimpiezaSellado = cabezalLimpiezaSellado;
	}
	public String getPresetCara1() {
		return presetCara1;
	}
	public void setPresetCara1(String presetCara1) {
		this.presetCara1 = presetCara1;
	}
	public String getPresetCara2() {
		return presetCara2;
	}
	public void setPresetCara2(String presetCara2) {
		this.presetCara2 = presetCara2;
	}
	public String getPruebaDisplayIluminacion() {
		return pruebaDisplayIluminacion;
	}
	public void setPruebaDisplayIluminacion(String pruebaDisplayIluminacion) {
		this.pruebaDisplayIluminacion = pruebaDisplayIluminacion;
	}
	public String getVisualYLimpieza() {
		return visualYLimpieza;
	}
	public void setVisualYLimpieza(String visualYLimpieza) {
		this.visualYLimpieza = visualYLimpieza;
	}
	public String getParteElectrica() {
		return parteElectrica;
	}
	public void setParteElectrica(String parteElectrica) {
		this.parteElectrica = parteElectrica;
	}
	public String getPlanSellado() {
		return planSellado;
	}
	public void setPlanSellado(String planSellado) {
		this.planSellado = planSellado;
	}
	
	
	public ChequeoSurtidorData() {
		super();
	}

}
