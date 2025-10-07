package ProgramaciónFuncional;

import java.util.*;

public class ComparatorInterface {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        List<String> lista = new ArrayList<>(List.of("Percy", "Luis", "Armando"));

        // 1) Comparator clásico con compareTo (método de String,Integer,double,etc)
        Comparator<String> comparador = (a, b) -> a.compareTo(b);

        // Ascendente (usa compareTo)
        lista.sort(comparador);
        System.out.println("Orden ascendente (compareTo): " + lista);

        // Descendente (invirtiendo los parámetros)
        lista.sort((a, b) -> b.compareTo(a));
        System.out.println("Orden descendente (compareTo): " + lista);

        // 2) Streams con sorted() y comparador
        List<String> listaprueba = lista.stream().sorted(comparador).toList();
        System.out.println("Orden ascendente con stream.sorted(): " + listaprueba);

        // 3) Comparator.naturalOrder() (equivalente a compareTo ascendente)
        List<String> ordenNatural = lista.stream()
                .sorted(Comparator.naturalOrder())
                .toList();
        System.out.println("Orden natural con Comparator.naturalOrder(): " + ordenNatural);

        lista.sort(Comparator.naturalOrder()); // forma directa sobre lista
        System.out.println("Orden natural (lista.sort): " + lista);

        // 4) Comparator.reverseOrder() (equivalente a compareTo descendente)
        List<String> ordenInverso = lista.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        System.out.println("Orden inverso con Comparator.reverseOrder(): " + ordenInverso);

        lista.sort(Comparator.reverseOrder()); // forma directa sobre lista
        System.out.println("Orden inverso (lista.sort): " + lista);

        // 5) compareToIgnoreCase() (ignora mayúsculas y minúsculas, método de String)
        lista.sort((a, b) -> a.compareToIgnoreCase(b));
        System.out.println("Orden ignorando mayúsculas/minúsculas (lambda): " + lista);

        lista.sort(String::compareToIgnoreCase); // forma más directa con reference method
        System.out.println("Orden ignorando mayúsculas/minúsculas (reference method): " + lista);

        // 6) Comparator.comparing() con toLowerCase (método de String)
        lista.sort(Comparator.comparing(s -> s.toLowerCase()));
        System.out.println("Orden usando comparing(s -> s.toLowerCase()): " + lista);

        lista.sort(Comparator.comparing(String::toLowerCase));
        System.out.println("Orden usando comparing(String::toLowerCase): " + lista);

        // 7) thenComparing() (criterio secundario: longitud primero, luego orden alfabético)
        lista = new ArrayList<>(List.of("Ana", "Luis", "Jose", "Al", "Armando"));
        lista.sort(Comparator.comparing(String::length)
                .thenComparing(String::compareToIgnoreCase));
        System.out.println("Orden por longitud, luego alfabético: " + lista);

        // 8) nullsFirst y nullsLast (para manejar nulos sin error)
        lista = new ArrayList<>(Arrays.asList("Luis", null, "Ana", "Percy", null));

        lista.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("Orden con nulls primero: " + lista);

        lista.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("Orden con nulls al final: " + lista);
    }
}
