package app.client.dominio.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author dfleitas
 *
 */
public class ChequeoPicoJson {
	
	private int id;
	
	private Date ultimaModificacion;
	private List<ItemChequeadoJson> itemsChequeados = new ArrayList<ItemChequeadoJson>();
	
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
	
	private int idPreventivo;
	private int idPico;
	
	private CaudalJson caudal;
	
	private PrecintoJson precinto;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getIdPico() {
		return idPico;
	}

	public void setIdPico(int idPico) {
		this.idPico = idPico;
	}

	public CaudalJson getCaudal() {
		return caudal;
	}

	public void setCaudal(CaudalJson caudal) {
		this.caudal = caudal;
	}

	public PrecintoJson getPrecinto() {
		return precinto;
	}

	public void setPrecinto(PrecintoJson precinto) {
		this.precinto = precinto;
	}
	
	public int getIdPreventivo() {
		return idPreventivo;
	}

	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}

	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}
	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}
	public List<ItemChequeadoJson> getItemsChequeados() {
		return itemsChequeados;
	}
	public void setItemsChequeados(List<ItemChequeadoJson> itemsChequeados) {
		this.itemsChequeados = itemsChequeados;
	}
	
	public ChequeoPicoJson() {
		super();
	}

	
}
