package ProgramaciónFuncional;

import java.util.*;
import java.util.stream.*;

public class StreamInterfaceSet {
    public static void main(String[] args) {
        Set<String> nombres = new TreeSet<>(List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis"));

        // =========================================================
        // 1) TreeSet ya ordena automáticamente por orden natural
        // =========================================================
        System.out.println("TreeSet orden natural (automático): " + nombres);

        // =========================================================
        // 2) Distintas formas de recolectar en un SET
        // =========================================================

        // 2.1) Collectors.toSet() -> normalmente devuelve un HashSet (mutable, orden NO garantizado)
        Set<String> setHash = nombres.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        setHash.add("extra");
        System.out.println("Set (toSet -> HashSet, mutable, orden no garantizado): " + setHash);

        // 2.2) Collectors.toCollection(HashSet::new) -> fuerza HashSet explícitamente
        Set<String> setHash2 = nombres.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println("Set (toCollection(HashSet::new)): " + setHash2);

        // 2.3) Collectors.toCollection(LinkedHashSet::new) -> mantiene orden de aparición en el stream
        Set<String> setLinked = nombres.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("Set (LinkedHashSet, orden de inserción): " + setLinked);

        // 2.4) Collectors.toCollection(TreeSet::new) -> orden natural (alfabético)
        Set<String> setTreeNatural = nombres.stream()
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Set (TreeSet, orden natural): " + setTreeNatural);

        // 2.5) Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(String::length)))
        // -> TreeSet con COMPARATOR personalizado (por longitud)
        Set<String> setTreeCustom = nombres.stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparingInt(String::length))));
        System.out.println("Set (TreeSet con Comparator longitud): " + setTreeCustom);

        // 2.6) Collectors.toUnmodifiableSet() -> SET INMODIFICABLE
        Set<String> setInmod = nombres.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toUnmodifiableSet());
        System.out.println("Set (inmodificable): " + setInmod);

        // 2.7) collectingAndThen -> SET post-procesado (ejemplo: TreeSet -> Set inmodificable)
        Set<String> setCollectingAndThen = nombres.stream()
                .map(String::toLowerCase)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(TreeSet::new), // primero TreeSet ordenado
                        Collections::unmodifiableSet           // luego lo volvemos inmodificable
                ));
        System.out.println("Set (collectingAndThen, TreeSet -> inmodificable): " + setCollectingAndThen);

        // =========================================================
        // 3) Ejemplos específicos con lógica + SET
        // =========================================================

        // 3.1) Nombres con longitud >3, en mayúsculas, a TreeSet
        Set<String> mayoresA3 = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Nombres >3 letras (TreeSet): " + mayoresA3);

        // 3.2) Iniciales únicas ordenadas (TreeSet natural)
        Set<String> iniciales = nombres.stream()
                .map(s -> s.substring(0,1))
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Iniciales únicas (TreeSet): " + iniciales);

        // 3.3) Ordenar por última letra usando un comparator en TreeSet
        Set<String> ordenPorUltimaLetra = nombres.stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(s -> s.charAt(s.length()-1)))
                ));
        System.out.println("Orden por última letra (TreeSet + comparator): " + ordenPorUltimaLetra);

        // 3.4) LinkedHashSet para mantener orden de aparición
        Set<String> linked = nombres.stream()
                .filter(s -> s.length() > 2)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("LinkedHashSet (mantiene orden de aparición): " + linked);
    }
}
