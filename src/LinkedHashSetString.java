import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DEMO de LinkedHashSet<String> con menú interactivo.
 *
 * Claves:
 * - LinkedHashSet preserva el **orden de inserción** (a diferencia de HashSet).
 * - No admite duplicados.
 * - Operaciones típicas O(1) promedio (add, remove, contains).
 * - Unión/intersección/diferencia se hacen sobre copias para NO alterar el set base.
 */
public class LinkedHashSetString {

    // Última opción del menú (salida)
    public static final int OPCION_SALIR = 22;

    public static void main(String[] args) {
        // try-with-resources cierra el Scanner automáticamente
        try (Scanner sc = new Scanner(System.in)) {
            // Base de trabajo; mantiene orden de inserción
            LinkedHashSet<String> palabras = new LinkedHashSet<>();
            int opcion;

            do {
                mostrarMenu();
                // Valida que la opción esté en [1..OPCION_SALIR]
                opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

                switch (opcion) {

                    case 1 -> {
                        // add: agrega si NO existía. true si cambió el set.
                        String s = leerLineaNoVacia(sc, "Ingresa la palabra a agregar: ");
                        boolean ok = palabras.add(s);
                        System.out.println(ok ? "Agregado correctamente." : "El valor ya existía (no se repite).");
                    }

                    case 2 -> {
                        // remove(Object): elimina si existe. true si se eliminó.
                        String v = leerLineaNoVacia(sc, "Ingresa la palabra a eliminar: ");
                        boolean ok = palabras.remove(v);
                        System.out.println(ok ? "Eliminado correctamente." : "No se encuentra en el conjunto.");
                    }

                    case 3 -> {
                        // contains(Object): verifica pertenencia
                        String v = leerLineaNoVacia(sc, "Ingresa la palabra a buscar: ");
                        boolean ok = palabras.contains(v);
                        System.out.println(ok ? "Encontrado." : "No encontrado.");
                    }

                    case 4 -> {
                        // size / isEmpty: información básica
                        System.out.println("size(): " + palabras.size());
                        System.out.println("isEmpty(): " + palabras.isEmpty());
                    }

                    case 5 -> {
                        // Listado for-each – respeta orden de inserción
                        if (palabras.isEmpty()) {
                            System.out.println("Conjunto vacío.");
                        } else {
                            System.out.println("Listado (orden de inserción):");
                            for (String v : palabras) {
                                System.out.println("- " + v);
                            }
                        }
                    }

                    case 6 -> {
                        // Listado con Iterator (útil si quisieras remove() durante el recorrido)
                        if (palabras.isEmpty()) {
                            System.out.println("Conjunto vacío.");
                        } else {
                            Iterator<String> it = palabras.iterator();
                            int i = 1;
                            while (it.hasNext()) {
                                System.out.println((i++) + ": " + it.next());
                            }
                        }
                    }

                    case 7 -> {
                        // Unión A ∪ B (no destructiva): copia para no tocar 'palabras'
                        // LinkedHashSet en 'union' mantiene orden de inserción:
                        // primero lo ya presente en 'palabras', luego lo nuevo de 'otro'.
                        LinkedHashSet<String> otro = csv(sc, "Ingresa texto separado por comas: ");
                        LinkedHashSet<String> union = new LinkedHashSet<>(palabras);
                        boolean cambio = union.addAll(otro); // true si agregó elementos nuevos
                        System.out.println("Otro:  " + otro);
                        System.out.println("Unión: " + union);
                        System.out.println("¿Se agregaron elementos nuevos?: " + cambio);
                    }

                    case 8 -> {
                        // Intersección A ∩ B (no destructiva): copia de 'palabras'
                        // retainAll deja solo lo que esté también en 'otro'.
                        LinkedHashSet<String> otro = csv(sc, "Ingresa texto separado por comas: ");
                        LinkedHashSet<String> inter = new LinkedHashSet<>(palabras);
                        boolean cambio = inter.retainAll(otro); // true si cambió (se filtraron elementos)
                        System.out.println("Otro:          " + otro);
                        System.out.println("Intersección:  " + inter);
                        System.out.println("¿Cambió?: " + cambio);
                    }

                    case 9 -> {
                        // Diferencia A \ B (no destructiva): copia de 'palabras'
                        // removeAll elimina lo que también esté en 'otro'.
                        LinkedHashSet<String> otro = csv(sc, "Ingresa texto separado por comas: ");
                        LinkedHashSet<String> dif = new LinkedHashSet<>(palabras);
                        boolean cambio = dif.removeAll(otro); // true si removió algún elemento
                        System.out.println("Otro:        " + otro);
                        System.out.println("Diferencia:  " + dif);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                    }

                    case 10 -> {
                        // toArray tipado: evita castings desde Object[]
                        String[] arr = palabras.toArray(String[]::new);
                        System.out.println("Array: " + Arrays.toString(arr));
                    }

                    case 11 -> {
                        // containsAll: ¿'palabras' contiene TODOS los elementos de 'otro'?
                        LinkedHashSet<String> otro = csv(sc, "Ingresa texto separado por comas: ");
                        System.out.println("¿palabras contiene a 'otro'?: " + palabras.containsAll(otro));
                    }

                    case 12 -> {
                        // removeIf(Predicate): elimina todos los que cumplan el predicado
                        String pref = leerLineaNoVacia(sc, "Prefijo a eliminar: ");
                        Predicate<String> pred = s -> s != null && s.startsWith(pref);
                        boolean cambio = palabras.removeIf(pred);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                        System.out.println("Restante: " + palabras);
                    }

                    case 13 -> {
                        // equals / hashCode: igualdad por elementos (el orden es irrelevante para equals)
                        LinkedHashSet<String> otro = csv(sc, "Ingresa texto separado por comas: ");
                        System.out.println("equals?: " + palabras.equals(otro));
                        System.out.println("hashCode(palabras): " + palabras.hashCode());
                        System.out.println("hashCode(otro):     " + otro.hashCode());
                    }

                    case 14 -> {
                        // clone(): copia superficial – nueva instancia, mismos elementos y orden actual
                        @SuppressWarnings("unchecked")
                        LinkedHashSet<String> copia = (LinkedHashSet<String>) palabras.clone();
                        System.out.println("clone(): " + copia);
                        System.out.println("¿Misma instancia? " + (copia == palabras));
                        System.out.println("equals?: " + copia.equals(palabras));
                    }

                    case 15 -> {
                        // Streams: conteo por prefijo + orden natural (alfabético). El set NO cambia.
                        String pref = leerLineaNoVacia(sc, "Prefijo a contar: ");
                        long conteo = palabras.stream()
                                .filter(Objects::nonNull)
                                .filter(s -> s.startsWith(pref))
                                .count();
                        System.out.println("Coincidencias: " + conteo);

                        List<String> ordenNatural = palabras.stream()
                                .filter(Objects::nonNull)
                                .sorted() // alfabético ascendente
                                .toList();
                        System.out.println("Orden natural (sorted): " + ordenNatural);
                    }

                    case 16 -> {
                        // map: transformaciones – MAYÚSCULAS y longitudes (no muta el set)
                        List<String> mayus = palabras.stream()
                                .filter(Objects::nonNull)
                                .map(String::toUpperCase)
                                .toList();
                        System.out.println("MAYÚSCULAS: " + mayus);

                        List<Integer> longitudes = palabras.stream()
                                .filter(Objects::nonNull)
                                .map(String::length)
                                .toList();
                        System.out.println("Longitudes: " + longitudes);
                    }

                    case 17 -> {
                        // sorted con Comparator: reverso y por longitud (desempate alfabético)
                        List<String> reverso = palabras.stream()
                                .filter(Objects::nonNull)
                                .sorted(Comparator.reverseOrder())
                                .toList();
                        System.out.println("Orden inverso: " + reverso);

                        List<String> porLongitud = palabras.stream()
                                .filter(Objects::nonNull)
                                .sorted(
                                        Comparator.comparingInt(String::length)      // 1º: por longitud
                                                .thenComparing(Comparator.naturalOrder()) // 2º: alfabético
                                )
                                .toList();
                        System.out.println("Orden por longitud (y alfabético si empata): " + porLongitud);
                    }

                    case 18 -> {
                        // collect: filtrado→LinkedHashSet (preserva orden) + joining ordenado
                        String pref = leerLineaNoVacia(sc, "Prefijo para filtrar: ");
                        LinkedHashSet<String> filtrado = palabras.stream()
                                .filter(Objects::nonNull)
                                .filter(s -> s.startsWith(pref))
                                .collect(Collectors.toCollection(LinkedHashSet::new));
                        System.out.println("Filtrado→LinkedHashSet (orden preservado): " + filtrado);

                        String unidos = palabras.stream()
                                .filter(Objects::nonNull)
                                .sorted()
                                .collect(Collectors.joining(", "));
                        System.out.println("joining (orden natural): " + unidos);
                    }

                    case 19 -> {
                        // anyMatch / allMatch / noneMatch: cuantificadores lógicos
                        String pref = leerLineaNoVacia(sc, "Prefijo: ");
                        boolean alguno = palabras.stream().filter(Objects::nonNull).anyMatch(s -> s.startsWith(pref));
                        boolean todos  = palabras.stream().filter(Objects::nonNull).allMatch(s -> s.startsWith(pref));
                        boolean ninguno = palabras.stream().filter(Objects::nonNull).noneMatch(s -> s.startsWith(pref));
                        System.out.println("anyMatch: " + alguno + " | allMatch: " + todos + " | noneMatch: " + ninguno);
                    }

                    case 20 -> {
                        // groupingBy (clave = longitud) con LinkedHashMap para ver orden de aparición de claves
                        // toMap que preserva orden de inserción de claves resultantes
                        Map<Integer, List<String>> porLen = palabras.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.groupingBy(
                                        String::length,
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                ));
                        System.out.println("groupingBy(longitud) (orden de aparición): " + porLen);

                        Map<String, Integer> mapa = palabras.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toMap(
                                        s -> s,
                                        String::length,
                                        (a, b) -> a,        // merge (no debería haber colisión en un set, pero requerido por contrato)
                                        LinkedHashMap::new  // preserva orden de inserción de claves
                                ));
                        System.out.println("toMap(palabra → longitud) (orden de inserción): " + mapa);
                    }

                    case 21 -> {
                        // clear: vacía el conjunto
                        palabras.clear();
                        System.out.println("Conjunto limpiado.");
                    }

                    case OPCION_SALIR -> System.out.println("Saliendo...");

                    default -> System.out.println("Ingresa un valor válido.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    // ----------------- Helpers -----------------

    /** Muestra el menú principal. */
    static void mostrarMenu() {
        System.out.println("""
                
                --- MENÚ LINKEDHASHSET (Strings) ---
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
                12 . Borrado condicional por prefijo (removeIf)
                13 . Igualdad/Hash (equals / hashCode)
                14 . Clonar (clone)
                15 . Stream: contar / orden natural
                16 . Stream: map (MAYÚSCULAS / longitudes)
                17 . Stream: sorted (reverso / por longitud)
                18 . Stream: collect (toCollection LinkedHashSet / joining)
                19 . Stream: anyMatch / allMatch / noneMatch
                20 . Stream: groupingBy (LinkedHashMap) / toMap (LinkedHashMap)
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

    /** Lee una línea no vacía (trim aplicada). */
    static String leerLineaNoVacia(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.isBlank()) return s.trim();
            System.out.println("Entrada vacía. Intenta de nuevo.");
        }
    }

    /**
     * Parsea una línea CSV "a,b,c" → LinkedHashSet<String>{"a","b","c"}.
     * - Preserva el **orden de aparición** y elimina duplicados.
     * - Ignora tokens vacíos.
     */
    static LinkedHashSet<String> csv(Scanner sc, String prompt) {
        System.out.print(prompt);
        String csv = sc.nextLine();
        LinkedHashSet<String> set = new LinkedHashSet<>();
        if (csv == null || csv.isBlank()) return set;

        for (String p : csv.split(",")) {
            String t = p.trim();
            if (!t.isEmpty()) set.add(t);
        }
        return set;
    }
}
