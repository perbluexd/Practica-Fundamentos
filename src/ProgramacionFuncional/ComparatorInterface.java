package ProgramacionFuncional;

import java.util.*;

/**
 * COMPARATOR & SORT – Guía por Patrones
 * -------------------------------------
 * Patrones cubiertos:
 * 1) Orden natural / descendente (compareTo, naturalOrder, reverseOrder)
 * 2) Streams + sorted (con y sin comparador)
 * 3) Comparación que ignora mayúsculas (compareToIgnoreCase)
 * 4) Comparadores por llave (Comparator.comparing / method refs)
 * 5) Criterio compuesto (thenComparing)
 * 6) Manejo de nulos (nullsFirst / nullsLast)
 *
 * Extra:
 * - Helpers y datasets de prueba
 */
public class ComparatorInterface {

    public static void main(String[] args) {
        // Ejecuta todos los patrones en orden (puedes comentar/activar a voluntad)
        patron1_ordenBasico();
        patron2_streamsSorted();
        patron3_ignoreCase();
        patron4_comparingKeyExtractor();
        patron5_thenComparingCriterioCompuesto();
        patron6_nullsFirstNullsLast();
    }

    // ============================================================
    // PATRÓN 1: Orden natural / descendente (Comparator básico)
    // ============================================================
    static void patron1_ordenBasico() {
        System.out.println("\n[PATRÓN 1] Orden básico (natural / descendente)");

        List<String> lista = new ArrayList<>(List.of("Percy", "Luis", "Armando"));

        // 1A) Comparator clásico usando compareTo (ascendente)
        Comparator<String> cmpAsc = (a, b) -> a.compareTo(b);
        lista.sort(cmpAsc);
        System.out.println("Ascendente (compareTo): " + lista);

        // 1B) Descendente invirtiendo parámetros
        lista.sort((a, b) -> b.compareTo(a));
        System.out.println("Descendente (compareTo invertido): " + lista);

        // 1C) Ascendente con Comparator.naturalOrder()
        lista.sort(Comparator.naturalOrder());
        System.out.println("Ascendente (naturalOrder): " + lista);

        // 1D) Descendente con Comparator.reverseOrder()
        lista.sort(Comparator.reverseOrder());
        System.out.println("Descendente (reverseOrder): " + lista);
    }

    // ============================================================
    // PATRÓN 2: Streams + sorted (con y sin comparador)
    // ============================================================
    static void patron2_streamsSorted() {
        System.out.println("\n[PATRÓN 2] Streams + sorted");

        List<String> base = List.of("Percy", "Luis", "Armando");

        // 2A) sorted() con comparador explícito
        List<String> asc = base.stream().sorted((a, b) -> a.compareTo(b)).toList();
        System.out.println("Stream.sorted asc (lambda compareTo): " + asc);

        // 2B) sorted() con naturalOrder()
        List<String> natural = base.stream().sorted(Comparator.naturalOrder()).toList();
        System.out.println("Stream.sorted asc (naturalOrder): " + natural);

        // 2C) sorted() con reverseOrder()
        List<String> inverso = base.stream().sorted(Comparator.reverseOrder()).toList();
        System.out.println("Stream.sorted desc (reverseOrder): " + inverso);
    }

    // ============================================================
    // PATRÓN 3: Comparación que ignora mayúsculas/minúsculas
    // ============================================================
    static void patron3_ignoreCase() {
        System.out.println("\n[PATRÓN 3] Ignore case");

        List<String> lista = new ArrayList<>(List.of("luis", "Luis", "ana", "Ana", "PERcY"));

        // 3A) Lambda con compareToIgnoreCase
        lista.sort((a, b) -> a.compareToIgnoreCase(b));
        System.out.println("IgnoreCase (lambda): " + lista);

        // 3B) Method reference directo
        lista.sort(String::compareToIgnoreCase);
        System.out.println("IgnoreCase (method ref): " + lista);
    }

    // ============================================================
    // PATRÓN 4: Comparadores por llave (key extractor)
    // ============================================================
    static void patron4_comparingKeyExtractor() {
        System.out.println("\n[PATRÓN 4] Comparator.comparing (key extractor)");

        List<String> lista = new ArrayList<>(List.of("Percy", "Luis", "Armando", "alberto"));

        // 4A) comparing con lambda (toLowerCase)
        lista.sort(Comparator.comparing(s -> s.toLowerCase()));
        System.out.println("Comparing(s -> s.toLowerCase()): " + lista);

        // 4B) comparing con method reference
        lista.sort(Comparator.comparing(String::toLowerCase));
        System.out.println("Comparing(String::toLowerCase): " + lista);

        // 4C) comparing por longitud
        lista.sort(Comparator.comparingInt(String::length));
        System.out.println("ComparingInt(String::length): " + lista);
    }

    // ============================================================
    // PATRÓN 5: Criterio compuesto (thenComparing)
    // ============================================================
    static void patron5_thenComparingCriterioCompuesto() {
        System.out.println("\n[PATRÓN 5] thenComparing (criterio secundario)");

        List<String> lista = new ArrayList<>(List.of("Ana", "Luis", "Jose", "Al", "Armando", "alba", "AL"));

        // Primero por longitud; si empata, alfabético ignorando mayúsculas
        lista.sort(
                Comparator.comparingInt(String::length)
                        .thenComparing(String::compareToIgnoreCase)
        );
        System.out.println("Longitud → luego alfabético (ignoreCase): " + lista);

        // Variante: longitud desc, luego natural asc
        lista.sort(
                Comparator.comparingInt(String::length).reversed()
                        .thenComparing(Comparator.naturalOrder())
        );
        System.out.println("Longitud DESC → luego natural: " + lista);
    }

    // ============================================================
    // PATRÓN 6: Manejo de nulos (nullsFirst / nullsLast)
    // ============================================================
    static void patron6_nullsFirstNullsLast() {
        System.out.println("\n[PATRÓN 6] nullsFirst / nullsLast");

        List<String> lista = new ArrayList<>(Arrays.asList("Luis", null, "Ana", "Percy", null));

        // Nulos al inicio
        lista.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("nullsFirst(naturalOrder): " + lista);

        // Nulos al final
        lista.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("nullsLast(naturalOrder): " + lista);

        // Nulos al final + clave por longitud
        lista.sort(Comparator.nullsLast(Comparator.comparingInt(s -> s == null ? 0 : s.length())));
        System.out.println("nullsLast + comparingInt(length): " + lista);
    }
}
