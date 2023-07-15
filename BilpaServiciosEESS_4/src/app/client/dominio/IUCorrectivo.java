package app.client.dominio;

public class IUCorrectivo {
	public enum IUCorrectivoEnum {
		ALTA, INI, TEC
	}
	
	public static boolean esAlta(IUCorrectivoEnum iu){
		return iu.equals(IUCorrectivoEnum.ALTA);
	}
	
	public static boolean esIni(IUCorrectivoEnum iu){
		return iu.equals(IUCorrectivoEnum.INI);
	}
	
	public static boolean esTec(IUCorrectivoEnum iu){
		return iu.equals(IUCorrectivoEnum.TEC);
	}
	
	public static boolean esAltaOIni(IUCorrectivoEnum iu){
		return iu.equals(IUCorrectivoEnum.ALTA) || iu.equals(IUCorrectivoEnum.INI);
	}
	
	public static boolean esAltaOTec(IUCorrectivoEnum iu){
		return iu.equals(IUCorrectivoEnum.ALTA) || iu.equals(IUCorrectivoEnum.TEC);
	}
	
	public static boolean esIniOTec(IUCorrectivoEnum iu){
		return iu.equals(IUCorrectivoEnum.INI) || iu.equals(IUCorrectivoEnum.TEC);
	}
}
