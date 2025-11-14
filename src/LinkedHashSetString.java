import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DEMO de LinkedHashSet<String> con menú interactivo.
 *
 * 🔹 Mantiene el orden de inserción (a diferencia de HashSet).
 * 🔹 No admite duplicados.
 * 🔹 Operaciones típicas O(1) promedio (add, remove, contains).
 * 🔹 Unión/intersección/diferencia se hacen sobre copias.
 */
public class LinkedHashSetString {

    public static final int OPCION_SALIR = 22;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            LinkedHashSet<String> palabras = new LinkedHashSet<>();
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

                switch (opcion) {

                    // ====================================================
                    // 🧠 PATRÓN: CONSTRUIR / AGREGAR
                    // ====================================================
                    case 1 -> {
                        String s = leerLineaNoVacia(sc, "Palabra a agregar: ");
                        boolean ok = palabras.add(s);
                        System.out.println(ok ? "✅ Agregado correctamente." : "⚠️ Ya existía (no se repite).");
                    }

                    // ====================================================
                    // 🧠 PATRÓN: ELIMINAR
                    // ====================================================
                    case 2 -> {
                        String v = leerLineaNoVacia(sc, "Palabra a eliminar: ");
                        boolean ok = palabras.remove(v);
                        System.out.println(ok ? "🗑️ Eliminado correctamente." : "❌ No se encuentra en el conjunto.");
                    }

                    // ====================================================
                    // 🧠 PATRÓN: CONSULTAR / ESTADO
                    // ====================================================
                    case 3 -> {
                        String v = leerLineaNoVacia(sc, "Palabra a buscar: ");
                        System.out.println(palabras.contains(v) ? "✅ Encontrada." : "❌ No encontrada.");
                    }
                    case 4 -> {
                        System.out.println("size(): " + palabras.size());
                        System.out.println("isEmpty(): " + palabras.isEmpty());
                    }

                    // ====================================================
                    // 🧠 PATRÓN: RECORRER / LISTAR
                    // ====================================================
                    case 5 -> {
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.println("Listado (orden de inserción):");
                            palabras.forEach(p -> System.out.println("- " + p));
                        }
                    }
                    case 6 -> {
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            Iterator<String> it = palabras.iterator();
                            int i = 1;
                            while (it.hasNext()) System.out.println((i++) + ": " + it.next());
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: TEORÍA DE CONJUNTOS (UNIÓN / INTERSECCIÓN / DIFERENCIA)
                    // ====================================================
                    case 7 -> {
                        LinkedHashSet<String> otro = csv(sc, "Texto separado por comas: ");
                        LinkedHashSet<String> union = new LinkedHashSet<>(palabras);
                        boolean cambio = union.addAll(otro);
                        System.out.println("Otro: " + otro);
                        System.out.println("Unión: " + union);
                        System.out.println("¿Se agregaron elementos nuevos?: " + cambio);
                    }
                    case 8 -> {
                        LinkedHashSet<String> otro = csv(sc, "Texto separado por comas: ");
                        LinkedHashSet<String> inter = new LinkedHashSet<>(palabras);
                        boolean cambio = inter.retainAll(otro);
                        System.out.println("Intersección: " + inter);
                        System.out.println("¿Cambió?: " + cambio);
                    }
                    case 9 -> {
                        LinkedHashSet<String> otro = csv(sc, "Texto separado por comas: ");
                        LinkedHashSet<String> dif = new LinkedHashSet<>(palabras);
                        boolean cambio = dif.removeAll(otro);
                        System.out.println("Diferencia (A\\B): " + dif);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: CONVERSIÓN / UTILIDADES
                    // ====================================================
                    case 10 -> {
                        String[] arr = palabras.toArray(String[]::new);
                        System.out.println("Array: " + Arrays.toString(arr));
                    }
                    case 11 -> {
                        LinkedHashSet<String> otro = csv(sc, "Texto separado por comas: ");
                        System.out.println("¿palabras contiene a 'otro'?: " + palabras.containsAll(otro));
                    }
                    case 13 -> {
                        LinkedHashSet<String> otro = csv(sc, "Texto separado por comas: ");
                        System.out.println("equals?: " + palabras.equals(otro));
                        System.out.println("hashCode(palabras): " + palabras.hashCode());
                        System.out.println("hashCode(otro): " + otro.hashCode());
                    }
                    case 14 -> {
                        @SuppressWarnings("unchecked")
                        LinkedHashSet<String> copia = (LinkedHashSet<String>) palabras.clone();
                        System.out.println("clone(): " + copia);
                        System.out.println("¿Misma instancia? " + (copia == palabras));
                        System.out.println("equals?: " + copia.equals(palabras));
                    }

                    // ====================================================
                    // 🧠 PATRÓN: FILTRAR / ELIMINAR CONDICIONALMENTE
                    // ====================================================
                    case 12 -> {
                        String pref = leerLineaNoVacia(sc, "Prefijo a eliminar: ");
                        Predicate<String> pred = s -> s != null && s.startsWith(pref);
                        boolean cambio = palabras.removeIf(pred);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                        System.out.println("Restante: " + palabras);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: STREAMS / ANÁLISIS (map, sorted, collect, matchers)
                    // ====================================================
                    case 15 -> {
                        String pref = leerLineaNoVacia(sc, "Prefijo a contar: ");
                        long conteo = palabras.stream()
                                .filter(Objects::nonNull)
                                .filter(s -> s.startsWith(pref))
                                .count();
                        System.out.println("Coincidencias: " + conteo);

                        List<String> ordenNatural = palabras.stream()
                                .filter(Objects::nonNull)
                                .sorted()
                                .toList();
                        System.out.println("Orden natural (sorted): " + ordenNatural);
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
                        System.out.println("Orden por longitud (y alfabético): " + porLongitud);
                    }
                    case 18 -> {
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
                        String pref = leerLineaNoVacia(sc, "Prefijo: ");
                        boolean alguno = palabras.stream().anyMatch(s -> s.startsWith(pref));
                        boolean todos = palabras.stream().allMatch(s -> s.startsWith(pref));
                        boolean ninguno = palabras.stream().noneMatch(s -> s.startsWith(pref));
                        System.out.println("anyMatch: " + alguno + " | allMatch: " + todos + " | noneMatch: " + ninguno);
                    }
                    case 20 -> {
                        Map<Integer, List<String>> porLen = palabras.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.groupingBy(
                                        String::length,
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                ));
                        System.out.println("groupingBy(longitud): " + porLen);

                        Map<String, Integer> mapa = palabras.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toMap(
                                        s -> s,
                                        String::length,
                                        (a, b) -> a,
                                        LinkedHashMap::new
                                ));
                        System.out.println("toMap(palabra → longitud): " + mapa);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: LIMPIEZA / RESET
                    // ====================================================
                    case 21 -> {
                        palabras.clear();
                        System.out.println("🧹 Conjunto limpiado.");
                    }

                    // ====================================================
                    // 🏁 SALIR
                    // ====================================================
                    case OPCION_SALIR -> System.out.println("👋 Saliendo...");

                    default -> System.out.println("Ingresa una opción válida.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    // ==========================================================
    // 🔧 UTILIDADES / INPUT HELPERS
    // ==========================================================

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ LINKEDHASHSET (Strings) — AGRUPADO POR PATRONES ---");
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
        System.out.println(" 12 . Borrado condicional por prefijo (FILTRAR)");
        System.out.println(" 13 . Igualdad/Hash (UTILIDADES)");
        System.out.println(" 14 . Clonar clone (UTILIDADES)");
        System.out.println(" 15 . Stream: conteo / orden natural (STREAMS)");
        System.out.println(" 16 . Stream: map (MAYÚSCULAS / longitudes) (STREAMS)");
        System.out.println(" 17 . Stream: sorted (reverso / longitud) (STREAMS)");
        System.out.println(" 18 . Stream: collect (LinkedHashSet / joining) (STREAMS)");
        System.out.println(" 19 . Stream: anyMatch / allMatch / noneMatch (STREAMS)");
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
                System.out.println("Por favor ingresa un número válido.");
            }
        }
    }

    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Valor fuera de rango [" + min + " - " + max + "].");
                continue;
            }
            return n;
        }
    }

    static String leerLineaNoVacia(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.isBlank()) return s.trim();
            System.out.println("Entrada vacía. Intenta de nuevo.");
        }
    }

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
