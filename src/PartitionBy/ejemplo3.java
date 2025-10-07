package PartitionBy;

import java.util.*;
import java.util.stream.Collectors;

public class ejemplo3 {
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

    public static void main(String[] args) {
        List<Empleado> empleados = List.of(
                new Empleado("Ana", 3800),
                new Empleado("Bruno", 4200),
                new Empleado("Clara", 5000),
                new Empleado("David", 3900),
                new Empleado("Elena", 4200),
                new Empleado("Fede",  5600)
        );

        // 1) Máximo salario en cada partición (salario > 4000 ?)
        Map<Boolean, Optional<Empleado>> maxPorParticion =
                empleados.stream().collect(Collectors.partitioningBy(
                        e -> e.getSalario() > 4000,
                        Collectors.maxBy(Comparator.comparingInt(Empleado::getSalario))
                ));

        // 1b) Igual que 1), pero devolviendo directamente el NOMBRE (o "—" si vacío)
        Map<Boolean, String> nombreMaxPorParticion =
                empleados.stream().collect(Collectors.partitioningBy(
                        e -> e.getSalario() > 4000,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Empleado::getSalario)),
                                opt -> opt.map(Empleado::getNombre).orElse("—")
                        )
                ));

        // 2) Top-3 por partición (ordenados por salario desc, y por nombre para desempatar)
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

        // --- Mostrar resultados ---
        System.out.println("Máximo por partición (Optional): " + maxPorParticion);
        System.out.println("Nombre del máximo por partición: " + nombreMaxPorParticion);
        System.out.println("Top-3 por partición: " + top3PorParticion);
    }
}
