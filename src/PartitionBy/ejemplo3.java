package PartitionBy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PARTITIONINGBY AVANZADO (máximo y top-k por partición)
 * ------------------------------------------------------
 * Patrones cubiertos:
 * 1️⃣ partitioningBy + maxBy → máximo elemento por partición
 * 1️⃣b partitioningBy + collectingAndThen(maxBy) → proyectar campo (nombre) con fallback
 * 2️⃣ partitioningBy + collectingAndThen(toList→sorted→limit) → Top-K por partición
 *
 * Nota:
 * - La partición es: salario > 4000 (true/false)
 * - Se preservan todos los nombres y firmas originales.
 */
public class ejemplo3 {

    // ============================================================
    // 0) MODELO BASE
    // ============================================================
    static class Empleado {
        private final String nombre;
        private final int salario;
        public Empleado(String nombre, int salario) {
            this.nombre = nombre; this.salario = salario;
        }
        public String getNombre() { return nombre; }
        public int getSalario()   { return salario; }
        @Override public String toString() { return nombre + "(" + salario + ")"; }
    }

    // ============================================================
    // MAIN — Demostración de patrones con partitioningBy
    // ============================================================
    public static void main(String[] args) {
        List<Empleado> empleados = List.of(
                new Empleado("Ana", 3800),
                new Empleado("Bruno", 4200),
                new Empleado("Clara", 5000),
                new Empleado("David", 3900),
                new Empleado("Elena", 4200),
                new Empleado("Fede",  5600)
        );

        // ------------------------------------------------------------
        // 1) PATRÓN: máximo por partición (Optional<Empleado>)
        //    - Predicado: e.getSalario() > 4000
        //    - Downstream: maxBy(comparingInt)
        // ------------------------------------------------------------
        Map<Boolean, Optional<Empleado>> maxPorParticion =
                empleados.stream().collect(Collectors.partitioningBy(
                        e -> e.getSalario() > 4000,
                        Collectors.maxBy(Comparator.comparingInt(Empleado::getSalario))
                ));

        // ------------------------------------------------------------
        // 1b) PATRÓN: máximo por partición → proyectado a NOMBRE (String)
        //    - collectingAndThen(maxBy, transformar Optional → String con orElse)
        // ------------------------------------------------------------
        Map<Boolean, String> nombreMaxPorParticion =
                empleados.stream().collect(Collectors.partitioningBy(
                        e -> e.getSalario() > 4000,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Empleado::getSalario)),
                                opt -> opt.map(Empleado::getNombre).orElse("—")
                        )
                ));

        // ------------------------------------------------------------
        // 2) PATRÓN: Top-3 por partición (lista ordenada desc por salario, y nombre para empate)
        //    - collectingAndThen(toList(), stream->sorted->limit(3)->toList)
        // ------------------------------------------------------------
        Comparator<Empleado> cmp = Comparator
                .comparingInt(Empleado::getSalario).reversed()
                .thenComparing(Empleado::getNombre);

        Map<Boolean, List<Empleado>> top3PorParticion =
                empleados.stream().collect(Collectors.partitioningBy(
                        e -> e.getSalario() > 4000,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> lista.stream()
                                        .sorted(cmp)
                                        .limit(3)
                                        .toList()
                        )
                ));

        // ------------------------------------------------------------
        // Mostrar resultados
        // ------------------------------------------------------------
        System.out.println("Máximo por partición (Optional): " + maxPorParticion);
        System.out.println("Nombre del máximo por partición: " + nombreMaxPorParticion);
        System.out.println("Top-3 por partición: " + top3PorParticion);
    }
}
