package util;

import java.net.InetAddress;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;


public class EmailUtil {
	
	private static final Logger logger = Logger.getLogger(EmailUtil.class); 
	
	public static final String SMTP_SERVER 			= "wdce7appin@internal.cigna.com";
	public static final String SMTP_PORT 			= "25";
	public static final String SMTP_USERNAME		= "no.username";
	public static final String SMTP_PASSWORD		= "no.password";
	public static final boolean SMTP_SECURE			= false;
	public static final boolean SMTP_AUTHENTICATE	= false;
	
	public static final String MESSAGE_FROM 		= "xrp-no-reply@cigna.com";
	public static final String MESSAGE_TO 			= "adil.rehan@cigna.com";
	
	public static void sendMail( String subjectText, String messageText ) {

		try {
			String hostName = InetAddress.getLocalHost().getHostName();
			Properties properties = new Properties();
			//properties.put("mail.smtp.host", "smtp.server.com");
			properties.put("mail.smtp.host", SMTP_SERVER);
			properties.put("mail.smtp.port", SMTP_PORT);			
			if( SMTP_AUTHENTICATE )properties.put("mail.smtp.auth", "true");
			if( SMTP_SECURE ) properties.put("mail.smtp.starttls.enable", "true");
			
			Session session = Session.getDefaultInstance(properties);
			if( SMTP_SECURE ) {
				session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
						}
				});
			}
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(MESSAGE_FROM));
			for( String to : MESSAGE_TO.split(";") ) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			}
			message.setSubject( String.format("[%s] %s", hostName.toUpperCase(), subjectText));
			message.setText( messageText );
			Transport.send(message);
			logger.info( String.format("Message sent to %s", MESSAGE_TO ) );
		} catch( Exception ex ) {
			logger.error( String.format("Unable to send message to %s", MESSAGE_TO ), ex );
		}
		
	}
	
	
	

}
