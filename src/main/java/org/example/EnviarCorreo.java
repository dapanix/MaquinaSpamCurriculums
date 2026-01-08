

package org.example;

import java.util.ArrayList;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import org.example.ReclutarEmailsLikeABoss.*;
public class EnviarCorreo {
    public static void main(String[] args) {
        ReclutarEmailsLikeABoss reclutador = new ReclutarEmailsLikeABoss();
        ArrayList<String> listaEmails = reclutador.devolverListaEmails();
        System.out.println("Emails obtenidos: " + listaEmails.size());


        String usuario = "danielfernandezneira@gmail.com";
        String contrasena = "obllxhkaadrhgewp";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("danielfernandezneira@gmail.com", "obllxhkaadrhgewp");
            }
        });

        try {
            String rutaPdf = System.getProperty("user.home") + "/Desktop/curriculumParaGmail.pdf";
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("danielfernandezneira@gmail.com"));
            msg.setSubject("Prueba Jakarta Mail");
            File pdf = new File(rutaPdf);
            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(",\n\nAdjunto encontrarás el PDF.\n\nSaludos.");
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(rutaPdf);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);
            msg.setContent(multipart);
            for (int i = 0; i < listaEmails.toArray().length; i++) {

                msg.setRecipients(RecipientType.TO, InternetAddress.parse(listaEmails.get(i)));
                if (!pdf.exists()) {
                    System.out.println(" No se encontró el PDF en: " + rutaPdf);
                    return;
                }
                Transport.send(msg);
                System.out.println("Correo enviado ");
            }
        }catch (IOException | MessagingException e) {
            ((Exception)e).printStackTrace();
        }

    }
}

