import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DEMO de LinkedHashSet<Integer> con menú interactivo.
 *
 * Puntos clave:
 * - LinkedHashSet preserva el **orden de inserción** (a diferencia de HashSet).
 * - No admite duplicados.
 * - Operaciones típicas O(1) promedio (add, remove, contains).
 * - En unión/intersección/diferencia usamos copias para NO modificar el set original.
 */
public class LinkedHashSetInteger {

    // Última opción del menú (salida)
    public static final int OPCION_SALIR = 22;

    public static void main(String[] args) {
        // try-with-resources para cerrar el Scanner al terminar
        try (Scanner sc = new Scanner(System.in)) {
            // Base de trabajo: mantiene el orden de inserción
            LinkedHashSet<Integer> numeros = new LinkedHashSet<>();
            int opcion;

            do {
                mostrarMenu();
                // Validamos que la opción esté en [1..OPCION_SALIR]
                opcion = leerEntero(sc, "Ingresa la opción que desees: ", 1, OPCION_SALIR);

                switch (opcion) {

                    case 1 -> {
                        // add: inserta si NO existía. Retorna true si cambió el set.
                        int n = leerEntero(sc, "Ingresa el número a agregar: ");
                        boolean ok = numeros.add(n);
                        System.out.println(ok ? "Agregado correctamente." : "El valor ya existía (no se repite).");
                    }

                    case 2 -> {
                        // remove(Object): elimina si existe. Retorna true si eliminó.
                        int v = leerEntero(sc, "Ingresa el número a eliminar: ");
                        boolean ok = numeros.remove(v);
                        System.out.println(ok ? "Eliminado correctamente." : "No se encuentra en el conjunto.");
                    }

                    case 3 -> {
                        // contains(Object): pertenencia
                        int v = leerEntero(sc, "Ingresa el número a buscar: ");
                        boolean ok = numeros.contains(v);
                        System.out.println(ok ? "Encontrado." : "No encontrado.");
                    }

                    case 4 -> {
                        // size / isEmpty: info básica
                        System.out.println("size(): " + numeros.size());
                        System.out.println("isEmpty(): " + numeros.isEmpty());
                    }

                    case 5 -> {
                        // Recorrido for-each respeta **orden de inserción**
                        if (numeros.isEmpty()) {
                            System.out.println("Conjunto vacío.");
                        } else {
                            System.out.println("Listado (orden de inserción):");
                            for (int v : numeros) {
                                System.out.println("- " + v);
                            }
                        }
                    }

                    case 6 -> {
                        // Recorrido con Iterator (útil si quisieras remove() seguro durante la iteración)
                        if (numeros.isEmpty()) {
                            System.out.println("Conjunto vacío.");
                        } else {
                            Iterator<Integer> it = numeros.iterator();
                            int i = 1;
                            while (it.hasNext()) {
                                System.out.println((i++) + ": " + it.next());
                            }
                        }
                    }

                    case 7 -> {
                        // Unión: A ∪ B (copia para NO tocar 'numeros'); LinkedHashSet preserva inserción.
                        LinkedHashSet<Integer> otro = csv(sc, "Ingresa números separados por comas: ");
                        LinkedHashSet<Integer> union = new LinkedHashSet<>(numeros);
                        boolean cambio = union.addAll(otro); // true si agregó algo no presente
                        System.out.println("Otro:  " + otro);
                        System.out.println("Unión: " + union);
                        System.out.println("¿Se agregaron elementos nuevos?: " + cambio);
                    }

                    case 8 -> {
                        // Intersección: A ∩ B (copia). retainAll deja solo lo común con 'otro'.
                        LinkedHashSet<Integer> otro = csv(sc, "Ingresa números separados por comas: ");
                        LinkedHashSet<Integer> inter = new LinkedHashSet<>(numeros);
                        boolean cambio = inter.retainAll(otro); // true si cambió (se eliminaron no comunes)
                        System.out.println("Otro:          " + otro);
                        System.out.println("Intersección:  " + inter);
                        System.out.println("¿Cambió?: " + cambio);
                    }

                    case 9 -> {
                        // Diferencia: A \ B (copia). removeAll borra lo que esté en 'otro'.
                        LinkedHashSet<Integer> otro = csv(sc, "Ingresa números separados por comas: ");
                        LinkedHashSet<Integer> dif = new LinkedHashSet<>(numeros);
                        boolean cambio = dif.removeAll(otro); // true si eliminó elementos
                        System.out.println("Otro:        " + otro);
                        System.out.println("Diferencia:  " + dif);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                    }

                    case 10 -> {
                        // toArray tipado: evita castings
                        Integer[] arr = numeros.toArray(Integer[]::new);
                        System.out.println("Array: " + Arrays.toString(arr));
                    }

                    case 11 -> {
                        // containsAll: ¿'numeros' contiene TODOS los elementos de 'otro'?
                        LinkedHashSet<Integer> otro = csv(sc, "Ingresa números separados por comas: ");
                        System.out.println("¿numeros contiene a 'otro'?: " + numeros.containsAll(otro));
                    }

                    case 12 -> {
                        // removeIf(Predicate): elimina todos los que cumplan el predicado. True si cambió.
                        int limite = leerEntero(sc, "Eliminar números menores a: ");
                        Predicate<Integer> pred = x -> x < limite;
                        boolean cambio = numeros.removeIf(pred);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                        System.out.println("Restante: " + numeros);
                    }

                    case 13 -> {
                        // equals / hashCode: igualdad por elementos (orden irrelevante para equals)
                        LinkedHashSet<Integer> otro = csv(sc, "Ingresa números separados por comas: ");
                        System.out.println("equals?: " + numeros.equals(otro));
                        System.out.println("hashCode(numeros): " + numeros.hashCode());
                        System.out.println("hashCode(otro):     " + otro.hashCode());
                    }

                    case 14 -> {
                        // clone(): copia superficial (nueva instancia, mismos elementos, mismo orden de inserción actual)
                        @SuppressWarnings("unchecked")
                        LinkedHashSet<Integer> copia = (LinkedHashSet<Integer>) numeros.clone();
                        System.out.println("clone(): " + copia);
                        System.out.println("¿Misma instancia? " + (copia == numeros));
                        System.out.println("equals?: " + copia.equals(numeros));
                    }

                    case 15 -> {
                        // Streams: conteo filtrado + orden natural (asc). El set NO cambia.
                        int mayorQue = leerEntero(sc, "Contar números mayores a: ");
                        long conteo = numeros.stream().filter(n -> n > mayorQue).count();
                        System.out.println("Coincidencias: " + conteo);

                        List<Integer> ordenNatural = numeros.stream()
                                .sorted() // orden natural ascendente
                                .toList();
                        System.out.println("Orden natural (sorted): " + ordenNatural);
                    }

                    case 16 -> {
                        // map / filter: cuadrados y pares (ejemplos simples)
                        var cuadrados = numeros.stream().map(n -> n * n).toList();
                        System.out.println("Cuadrados: " + cuadrados);

                        var pares = numeros.stream().filter(n -> n % 2 == 0).toList();
                        System.out.println("Pares: " + pares);
                    }

                    case 17 -> {
                        // sorted con Comparator: reverso y por valor absoluto
                        var reverso = numeros.stream()
                                .sorted(Comparator.reverseOrder())
                                .toList();
                        System.out.println("Orden inverso: " + reverso);

                        var porValorAbsoluto = numeros.stream()
                                .sorted(Comparator.comparingInt(Math::abs))
                                .toList();
                        System.out.println("Orden por valor absoluto: " + porValorAbsoluto);
                    }

                    case 18 -> {
                        // collect: toCollection(LinkedHashSet) preserva orden de inserción del stream
                        int mayorQue = leerEntero(sc, "Filtrar números mayores a: ");
                        LinkedHashSet<Integer> filtrado = numeros.stream()
                                .filter(n -> n > mayorQue)
                                .collect(Collectors.toCollection(LinkedHashSet::new));
                        System.out.println("Filtrado→LinkedHashSet (orden preservado): " + filtrado);

                        String unidos = numeros.stream()
                                .sorted()                // para unir ordenado asc
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "));
                        System.out.println("joining (orden natural): " + unidos);
                    }

                    case 19 -> {
                        // anyMatch / allMatch / noneMatch: cuantificadores lógicos
                        int limite = leerEntero(sc, "Ingresa un número límite: ");
                        boolean alguno = numeros.stream().anyMatch(n -> n > limite);
                        boolean todos = numeros.stream().allMatch(n -> n > limite);
                        boolean ninguno = numeros.stream().noneMatch(n -> n > limite);
                        System.out.println("anyMatch: " + alguno + " | allMatch: " + todos + " | noneMatch: " + ninguno);
                    }

                    case 20 -> {
                        // groupingBy / toMap (con LinkedHashMap para ver el orden de inserción de claves calculadas)
                        Map<Integer, List<Integer>> porResto = numeros.stream()
                                .collect(Collectors.groupingBy(
                                        n -> n % 3,           // clave del grupo
                                        LinkedHashMap::new,   // mapa destino (preserva orden de inserción de claves de grupo)
                                        Collectors.toList()   // colección por grupo
                                ));
                        System.out.println("groupingBy (n % 3): " + porResto);

                        Map<Integer, Integer> mapa = numeros.stream()
                                .collect(Collectors.toMap(
                                        n -> n,               // clave
                                        n -> n * n,           // valor
                                        (a, b) -> a,          // resolver colisiones (no debería haber en un set)
                                        LinkedHashMap::new    // tipo de mapa destino
                                ));
                        System.out.println("toMap(numero → cuadrado): " + mapa);
                    }

                    case 21 -> {
                        // clear: vacía el conjunto por completo
                        numeros.clear();
                        System.out.println("Conjunto limpiado.");
                    }

                    case OPCION_SALIR -> {
                        System.out.println("Saliendo...");
                    }

                    default -> System.out.println("Ingresa un valor válido.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    // ----------------- Helpers (I/O y parsing) -----------------

    /** Muestra el menú principal. */
    static void mostrarMenu() {
        System.out.println("""
                
                --- MENÚ LINKEDHASHSET (Integers) ---
                1  . Agregar (add)
                2  . Eliminar por valor (remove)
                3  . ¿Contiene? (contains)
                4  . Tamaño/Vacío (size / isEmpty)
                5  . Listar (for-each) [orden de inserción]
                6  . Listar con Iterator (indexado)
                7  . Unión (addAll) [no destructiva]
                8  . Intersección (retainAll) [no destructiva]
                9  . Diferencia (removeAll) [no destructiva]
                10 . Convertir a array (toArray)
                11 . Subconjunto (containsAll)
                12 . Borrado condicional (< límite)
                13 . Igualdad/Hash (equals / hashCode)
                14 . Clonar (clone)
                15 . Stream: contar / orden natural
                16 . Stream: map (cuadrados / pares)
                17 . Stream: sorted (reverso / por valor absoluto)
                18 . Stream: collect (toCollection LinkedHashSet / joining)
                19 . Stream: anyMatch / allMatch / noneMatch
                20 . Stream: groupingBy (resto) / toMap (n→cuadrado)
                21 . Limpiar (clear)
                22 . Salir
                """);
    }

    /** Lee un entero con reintentos hasta que sea válido. */
    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
            }
        }
    }

    /** Lee un entero validado en el rango [min..max]. */
    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Valor fuera de rango (" + min + " - " + max + "). Intenta de nuevo.");
                continue;
            }
            return n;
        }
    }

    /**
     * Parsea una línea CSV "1, 2, 3" → LinkedHashSet<Integer>{1,2,3}.
     * - Ignora tokens vacíos/espacios.
     * - Preserva orden de inserción según aparecen en el CSV.
     */
    static LinkedHashSet<Integer> csv(Scanner sc, String prompt) {
        System.out.print(prompt);
        String csv = sc.nextLine();
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        if (csv == null || csv.isBlank()) return set;

        for (String p : csv.split(",")) {
            String t = p.trim();
            if (t.isEmpty()) continue;
            try {
                set.add(Integer.parseInt(t));
            } catch (NumberFormatException ignored) {
                // Puedes avisar si quieres: System.out.println("Valor inválido ignorado: " + t);
            }
        }
        return set;
    }
}
