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
        String txt = "C:\\Users\\Techie17_Mañana\\Downloads\\SpamEmails\\listaEmpresasYaEnviadas";
        List<String> listaEmpresas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
            String line;
            while ((line = br.readLine()) != null) {
                listaEmpresas.add(line);
            }
        }
        String resultado = String.join(",", listaEmpresas);
        String prompt = "antes de emprezar: TIENES QUE RESPONDER UNICAMENTE LA LISTA DE EMPRESAS SEPARADA POR COMAS Y SIN ESPACIOS, nada mas, ningun comentario de mas, no quiero ninguna palabra en tu respuesta" +
                "que no sea el nombre de una empresa, ni introduccion para el prompt, ni  descripcion de las empresas, NADA que no sea el titulo de" +
                "las empresas condicion crucial, sabiendo esto dame una lista con 20 empresas de calidad que y que cada una cumpla las siguientes condiciones:\n" +
                "1.que sea una empresa de calidad y relativamente grande\n2. que sea una empresa qeu trabaje en el sector de la informatica" +
                "(preferiblemente en el desarrollo de software, desarrollo de aplicaciones multimedia)\n3.que sea una empresa que este aceptando" +
                " practicas y puedan estar interesados en alumnos de practicas de DAM de grado superior" +
                "\n5.MUY IMPORTANTE: esa lista de empresas no puede tener ninguna empresa de la siguiente lista (\n"+resultado+") en el caso de que no haya " +
                "la lista esta entre parentesis, en el caso de no haber nada entre parentesis ignora la condicion 4, añade las listas que desees teniendo en cuenta las anteriores condiciones";
        String raw = ask("llama3", prompt);
        String respuestaString =extraerResponse(raw);
        String regex=",";
        listaEmpresas.clear();
        listaEmpresas = new ArrayList<>(List.of(respuestaString.split(regex)));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(txt))) {
            for (String elemento : listaEmpresas) {
                System.out.println(elemento);
                bw.write(elemento.trim());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return CrearCorreo.crearCorreos(listaEmpresas);


    }
}

