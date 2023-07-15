package app.server;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class UtilFechas {

	public static String getDia(Date fecha)
	{
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");;
		String retorno = "";
		if(fecha != null)
		{
			retorno = formato.format(fecha);
		}
		return retorno;
	}

	public static String getHora(Date fecha)
	{
		SimpleDateFormat formato = new SimpleDateFormat("HH:mm");;
		String retorno = "";
		if(fecha != null)
		{
			retorno = formato.format(fecha);
		}
		return retorno;
	}

	public static String getMonthForInt(Date fechaProximaVisita) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaProximaVisita);
		return getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
	}
	
	public static String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es_ES"));
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11 ) {
			month = months[num];
		}

		String meses[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

		return meses[num];
	}
	
	public static int getDiasSinVisitas(Date fechaUltimaVisita)  {
		int dias = 0;
		if (fechaUltimaVisita != null) {
			Date today = new Date();
			GregorianCalendar fechaUVisita = new GregorianCalendar();
			fechaUVisita.setTime(fechaUltimaVisita);
			GregorianCalendar fechaActual = new GregorianCalendar();
			fechaActual.setTime(today);
			if (fechaActual.get(Calendar.YEAR) == fechaUVisita.get(Calendar.YEAR)) {
				dias = (fechaActual.get(Calendar.DAY_OF_YEAR) - fechaUVisita.get(Calendar.DAY_OF_YEAR));
			} else {
				int rangoAnios = fechaActual.get(Calendar.YEAR)	- fechaUVisita.get(Calendar.YEAR);
				for (int i = 0; i <= rangoAnios; i++) {
					int diasAnio = fechaUVisita.isLeapYear(fechaUVisita.get(Calendar.YEAR)) ? 366 : 365;
					if (i == 0) {
						dias = dias + (diasAnio - fechaUVisita.get(Calendar.DAY_OF_YEAR));
					} else if (i == rangoAnios) {
						dias = dias + fechaActual.get(Calendar.DAY_OF_YEAR);
					} else {
						dias = dias + diasAnio;
					}
				}
			}
		}
		return dias;
	}
	
	public static Date getFormattedDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat(app.client.utilidades.UtilFechas.DATE_FORMAT);
		if(date != null){
			try {
				if (date.toString().contains(".")){
					return format.parse(date.toString().substring(0, date.toString().indexOf(".")));
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				
				return format.parse(format.format(date).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
