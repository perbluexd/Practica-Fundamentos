package ProgramacionFuncional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * STREAMS → Recolección en SETS
 * ------------------------------
 * Patrones cubiertos:
 * 1) TreeSet como estructura base (orden natural)
 * 2) Recolectores hacia Set (toSet, toCollection, toUnmodifiableSet, collectingAndThen)
 * 3) Casos prácticos con lógica + Set (filtros, mapeos, comparators)
 */
public class StreamInterfaceSet {

    public static void main(String[] args) {
        // Fuente con duplicados (TreeSet en main solo para mostrar orden natural inmediato)
        Set<String> nombres = new TreeSet<>(List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis"));

        patron1_treeSetOrdenNatural(nombres);
        patron2_formasDeRecolectar(List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis"));
        patron3_casosPracticos(List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis"));
    }

    // =========================================================
    // PATRÓN 1: TreeSet ya ordena automáticamente por orden natural
    // =========================================================
    static void patron1_treeSetOrdenNatural(Set<String> nombres) {
        System.out.println("TreeSet orden natural (automático): " + nombres);
    }

    // =========================================================
    // PATRÓN 2: Distintas formas de recolectar en un SET
    // =========================================================
    static void patron2_formasDeRecolectar(List<String> base) {
        // 2.1) Collectors.toSet() -> normalmente HashSet (mutable, orden NO garantizado)
        Set<String> setHash = base.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        setHash.add("extra");
        System.out.println("Set (toSet -> HashSet, mutable, orden no garantizado): " + setHash);

        // 2.2) toCollection(HashSet::new) -> fuerza HashSet
        Set<String> setHash2 = base.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println("Set (toCollection(HashSet::new)): " + setHash2);

        // 2.3) toCollection(LinkedHashSet::new) -> mantiene orden de aparición del stream
        Set<String> setLinked = base.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("Set (LinkedHashSet, orden de inserción): " + setLinked);

        // 2.4) toCollection(TreeSet::new) -> orden natural (alfabético)
        Set<String> setTreeNatural = base.stream()
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Set (TreeSet, orden natural): " + setTreeNatural);

        // 2.5) TreeSet con Comparator personalizado (por longitud)
        Set<String> setTreeCustom = base.stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparingInt(String::length))));
        System.out.println("Set (TreeSet con Comparator longitud): " + setTreeCustom);

        // 2.6) toUnmodifiableSet() -> SET INMODIFICABLE
        Set<String> setInmod = base.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toUnmodifiableSet());
        System.out.println("Set (inmodificable): " + setInmod);

        // 2.7) collectingAndThen -> post-proceso (TreeSet → inmodificable)
        Set<String> setCollectingAndThen = base.stream()
                .map(String::toLowerCase)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(TreeSet::new),
                        Collections::unmodifiableSet
                ));
        System.out.println("Set (collectingAndThen, TreeSet -> inmodificable): " + setCollectingAndThen);
    }

    // =========================================================
    // PATRÓN 3: Ejemplos específicos con lógica + SET
    // =========================================================
    static void patron3_casosPracticos(List<String> base) {
        // 3.1) Nombres con longitud >3, en mayúsculas, a TreeSet
        Set<String> mayoresA3 = base.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Nombres >3 letras (TreeSet): " + mayoresA3);

        // 3.2) Iniciales únicas ordenadas (TreeSet natural)
        Set<String> iniciales = base.stream()
                .map(s -> s.substring(0,1))
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Iniciales únicas (TreeSet): " + iniciales);

        // 3.3) Orden por última letra usando un Comparator en TreeSet
        Set<String> ordenPorUltimaLetra = base.stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(s -> s.charAt(s.length() - 1)))
                ));
        System.out.println("Orden por última letra (TreeSet + comparator): " + ordenPorUltimaLetra);

        // 3.4) LinkedHashSet para mantener orden de aparición tras filtros/map
        Set<String> linked = base.stream()
                .filter(s -> s.length() > 2)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("LinkedHashSet (mantiene orden de aparición): " + linked);
    }
}
