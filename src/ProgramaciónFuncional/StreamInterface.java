package ProgramaciónFuncional;

import java.util.*;
import java.util.stream.*;

public class StreamInterface {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);

        List<String> nombres = List.of("Percy", "Luis", "Ana", "Armando", "Ana", "Luis");

        // =========================================================
        // 1) filter + map + sorted  -> distintas formas de LISTA
        // =========================================================

        // 1.1) toList() -> LISTA INMODIFICABLE (no add/remove)
        List<String> mayoresA3 = nombres.stream()
                .filter(s -> s.length() > 3)                 // Predicate
                .map(String::toUpperCase)                    // Function
                .sorted()                                    // Comparator natural
                .toList();
        System.out.println("Filter + Map + Sorted (toList, inmod): " + mayoresA3);

        // 1.2) Collectors.toList() -> LISTA MUTABLE (normalmente ArrayList)
        List<String> mayoresA3_mutable = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
        mayoresA3_mutable.add("NUEVO"); // OK: es mutable
        System.out.println("Filter + Map + Sorted (Collectors.toList, mutable): " + mayoresA3_mutable);

        // 1.3) Collectors.toCollection(ArrayList::new) -> fuerzas ArrayList (mutable)
        List<String> mayoresA3_arrayList = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Filter + Map + Sorted (toCollection(ArrayList::new)): " + mayoresA3_arrayList);

        // 1.4) Collectors.toUnmodifiableList() -> LISTA INMODIFICABLE explícita
        List<String> mayoresA3_inmod2 = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toUnmodifiableList());
        System.out.println("Filter + Map + Sorted (toUnmodifiableList): " + mayoresA3_inmod2);

        // 1.5) collectingAndThen -> post-proceso: convertir a inmodificable
        List<String> mayoresA3_collectingThen = nombres.stream()
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.collectingAndThen(Collectors.toList(), List::copyOf));
        System.out.println("Filter + Map + Sorted (collectingAndThen -> inmod): " + mayoresA3_collectingThen);

        // 1.6) Obtener una copia MUTABLE desde una lista inmodificable (patrón común)
        List<String> copiaMutable = new ArrayList<>(mayoresA3); // mayoresA3 era inmodificable
        copiaMutable.add("OTRO");
        System.out.println("Copia mutable desde inmodificable: " + copiaMutable);

        // =========================================================
        // 2) distinct + sorted con Comparator -> más variantes de LISTA
        // =========================================================
        List<String> sinDuplicados_toList = nombres.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length))
                .toList(); // inmodificable
        System.out.println("Distinct + Sorted por longitud (toList, inmod): " + sinDuplicados_toList);

        List<String> sinDuplicados_toMutableList = nombres.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList()); // mutable
        sinDuplicados_toMutableList.removeIf(s -> s.length() == 3);
        System.out.println("Distinct + Sorted por longitud (Collectors.toList, mutable): " + sinDuplicados_toMutableList);

        // =========================================================
        // 3) iniciales únicas -> variantes de LISTA
        // =========================================================
        List<String> iniciales_inmod = nombres.stream()
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(0,1))
                .distinct()
                .toList(); // inmodificable
        System.out.println("Iniciales únicas (toList, inmod): " + iniciales_inmod);

        List<String> iniciales_mutable = nombres.stream()
                .filter(s -> s.length() > 2)
                .map(s -> s.substring(0,1))
                .distinct()
                .collect(Collectors.toList()); // mutable
        iniciales_mutable.add("Z");
        System.out.println("Iniciales únicas (Collectors.toList, mutable): " + iniciales_mutable);

        // =========================================================
        // 4) Orden por última letra -> variantes de LISTA
        // =========================================================
        List<String> ordenPorUltima_inmod = nombres.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .sorted(Comparator.comparing(s -> s.charAt(s.length()-1)))
                .toList(); // inmodificable
        System.out.println("Orden por última letra (toList, inmod): " + ordenPorUltima_inmod);

        List<String> ordenPorUltima_mutableArrayList = nombres.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .sorted(Comparator.comparing(s -> s.charAt(s.length()-1)))
                .collect(Collectors.toCollection(ArrayList::new)); // mutable ArrayList
        ordenPorUltima_mutableArrayList.add("extra");
        System.out.println("Orden por última letra (toCollection(ArrayList), mutable): " + ordenPorUltima_mutableArrayList);

        // =========================================================
        // 5) Numérico: filter + map + distinct + sorted -> variantes de LISTA
        // =========================================================
        List<Integer> numeros = List.of(5, 2, 2, 9, 4, 4, 8, 10, 1);

        List<Integer> procesados_inmod = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList(); // inmodificable
        System.out.println("Números pares*10 únicos desc (toList, inmod): " + procesados_inmod);

        List<Integer> procesados_mutable = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()); // mutable
        procesados_mutable.replaceAll(x -> x + 1);
        System.out.println("Números pares*10 únicos desc (Collectors.toList, mutable + replaceAll): " + procesados_mutable);

        List<Integer> procesados_arrayList = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ArrayList::new)); // fuerza ArrayList
        System.out.println("Números pares*10 únicos desc (toCollection(ArrayList)): " + procesados_arrayList);

        List<Integer> procesados_inmod_collecting = numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.collectingAndThen(Collectors.toList(), List::copyOf)); // inmod por post-proceso
        System.out.println("Números pares*10 únicos desc (collectingAndThen -> inmod): " + procesados_inmod_collecting);
    }
}
