package ProgramacionFuncional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * STREAMS → COLECTORES HACIA MAPAS 🗺️
 * ------------------------------------
 * Patrones cubiertos:
 * 1️⃣ toMap – construcción directa de mapas
 * 2️⃣ groupingBy – agrupación uno-a-muchos
 * 3️⃣ partitioningBy – separación por predicado booleano
 * 4️⃣ Buenas prácticas y notas de diseño
 * 5️⃣ Bonus: Map.of / Map.copyOf / conversiones
 *
 * Objetivo: entender cómo pasar de Stream<T> → Map<K,V> o Map<K,Collection<V>>
 */
public class StreamInterfaceMap {

    public static void main(String[] args) {
        List<String> nombres = List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis");
        List<Integer> numeros = List.of(5, 2, 2, 9, 4, 4, 8, 10, 1);

        patron1_toMap_variantes(nombres);
        patron2_groupingBy_variantes(nombres);
        patron3_partitioningBy_variantes(numeros);
        patron4_notas_tecnicas();
        patron5_bonus_map_utilidades();
    }

    // ============================================================
    // 🧠 PATRÓN 1: toMap — construcción directa de Map<K,V>
    // ============================================================
    static void patron1_toMap_variantes(List<String> nombres) {
        System.out.println("\n[PATRÓN 1] toMap — mapeo directo clave/valor");

        // 1.1) Básico (sin duplicados)
        Map<String, Integer> mapaBasico = nombres.stream()
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),   // clave = nombre
                        String::length         // valor = longitud
                ));
        System.out.println("1.1) Básico (sin duplicados): " + mapaBasico);

        // 1.2) Con merge function (resolver duplicados)
        Map<String, Integer> mapaMerge = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        String::length,
                        Integer::max             // si repite, conservar longitud mayor
                ));
        System.out.println("1.2) Con merge (Integer::max): " + mapaMerge);

        // 1.3) Con Supplier → LinkedHashMap (preserva orden)
        Map<String, Integer> mapaLinked = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        String::length,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        System.out.println("1.3) LinkedHashMap (orden inserción): " + mapaLinked);

        // 1.4) Con TreeMap → orden natural de claves
        Map<String, Integer> mapaTree = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        String::length,
                        (a, b) -> a,
                        TreeMap::new
                ));
        System.out.println("1.4) TreeMap (orden natural): " + mapaTree);

        // 1.5) Mapa de frecuencias (clave → conteo)
        Map<String, Long> frecuencias = nombres.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> 1L,
                        Long::sum
                ));
        System.out.println("1.5) Frecuencias por nombre: " + frecuencias);

        // 1.6) Mapa inmodificable
        Map<String, Integer> mapaInmod = nombres.stream()
                .distinct()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Function.identity(), String::length),
                        Collections::unmodifiableMap
                ));
        System.out.println("1.6) Mapa inmodificable: " + mapaInmod);
    }

    // ============================================================
    // 🧠 PATRÓN 2: groupingBy — agrupación uno-a-muchos
    // ============================================================
    static void patron2_groupingBy_variantes(List<String> nombres) {
        System.out.println("\n[PATRÓN 2] groupingBy — agrupación uno-a-muchos");

        // 2.1) Agrupar por longitud
        Map<Integer, List<String>> porLongitud = nombres.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("2.1) Por longitud → List: " + porLongitud);

        // 2.2) Agrupar + contar
        Map<Integer, Long> conteoPorLongitud = nombres.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.counting()
                ));
        System.out.println("2.2) groupingBy + counting: " + conteoPorLongitud);

        // 2.3) Agrupar + mapping → iniciales únicas (Set)
        Map<Integer, Set<String>> inicialesPorLongitud = nombres.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.mapping(s -> s.substring(0, 1), Collectors.toSet())
                ));
        System.out.println("2.3) groupingBy + mapping (iniciales únicas): " + inicialesPorLongitud);

        // 2.4) Agrupar + Supplier → LinkedHashMap (preservar orden)
        Map<Integer, List<String>> porLongitudLinked = nombres.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        System.out.println("2.4) groupingBy + LinkedHashMap: " + porLongitudLinked);

        // 2.5) groupingBy + downstream sum
        Map<String, Integer> sumaLongitudesPorInicial = nombres.stream()
                .collect(Collectors.groupingBy(
                        s -> s.substring(0, 1),
                        Collectors.summingInt(String::length)
                ));
        System.out.println("2.5) groupingBy + summingInt: " + sumaLongitudesPorInicial);
    }

    // ============================================================
    // 🧠 PATRÓN 3: partitioningBy — separación por predicado
    // ============================================================
    static void patron3_partitioningBy_variantes(List<Integer> numeros) {
        System.out.println("\n[PATRÓN 3] partitioningBy — división booleana");

        // 3.1) Pares vs impares
        Map<Boolean, List<Integer>> paresVsImpares = numeros.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("3.1) Pares vs impares: " + paresVsImpares);

        // 3.2) Conteo de pares vs impares
        Map<Boolean, Long> conteoParesVsImpares = numeros.stream()
                .collect(Collectors.partitioningBy(
                        n -> n % 2 == 0,
                        Collectors.counting()
                ));
        System.out.println("3.2) partitioningBy + counting: " + conteoParesVsImpares);

        // 3.3) partitioningBy + mapping → dobles únicos
        Map<Boolean, Set<Integer>> doblesUnicos = numeros.stream()
                .collect(Collectors.partitioningBy(
                        n -> n % 2 == 0,
                        Collectors.mapping(n -> n * 2, Collectors.toSet())
                ));
        System.out.println("3.3) partitioningBy + mapping (dobles únicos): " + doblesUnicos);
    }

    // ============================================================
    // 🧠 PATRÓN 4: Notas y buenas prácticas sobre Map
    // ============================================================
    static void patron4_notas_tecnicas() {
        System.out.println("\n[PATRÓN 4] Notas técnicas sobre Map en Streams");

        System.out.println("""
                - toMap() lanza IllegalStateException si hay claves duplicadas y no defines mergeFn.
                - No admite null como clave ni valor.
                - Usa mergeFn (Integer::sum, Integer::max, (a,b)->a, etc.) para resolver colisiones.
                - Usa Supplier (LinkedHashMap::new, TreeMap::new) para elegir tipo de mapa.
                - groupingBy es para uno-a-muchos; toMap para uno-a-uno.
                - partitioningBy es un caso especial de grouping en dos grupos (true/false).
                """);
    }

    // ============================================================
    // 🧠 PATRÓN 5: Bonus – creación y copia de mapas inmodificables
    // ============================================================
    static void patron5_bonus_map_utilidades() {
        System.out.println("\n[PATRÓN 5] Map.of / Map.copyOf / conversiones");

        Map<String, Integer> mapaEstatico = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> mapaBasico = Map.of("x", 10, "y", 20);

        Map<String, Integer> copiaInmod = Map.copyOf(mapaBasico);
        System.out.println("Map.of: " + mapaEstatico);
        System.out.println("Map.copyOf: " + copiaInmod);

        // Crear mutable desde inmodificable
        Map<String, Integer> mutableDesdeInmod = new LinkedHashMap<>(copiaInmod);
        mutableDesdeInmod.put("z", 99);
        System.out.println("Mutable desde inmodificable: " + mutableDesdeInmod);
    }
}
