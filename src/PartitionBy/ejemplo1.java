package PartitionBy;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PARTITIONINGBY — División de colecciones en dos grupos (true / false)
 * ---------------------------------------------------------------------
 * Patrones cubiertos:
 * 1️⃣ Modelo base (Log)
 * 2️⃣ partitioningBy simple (booleano directo)
 * 3️⃣ Recorrido y presentación de resultados
 *
 * Concepto:
 * - partitioningBy(Predicate<T>) → genera un Map<Boolean, List<T>>
 *   - true  → elementos que cumplen la condición
 *   - false → los que no la cumplen
 */
public class ejemplo1 {

    // ============================================================
    // 1️⃣ MODELO BASE — clase Log con nivel y mensaje
    // ============================================================
    enum Level { INFO, WARN, ERROR }

    static class Log {
        private final LocalDateTime ts;
        private final Level nivel;
        private final String mensaje;

        public Log(Level nivel, String mensaje, LocalDateTime ts) {
            this.nivel = nivel;
            this.mensaje = mensaje;
            this.ts = ts;
        }

        public LocalDateTime getTs() { return ts; }
        public Level getNivel() { return nivel; }
        public String getMensaje() { return mensaje; }
    }

    // ============================================================
    // MAIN — Ejecución del patrón partitioningBy
    // ============================================================
    public static void main(String[] args) {
        List<Log> logs = List.of(
                new Log(Level.INFO,  "Inicio del sistema", LocalDateTime.now()),
                new Log(Level.ERROR, "Fallo de conexión", LocalDateTime.now()),
                new Log(Level.WARN,  "Advertencia CPU alta", LocalDateTime.now()),
                new Log(Level.ERROR, "Fallo de autenticación", LocalDateTime.now())
        );

        // ------------------------------------------------------------
        // 2️⃣ PATRÓN: partitioningBy (dividir en dos listas)
        // ------------------------------------------------------------
        // Divide los logs entre errores y no errores
        Map<Boolean, List<Log>> particion = logs.stream()
                .collect(Collectors.partitioningBy(
                        l -> l.getNivel() == Level.ERROR
                ));

        // ------------------------------------------------------------
        // 3️⃣ MOSTRAR RESULTADOS — formato claro
        // ------------------------------------------------------------
        particion.forEach((esError, lista) -> {
            System.out.println(esError ? "ERRORES:" : "NO ERRORES:");
            lista.forEach(l -> System.out.println("  " + l.getMensaje()));
        });
    }
}
