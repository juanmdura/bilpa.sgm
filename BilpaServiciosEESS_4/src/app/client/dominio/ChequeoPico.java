package app.client.dominio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import app.client.dominio.data.CaudalData;
import app.client.dominio.data.ChequeoPicoData;
import app.client.dominio.data.ItemChequeadoData;
import app.client.dominio.json.ChequeoPicoJson;
import app.server.UtilDecimal;


public class ChequeoPico extends Chequeo implements Comparable<ChequeoPico> {
	
	private int id;
	
	private Long totalizadorMecanicoInicial;
	private Long totalizadorMecanicoFinal;
	private Double totalizadorElectronicoInicial;
	private Double totalizadorElectronicoFinal;
	
	private Preventivo preventivo;
	
	private Pico pico;
	
	private Caudal caudal;
	private Calibre calibre;
	private Precinto precinto;
	

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
	
	public String getTotalizadorElectronicoFinalFormatted() {
		return getValorFormatted(totalizadorElectronicoFinal);
	}
	
	public String getTotalizadorMecanicoFinalFormatted() {
		return getValorFormatted(totalizadorMecanicoFinal);
	}

	private String getValorFormatted(java.lang.Number valor) {
		String pattern = "#.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String valorFormatted = decimalFormat.format(valor);
		return valorFormatted;
	}

	public void setTotalizadorElectronicoFinal(Double totalizadorElectronicoFinal) {
		this.totalizadorElectronicoFinal = totalizadorElectronicoFinal;
	}

	public Pico getPico() {
		return pico;
	}

	public void setPico(Pico pico) {
		this.pico = pico;
	}

	public Caudal getCaudal() {
		return caudal;
	}

	public void setCaudal(Caudal caudal) {
		this.caudal = caudal;
	}

	public Precinto getPrecinto() {
		return precinto;
	}

	public void setPrecinto(Precinto precinto) {
		this.precinto = precinto;
	}
	
	public Preventivo getPreventivo() {
		return preventivo;
	}

	public void setPreventivo(Preventivo preventivo) {
		this.preventivo = preventivo;
	}

	public Calibre getCalibre() {
		return calibre;
	}

	public void setCalibre(Calibre calibre) {
		this.calibre = calibre;
	}

	public ChequeoPico(){
		super();
	}

	
	public ChequeoPico(Pico pico2, Preventivo preventivo2) {
		setPico(pico2);
		setPreventivo(preventivo2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChequeoPico other = (ChequeoPico) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setFromChequeoPicoJson(ChequeoPicoJson chequeoPicoJson) {
		
		setId(chequeoPicoJson.getId());

		setPreventivo(new Preventivo(chequeoPicoJson.getIdPreventivo()));
		setPico(new Pico(chequeoPicoJson.getIdPico()));
		
		Calibre calibre = new Calibre();
		setCalibre(calibre);
		Pico pico = new Pico(chequeoPicoJson.getIdPico());
		calibre.setPico(pico);
		
		calibre.setCalibre1(chequeoPicoJson.getCalibre1());
		calibre.setCalibre2(chequeoPicoJson.getCalibre2());
		calibre.setCalibre3(chequeoPicoJson.getCalibre3());

		calibre.setCalibre4(chequeoPicoJson.getCalibre4());
		calibre.setCalibre5(chequeoPicoJson.getCalibre5());
		calibre.setCalibre6(chequeoPicoJson.getCalibre6());
		
		Caudal caudal = new Caudal();
		caudal.setFromCaudalJson(chequeoPicoJson.getCaudal(), chequeoPicoJson.getIdPico());
		setCaudal(caudal);
		
		Precinto precinto = new Precinto();
		precinto.merge(chequeoPicoJson.getPrecinto(), pico);
		setPrecinto(precinto);
		
		setTotalizadorElectronicoFinal(chequeoPicoJson.getTotalizadorElectronicoFinal());
		setTotalizadorElectronicoInicial(chequeoPicoJson.getTotalizadorElectronicoInicial());
		setTotalizadorMecanicoFinal(chequeoPicoJson.getTotalizadorMecanicoFinal());
		setTotalizadorMecanicoInicial(chequeoPicoJson.getTotalizadorMecanicoInicial());
		
	}

	@Override
	public void merge(Chequeo chequeo) {
		
		super.merge(chequeo);
		
		ChequeoPico chequeoPico = (ChequeoPico) chequeo;

		Calibre calibre = new Calibre();
		setCalibre(calibre);
		calibre.setPico(chequeoPico.getPico());
		
		if (chequeoPico.getCalibre() != null){
			calibre.setCalibre1(chequeoPico.getCalibre().getCalibre1());
			calibre.setCalibre2(chequeoPico.getCalibre().getCalibre2());
			calibre.setCalibre3(chequeoPico.getCalibre().getCalibre3());
			
			calibre.setCalibre4(chequeoPico.getCalibre().getCalibre4());
			calibre.setCalibre5(chequeoPico.getCalibre().getCalibre5());
			calibre.setCalibre6(chequeoPico.getCalibre().getCalibre6());
		}
		
		setCaudal(chequeoPico.getCaudal());
		
		setPrecinto(chequeoPico.getPrecinto());
		setPico(chequeoPico.getPico());
		
		setTotalizadorElectronicoFinal(chequeoPico.getTotalizadorElectronicoFinal());
		setTotalizadorElectronicoInicial(chequeoPico.getTotalizadorElectronicoInicial());
		setTotalizadorMecanicoFinal(chequeoPico.getTotalizadorMecanicoFinal());
		setTotalizadorMecanicoInicial(chequeoPico.getTotalizadorMecanicoInicial());
		
	}

	@Override
	public ChequeoPicoData getChequeoData() {
		
		ChequeoPicoData cpd = new ChequeoPicoData();
		cpd.setId(getId());
		cpd.setIdPreventivo(getPreventivo().getId());
		if (getCalibre() != null){
			cpd.setCalibre1(getCalibre().getCalibre1());
			cpd.setCalibre2(getCalibre().getCalibre2());
			cpd.setCalibre3(getCalibre().getCalibre3());
			
			cpd.setCalibre4(getCalibre().getCalibre4());
			cpd.setCalibre5(getCalibre().getCalibre5());
			cpd.setCalibre6(getCalibre().getCalibre6());
		}
		
		if (getCaudal() != null){
			CaudalData cd = getCaudal().getCaudalData();
			cd.setLitrosPorMinuto(UtilDecimal.getLitrosPorMinuto(getCaudal().getTiempo(), getCaudal().getVolumen()));
			cpd.setCaudal(cd);
		}
		if (getPrecinto()!= null)cpd.setPrecinto(getPrecinto().getPrecintoData());
		cpd.setPico(getPico().getPicoData());
		
		List<ItemChequeadoData> itemsChequeadosData = new ArrayList<ItemChequeadoData>();
		for(ItemChequeado ic : getItemsChequeados()) {
			itemsChequeadosData.add(ic.getItemChequeadoData());
		}
		cpd.setItemsChequeados(itemsChequeadosData);
		
		cpd.setTotalizadorElectronicoFinal(getTotalizadorElectronicoFinal());
		cpd.setTotalizadorElectronicoInicial(getTotalizadorElectronicoInicial());
		cpd.setTotalizadorMecanicoFinal(getTotalizadorMecanicoFinal());
		cpd.setTotalizadorMecanicoInicial(getTotalizadorMecanicoInicial());
		
		return cpd;
		
	}

	public boolean tieneTotalizador() {
		return getTotalizadorElectronicoFinal() != null && getTotalizadorElectronicoInicial() != null;
	}
	
	public boolean tieneTotalizadorMecoanicoOElectronico() {
		return tieneTotalizadorElectronico() ||
				tieneTotalizadorMecanico();
	}

	public boolean tieneTotalizadorMecanico() {
		return getTotalizadorMecanicoFinal() != null && getTotalizadorMecanicoInicial() != null;
	}

	public boolean tieneTotalizadorElectronico() {
		return getTotalizadorElectronicoFinal() != null && getTotalizadorElectronicoInicial() != null;
	}
	
	public boolean tieneValoresPorManguera() {
		return tieneCaudal() || tienePrecintos();
	}

	public boolean tieneCaudal(){
		return getCaudal() != null && getCaudal().getTiempo() > 0 && getCaudal().getVolumen() != null;
	}
	
	public boolean tienePrecintos(){
		return getCalibre()!= null && (
				getCalibre().getCalibre1() != null && getCalibre().getCalibre2() != null && getCalibre().getCalibre3() != null && getCalibre().getCalibre1() > 0 && getCalibre().getCalibre2() > 0 && getCalibre().getCalibre3() > 0);
	}
	
	public boolean tienePrecintosReemplazados(){
		return getCalibre() != null && (
				getCalibre().getCalibre4() != null && getCalibre().getCalibre5() != null && getCalibre().getCalibre6() != null && getCalibre().getCalibre4() > 0 && getCalibre().getCalibre5() > 0 && getCalibre().getCalibre6() > 0);
	}

	public int compareTo(ChequeoPico o) {
		if (this.getPico().getNumeroEnLaEstacion() > o.getPico().getNumeroEnLaEstacion()){
			return 1;
		} else if (this.getPico().getNumeroEnLaEstacion() < o.getPico().getNumeroEnLaEstacion()){
			return -1;
		}
		return 0;
	}

	public String getPrecintoTexto() {
		if (getPrecinto()!=null){
			if (getPrecinto().getRemplazado().equals(ValorSiNoNa.SI)){
				return "Si";
			}
			if (getPrecinto().getRemplazado().equals(ValorSiNoNa.NO)){
				return "No";
			}
			if (getPrecinto().getRemplazado().equals(ValorSiNoNa.N_A)){
				return "N/A";
			}
		}
		return  "";
	}

	@Override
	public boolean tieneOtraData() {
		return false;
	}
}
