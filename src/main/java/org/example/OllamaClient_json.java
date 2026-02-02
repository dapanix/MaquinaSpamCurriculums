package org.example;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class OllamaClient_json{

    static String ask(String model, String prompt) throws Exception {
        URL url = new URL("http://localhost:11434/api/generate");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Construir JSON correctamente
        JSONObject json = new JSONObject();
        json.put("model", "llama3");//usaremos llama3 para este caso
        json.put("prompt", prompt);
        json.put("stream", false);

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.toString().getBytes("UTF-8"));
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8"))) {

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    static String extraerResponse(String json) {
        JSONObject obj = new JSONObject(json);
        return obj.getString("response");
    }

    public List<String> devolverListaEmpresas() throws Exception {

        List<String> listaEmpresas = new ArrayList<>();

        // 1. Leer estado actual desde el JSON
        EstadoApp estado = JSONManager.leer();

        // 2. Construir lista de empresas ya usadas
        String resultado = String.join(",", estado.empresasEnviadas);

        // 3. Construir prompt
        String prompt =
                "antes de emprezar: TIENES QUE RESPONDER UNICAMENTE LA LISTA DE EMPRESAS SEPARADA POR COMAS Y SIN ESPACIOS, nada mas, ningun comentario de mas, no quiero ninguna palabra en tu respuesta" +
                        "que no sea el nombre de una empresa, ni introduccion para el prompt, ni  descripcion de las empresas, NADA que no sea el titulo de" +
                        "las empresas condicion crucial, sabiendo esto dame una lista con 20 empresas de calidad que y que cada una cumpla las siguientes condiciones:\n" +
                        "1.que sea una empresa de calidad y relativamente grande...\n" +
                        "2. que sea una empresa que trabaje en el sector de la informatica\n" +
                        "3.MUY IMPORTANTE: las empresas que me des no pueden tener espacios...\n" +
                        "5.MUY IMPORTANTE: esa lista de empresas no puede tener ninguna empresa de la siguiente lista (\n"
                        + resultado + ")";

        // 4. Llamar a Ollama
        String raw = ask("llama3", prompt);
        String respuestaString = extraerResponse(raw);

        // 5. Parsear respuesta
        listaEmpresas = new ArrayList<>(List.of(respuestaString.split(",")));

        for (int i = 0; i < listaEmpresas.size(); i++) {
            listaEmpresas.set(i, listaEmpresas.get(i).trim());
        }

        // 6. Actualizar estado persistente
        for (String empresa : listaEmpresas) {
            estado.agregarEmpresa(empresa);
        }

        JSONManager.escribir(estado);

        // 7. Devolver las últimas 20
        return listaEmpresas.subList(
                Math.max(0, listaEmpresas.size() - 20),
                listaEmpresas.size()
        );
    }

}

