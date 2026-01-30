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


            for (String empresa :listaEmpresas) {

                emails.add(prefijos.get(0) + empresa + ".com");
                emails.add(prefijos.get(0) + empresa + ".es");
                emails.add(prefijos.get(1) + empresa + ".com");
                emails.add(prefijos.get(1) + empresa + ".es");




            }


        return emails;
    }
}
