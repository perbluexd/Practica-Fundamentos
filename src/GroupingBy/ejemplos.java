package GroupingBy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

public class ejemplos {
    public static void main(String[] args) {
        List<String> palabras = List.of("sol", "luz", "cielo", "mar", "pez", "flor", "aire");

        // 1) Conteo por longitud
        Map<Integer, Long> mapa = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> p.length(),
                        Collectors.counting()
                ));

        // 2) Suma de longitudes por inicial (en mayúscula)
        Map<Character, Integer> mapa2 = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.summingInt(p -> p.length())
                ));

        // 3) Promedio de longitudes por inicial
        Map<Character, Double> mapa3 = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.averagingInt(p -> p.length())
                ));

        // 4) Iniciales (sin repetir) por longitud  [groupingBy + mapping + toSet]
        Map<Integer, Set<Character>> mapa4 = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> p.length(),
                        Collectors.mapping(
                                p -> Character.toUpperCase(p.charAt(0)),
                                Collectors.toSet()
                        )
                ));

        // 5) Resumen estadístico por inicial  [groupingBy + summarizingInt]
        Map<Character, IntSummaryStatistics> resumen = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.summarizingInt(p -> p.length())
                ));

        // Mostrar el resumen por inicial (ok dejarlo)
        resumen.forEach((inicial, stats) -> {
            System.out.println(inicial + " → "
                    + "count=" + stats.getCount()
                    + ", sum=" + stats.getSum()
                    + ", avg=" + stats.getAverage()
                    + ", min=" + stats.getMin()
                    + ", max=" + stats.getMax());
        });

        List<String> frutas = List.of("mango", "melón", "manzana", "pera","fresa", "plátano", "kiwi", "mandarina");

        Map<Character,Set<Integer>> mapa6 = frutas.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.mapping(p -> p.length(), Collectors.toSet())

                ));
        Map<Character, IntSummaryStatistics> resumen2 = frutas.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.summarizingInt(p -> p.length())
                ));





    }
}
