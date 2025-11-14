package GroupingBy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

public class ejemplos {
    public static void main(String[] args) {
        List<String> palabras = List.of("sol", "luz", "cielo", "mar", "pez", "flor", "aire");

        // =========================================================
        // 1) PATRÓN: groupingBy + counting (conteo por clave)
        //    Clave: longitud de la palabra → Valor: cuántas tienen esa longitud
        // =========================================================
        Map<Integer, Long> mapa = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> p.length(),
                        Collectors.counting()
                ));
        System.out.println("1) Conteo por longitud -> " + mapa);

        // =========================================================
        // 2) PATRÓN: groupingBy + summingInt (suma por clave)
        //    Clave: inicial (mayúscula) → Valor: suma de longitudes
        // =========================================================
        Map<Character, Integer> mapa2 = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.summingInt(p -> p.length())
                ));
        System.out.println("2) Suma de longitudes por inicial -> " + mapa2);

        // =========================================================
        // 3) PATRÓN: groupingBy + averagingInt (promedio por clave)
        //    Clave: inicial (mayúscula) → Valor: promedio de longitudes
        // =========================================================
        Map<Character, Double> mapa3 = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.averagingInt(p -> p.length())
                ));
        System.out.println("3) Promedio de longitudes por inicial -> " + mapa3);

        // =========================================================
        // 4) PATRÓN: groupingBy + mapping + toSet (transformación y colección única)
        //    Clave: longitud → Valor: SET de iniciales únicas (mayúsculas)
        // =========================================================
        Map<Integer, Set<Character>> mapa4 = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> p.length(),
                        Collectors.mapping(
                                p -> Character.toUpperCase(p.charAt(0)),
                                Collectors.toSet()
                        )
                ));
        System.out.println("4) Iniciales únicas por longitud -> " + mapa4);

        // =========================================================
        // 5) PATRÓN: groupingBy + summarizingInt (estadística completa por clave)
        //    Clave: inicial (mayúscula) → Valor: IntSummaryStatistics (count/sum/avg/min/max)
        // =========================================================
        Map<Character, IntSummaryStatistics> resumen = palabras.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.summarizingInt(p -> p.length())
                ));

        // Mostrar el resumen por inicial (mantengo exactamente tu formato)
        resumen.forEach((inicial, stats) -> {
            System.out.println(inicial + " → "
                    + "count=" + stats.getCount()
                    + ", sum=" + stats.getSum()
                    + ", avg=" + stats.getAverage()
                    + ", min=" + stats.getMin()
                    + ", max=" + stats.getMax());
        });

        // =========================================================
        // EXTRA con mismos patrones sobre otra lista (frutas)
        // =========================================================
        List<String> frutas = List.of("mango", "melón", "manzana", "pera","fresa", "plátano", "kiwi", "mandarina");

        // groupingBy + mapping a SET de longitudes (por inicial mayúscula)
        Map<Character,Set<Integer>> mapa6 = frutas.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.mapping(p -> p.length(), Collectors.toSet())
                ));
        System.out.println("6) Longitudes únicas por inicial (frutas) -> " + mapa6);

        // groupingBy + summarizingInt (estadística por inicial)
        Map<Character, IntSummaryStatistics> resumen2 = frutas.stream()
                .collect(Collectors.groupingBy(
                        p -> Character.toUpperCase(p.charAt(0)),
                        Collectors.summarizingInt(p -> p.length())
                ));

        // Mostrar resumen2 con el mismo estilo
        resumen2.forEach((inicial, stats) -> {
            System.out.println("[Frutas] " + inicial + " → "
                    + "count=" + stats.getCount()
                    + ", sum=" + stats.getSum()
                    + ", avg=" + stats.getAverage()
                    + ", min=" + stats.getMin()
                    + ", max=" + stats.getMax());
        });
    }
}
