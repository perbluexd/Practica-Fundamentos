package PartitionBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ejemplo1 {
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

    public static void main(String[] args) {
        List<Log> logs = List.of(
                new Log(Level.INFO,  "Inicio del sistema", LocalDateTime.now()),
                new Log(Level.ERROR, "Fallo de conexión", LocalDateTime.now()),
                new Log(Level.WARN,  "Advertencia CPU alta", LocalDateTime.now()),
                new Log(Level.ERROR, "Fallo de autenticación", LocalDateTime.now())
        );

        // 👉 Particionamos entre errores y no errores
        Map<Boolean, List<Log>> particion = logs.stream()
                .collect(Collectors.partitioningBy(
                        l -> l.getNivel() == Level.ERROR
                ));

        // Mostrar
        particion.forEach((esError, lista) -> {
            System.out.println(esError ? "ERRORES:" : "NO ERRORES:");
            lista.forEach(l -> System.out.println("  " + l.getMensaje()));
        });
    }
}
