package app.server;

import java.text.DecimalFormat;

public class UtilDecimal {

	public static Double getLitrosPorMinuto(int tiempo, Double volumen) {
		if (volumen != null && volumen > 0 && tiempo > 0){
			
			double valor = Double.valueOf(volumen * 60) / tiempo;
			String pattern = "#.##";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			String valorFormatted = decimalFormat.format(valor);
			return Double.valueOf(valorFormatted);
			
		}
		return null;
	}
}
