package GroupingBy;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GROUPINGBY + SUMMARIZINGINT
 * ----------------------------
 * Ejemplo de agrupación anidada con resumen estadístico.
 *
 * Patrones cubiertos:
 * 1️⃣ Modelo base: clase Orden
 * 2️⃣ groupBy doble → cliente + mes
 * 3️⃣ summarizingInt → resumen numérico (count, sum, avg, min, max)
 * 4️⃣ Recorrido y presentación jerárquica
 */
public class ejemplo3 {

    // ============================================================
    // 1️⃣ MODELO BASE: Orden con cliente, fecha y total
    // ============================================================
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

    // ============================================================
    // MAIN — flujo principal de procesamiento
    // ============================================================
    public static void main(String[] args) {

        // Datos base
        List<Orden> ordenes = List.of(
                new Orden("Ana",   LocalDate.of(2025, 1, 15), 1200),
                new Orden("Ana",   LocalDate.of(2025, 1, 30), 800),
                new Orden("Ana",   LocalDate.of(2025, 2, 10), 1500),
                new Orden("Bruno", LocalDate.of(2025, 1, 12), 600),
                new Orden("Bruno", LocalDate.of(2025, 2, 20), 900),
                new Orden("Clara", LocalDate.of(2025, 2, 5), 700)
        );

        // ------------------------------------------------------------
        // 2️⃣ AGRUPACIÓN ANIDADA: por cliente → por mes (YearMonth)
        // ------------------------------------------------------------
        Map<String, Map<YearMonth, IntSummaryStatistics>> resumen = ordenes.stream()
                .collect(Collectors.groupingBy(
                        Orden::getCliente,  // 1er nivel: cliente
                        Collectors.groupingBy(
                                o -> YearMonth.from(o.getFecha()), // 2º nivel: mes-año
                                Collectors.summarizingInt(Orden::getTotal) // estadística
                        )
                ));

        // ------------------------------------------------------------
        // 3️⃣ RECORRIDO: presentación jerárquica de los resultados
        // ------------------------------------------------------------
        resumen.forEach((cliente, mapaMes) -> {
            System.out.println(cliente + ":");
            mapaMes.forEach((mes, stats) -> System.out.printf(
                    "  %s → count=%d, sum=%d, avg=%.2f, min=%d, max=%d%n",
                    mes, stats.getCount(), stats.getSum(), stats.getAverage(),
                    stats.getMin(), stats.getMax()
            ));
        });
    }
}
