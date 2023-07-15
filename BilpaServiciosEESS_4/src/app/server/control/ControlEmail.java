package app.server.control;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import app.server.propiedades.PropiedadTest;

public class ControlEmail {

	private static ControlEmail instancia = null;
	private Properties propiedades = new Properties();

	private PropiedadTest propiedadTest = new PropiedadTest();

	public static ControlEmail getInstancia() {
		if(instancia == null){
			instancia = new ControlEmail();
		}
		return instancia;
	}

	public static void setInstancia(ControlEmail instancia) {
		ControlEmail.instancia = instancia;
	}

	private ControlEmail (){   

		/*propiedades.put("mail.smtp.host", "smtp.office365.com");
		propiedades.put("mail.smtp.auth", "true");
		propiedades.put("mail.smtp.port", "587");
		*/
		
		propiedades.put("mail.smtp.starttls.enable", "true");
		propiedades.put("mail.smtp.port", "587");
		propiedades.put("mail.smtp.host", "m.outlook.com");
		propiedades.put("mail.smtp.auth", "true"); 
	}

	public Properties getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Properties propiedades){
		this.propiedades = propiedades;
	}

	public void enviarEmail(String body, String email)
	{

		if(!propiedadTest.getSistemaEnModoTest())
		{			
			Session session = Session.getInstance(this.propiedades);

			Message msg = this.getMessage(session, "bilpaportal@bilpa.com.uy", email, body);
			try
			{
				Transport tr = session.getTransport("smtp");
				tr.connect("bilpaportal@bilpa.com.uy", "BiLPoR1509$%");
				msg.saveChanges();      
				tr.sendMessage(msg, msg.getAllRecipients());
				tr.close();
			} 
			catch (MessagingException e)
			{
				// System.out.println("Error al enviar email \n");
				// e.printStackTrace();
			}
		}
	}

	private MimeMessage getMessage(Session session, String from, String to, String texto)
	{
		try
		{
			MimeMessage msg = new MimeMessage(session);
			msg.setText(texto);
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setFrom(new InternetAddress(from,"BILPA"));

			msg.setSubject("Mensaje generado desde el portal Bilpa");
			return msg;
		}
		catch (java.io.UnsupportedEncodingException ex)
		{
			return null;
		}
		catch (MessagingException ex)
		{
			return null;
		} 
	}
	
	


}
