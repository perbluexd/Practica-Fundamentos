package PartitionBy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PARTITIONINGBY + MAPPING → clasificación y transformación
 * ---------------------------------------------------------
 * Patrones cubiertos:
 * 1️⃣ Modelo base (Estudiante)
 * 2️⃣ partitioningBy con mapping (agrupación booleana + transformación)
 * 3️⃣ Visualización de resultados
 *
 * Concepto:
 * - partitioningBy(Predicate, downstream)
 *     → Map<Boolean, <downstream resultado>>
 *   Ejemplo: separar aprobados y desaprobados, pero guardando solo nombres.
 */
public class ejemplo2 {

    // ============================================================
    // 1️⃣ MODELO BASE — clase Estudiante
    // ============================================================
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

    // ============================================================
    // MAIN — Ejecución del patrón partitioningBy + mapping
    // ============================================================
    public static void main(String[] args) {
        List<Estudiante> estudiantes = List.of(
                new Estudiante("Ana", 14),
                new Estudiante("Alonso", 10),
                new Estudiante("Beatriz", 16),
                new Estudiante("Bruno", 9),
                new Estudiante("Carlos", 12)
        );

        // ------------------------------------------------------------
        // 2️⃣ PATRÓN: partitioningBy + mapping
        // ------------------------------------------------------------
        // Divide en dos grupos (aprobados / desaprobados)
        // y transforma cada nombre a mayúsculas antes de recolectar en un Set.
        Map<Boolean, Set<String>> grupos =
                estudiantes.stream()
                        .collect(Collectors.partitioningBy(
                                e -> e.getNota() >= 11,                     // predicado booleano
                                Collectors.mapping(                         // downstream: transformación + recolección
                                        e -> e.getNombre().toUpperCase(),
                                        Collectors.toSet()
                                )
                        ));

        // ------------------------------------------------------------
        // 3️⃣ MOSTRAR RESULTADOS
        // ------------------------------------------------------------
        grupos.forEach((aprobado, nombres) -> {
            System.out.println((aprobado ? "Aprobados" : "Desaprobados") + " → " + nombres);
        });
    }
}
