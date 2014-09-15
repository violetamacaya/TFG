package com.pfc.ballots.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pfc.ballots.entities.EmailAccount;
/** 
 * Email utilities
 * 
 * @author Mario Temprano Martin
 * @version 2.0 JUN-2014
 *
 */	
public class Mail {
	
	
	
	
	
	
	
	/**
	 * Sends an email with an gmail account
	 * 
	 * @param emailEmisor of the gmail account
	 * @param passEmisor of the gmail account
	 * @param emailReceptor of the receptor of the mail
	 * @param asunto subject of the mail
	 * @param mensaje message of the mail
	 * @return true if the mail is dispatched and false if can't be dispatched
	 */
	public static boolean sendMail(String emailEmisor,String passEmisor,String emailReceptor,String asunto,String mensaje)
	{
		 Properties props=new Properties();
		 
		String [] temp=emailEmisor.split("@");
		String user;
		
		if(temp[1].equals("gmail.com"))
		{	
			// Nombre del host de correo, en este caso smtp.gmail.com
			props.setProperty("mail.smtp.host", "smtp.gmail.com");

			// Puerto de gmail para el envío de correos
			props.setProperty("mail.smtp.port", "587");
			
			user=emailEmisor;
		}
		else
		{
			// Nombre del host de correo, en este caso smtp.gmail.com
			props.setProperty("mail.smtp.host", "aida.usal.es");

			// Puerto de gmail para el envío de correos
			props.setProperty("mail.smtp.port", "25");
			user=temp[0];
			
		}
			
			
			// true or false si TLS está disponible
			props.setProperty("mail.smtp.starttls.enable", "true");

			

			// correo del emisor
			props.setProperty("mail.smtp.user", emailEmisor);

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");
		 
		
			try
			{
				// Creamos nuestra sesión del correo
				Session session = Session.getDefaultInstance(props);
				//session.setDebug(true);

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
				t.connect(user,passEmisor);
				
				// Enviamos el mensaje
				t.sendMessage(message, message.getAllRecipients());

				// Cerramos la conexión
				t.close();

			}
			catch (Exception e)
			{	
				return false;
			}

			return true;	
			
		}
		/**
		 * Checks if is a usal or gmail account
		 * @param email to check
		 * @return true if is a usal or gmail account and false if it isn't
		 */
		public static boolean isValidEmail(String email)
		{
			try
			{
				String [] temp=email.split("@");
				if(temp[1].equals("gmail.com")|| temp[1].equals("usal.es")||temp[1].equals("inpo.usal.es"))
				{
					System.out.println("true");
					return true;
				}
				else
				{
					return false;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		/**
		 * Checks if is the password of an email account
		 * @param email account to check
		 * @param pass password to check
		 * @return true if the password works false if not
		 */
		public static boolean checkAccount(String email,String pass)
		{
			if(isValidEmail(email))
			{
				System.out.println(email);
				Properties properties=new Properties();
				String [] temp=email.split("@");
				String user;
				if(temp[1].equals("gmail.com"))
				{
					System.out.println("GMAIL");
					// Nombre del host de correo, en este caso smtp.gmail.com
					properties.setProperty("mail.smtp.host", "smtp.gmail.com");

					// Puerto de gmail para el envío de correos
					properties.setProperty("mail.smtp.port", "587");
					
					user=email;
				}
				else
				{
					System.out.println("USAL");

					// Nombre del host de correo, en este caso smtp.gmail.com
					properties.setProperty("mail.smtp.host", "aida.usal.es");

					// Puerto de gmail para el envío de correos
					properties.setProperty("mail.smtp.port", "25");
					user=temp[0];
					
				}
				// true or false si TLS está disponible
				properties.setProperty("mail.smtp.starttls.enable", "true");


				// correo del emisor
				properties.setProperty("mail.smtp.user", email);

				// Si requiere o no usuario y password para conectarse.
				properties.setProperty("mail.smtp.auth", "true");
				try {

					// Creamos nuestra sesión del correo
					Session session = Session.getDefaultInstance(properties);
					//session.setDebug(true);
					// Para enviar el mensaje
					Transport t = session.getTransport("smtp");
		 
					// Credenciales email emisor
					System.out.println("User->"+user+" pass->"+pass);
					t.connect(user,pass);

					// Cerramos la conexión
					t.close();

				} catch (Exception e) {
					System.out.println("INVALIDO");
					return false;
					
				}
				System.out.println("VALIDO");
				return true;
			}
			else
			{
				return false;
			}
		}
		/**
		 * Checks if is the password of and the email of an EmailAccount entity works
		 * @return true if the password works false if not
		 */
		public static boolean checkAccount(EmailAccount emailAccount)
		{
			if(emailAccount==null)
			{
				return false;
			}
			if(isValidEmail(emailAccount.getEmail()))
			{
				System.out.println(emailAccount.getEmail());
				String [] temp=emailAccount.getEmail().split("@");
				String user;
				Properties properties=new Properties();
				
				if(temp[1].equals("gmail.com"))
				{	
					System.out.println("GMAIL");

					// Nombre del host de correo, en este caso smtp.gmail.com
					properties.setProperty("mail.smtp.host", "smtp.gmail.com");

					// Puerto de gmail para el envío de correos
					properties.setProperty("mail.smtp.port", "587");
					
					user=emailAccount.getEmail();
				}
				else
				{
					System.out.println("USAL");

					// Nombre del host de correo, en este caso smtp.gmail.com
					properties.setProperty("mail.smtp.host", "aida.usal.es");

					// Puerto de gmail para el envío de correos
					properties.setProperty("mail.smtp.port", "25");
					user=temp[0];
					
				}

				// true or false si TLS está disponible
				properties.setProperty("mail.smtp.starttls.enable", "true");

				

				// correo del emisor
				properties.setProperty("mail.smtp.user", emailAccount.getEmail());

				// Si requiere o no usuario y password para conectarse.
				properties.setProperty("mail.smtp.auth", "true");
				try {

					// Creamos nuestra sesión del correo
					Session session = Session.getDefaultInstance(properties);
					session.setDebug(true);
					// Para enviar el mensaje
					Transport t = session.getTransport("smtp");
		 
					// Credenciales email emisor
					System.out.println("User->"+user+" pass->"+emailAccount.getPassword());
					t.connect(user,emailAccount.getPassword());

					// Cerramos la conexión
					t.close();
					System.out.println("VALIDO");
					return true;
					
				} catch (Exception e) {
					System.out.println("INVALIDO");
					return false;
				}
			
			}
			else
			{
				System.out.println("INVALIDO");
				return false;
			}
		}

}
