package org.example;

import java.util.ArrayList;
import java.util.List;

public class EstadoApp {
    public List<String> empresasEnviadas = new ArrayList<>();

    // Constructor vacío requerido por Jackson
    public EstadoApp() {}

    // Opcional: método de ayuda
    public void agregarEmpresa(String empresa) {
        if (!empresasEnviadas.contains(empresa)) {
            empresasEnviadas.add(empresa);
        }
    }
}