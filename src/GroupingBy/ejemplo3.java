package GroupingBy;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ejemplo3 {
    static class Orden {
        private final String cliente;
        private final LocalDate fecha;
        private final int total;

        public Orden(String cliente, LocalDate fecha, int total) {
            this.cliente = cliente;
            this.fecha = fecha;
            this.total = total;
        }

        public String getCliente() { return cliente; }
        public LocalDate getFecha() { return fecha; }
        public int getTotal() { return total; }
    }

    public static void main(String[] args) {
        List<Orden> ordenes = List.of(
                new Orden("Ana", LocalDate.of(2025, 1, 15), 1200),
                new Orden("Ana", LocalDate.of(2025, 1, 30), 800),
                new Orden("Ana", LocalDate.of(2025, 2, 10), 1500),
                new Orden("Bruno", LocalDate.of(2025, 1, 12), 600),
                new Orden("Bruno", LocalDate.of(2025, 2, 20), 900),
                new Orden("Clara", LocalDate.of(2025, 2, 5), 700)
        );

        // 👇 agrupamos por cliente y mes, y resumimos los totales
        Map<String, Map<YearMonth, IntSummaryStatistics>> resumen = ordenes.stream()
                .collect(Collectors.groupingBy(
                        Orden::getCliente,
                        Collectors.groupingBy(
                                o -> YearMonth.from(o.getFecha()),
                                Collectors.summarizingInt(Orden::getTotal)
                        )
                ));

        // Mostrar resultados
        resumen.forEach((cliente, mapaMes) -> {
            System.out.println(cliente + ":");
            mapaMes.forEach((mes, stats) -> System.out.println(
                    "  " + mes + " → count=" + stats.getCount()
                            + ", sum=" + stats.getSum()
                            + ", avg=" + stats.getAverage()
                            + ", min=" + stats.getMin()
                            + ", max=" + stats.getMax()
            ));
        });
    }
}
