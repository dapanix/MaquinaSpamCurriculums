package org.example;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class EnviarCorreo {

    public static void main(String[] args) {
        int cont = 0;

        // Usamos un SET para evitar duplicados automáticamente
        Set<String> setEmails = new HashSet<>();
        setEmails.add("info@lynka.net");
        setEmails.add("info@evolucionaconsultores.es");
        setEmails.add("info@hostgreen.com");
        setEmails.add("info@hrcs.es");
        setEmails.add("info@golineuro.es");
        // Los repetidos se ignorarán automáticamente gracias al HashSet
        setEmails.add("info@ayesa.com");
        setEmails.add("contact@ibermatica.com");
        setEmails.add("info@soprasteria.es");
        setEmails.add("contact@indra.es");
        setEmails.add("info@everis.com");
        setEmails.add("info@capgemini.com");
        setEmails.add("contact@gft.com");
        setEmails.add("info@t-systems.com");
        setEmails.add("contact@vass.es");
        setEmails.add("info@hiberus.com");
        setEmails.add("info@unitedconsulting.es");
        setEmails.add("contact@tecnalia.com");
        setEmails.add("info@grupoaltia.com");
        setEmails.add("contact@neoris.com");
        setEmails.add("info@globant.com");
        setEmails.add("contact@aitorconsulting.com");
        setEmails.add("info@atSistemas.com");
        setEmails.add("contact@opentrends.net");
        setEmails.add("info@techonrails.com");
        setEmails.add("contact@everisglobal.com");
        setEmails.add("info@iberdrolaingenieria.com");
        setEmails.add("contact@geekshubs.com");
        setEmails.add("info@clarice.es");
        setEmails.add("contact@deloitte.com");
        setEmails.add("info@kpmg.es");

        // Convertimos de nuevo a lista para mantener tu estructura
        ArrayList<String> listaEmails = new ArrayList<>(setEmails);

        if (listaEmails.isEmpty()) {
            System.out.println("No hay emails para enviar.");
            return;
        }

        String usuario = System.getenv("GMAIL_USER");
        String contrasena = System.getenv("GMAIL_APP_PASSWORD");

        if (usuario == null || contrasena == null) {
            System.out.println("Faltan las variables de entorno GMAIL_USER o GMAIL_APP_PASSWORD");
            return;
        }

        // 3. Configuración SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // --- SOLUCIÓN AL ERROR SSL ---
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // A veces ayuda forzar el protocolo TLS v1.2
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contrasena);
            }
        });

        // 4. PDF adjunto
        String rutaPdf = System.getProperty("user.home") + "/Desktop/curriculumParaGmail.pdf";
        File pdf = new File(rutaPdf);

        if (!pdf.exists()) {
            System.out.println("No se encontró el PDF en: " + rutaPdf);
            return;
        }

        System.out.println("Iniciando envío a " + listaEmails.size() + " empresas...");

        // 5. Envío de correos (UNO A UNO)
        for (String email : listaEmails) {
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(usuario));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                msg.setSubject("Candidato en prácticas FP DAM – CV adjunto");

                MimeBodyPart texto = new MimeBodyPart();
                texto.setText(
                        "Buenos días,\n\n" +
                                "Mi nombre es Daniel Fernández y soy estudiante de segundo curso del ciclo formativo " +
                                "de Desarrollo de Aplicaciones Multiplataforma (DAM).\n\n" +
                                "Actualmente me encuentro en búsqueda de una empresa en la que poder realizar las prácticas " +
                                "y me gustaría poder hacerlo en una organización como la vuestra.\n\n" +
                                "Adjunto a este correo mi currículum para su valoración.\n\n" +
                                "Un cordial saludo,\n" +
                                "Daniel Fernández"
                );

                MimeBodyPart adjunto = new MimeBodyPart();
                adjunto.attachFile(pdf);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(texto);
                multipart.addBodyPart(adjunto);

                msg.setContent(multipart);

                Transport.send(msg);
                cont++;
                System.out.println("(" + cont + "/" + listaEmails.size() + ") Correo enviado a: " + email);

                // Pausa para evitar bloqueo SMTP (Gmail es estricto, mejor 2 segundos si son muchos)
                Thread.sleep(2000);

            } catch (MessagingException | IOException e) {
                System.err.println("Error enviando correo a: " + email);
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Proceso finalizado. Total enviados: " + cont);
    }
}
