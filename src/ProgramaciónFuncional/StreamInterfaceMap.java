package ProgramaciónFuncional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamInterfaceMap {
    public static void main(String[] args) {

        // =========================
        // DATOS DE PARTIDA
        // =========================
        List<String> nombres = List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis");
        List<Integer> numeros = List.of(5, 2, 2, 9, 4, 4, 8, 10, 1);

        // =========================================================
        // 1) toMap — mapeo directo clave/valor
        // =========================================================

        // 1.1) toMap básico (clave única) -> Map<Nombre, Longitud>
        // ⚠️ Si hay claves duplicadas: IllegalStateException (usa merge si los habrá)
        Map<String, Integer> mapaBasico = nombres.stream()
                .distinct() // evitamos duplicados para no chocar
                .collect(Collectors.toMap(
                        Function.identity(),   // keyMapper: s -> s
                        String::length         // valueMapper: s -> s.length()
                ));
        System.out.println("1.1) Map básico (sin duplicados): " + mapaBasico);

        // 1.2) toMap con MERGE FUNCTION para resolver claves duplicadas
        // Aquí: si una clave se repite, nos quedamos con la longitud MAYOR
        Map<String, Integer> mapaMerge = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),           // clave: el nombre tal cual
                        String::length,                // valor: longitud del nombre
                        Integer::max                   // merge: qué hacer ante duplicados
                ));
        System.out.println("1.2) toMap con merge (max longitud si duplica): " + mapaMerge);

        // 1.3) toMap con SUPPLIER para elegir implementación -> LinkedHashMap (mantiene orden de aparición en el stream)
        Map<String, Integer> mapaLinked = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        String::length,
                        (a, b) -> a,                  // si duplica, nos quedamos con el primero
                        LinkedHashMap::new            // tipo concreto de Map
                ));
        System.out.println("1.3) toMap con LinkedHashMap (orden de inserción): " + mapaLinked);

        // 1.4) toMap con TreeMap (orden por clave natural)
        Map<String, Integer> mapaTree = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        String::length,
                        (a, b) -> a,
                        TreeMap::new                  // ordena por clave (natural)
                ));
        System.out.println("1.4) toMap con TreeMap (orden por clave natural): " + mapaTree);

        // 1.5) toMap para construir un MAPA DE FRECUENCIAS (conteo de ocurrencias)
        // clave = nombre, valor = conteo
        Map<String, Long> frecuencias = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> 1L,
                        Long::sum                     // sumamos cuando hay duplicados
                ));
        System.out.println("1.5) Frecuencias por nombre: " + frecuencias);

        // 1.6) Mapa INMODIFICABLE con collectingAndThen
        Map<String, Integer> mapaInmod = nombres.stream()
                .distinct()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Function.identity(), String::length),
                        Collections::unmodifiableMap
                ));
        System.out.println("1.6) Map inmodificable: " + mapaInmod);

        // =========================================================
        // 2) groupingBy — agrupar por una clave (→ Map<Clave, Lista/Set/...)
        // =========================================================

        // 2.1) groupingBy simple: agrupar por longitud -> Map<Longitud, List<Nombre>>
        Map<Integer, List<String>> porLongitud = nombres.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("2.1) groupingBy longitud -> List: " + porLongitud);

        // 2.2) groupingBy + counting: cuántos nombres por longitud -> Map<Longitud, Conteo>
        Map<Integer, Long> conteoPorLongitud = nombres.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.counting()
                ));
        System.out.println("2.2) groupingBy + counting -> conteo: " + conteoPorLongitud);

        // 2.3) groupingBy + mapping: agrupar por longitud y quedarnos con INICIALES únicas (Set)
        Map<Integer, Set<String>> inicialesPorLongitud = nombres.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.mapping(s -> s.substring(0, 1), Collectors.toSet())
                ));
        System.out.println("2.3) groupingBy + mapping -> iniciales únicas: " + inicialesPorLongitud);

        // 2.4) groupingBy con SUPPLIER para controlar el tipo de Map (ej: LinkedHashMap para preservar orden de claves)
        Map<Integer, List<String>> porLongitudLinked = nombres.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        LinkedHashMap::new,            // tipo de Map resultado
                        Collectors.toList()
                ));
        System.out.println("2.4) groupingBy con LinkedHashMap: " + porLongitudLinked);

        // 2.5) groupingBy + downstream sum: sumar longitudes por inicial
        Map<String, Integer> sumaLongitudesPorInicial = nombres.stream()
                .collect(Collectors.groupingBy(
                        s -> s.substring(0, 1),        // clave = inicial
                        Collectors.summingInt(String::length) // valor = suma de longitudes
                ));
        System.out.println("2.5) groupingBy + summingInt (por inicial): " + sumaLongitudesPorInicial);

        // =========================================================
        // 3) partitioningBy — particionar por un predicado (→ Map<Boolean, Lista/Set/...>)
        // =========================================================

        // 3.1) Números: pares vs impares -> Map<Boolean, List<Integer>>
        Map<Boolean, List<Integer>> paresVsImpares = numeros.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("3.1) partitioningBy pares vs impares: " + paresVsImpares);

        // 3.2) partitioningBy + counting: cuántos pares vs impares
        Map<Boolean, Long> conteoParesVsImpares = numeros.stream()
                .collect(Collectors.partitioningBy(
                        n -> n % 2 == 0,
                        Collectors.counting()
                ));
        System.out.println("3.2) partitioningBy + counting: " + conteoParesVsImpares);

        // 3.3) partitioningBy + mapping: pares vs impares pero nos quedamos con *dobles* únicos en Set
        Map<Boolean, Set<Integer>> doblesUnicos = numeros.stream()
                .collect(Collectors.partitioningBy(
                        n -> n % 2 == 0,
                        Collectors.mapping(n -> n * 2, Collectors.toSet())
                ));
        System.out.println("3.3) partitioningBy + mapping (dobles únicos): " + doblesUnicos);

        // =========================================================
        // 4) Notas importantes para Map en Streams (para tus apuntes)
        // =========================================================
        // - Collectors.toMap(...) lanza IllegalStateException si hay claves duplicadas y NO proporcionas mergeFn.
        // - Collectors.toMap(...) no admite null como clave ni como valor (NullPointerException).
        // - Usa mergeFn (p.ej., Integer::sum, (a,b) -> a, (a,b) -> b, Integer::max) para resolver colisiones.
        // - Usa el "map supplier" (ej., LinkedHashMap::new, TreeMap::new) para controlar el tipo de mapa y su orden.
        // - groupingBy es para "uno-a-muchos" (agrupaciones); toMap es "uno-a-uno" (clave única).
        // - partitioningBy es un caso especial de grouping en dos grupos (true/false).

        // =========================================================
        // 5) Bonus corto: Map.of / Map.copyOf (crear mapas estáticos o inmodificables)
        // =========================================================
        Map<String, Integer> mapaEstatico = Map.of("a", 1, "b", 2, "c", 3); // inmodificable
        Map<String, Integer> copiaInmod = Map.copyOf(mapaBasico);           // inmodificable
        System.out.println("5) Map.of: " + mapaEstatico + " | Map.copyOf: " + copiaInmod);

        // (listas mutables/inmutables a partir de mapas)
        Map<String, Integer> mutableDesdeInmod = new LinkedHashMap<>(mapaInmod);
        mutableDesdeInmod.put("Z", 99);
        System.out.println("Mutable desde inmodificable: " + mutableDesdeInmod);
    }
}
