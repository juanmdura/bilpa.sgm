package app.client.utilidades;


public class UtilFechas {

	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT_SHORT = "yyyy-MM-dd";

	/*public static Date getFormattedDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		if(date != null){
			try {
				if (date.toString().contains(".")){
					return format.parse(date.toString().substring(0, date.toString().indexOf(".")));
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				
				return format.parse(format.format(date).toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}*/

}
