package PartitionBy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ejemplo2 {
    static class Estudiante {
        private final String nombre;
        private final int nota;

        public Estudiante(String nombre, int nota) {
            this.nombre = nombre;
            this.nota = nota;
        }

        public String getNombre() { return nombre; }
        public int getNota() { return nota; }
    }

    public static void main(String[] args) {
        List<Estudiante> estudiantes = List.of(
                new Estudiante("Ana", 14),
                new Estudiante("Alonso", 10),
                new Estudiante("Beatriz", 16),
                new Estudiante("Bruno", 9),
                new Estudiante("Carlos", 12)
        );

        Map<Boolean, Set<String>> grupos =
                estudiantes.stream()
                        .collect(Collectors.partitioningBy(
                                e -> e.getNota() >= 11,
                                Collectors.mapping(
                                        e -> e.getNombre().toUpperCase(),
                                        Collectors.toSet()
                                )
                        ));

        grupos.forEach((aprobado, nombres) -> {
            System.out.println(
                    (aprobado ? "Aprobados" : "Desaprobados") + " → " + nombres
            );
        });
    }
}
