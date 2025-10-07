package GroupingBy;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ejemplos2 {
    static class Empleado {
        private final String depto;
        private final String nombre;
        private final int salario;

        public Empleado(String depto, String nombre, int salario) {
            this.depto = depto;
            this.nombre = nombre;
            this.salario = salario;
        }
        public String getDepto()  { return depto; }
        public String getNombre() { return nombre; }
        public int getSalario()   { return salario; }
    }

    public static void main(String[] args) {
        List<Empleado> empleados = List.of(
                new Empleado("IT",     "Ana",     4500),
                new Empleado("IT",     "Alonso",  5200),
                new Empleado("IT",     "Bruno",   4800),
                new Empleado("Ventas", "Beatriz", 4000),
                new Empleado("Ventas", "Carlos",  4300),
                new Empleado("RRHH",   "Cecilia", 3900)
        );

        // 1) Resumen de salarios por departamento (summarizingInt)
        Map<String, IntSummaryStatistics> s1 = empleados.stream()
                .collect(Collectors.groupingBy(
                        Empleado::getDepto,
                        Collectors.summarizingInt(Empleado::getSalario)
                ));

        // 2) Iniciales (mayúsculas) por departamento (mapping + toSet)
        Map<String, Set<Character>> s2 = empleados.stream()
                .collect(Collectors.groupingBy(
                        Empleado::getDepto,
                        Collectors.mapping(
                                e -> Character.toUpperCase(e.getNombre().charAt(0)),
                                Collectors.toSet()
                        )
                ));

        // Impresión ejemplo
        System.out.println("— Resumen salarios —");
        s1.forEach((depto, stats) -> System.out.println(
                depto + " → count=" + stats.getCount()
                        + ", sum=" + stats.getSum()
                        + ", avg=" + stats.getAverage()
                        + ", min=" + stats.getMin()
                        + ", max=" + stats.getMax()
        ));

        System.out.println("\n— Iniciales por depto —");
        s2.forEach((depto, iniciales) -> System.out.println(depto + " → " + iniciales));
    }
}
