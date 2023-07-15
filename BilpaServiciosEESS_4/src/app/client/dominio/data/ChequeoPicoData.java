package app.client.dominio.data;


public class ChequeoPicoData extends ChequeoData{
	
	private Long totalizadorMecanicoInicial;
	private Long totalizadorMecanicoFinal;
	private Double totalizadorElectronicoInicial;
	private Double totalizadorElectronicoFinal;

	private Double calibre1;
	private Double calibre2;
	private Double calibre3;
	
	private Double calibre4;
	private Double calibre5;
	private Double calibre6;
	
	private String picoForroManguera;
	private String sistemaBloqueo;
	private String identificacionDelProducto;
	private String predeterminacion;
	private String fugas;
	
	private int idPreventivo;
	
	private PicoData pico;
	
	private CaudalData caudal;
	
	private PrecintoData precinto;

	public Long getTotalizadorMecanicoInicial() {
		return totalizadorMecanicoInicial;
	}

	public void setTotalizadorMecanicoInicial(Long totalizadorMecanicoInicial) {
		this.totalizadorMecanicoInicial = totalizadorMecanicoInicial;
	}

	public Long getTotalizadorMecanicoFinal() {
		return totalizadorMecanicoFinal;
	}

	public void setTotalizadorMecanicoFinal(Long totalizadorMecanicoFinal) {
		this.totalizadorMecanicoFinal = totalizadorMecanicoFinal;
	}

	public Double getTotalizadorElectronicoInicial() {
		return totalizadorElectronicoInicial;
	}

	public void setTotalizadorElectronicoInicial(Double totalizadorElectronicoInicial) {
		this.totalizadorElectronicoInicial = totalizadorElectronicoInicial;
	}

	public Double getTotalizadorElectronicoFinal() {
		return totalizadorElectronicoFinal;
	}

	public void setTotalizadorElectronicoFinal(Double totalizadorElectronicoFinal) {
		this.totalizadorElectronicoFinal = totalizadorElectronicoFinal;
	}

	public Double getCalibre1() {
		return calibre1;
	}

	public void setCalibre1(Double calibre1) {
		this.calibre1 = calibre1;
	}

	public Double getCalibre2() {
		return calibre2;
	}

	public void setCalibre2(Double calibre2) {
		this.calibre2 = calibre2;
	}

	public Double getCalibre3() {
		return calibre3;
	}

	public void setCalibre3(Double calibre3) {
		this.calibre3 = calibre3;
	}

	public Double getCalibre4() {
		return calibre4;
	}

	public void setCalibre4(Double calibre4) {
		this.calibre4 = calibre4;
	}

	public Double getCalibre5() {
		return calibre5;
	}

	public void setCalibre5(Double calibre5) {
		this.calibre5 = calibre5;
	}

	public Double getCalibre6() {
		return calibre6;
	}

	public void setCalibre6(Double calibre6) {
		this.calibre6 = calibre6;
	}

	public String getPicoForroManguera() {
		return picoForroManguera;
	}

	public void setPicoForroManguera(String picoForroManguera) {
		this.picoForroManguera = picoForroManguera;
	}

	public String getSistemaBloqueo() {
		return sistemaBloqueo;
	}

	public void setSistemaBloqueo(String sistemaBloqueo) {
		this.sistemaBloqueo = sistemaBloqueo;
	}

	public String getIdentificacionDelProducto() {
		return identificacionDelProducto;
	}

	public void setIdentificacionDelProducto(String identificacionDelProducto) {
		this.identificacionDelProducto = identificacionDelProducto;
	}

	public String getPredeterminacion() {
		return predeterminacion;
	}

	public void setPredeterminacion(String predeterminacion) {
		this.predeterminacion = predeterminacion;
	}

	public String getFugas() {
		return fugas;
	}

	public void setFugas(String fugas) {
		this.fugas = fugas;
	}

	public PicoData getPico() {
		return pico;
	}

	public void setPico(PicoData pico) {
		this.pico = pico;
	}

	public CaudalData getCaudal() {
		return caudal;
	}

	public void setCaudal(CaudalData caudal) {
		this.caudal = caudal;
	}

	public PrecintoData getPrecinto() {
		return precinto;
	}

	public void setPrecinto(PrecintoData precinto) {
		this.precinto = precinto;
	}

	public int getIdPreventivo() {
		return idPreventivo;
	}

	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}

	public ChequeoPicoData() {
		super();
	}

}
