package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ReclutarEmailsLikeABoss {

    // Consultoras tecnológicas en España

    public ArrayList<String> devolverListaEmails(){

        ArrayList<String> listaWebs = new ArrayList<String>();
        listaWebs.add("https://www.seidor.com");              // SEIDOR, consultora tecnológica global :contentReference[oaicite:1]{index=1}
        listaWebs.add("https://www.indracompany.com");        // Indra, IT y consultoría de servicios :contentReference[oaicite:2]{index=2}
        listaWebs.add("https://www.iecisa.com");              // Informática El Corte Inglés / Inetum España :contentReference[oaicite:3]{index=3}
        listaWebs.add("https://www.soprasteria.es");          // Sopra Steria España :contentReference[oaicite:4]{index=4}
        listaWebs.add("https://www.ayesa.com");               // AYESA consultoría tecnológica :contentReference[oaicite:5]{index=5}
        listaWebs.add("https://www.babel.es");                // BABEL IT consulting :contentReference[oaicite:6]{index=6}
        listaWebs.add("https://www.ibermatica.com");          // Ibermática, servicios TI :contentReference[oaicite:7]{index=7}
        listaWebs.add("https://www.unisys.com/es-es");        // Unisys España, servicios tecnológicos :contentReference[oaicite:8]{index=8}
        listaWebs.add("https://www.rsm.global/spain");        // RSM Spain IT consulting :contentReference[oaicite:9]{index=9}
        listaWebs.add("https://www.izertis.com");             // Izertis consultoría digital (crecimiento reciente) :contentReference[oaicite:10]{index=10}


        ArrayList<String> listaEmails = new ArrayList<String>();
        Pattern pattern = Pattern.compile(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );

        try{
            for (int i=0; i<listaWebs.size();i++){
                String url= listaWebs.get(i);
                Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0").timeout(10_000).get();
                Matcher matcher = pattern.matcher(document.text());
                while (matcher.find()) {
                    listaEmails.add(matcher.group());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listaEmails;
    }
}
