/*
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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import java.util.*;


public class EnviarCorreo {

    public static void main(String[] args){
        int cont = 0;

        OllamaClient_json ollama = new OllamaClient_json();

        List<String> listaEmpresas;

        try {
            listaEmpresas = ollama.devolverListaEmpresas();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        List<String> listaEmails = CrearCorreo.crearCorreos(listaEmpresas);


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
        String rutaPdf = System.getProperty("user.home") + "/Desktop/curriculumDanielFernandez.pdf";
        //en este caso quiero añadir mi CV al email, esto es opcional, si se quiere mandar un curriculum
        // basta con ajustar la ruta y el nombre del archivo
        File pdf = new File(rutaPdf);

        if (!pdf.exists()) {
            System.out.println("No se encontró el PDF en: " + rutaPdf);
            return;
        }

        System.out.println("Iniciando envío a " + listaEmails.size() + " empresas...");


        // 5. Envío de correos (UNO A UNO)
        for (String email : listaEmails) {
            try {
                //codigo para elegir aleatoriamente el texto para el cuerpo y cabecera del correo
                //he hecho 3 tipos distintos de cada para que sea mas dificil detectar el correo como spam
                ArrayList<String> textoTotal;
                textoTotal=cabeceraYCuerpo();
                String cabecera = textoTotal.get(0);
                String cuerpo = textoTotal.get(1);

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(usuario));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                msg.setSubject(cabecera);

                MimeBodyPart texto = new MimeBodyPart();
                texto.setText(cuerpo);

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
                System.out.println("correo enviado a: " + email);

            } catch (MessagingException | IOException e) {
                System.err.println("Error enviando correo a: " + email);
                e.printStackTrace();
            } catch (InterruptedException | URISyntaxException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Proceso finalizado. Total enviados: " + cont);
    }


    private static ArrayList<String> cabeceraYCuerpo() throws IOException, URISyntaxException {
        Random random = new Random();
        int ranCabecera = random.nextInt(3) + 1;
        int ranCuerpo = random.nextInt(3) + 1;


        String cabecera = "";
        String cuerpo = "";

        switch (ranCabecera) {
            case 1:
                cabecera=leerArchivos("Cabecera1");

                break;
            case 2:
                cabecera=leerArchivos("Cabecera2");

                break;
            case 3:
                cabecera=leerArchivos("Cabecera3");


                break;
        }

        switch (ranCuerpo) {
            case 1:
                cuerpo=leerArchivos("CuerpoEmail1");

                break;
            case 2:
                cuerpo=leerArchivos("CuerpoEmail2");
                break;
            case 3:
                cuerpo=leerArchivos("CuerpoEmail3");
                break;
        }

        ArrayList<String> resultado = new ArrayList<>();
        resultado.add(cabecera);
        resultado.add(cuerpo);
        return resultado;
    }


    public static String leerArchivos(String nombreArchivo) {

        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(nombreArchivo)) {

            if (is == null) {
                throw new IllegalArgumentException(
                        "No se encontró el recurso: " + nombreArchivo);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error leyendo el recurso: " + nombreArchivo, e);
        }
    }

}

 */