package ProgramacionFuncional;

import java.util.*;
import java.util.stream.*;

/**
 * STREAMS – Guía por Patrones (List<String> y List<Integer>)
 * ----------------------------------------------------------
 * Patrones cubiertos:
 * 0️⃣ Dataset base (nombres y números)
 * 1️⃣ Pipeline clásico: filter + map + sorted  (variantes de lista)
 * 2️⃣ distinct + sorted con Comparator (variantes de lista)
 * 3️⃣ Transformación a iniciales únicas
 * 4️⃣ Orden por última letra (Comparator derivado)
 * 5️⃣ Pipeline numérico: filter + map + distinct + sorted (variantes de lista)
 *
 * Notas clave:
 * - toList() (desde Java 16) → lista inmodificable.
 * - Collectors.toList() → típicamente ArrayList mutable.
 * - Collectors.toCollection(ArrayList::new) → fuerzas ArrayList mutable.
 * - Collectors.toUnmodifiableList() → inmodificable explícito.
 * - collectingAndThen(…, List::copyOf) → post-proceso a inmodificable.
 */
public class StreamInterface {

    public static void main(String[] args){
        // 0) Dataset base
        List<String> nombres = datasetNombres();
        List<Integer> numeros = datasetNumeros();

        // 1) filter + map + sorted → variantes de LISTA
        patron1_filterMapSorted_listVariants(nombres);

        // 2) distinct + sorted(Comparator) → variantes de LISTA
        patron2_distinctSorted_listVariants(nombres);

        // 3) iniciales únicas
        patron3_inicialesUnicas(nombres);

        // 4) orden por última letra
        patron4_ordenPorUltimaLetra(nombres);

        // 5) numérico: filter + map + distinct + sorted → variantes
        patron5_numerico_pipeline(numeros);
    }

    // ============================================================
    // 0️⃣ DATASETS
    // ============================================================
    static List<String> datasetNombres() {
        return List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis");
    }
    static List<Integer> datasetNumeros() {
        return List.of(5, 2, 2, 9, 4, 4, 8, 10, 1);
    }

    // ============================================================
    // 1️⃣ filter + map + sorted  → variantes de LISTA
    // ============================================================
    static void patron1_filterMapSorted_listVariants(List<String> nombres) {
        System.out.println("\n[PATRÓN 1] filter + map + sorted → variantes de LISTA");

        // 1.1) toList() → LISTA INMODIFICABLE
        List<String> mayoresA3 = nombres.stream()
                .filter(s -> s.length() > 3)           // Predicate
                .map(String::toUpperCase)              // Function
                .sorted()                              // Comparator natural
                .toList();                             // inmodificable
        System.out.println("toList (inmod): " + mayoresA3);

        // 1.2) Collectors.toList() → LISTA MUTABLE
        List<String> mayoresA3_mutable = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());         // mutable
        mayoresA3_mutable.add("NUEVO");
        System.out.println("Collectors.toList (mutable): " + mayoresA3_mutable);

        // 1.3) toCollection(ArrayList::new) → fuerzas ArrayList (mutable)
        List<String> mayoresA3_arrayList = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("toCollection(ArrayList): " + mayoresA3_arrayList);

        // 1.4) toUnmodifiableList() → inmodificable explícito
        List<String> mayoresA3_inmod2 = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toUnmodifiableList());
        System.out.println("toUnmodifiableList (inmod): " + mayoresA3_inmod2);

        // 1.5) collectingAndThen → post-proceso a inmodificable
        List<String> mayoresA3_collectingThen = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.collectingAndThen(Collectors.toList(), List::copyOf));
        System.out.println("collectingAndThen→copyOf (inmod): " + mayoresA3_collectingThen);

        // 1.6) Crear copia MUTABLE desde una lista inmodificable
        List<String> copiaMutable = new ArrayList<>(mayoresA3);
        copiaMutable.add("OTRO");
        System.out.println("Copia mutable desde inmod: " + copiaMutable);
    }

    // ============================================================
    // 2️⃣ distinct + sorted(Comparator) → variantes de LISTA
    // ============================================================
    static void patron2_distinctSorted_listVariants(List<String> nombres) {
        System.out.println("\n[PATRÓN 2] distinct + sorted(Comparator) → variantes de LISTA");

        // 2.1) toList() → inmodificable
        List<String> sinDuplicados_toList = nombres.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length))
                .toList();
        System.out.println("distinct + sorted por longitud (toList, inmod): " + sinDuplicados_toList);

        // 2.2) Collectors.toList() → mutable
        List<String> sinDuplicados_toMutableList = nombres.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList()); // mutable
        sinDuplicados_toMutableList.removeIf(s -> s.length() == 3);
        System.out.println("distinct + sorted por longitud (Collectors.toList, mutable): " + sinDuplicados_toMutableList);
    }

    // ============================================================
    // 3️⃣ Iniciales únicas → variantes de LISTA
    // ============================================================
    static void patron3_inicialesUnicas(List<String> nombres) {
        System.out.println("\n[PATRÓN 3] Iniciales únicas");

        // 3.1) toList() → inmodificable
        List<String> iniciales_inmod = nombres.stream()
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(0, 1))
                .distinct()
                .toList();
        System.out.println("Iniciales únicas (toList, inmod): " + iniciales_inmod);

        // 3.2) Collectors.toList() → mutable
        List<String> iniciales_mutable = nombres.stream()
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(0, 1))
                .distinct()
                .collect(Collectors.toList());
        iniciales_mutable.add("Z");
        System.out.println("Iniciales únicas (Collectors.toList, mutable): " + iniciales_mutable);
    }

    // ============================================================
    // 4️⃣ Orden por última letra → variantes de LISTA
    // ============================================================
    static void patron4_ordenPorUltimaLetra(List<String> nombres) {
        System.out.println("\n[PATRÓN 4] Orden por última letra");

        // 4.1) toList() → inmodificable
        List<String> ordenPorUltima_inmod = nombres.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .sorted(Comparator.comparing(s -> s.charAt(s.length() - 1)))
                .toList();
        System.out.println("Última letra (toList, inmod): " + ordenPorUltima_inmod);

        // 4.2) toCollection(ArrayList::new) → mutable
        List<String> ordenPorUltima_mutableArrayList = nombres.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .sorted(Comparator.comparing(s -> s.charAt(s.length() - 1)))
                .collect(Collectors.toCollection(ArrayList::new));
        ordenPorUltima_mutableArrayList.add("extra");
        System.out.println("Última letra (toCollection(ArrayList), mutable): " + ordenPorUltima_mutableArrayList);
    }

    // ============================================================
    // 5️⃣ Numérico: filter + map + distinct + sorted → variantes
    // ============================================================
    static void patron5_numerico_pipeline(List<Integer> numeros) {
        System.out.println("\n[PATRÓN 5] Numérico – pares*10 únicos descendente");

        // 5.1) toList() → inmodificable
        List<Integer> procesados_inmod = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();
        System.out.println("Pares*10 únicos desc (toList, inmod): " + procesados_inmod);

        // 5.2) Collectors.toList() → mutable (con post-proceso replaceAll)
        List<Integer> procesados_mutable = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        procesados_mutable.replaceAll(x -> x + 1);
        System.out.println("Pares*10 únicos desc (+1) (Collectors.toList, mutable): " + procesados_mutable);

        // 5.3) toCollection(ArrayList::new) → fuerzas ArrayList
        List<Integer> procesados_arrayList = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Pares*10 únicos desc (toCollection(ArrayList)): " + procesados_arrayList);

        // 5.4) collectingAndThen → toList + copyOf (inmodificable)
        List<Integer> procesados_inmod_collecting = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.collectingAndThen(Collectors.toList(), List::copyOf));
        System.out.println("Pares*10 únicos desc (collectingAndThen→copyOf, inmod): " + procesados_inmod_collecting);
    }
}
