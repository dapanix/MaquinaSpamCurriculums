package org.example;

import java.util.ArrayList;
import java.util.List;


public class CrearCorreo {

    public static List<String> crearCorreos(List<String> listaEmpresas) {

        List<String> prefijos = List.of(
                "rrhh@",
                "carreers@"
        );

        List<String> emails = new ArrayList<>();

        for (String p : prefijos) {
            for (String empresa :listaEmpresas) {

                emails.add(p + empresa + ".com");
                emails.add(p + empresa + ".es");
            }
        }

        return emails;
    }
}
