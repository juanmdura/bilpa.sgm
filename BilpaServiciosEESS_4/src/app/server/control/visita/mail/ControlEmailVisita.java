package app.server.control.visita.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import app.server.propiedades.PropiedadClaveEmailReportes;
import app.server.propiedades.PropiedadMailPreventivos;
import app.server.propiedades.PropiedadUsuarioEmailReportes;

public class ControlEmailVisita {

	private static ControlEmailVisita instancia = null;
	private Properties propiedades = new Properties();
	
	private String usuario;
	private String clave;
	
	public static ControlEmailVisita getInstancia() {
		if(instancia == null){
			instancia = new ControlEmailVisita();
		}
		return instancia;
	}

	public static void setInstancia(ControlEmailVisita instancia) {
		ControlEmailVisita.instancia = instancia;
	}

	private ControlEmailVisita (){
		usuario = new PropiedadUsuarioEmailReportes().getUsuario();
		clave = new PropiedadClaveEmailReportes().getClave();
	}

	public Properties getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Properties propiedades){
		this.propiedades = propiedades;
	}

	public void notificarEnvioExitoso(String destinatarios, String subject, String linea1, String linea2, byte[] pdf, boolean correctivo) throws Exception {  
		try {
			String html = getHtml(linea1, linea2);
			Session session = SetSession(usuario, clave);
			Message message = getMessage(destinatarios, subject, session);
			MimeMultipart mp = getAttachment(pdf, html, correctivo);

			message.setContent(mp);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	public void notificarEnvioFallido(String destinatarios, String subject, String linea1, String linea2, byte[] pdf, boolean correctivo) throws Exception {  
		try {
			String html = getHtml(linea1, linea2);
			Session session = SetSession(usuario, clave);
			Message message = getMessage(destinatarios, subject, session);
			MimeMultipart mp = getAttachment(pdf, html, correctivo);

			message.setContent(mp);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	private MimeMultipart getAttachment(byte[] pdf, String html, boolean correctivo)
			throws MessagingException {
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setContent(html,"text/html; charset=UTF-8");

		MimeBodyPart mbp2 = new MimeBodyPart();
		ByteArrayDataSource ds = new ByteArrayDataSource(pdf, "application/pdf"); 
		mbp2.setDataHandler(new DataHandler(ds));
		
		String name = "Reporte Mantenimiento Preventivo.pdf";
		if (correctivo){
			name = "Reporte Mantenimiento Correctivo.pdf";
		}
		mbp2.setFileName(name);

		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		return mp;
	}

	private Message getMessage(String destinatarios, String subject,
			Session session) throws MessagingException,
			UnsupportedEncodingException, AddressException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(usuario, "Bilpa Mantenimiento EE.SS."));
		message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(destinatarios));
		//message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("dfleitas@t2voice.com"/*,msilveira@t2voice.com"*/));
		message.setSubject(subject);
		return message;
	}

	private String getHtml(String linea1, String linea2) {
		String html =
				"<html>" +
						"  <head>" +
						"	 <link href='http://fonts.googleapis.com/css?family=Open+Sans:300' rel='stylesheet' type='text/css'>" +
						"    <style type='text/css'>" +
						"      body {}" +
						"      p {font-family: 'Open Sans', arial; font-size: 15px; margin: 10px 0; color: black;}" +
						"	   .logo-footer {float: right; margin: 15px 20px 0 0;}" +
						"		#footer {position:absolute; bottom:0; width: 100%; height: 84px; clear:both; background: #0D5B8E;}" +
						"    </style>" +
						"  </head>" +
						"  <body>" +
						"    <p>" + linea1 + "</p>" +
						"    <p>" + linea2 + "</p>" +
						"	 <div id='footer'>" +
						"	 	<div class='logo-footer'>" +
						"			<a href='http://bilpa.com.uy' target='_blank'><img src='http://179.27.66.44:6812/bilpa/img/logoCorreo.jpg' alt='Bilpa' /></a>" +
						"	 	</div>" +
						"	 </div>" +
						"  </body>" +
						"</html>";
		return html;
	}

	private Session SetSession(final String username, final String password) {
		Properties props = new Properties();
		
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "m.outlook.com");
		props.put("mail.smtp.auth", "true"); 
		/*  
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587");
		*/
		/*props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");*/

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		return session;
	}
}
