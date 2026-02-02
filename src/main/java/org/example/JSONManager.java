package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONManager {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Path archivo = Paths.get(
            "C:\\Users\\Techie17_Mañana\\Downloads\\SpamEmails\\src\\main\\EmpresasConsultadas.json"
    );

    // Leer el JSON
    public static EstadoApp leer() {
        try {
            // Si no existe, crear archivo vacío
            if (Files.notExists(archivo)) {
                EstadoApp estadoInicial = new EstadoApp();
                escribir(estadoInicial);
            }

            return mapper.readValue(archivo.toFile(), EstadoApp.class);
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo el JSON", e);
        }
    }

    // Guardar cambios en el JSON
    public static void escribir(EstadoApp estado) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(archivo.toFile(), estado);
        } catch (Exception e) {
            throw new RuntimeException("Error escribiendo el JSON", e);
        }
    }
}