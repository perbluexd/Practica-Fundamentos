import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DEMO de HashSet<String> con menú interactivo y operaciones clásicas + Streams.
 *
 * Puntos clave:
 * - HashSet NO garantiza orden de iteración (orden "arbitrario").
 * - No admite duplicados.
 * - Muchas operaciones son O(1) promedio (add, remove, contains).
 * - En casos 7/8/9 se trabaja con copias para NO modificar el set original.
 */
public class HashSetDemoString {

    // Opción de salida del menú (última opción)
    public static final int opcion_salir = 22;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        // Conjunto base; sin orden garantizado, sin duplicados
        HashSet<String> palabras = new HashSet<>();

        do {
            mostrarMenu();
            // Validamos que la opción esté en [1..opcion_salir]
            opcion = leer_entero(sc, "Ingresa la opción que deseas: ", 1, opcion_salir);

            switch (opcion) {

                case 1 -> {
                    // add: agrega si NO existe. Retorna true si cambió (se agregó), false si ya estaba.
                    String s = leer_linea(sc, "Ingresa la palabra a agregar: ");
                    boolean agregado = palabras.add(s);
                    System.out.println(agregado ? "Agregado correctamente." : "El valor ya existía (duplicado).");
                }

                case 2 -> {
                    // remove(Object): elimina si existe. Retorna true si lo eliminó.
                    String v = leer_linea(sc, "Ingresa la palabra a eliminar: ");
                    boolean eliminado = palabras.remove(v);
                    System.out.println(eliminado ? "Eliminado correctamente." : "No se encuentra en el conjunto.");
                }

                case 3 -> {
                    // contains(Object): pertenencia al set
                    String v = leer_linea(sc, "Ingresa la palabra a buscar: ");
                    boolean esta = palabras.contains(v);
                    System.out.println(esta ? "Encontrado." : "No encontrado.");
                }

                case 4 -> {
                    // size / isEmpty: info básica
                    System.out.println("size(): " + palabras.size());
                    System.out.println(palabras.isEmpty() ? "Está vacío." : "Hay elementos.");
                }

                case 5 -> {
                    // Recorrido for-each (orden NO garantizado)
                    if (palabras.isEmpty()) {
                        System.out.println("Conjunto vacío.");
                    } else {
                        for (String v : palabras) {
                            System.out.println("Elemento: " + v);
                        }
                    }
                }

                case 6 -> {
                    // Recorrido con Iterator (útil si quisieras remove() seguro durante el recorrido)
                    if (palabras.isEmpty()) {
                        System.out.println("Conjunto vacío.");
                    } else {
                        Iterator<String> it = palabras.iterator();
                        int n = 1;
                        while (it.hasNext()) {
                            System.out.println(n++ + ": " + it.next());
                        }
                    }
                }

                case 7 -> {
                    // Unión: A ∪ B (sin duplicados). Trabajamos sobre una copia para NO alterar 'palabras'.
                    Set<String> otro = csv(sc, "Ingresa palabras separadas por comas: ");
                    Set<String> union = new HashSet<>(palabras);
                    boolean cambio = union.addAll(otro); // true si se agregaron elementos nuevos
                    System.out.println("Otro: " + otro);
                    System.out.println("Unión: " + union);
                    System.out.println(cambio ? "Se agregaron nuevos elementos." : "No hubo cambios (todos ya estaban).");
                }

                case 8 -> {
                    // Intersección: A ∩ B (lo común). Trabajamos sobre copia.
                    Set<String> otro = csv(sc, "Ingresa palabras separadas por comas: ");
                    Set<String> inter = new HashSet<>(palabras);
                    boolean cambio = inter.retainAll(otro); // true si se eliminaron elementos ajenos a la intersección
                    System.out.println("Otro: " + otro);
                    System.out.println("Intersección: " + inter);
                    System.out.println(cambio ? "El resultado difiere del original (se filtró a lo común)." : "Sin cambios (ya era subconjunto).");
                }

                case 9 -> {
                    // Diferencia: A \ B (lo que está en A y no en B). Copia para no alterar 'palabras'.
                    Set<String> otro = csv(sc, "Ingresa palabras separadas por comas: ");
                    Set<String> dif = new HashSet<>(palabras);
                    boolean cambio = dif.removeAll(otro); // true si se eliminaron elementos (los que coincidían con B)
                    System.out.println("Otro: " + otro);
                    System.out.println("Diferencia (A\\B): " + dif);
                    System.out.println(cambio ? "Se eliminaron elementos coincidentes con el segundo conjunto." : "No había elementos en común.");
                }

                case 10 -> {
                    // toArray: convierte el set a array tipado String[]
                    String[] arr = palabras.toArray(String[]::new);
                    System.out.println("Array: " + Arrays.toString(arr));
                }

                case 11 -> {
                    // containsAll: ¿palabras contiene TODOS los elementos de 'otro'?
                    Set<String> otro = csv(sc, "Ingresa palabras separadas por comas: ");
                    boolean sub = palabras.containsAll(otro);
                    System.out.println(sub ? "'palabras' contiene a 'otro' (superconjunto)." : "'palabras' NO contiene a 'otro'.");
                }

                case 12 -> {
                    // removeIf(Predicate): elimina todos los que cumplan el predicado. Retorna true si cambió.
                    String pref = leer_linea(sc, "Eliminar elementos que empiecen con el prefijo: ");
                    Predicate<String> pred = s -> s != null && s.startsWith(pref);
                    boolean cambio = palabras.removeIf(pred);
                    System.out.println(cambio ? "Se eliminaron elementos con ese prefijo." : "No se encontraron elementos con ese prefijo.");
                    System.out.println("Restante: " + palabras);
                }

                case 13 -> {
                    // equals / hashCode: igualdad por elementos (orden irrelevante). hashCode consistente con equals.
                    Set<String> otro = csv(sc, "Ingresa palabras separadas por comas: ");
                    System.out.println("¿Son iguales (mismos elementos)? " + palabras.equals(otro));
                    System.out.println("hashCode(palabras): " + palabras.hashCode());
                    System.out.println("hashCode(otro): " + otro.hashCode());
                }

                case 14 -> {
                    // clone(): copia superficial (nueva instancia, mismos elementos). Para String (inmutable), es suficiente.
                    @SuppressWarnings("unchecked")
                    HashSet<String> copia = (HashSet<String>) palabras.clone();
                    System.out.println("clone(): " + copia);
                    System.out.println("¿Misma instancia? " + (copia == palabras));
                    System.out.println("¿Mismos elementos? " + copia.equals(palabras));
                }

                case 15 -> {
                    // Streams: conteo por prefijo + orden natural (ascendente). El set original NO cambia.
                    String pre = leer_linea(sc, "Prefijo a contar: ");
                    long conteo = palabras.stream()
                            .filter(Objects::nonNull)
                            .filter(s -> s.startsWith(pre))
                            .count();
                    System.out.println("Coincidencias: " + conteo);

                    List<String> lista = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted() // orden natural (alfabético)
                            .toList();
                    System.out.println("Ordenadas (ASC): " + lista);
                }

                case 16 -> {
                    // map: transformaciones (a MAYÚSCULAS y a longitudes). NO cambia el set original.
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
                    // sorted con Comparator: reverso y por longitud (con desempate lexicográfico)
                    List<String> reverso = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted(Comparator.reverseOrder())
                            .toList();
                    System.out.println("Orden inverso (DESC): " + reverso);

                    List<String> porLongitud = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted(
                                    Comparator
                                            .comparingInt(String::length)     // 1º: longitud
                                            .thenComparing(Comparator.naturalOrder()) // 2º: alfabético
                            )
                            .toList();
                    System.out.println("Orden por longitud (y alfabético si empata): " + porLongitud);
                }

                case 18 -> {
                    // collect: filtrado a Set + joining a String (ordenado)
                    String pref = leer_linea(sc, "Prefijo para filtrar: ");
                    Set<String> filtrado = palabras.stream()
                            .filter(Objects::nonNull)
                            .filter(s -> s.startsWith(pref))
                            .collect(Collectors.toSet());
                    System.out.println("Filtrado→Set: " + filtrado);

                    String unidos = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted()
                            .collect(Collectors.joining(","));
                    System.out.println("Joining (ordenado, ','): " + unidos);
                }

                case 19 -> {
                    // anyMatch / allMatch / noneMatch: cuantificadores lógicos sobre el stream
                    String pref = leer_linea(sc, "Prefijo a evaluar: ");
                    boolean alguno = palabras.stream().filter(Objects::nonNull).anyMatch(s -> s.startsWith(pref));
                    boolean todos  = palabras.stream().filter(Objects::nonNull).allMatch(s -> s.startsWith(pref));
                    boolean ninguno = palabras.stream().filter(Objects::nonNull).noneMatch(s -> s.startsWith(pref));
                    System.out.println("Alguno: " + alguno + " | Todos: " + todos + " | Ninguno: " + ninguno);
                }

                case 20 -> {
                    // groupingBy / toMap: agrupación por longitud y diccionario (palabra->longitud)
                    Map<Integer, List<String>> porLen = palabras.stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.groupingBy(String::length));
                    System.out.println("groupingBy(longitud): " + porLen);

                    Map<String, Integer> mapa2 = palabras.stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toMap(s -> s, String::length));
                    System.out.println("toMap(palabra -> longitud): " + mapa2);
                }

                case 21 -> {
                    // clear: vacía el conjunto
                    System.out.println("Limpiando conjunto...");
                    palabras.clear();
                    System.out.println("Conjunto vacío.");
                }

                case opcion_salir -> {
                    System.out.println("Saliendo...");
                }

                default -> System.out.println("Ingresa una opción válida.");
            }

        } while (opcion != opcion_salir);

        sc.close();
    }

    // ======================
    // Helpers de interacción
    // ======================

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ HASHSET (V3 Streams+) ---");
        System.out.println("1  . Agregar (add)");
        System.out.println("2  . Eliminar por valor (remove)");
        System.out.println("3  . ¿Contiene? (contains)");
        System.out.println("4  . Tamaño/Vacío (size / isEmpty)");
        System.out.println("5  . Listar (for-each)");
        System.out.println("6  . Listar con Iterator");
        System.out.println("7  . Unión (addAll) [copia]");
        System.out.println("8  . Intersección (retainAll) [copia]");
        System.out.println("9  . Diferencia (removeAll) [copia]");
        System.out.println("10 . Convertir a array (toArray)");
        System.out.println("11 . Subconjunto (containsAll)");
        System.out.println("12 . Borrado condicional (removeIf)");
        System.out.println("13 . Igualdad/Hash (equals / hashCode)");
        System.out.println("14 . Clonar (clone)");
        System.out.println("15 . Stream: conteo / orden natural");
        System.out.println("16 . Stream: map (MAYÚSCULAS / longitudes)");
        System.out.println("17 . Stream: sorted (reverso / por longitud)");
        System.out.println("18 . Stream: collect (toSet / joining)");
        System.out.println("19 . Stream: anyMatch / allMatch / noneMatch");
        System.out.println("20 . Stream: groupingBy / toMap");
        System.out.println("21 . Limpiar (clear)");
        System.out.println("22 . Salir");
    }

    /** Lee un entero desde consola (reintenta hasta que sea válido). */
    static int leer_entero(Scanner sc, String promt) {
        while (true) {
            System.out.println(promt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un número válido.");
            }
        }
    }

    /** Lee un entero validado en rango [min..max]. */
    static int leer_entero(Scanner sc, String promt, int min, int max) {
        while (true) {
            int n = leer_entero(sc, promt);
            if (n < min || n > max) {
                System.out.println("Valor fuera de rango [" + min + ".." + max + "], intenta de nuevo.");
                continue;
            }
            return n;
        }
    }

    /** Lee una línea completa (puede contener espacios). */
    static String leer_linea(Scanner sc, String promt) {
        System.out.println(promt);
        return sc.nextLine();
    }

    /**
     * Parsea una línea CSV de palabras "a,b,c" -> Set<String>{"a","b","c"}.
     * - Ignora tokens vacíos.
     * - Aplica trim() a cada token.
     */
    static Set<String> csv(Scanner sc, String promt) {
        String csv = leer_linea(sc, promt);
        Set<String> v = new HashSet<>();
        if (csv == null || csv.isBlank()) return v;

        for (String s : csv.split(",")) {
            if (s != null) {
                String paso = s.trim();
                if (!paso.isEmpty()) {
                    v.add(paso);
                }
            }
        }
        return v;
    }
}
