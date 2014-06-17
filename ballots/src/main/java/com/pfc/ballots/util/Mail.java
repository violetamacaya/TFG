package com.pfc.ballots.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pfc.ballots.entities.EmailAccount;

/**
 * Mail Class that handles the sending of emails
 * 
 * @version 2.0, JUN-2014
 * @author Mario Temprano Martin
 */

//La llamada sería
//Mail miMail = new Mail ("emailemisor@gmail.com", "passEmisor","emailreceptor@gmail.com");
//miMail.sendMail("Asunto", "cuerpo del mensaje");


public class Mail {

	private String emailEmisor;
	private String passEmisor;
	private String emailReceptor;

	private Properties props;

	// emisor: correo del emisor -> soyemisor@gmail.com
	// pass emisor: pass del emisor
	// receptor: correo del receptor -> soyreceptor@gmail.com
	// props: propiedades de la conexión con el proovedor de correo del emisor

	
	
	//Constructor: para configurar emisor, receptor y modo de conexión con
	// el servidor de correo, en este caso gmail
	
	/** 
     * Sends an email
     * 
     * @param emailEmisor Sender's email
     * @param passEmisor Sender's email password
     * @param emailReceptor Receiver's email
     * 
     */	
	public Mail(String emailEmisor, String passEmisor, String emailReceptor) {

		this.emailEmisor = emailEmisor;
		this.passEmisor = passEmisor;
		this.emailReceptor = emailReceptor;

		this.props = new Properties();

		// Nombre del host de correo, en este caso smtp.gmail.com
		props.setProperty("mail.smtp.host", "smtp.gmail.com");

		// true or false si TLS está disponible
		props.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para el envío de correos
		props.setProperty("mail.smtp.port", "587");

		// correo del emisor
		props.setProperty("mail.smtp.user", emailEmisor);

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");

	}
	public Mail(EmailAccount emailAccount, String emailReceptor) {

		this.emailEmisor = emailAccount.getEmail();
		this.passEmisor = emailAccount.getPassword();
		this.emailReceptor = emailReceptor;

		this.props = new Properties();

		// Nombre del host de correo, en este caso smtp.gmail.com
		props.setProperty("mail.smtp.host", "smtp.gmail.com");

		// true or false si TLS está disponible
		props.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para el envío de correos
		props.setProperty("mail.smtp.port", "587");

		// correo del emisor
		props.setProperty("mail.smtp.user", emailEmisor);

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");

	}

	/** 
     * Sends the email
     * 
     * @param asunto Email subject
     * @param mensaje Message
     * @return boolean True if everything goes fine, false otherwise
     * 
     */	
	public boolean sendMail(String asunto, String mensaje) {

		try {

			// Creamos nuestra sesión del correo
			Session session = Session.getDefaultInstance(props);

			// Construimos el mensaje
			MimeMessage message = new MimeMessage(session);

			// Emisor
			message.setFrom(new InternetAddress(emailEmisor));

			// Receptor
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceptor));

			// Asunto del mensaje
			message.setSubject(asunto);
			
			// Cuerpo del mensaje
			//message.setText(mensaje);
			message.setText(mensaje,"ISO-8859-1","html");//now we can user stuff like <br/> in the email text
			

			// Para enviar el mensaje
			Transport t = session.getTransport("smtp");
 
			// Credenciales email emisor
			t.connect(emailEmisor,passEmisor);
			
			// Enviamos el mensaje
			t.sendMessage(message, message.getAllRecipients());

			// Cerramos la conexión
			t.close();

		} catch (Exception e) {
			
			return false;
		}

		return true;
	}
	//Checks if is a gmail account
	public static boolean isValidEmail(String email)
	{
		String [] temp=email.split("@");
		if(temp[1].equals("gmail.com"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static boolean checkAccount(String email,String pass)
	{
		if(isValidEmail(email))
		{
			Properties properties=new Properties();
			
			// Nombre del host de correo, en este caso smtp.gmail.com
			properties.setProperty("mail.smtp.host", "smtp.gmail.com");

			// true or false si TLS está disponible
			properties.setProperty("mail.smtp.starttls.enable", "true");

			// Puerto de gmail para el envío de correos
			properties.setProperty("mail.smtp.port", "587");

			// correo del emisor
			properties.setProperty("mail.smtp.user", email);

			// Si requiere o no usuario y password para conectarse.
			properties.setProperty("mail.smtp.auth", "true");
			try {

				// Creamos nuestra sesión del correo
				Session session = Session.getDefaultInstance(properties);

				// Para enviar el mensaje
				Transport t = session.getTransport("smtp");
	 
				// Credenciales email emisor
				t.connect(email,pass);

				// Cerramos la conexión
				t.close();

			} catch (Exception e) {
				
				return false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkAccount(EmailAccount emailAccount)
	{
		if(isValidEmail(emailAccount.getEmail()))
		{
			Properties properties=new Properties();
			
			// Nombre del host de correo, en este caso smtp.gmail.com
			properties.setProperty("mail.smtp.host", "smtp.gmail.com");

			// true or false si TLS está disponible
			properties.setProperty("mail.smtp.starttls.enable", "true");

			// Puerto de gmail para el envío de correos
			properties.setProperty("mail.smtp.port", "587");

			// correo del emisor
			properties.setProperty("mail.smtp.user", emailAccount.getEmail());

			// Si requiere o no usuario y password para conectarse.
			properties.setProperty("mail.smtp.auth", "true");
			try {

				// Creamos nuestra sesión del correo
				Session session = Session.getDefaultInstance(properties);

				// Para enviar el mensaje
				Transport t = session.getTransport("smtp");
	 
				// Credenciales email emisor
				t.connect(emailAccount.getEmail(),emailAccount.getPassword());

				// Cerramos la conexión
				t.close();

			} catch (Exception e) {
				
				return false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}