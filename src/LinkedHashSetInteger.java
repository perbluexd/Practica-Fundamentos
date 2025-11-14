import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DEMO de LinkedHashSet<Integer> con menú interactivo.
 *
 * 🔹 Mantiene el orden de inserción (a diferencia de HashSet).
 * 🔹 No admite duplicados.
 * 🔹 Operaciones típicas O(1) promedio (add, remove, contains).
 * 🔹 Unión / Intersección / Diferencia se hacen sobre copias.
 */
public class LinkedHashSetInteger {

    public static final int OPCION_SALIR = 22;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            LinkedHashSet<Integer> numeros = new LinkedHashSet<>();
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Ingresa la opción que desees: ", 1, OPCION_SALIR);

                switch (opcion) {

                    // ====================================================
                    // 🧠 PATRÓN: CONSTRUIR / AGREGAR
                    // ====================================================
                    case 1 -> {
                        int n = leerEntero(sc, "Número a agregar: ");
                        boolean ok = numeros.add(n);
                        System.out.println(ok ? "✅ Agregado correctamente." : "⚠️ Ya existía (no se repite).");
                    }

                    // ====================================================
                    // 🧠 PATRÓN: ELIMINAR
                    // ====================================================
                    case 2 -> {
                        int v = leerEntero(sc, "Número a eliminar: ");
                        boolean ok = numeros.remove(v);
                        System.out.println(ok ? "🗑️ Eliminado correctamente." : "❌ No se encuentra en el conjunto.");
                    }

                    // ====================================================
                    // 🧠 PATRÓN: CONSULTAR / ESTADO
                    // ====================================================
                    case 3 -> {
                        int v = leerEntero(sc, "Número a buscar: ");
                        System.out.println(numeros.contains(v) ? "✅ Encontrado." : "❌ No encontrado.");
                    }
                    case 4 -> {
                        System.out.println("size(): " + numeros.size());
                        System.out.println("isEmpty(): " + numeros.isEmpty());
                    }

                    // ====================================================
                    // 🧠 PATRÓN: RECORRER / LISTAR
                    // ====================================================
                    case 5 -> {
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.println("Listado (orden de inserción):");
                            numeros.forEach(v -> System.out.println("- " + v));
                        }
                    }
                    case 6 -> {
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            Iterator<Integer> it = numeros.iterator();
                            int i = 1;
                            while (it.hasNext()) System.out.println((i++) + ": " + it.next());
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: TEORÍA DE CONJUNTOS (UNIÓN / INTERSECCIÓN / DIFERENCIA)
                    // ====================================================
                    case 7 -> {
                        LinkedHashSet<Integer> otro = csv(sc, "Números separados por comas: ");
                        LinkedHashSet<Integer> union = new LinkedHashSet<>(numeros);
                        boolean cambio = union.addAll(otro);
                        System.out.println("Otro: " + otro);
                        System.out.println("Unión: " + union);
                        System.out.println("¿Se agregaron elementos nuevos?: " + cambio);
                    }
                    case 8 -> {
                        LinkedHashSet<Integer> otro = csv(sc, "Números separados por comas: ");
                        LinkedHashSet<Integer> inter = new LinkedHashSet<>(numeros);
                        boolean cambio = inter.retainAll(otro);
                        System.out.println("Intersección: " + inter);
                        System.out.println("¿Cambió?: " + cambio);
                    }
                    case 9 -> {
                        LinkedHashSet<Integer> otro = csv(sc, "Números separados por comas: ");
                        LinkedHashSet<Integer> dif = new LinkedHashSet<>(numeros);
                        boolean cambio = dif.removeAll(otro);
                        System.out.println("Diferencia (A\\B): " + dif);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: CONVERSIÓN / UTILIDADES
                    // ====================================================
                    case 10 -> {
                        Integer[] arr = numeros.toArray(Integer[]::new);
                        System.out.println("Array: " + Arrays.toString(arr));
                    }
                    case 11 -> {
                        LinkedHashSet<Integer> otro = csv(sc, "Números separados por comas: ");
                        System.out.println("¿numeros contiene a 'otro'?: " + numeros.containsAll(otro));
                    }
                    case 13 -> {
                        LinkedHashSet<Integer> otro = csv(sc, "Números separados por comas: ");
                        System.out.println("equals?: " + numeros.equals(otro));
                        System.out.println("hashCode(numeros): " + numeros.hashCode());
                        System.out.println("hashCode(otro): " + otro.hashCode());
                    }
                    case 14 -> {
                        @SuppressWarnings("unchecked")
                        LinkedHashSet<Integer> copia = (LinkedHashSet<Integer>) numeros.clone();
                        System.out.println("clone(): " + copia);
                        System.out.println("¿Misma instancia? " + (copia == numeros));
                        System.out.println("equals?: " + copia.equals(numeros));
                    }

                    // ====================================================
                    // 🧠 PATRÓN: FILTRAR / ELIMINAR CONDICIONALMENTE
                    // ====================================================
                    case 12 -> {
                        int limite = leerEntero(sc, "Eliminar números menores a: ");
                        Predicate<Integer> pred = n -> n < limite;
                        boolean cambio = numeros.removeIf(pred);
                        System.out.println("¿Se eliminaron elementos?: " + cambio);
                        System.out.println("Restante: " + numeros);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: STREAMS / ANÁLISIS (MAP, FILTER, SORT, COLLECT)
                    // ====================================================
                    case 15 -> {
                        int limite = leerEntero(sc, "Contar números mayores a: ");
                        long conteo = numeros.stream().filter(n -> n > limite).count();
                        System.out.println("Coincidencias: " + conteo);

                        List<Integer> ordenNatural = numeros.stream().sorted().toList();
                        System.out.println("Orden natural: " + ordenNatural);
                    }
                    case 16 -> {
                        var cuadrados = numeros.stream().map(n -> n * n).toList();
                        System.out.println("Cuadrados: " + cuadrados);

                        var pares = numeros.stream().filter(n -> n % 2 == 0).toList();
                        System.out.println("Pares: " + pares);
                    }
                    case 17 -> {
                        var reverso = numeros.stream().sorted(Comparator.reverseOrder()).toList();
                        System.out.println("Orden inverso: " + reverso);

                        var porAbs = numeros.stream()
                                .sorted(Comparator.comparingInt(Math::abs))
                                .toList();
                        System.out.println("Orden por valor absoluto: " + porAbs);
                    }
                    case 18 -> {
                        int limite = leerEntero(sc, "Filtrar números mayores a: ");
                        LinkedHashSet<Integer> filtrado = numeros.stream()
                                .filter(n -> n > limite)
                                .collect(Collectors.toCollection(LinkedHashSet::new));
                        System.out.println("Filtrado→LinkedHashSet (orden preservado): " + filtrado);

                        String unidos = numeros.stream()
                                .sorted()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "));
                        System.out.println("joining (orden natural): " + unidos);
                    }
                    case 19 -> {
                        int limite = leerEntero(sc, "Ingresa un número límite: ");
                        boolean alguno = numeros.stream().anyMatch(n -> n > limite);
                        boolean todos = numeros.stream().allMatch(n -> n > limite);
                        boolean ninguno = numeros.stream().noneMatch(n -> n > limite);
                        System.out.println("anyMatch: " + alguno + " | allMatch: " + todos + " | noneMatch: " + ninguno);
                    }
                    case 20 -> {
                        Map<Integer, List<Integer>> porResto = numeros.stream()
                                .collect(Collectors.groupingBy(
                                        n -> n % 3,
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                ));
                        System.out.println("groupingBy (n % 3): " + porResto);

                        Map<Integer, Integer> mapa = numeros.stream()
                                .collect(Collectors.toMap(
                                        n -> n,
                                        n -> n * n,
                                        (a, b) -> a,
                                        LinkedHashMap::new
                                ));
                        System.out.println("toMap(numero → cuadrado): " + mapa);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: LIMPIEZA / RESET
                    // ====================================================
                    case 21 -> {
                        numeros.clear();
                        System.out.println("Conjunto limpiado.");
                    }

                    // ====================================================
                    // 🏁 SALIR
                    // ====================================================
                    case OPCION_SALIR -> System.out.println("Saliendo...");

                    default -> System.out.println("Ingresa una opción válida.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    // ==========================================================
    // 🔧 UTILIDADES / INPUT HELPERS
    // ==========================================================
    static void mostrarMenu() {
        System.out.println("\n--- MENÚ LINKEDHASHSET (Integers) — AGRUPADO POR PATRONES ---");
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
        System.out.println(" 12 . Borrado condicional removeIf (FILTRAR)");
        System.out.println(" 13 . Igualdad/Hash (UTILIDADES)");
        System.out.println(" 14 . Clonar clone (UTILIDADES)");
        System.out.println(" 15 . Stream: conteo / orden natural (STREAMS)");
        System.out.println(" 16 . Stream: map (cuadrados / pares) (STREAMS)");
        System.out.println(" 17 . Stream: sorted (reverso / valor abs) (STREAMS)");
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
            }
        }
        return set;
    }
}
