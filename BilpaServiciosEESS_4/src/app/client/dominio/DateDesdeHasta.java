package app.client.dominio;


public class DateDesdeHasta implements com.google.gwt.user.client.rpc.IsSerializable{

	String desde;
	String hasta;
	public String getDesde() {
		return desde;
	}
	public void setDesde(String desde) {
		this.desde = desde;
	}
	public String getHasta() {
		return hasta;
	}
	public void setHasta(String hasta) {
		this.hasta = hasta;
	}
	public DateDesdeHasta(String dateDesde, String dateHasta) 
	{
		this.desde = String.valueOf(dateDesde);
		this.hasta = String.valueOf(dateHasta);
	}
	public DateDesdeHasta() {}
}
