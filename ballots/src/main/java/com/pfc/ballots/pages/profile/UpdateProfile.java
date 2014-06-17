package com.pfc.ballots.pages.profile;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pfc.ballots.util.Mail;

public class UpdateProfile {


	public void onActionFromSendEmail() 
	{
		/*Properties props= new Properties();
	
		props.setProperty("mail.smtp.host", "smtp.gmail.com");

		// true or false si TLS está disponible
		props.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para el envío de correos
		props.setProperty("mail.smtp.port", "587");

		// correo del emisor
		props.setProperty("mail.smtp.user", "testsendballots@gmail.com");

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");
		
		Session session=Session.getDefaultInstance(props);
		session.setDebug(true);
		
		MimeMessage message=new MimeMessage(session);
		try
		{
			message.setFrom(new InternetAddress("testsendballots@gmail.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("emilianovaquero@gmail.com"));
			
			message.setSubject("Hola");
			message.setText("HOLA COSITA"+" SOY MARIO HACIENDO UNA PRUEBA"+" MANDANDO EMAIL DESDE MI PROYECTO");
			
			Transport t= session.getTransport("smtp");
			t.connect("testsendballots@gmail.com","629281711");
			t.sendMessage(message, message.getAllRecipients());
			t.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		
		Mail mail=new Mail("testsendballots@gmail.com","629281711","dianagomez91@gmail.com");
		
		mail.sendMail("PRUEBA", "ESTO ES UNA PRUEBA SIN HTML");
	}
	
	public void onActionFromCheckEmail()
	{
		String email="testsendballots@gmail.com";
	
		/*
		if(Mail.checkAccount("testsendballots@gmail.com","6292817"))
		{
			System.out.println("EMAIL-PASS CORRECTOS");
		}
		else
		{
			System.out.println("EMAIL-PASS ERRONEOS");

		}*/
	}
}
