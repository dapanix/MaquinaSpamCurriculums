package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONManager {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Path archivo = Paths.get(
            "C:\\Users\\Techie17_Mañana\\Downloads\\SpamEmails\\src\\main\\EmpresasConsultadas.json"
    );//cambiar esto para poner tu ruta

    // Leer el JSON
    public static EstadoApp leer() {
        try {
            // Crear archivo y estado inicial si no existe
            if (Files.notExists(archivo)) {
                EstadoApp estadoInicial = new EstadoApp();
                escribir(estadoInicial);
                return estadoInicial;
            }

            // Si existe pero está vacío → inicializar
            if (Files.size(archivo) == 0) {
                EstadoApp estadoInicial = new EstadoApp();
                escribir(estadoInicial);
                return estadoInicial;
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