import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DEMO de HashSet<String> con menú interactivo y operaciones clásicas + Streams.
 *
 * HashSet NO garantiza orden de iteración.
 * No admite duplicados.
 * add/remove/contains son O(1) promedio.
 */
public class HashSetDemoString {

    public static final int OPCION_SALIR = 22;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        HashSet<String> palabras = new HashSet<>();

        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

            switch (opcion) {

                // ====================================================
                // 🧠 PATRÓN: CONSTRUIR / AGREGAR
                // ====================================================
                case 1 -> {
                    String s = leerLinea(sc, "Palabra a agregar: ");
                    boolean agregado = palabras.add(s);
                    System.out.println(agregado ? "Agregado correctamente." : "Ya existía (duplicado).");
                }

                // ====================================================
                // 🧠 PATRÓN: ELIMINAR
                // ====================================================
                case 2 -> {
                    String v = leerLinea(sc, "Palabra a eliminar: ");
                    boolean eliminado = palabras.remove(v);
                    System.out.println(eliminado ? "Eliminado correctamente." : "No se encuentra en el conjunto.");
                }

                // ====================================================
                // 🧠 PATRÓN: CONSULTAR / ESTADO
                // ====================================================
                case 3 -> {
                    String v = leerLinea(sc, "Palabra a buscar: ");
                    System.out.println(palabras.contains(v) ? "Encontrado." : "No encontrado.");
                }
                case 4 -> {
                    System.out.println("size(): " + palabras.size());
                    System.out.println(palabras.isEmpty() ? "Está vacío." : "Hay elementos.");
                }

                // ====================================================
                // 🧠 PATRÓN: RECORRER / LISTAR
                // ====================================================
                case 5 -> {
                    if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                    else palabras.forEach(p -> System.out.println("Elemento: " + p));
                }
                case 6 -> {
                    if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                    else {
                        Iterator<String> it = palabras.iterator();
                        int n = 1;
                        while (it.hasNext()) System.out.println(n++ + ": " + it.next());
                    }
                }

                // ====================================================
                // 🧠 PATRÓN: TEORÍA DE CONJUNTOS (UNIÓN / INTERSECCIÓN / DIFERENCIA)
                // ====================================================
                case 7 -> {
                    Set<String> otro = csv(sc, "Palabras separadas por comas: ");
                    Set<String> union = new HashSet<>(palabras);
                    boolean cambio = union.addAll(otro);
                    System.out.println("Otro: " + otro);
                    System.out.println("Unión: " + union);
                    System.out.println(cambio ? "Se agregaron nuevos elementos." : "No hubo cambios.");
                }
                case 8 -> {
                    Set<String> otro = csv(sc, "Palabras separadas por comas: ");
                    Set<String> inter = new HashSet<>(palabras);
                    boolean cambio = inter.retainAll(otro);
                    System.out.println("Intersección: " + inter);
                    System.out.println(cambio ? "Filtrado a lo común." : "Sin cambios (ya era subconjunto).");
                }
                case 9 -> {
                    Set<String> otro = csv(sc, "Palabras separadas por comas: ");
                    Set<String> dif = new HashSet<>(palabras);
                    boolean cambio = dif.removeAll(otro);
                    System.out.println("Diferencia (A\\B): " + dif);
                    System.out.println(cambio ? "Se eliminaron coincidencias." : "No había elementos comunes.");
                }

                // ====================================================
                // 🧠 PATRÓN: CONVERSIÓN / UTILIDADES
                // ====================================================
                case 10 -> {
                    String[] arr = palabras.toArray(String[]::new);
                    System.out.println("Array: " + Arrays.toString(arr));
                }
                case 11 -> {
                    Set<String> otro = csv(sc, "Palabras separadas por comas: ");
                    System.out.println(palabras.containsAll(otro)
                            ? "'palabras' contiene a 'otro'."
                            : "'palabras' NO contiene a 'otro'.");
                }
                case 13 -> {
                    Set<String> otro = csv(sc, "Palabras separadas por comas: ");
                    System.out.println("¿Iguales? " + palabras.equals(otro));
                    System.out.println("hashCode(palabras): " + palabras.hashCode());
                    System.out.println("hashCode(otro): " + otro.hashCode());
                }
                case 14 -> {
                    @SuppressWarnings("unchecked")
                    HashSet<String> copia = (HashSet<String>) palabras.clone();
                    System.out.println("clone(): " + copia);
                    System.out.println("¿Misma instancia? " + (copia == palabras));
                    System.out.println("¿Mismos elementos? " + copia.equals(palabras));
                }

                // ====================================================
                // 🧠 PATRÓN: FILTRAR / TRANSFORMAR EN BLOQUE
                // ====================================================
                case 12 -> {
                    String pref = leerLinea(sc, "Eliminar elementos que empiecen con: ");
                    Predicate<String> pred = s -> s != null && s.startsWith(pref);
                    boolean cambio = palabras.removeIf(pred);
                    System.out.println(cambio ? "Se eliminaron elementos." : "No se encontraron coincidencias.");
                    System.out.println("Restante: " + palabras);
                }

                // ====================================================
                // 🧠 PATRÓN: STREAMS / ANÁLISIS (map, sorted, collect, matchers)
                // ====================================================
                case 15 -> {
                    String pre = leerLinea(sc, "Prefijo a contar: ");
                    long conteo = palabras.stream()
                            .filter(Objects::nonNull)
                            .filter(s -> s.startsWith(pre))
                            .count();
                    System.out.println("Coincidencias: " + conteo);

                    List<String> lista = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted()
                            .toList();
                    System.out.println("Ordenadas (ASC): " + lista);
                }
                case 16 -> {
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
                    List<String> reverso = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted(Comparator.reverseOrder())
                            .toList();
                    System.out.println("Orden inverso: " + reverso);

                    List<String> porLongitud = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted(
                                    Comparator
                                            .comparingInt(String::length)
                                            .thenComparing(Comparator.naturalOrder())
                            )
                            .toList();
                    System.out.println("Por longitud (y alfabético): " + porLongitud);
                }
                case 18 -> {
                    String pref = leerLinea(sc, "Prefijo para filtrar: ");
                    Set<String> filtrado = palabras.stream()
                            .filter(Objects::nonNull)
                            .filter(s -> s.startsWith(pref))
                            .collect(Collectors.toSet());
                    System.out.println("Filtrado→Set: " + filtrado);

                    String unidos = palabras.stream()
                            .filter(Objects::nonNull)
                            .sorted()
                            .collect(Collectors.joining(","));
                    System.out.println("Joining: " + unidos);
                }
                case 19 -> {
                    String pref = leerLinea(sc, "Prefijo a evaluar: ");
                    boolean alguno = palabras.stream().anyMatch(s -> s.startsWith(pref));
                    boolean todos = palabras.stream().allMatch(s -> s.startsWith(pref));
                    boolean ninguno = palabras.stream().noneMatch(s -> s.startsWith(pref));
                    System.out.println("Alguno: " + alguno + " | Todos: " + todos + " | Ninguno: " + ninguno);
                }
                case 20 -> {
                    Map<Integer, List<String>> porLen = palabras.stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.groupingBy(String::length));
                    System.out.println("groupingBy(longitud): " + porLen);

                    Map<String, Integer> mapa2 = palabras.stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toMap(s -> s, String::length));
                    System.out.println("toMap(palabra->longitud): " + mapa2);
                }

                // ====================================================
                // 🧠 PATRÓN: LIMPIEZA / RESET
                // ====================================================
                case 21 -> {
                    palabras.clear();
                    System.out.println("Conjunto limpiado.");
                }

                // ====================================================
                // 🏁 SALIR
                // ====================================================
                case OPCION_SALIR -> System.out.println("Saliendo...");

                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != OPCION_SALIR);
        sc.close();
    }

    // ======================
    // 🔧 UTILIDADES DE ENTRADA
    // ======================

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ HASHSET (STRINGS) — AGRUPADO POR PATRONES ---");
        System.out.println(" 1  . Agregar (CONSTRUIR/AGREGAR)");
        System.out.println(" 2  . Eliminar (ELIMINAR)");
        System.out.println(" 3  . Buscar (CONSULTAR)");
        System.out.println(" 4  . Tamaño/Vacío (CONSULTAR)");
        System.out.println(" 5  . Listar for-each (RECORRER)");
        System.out.println(" 6  . Listar con Iterator (RECORRER)");
        System.out.println(" 7  . Unión (CONJUNTOS)");
        System.out.println(" 8  . Intersección (CONJUNTOS)");
        System.out.println(" 9  . Diferencia (CONJUNTOS)");
        System.out.println(" 10 . Convertir a array (UTILIDADES)");
        System.out.println(" 11 . Subconjunto containsAll (UTILIDADES)");
        System.out.println(" 12 . Borrado condicional removeIf (FILTRAR/BLOQUE)");
        System.out.println(" 13 . Igualdad/Hash (UTILIDADES)");
        System.out.println(" 14 . Clonar clone (UTILIDADES)");
        System.out.println(" 15 . Stream: conteo / orden natural (STREAMS)");
        System.out.println(" 16 . Stream: map (MAYÚSCULAS / longitudes) (STREAMS)");
        System.out.println(" 17 . Stream: sorted (reverso / por longitud) (STREAMS)");
        System.out.println(" 18 . Stream: collect (toSet / joining) (STREAMS)");
        System.out.println(" 19 . Stream: any/all/noneMatch (STREAMS)");
        System.out.println(" 20 . Stream: groupingBy / toMap (STREAMS)");
        System.out.println(" 21 . Limpiar (LIMPIEZA)");
        System.out.println(" 22 . Salir");
    }

    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, intenta de nuevo.");
            }
        }
    }

    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Fuera de rango [" + min + " .. " + max + "].");
                continue;
            }
            return n;
        }
    }

    static String leerLinea(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    static Set<String> csv(Scanner sc, String prompt) {
        String csv = leerLinea(sc, prompt);
        Set<String> v = new HashSet<>();
        if (csv == null || csv.isBlank()) return v;
        for (String s : csv.split(",")) {
            if (s != null) {
                String paso = s.trim();
                if (!paso.isEmpty()) v.add(paso);
            }
        }
        return v;
    }
}
