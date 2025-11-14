import java.util.*;
import java.util.stream.Collectors;

/**
 * DEMO DE TreeMap<String, List<Integer>> — AGRUPADO POR PATRONES MENTALES
 *
 * 🔹 Usa NavigableMap (TreeMap) para mantener claves ordenadas naturalmente.
 * 🔹 Integra Streams, subMap, replaceAll, merge y otras operaciones funcionales.
 * 🔹 Estructura agrupada para memorización progresiva.
 */
public class TreeMapMenu {

    private static final int OPCION_SALIR = 30;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NavigableMap<String, List<Integer>> mapa = new TreeMap<>();
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Elige opción: ", 1, OPCION_SALIR);

            switch (opcion) {

                // ============================================================
                // 🧠 PATRÓN 1: INSERCIÓN Y REEMPLAZO DIRECTO
                // ============================================================
                case 1 -> { // agregar un entero (append)
                    String k = leerLinea(sc, "Clave: ");
                    int v = leerEntero(sc, "Valor entero: ");
                    mapa.computeIfAbsent(k, kk -> new ArrayList<>()).add(v);
                    System.out.println("OK. " + k + " -> " + mapa.get(k));
                }

                case 2 -> { // agregar CSV a una clave
                    String k = leerLinea(sc, "Clave: ");
                    String csv = leerLinea(sc, "Valores CSV (ej: 10,12,15): ");
                    List<Integer> vals = parseCSVIntSimple(csv);
                    mapa.computeIfAbsent(k, kk -> new ArrayList<>()).addAll(vals);
                    System.out.println("OK. " + k + " -> " + mapa.get(k));
                }

                case 3 -> { // reemplazar lista completa
                    String k = leerLinea(sc, "Clave: ");
                    String csv = leerLinea(sc, "Nueva lista CSV (ej: 10,12,15): ");
                    List<Integer> vals = parseCSVIntSimple(csv);
                    mapa.put(k, new ArrayList<>(vals));
                    System.out.println("Reemplazado. " + k + " -> " + mapa.get(k));
                }

                // ============================================================
                // 🧠 PATRÓN 2: ELIMINACIÓN Y LIMPIEZA
                // ============================================================
                case 4 -> { // eliminar clave
                    String k = leerLinea(sc, "Clave a eliminar: ");
                    List<Integer> removed = mapa.remove(k);
                    System.out.println(removed != null ? "Eliminado: " + k + " -> " + removed : "No existía la clave.");
                }

                case 5 -> { // eliminar primera ocurrencia
                    String k = leerLinea(sc, "Clave: ");
                    int v = leerEntero(sc, "Valor a eliminar: ");
                    List<Integer> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    boolean ok = list.remove((Integer) v);
                    System.out.println(ok ? "Eliminado " + v + " en " + k + ". Lista: " + list : "Valor no encontrado.");
                }

                case 6 -> { // eliminar todas las ocurrencias
                    String k = leerLinea(sc, "Clave: ");
                    int v = leerEntero(sc, "Valor a eliminar (todas las ocurrencias): ");
                    List<Integer> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    boolean ok = list.removeIf(x -> x == v);
                    System.out.println(ok ? "Eliminadas todas las ocurrencias. Lista: " + list : "No había ocurrencias.");
                }

                case 28 -> { // limpiar mapa completo
                    mapa.clear();
                    System.out.println("Mapa limpiado.");
                }

                // ============================================================
                // 🧠 PATRÓN 3: CONSULTAS BÁSICAS Y VISUALIZACIÓN
                // ============================================================
                case 7 -> {
                    String k = leerLinea(sc, "Clave: ");
                    System.out.println(k + " -> " + mapa.get(k));
                }

                case 8 -> {
                    if (mapa.isEmpty()) {
                        System.out.println("Mapa vacío.");
                    } else {
                        mapa.forEach((k, v) -> System.out.println(k + " -> " + v));
                    }
                }

                case 9 -> { // vistas del mapa
                    System.out.println("Claves: " + mapa.navigableKeySet());
                    System.out.println("Tamaño del mapa: " + mapa.size());
                    int totalValores = mapa.values().stream().mapToInt(List::size).sum();
                    System.out.println("Total de elementos en listas: " + totalValores);
                }

                case 29 -> System.out.println("size=" + mapa.size() + " | isEmpty=" + mapa.isEmpty());

                // ============================================================
                // 🧠 PATRÓN 4: ORDENAMIENTO Y NORMALIZACIÓN
                // ============================================================
                case 10 -> { // ordenar asc/desc
                    String k = leerLinea(sc, "Clave: ");
                    String ord = leerLinea(sc, "Orden (ASC/DESC): ").trim().toUpperCase();
                    List<Integer> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    list.sort(Comparator.naturalOrder());
                    if ("DESC".equals(ord)) Collections.reverse(list);
                    System.out.println("OK. " + k + " -> " + list);
                }

                case 11 -> { // normalizar clave
                    String k = leerLinea(sc, "Clave: ");
                    List<Integer> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    list.sort(Comparator.naturalOrder());
                    List<Integer> norm = list.stream().distinct().collect(Collectors.toList());
                    mapa.put(k, norm);
                    System.out.println("Normalizada. " + k + " -> " + norm);
                }

                case 12 -> { // normalizar todas
                    mapa.replaceAll((key, list) -> {
                        if (list == null) return new ArrayList<>();
                        List<Integer> cp = new ArrayList<>(list);
                        cp.sort(Comparator.naturalOrder());
                        return cp.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                    });
                    System.out.println("Todas las listas normalizadas.");
                }

                // ============================================================
                // 🧠 PATRÓN 5: ESTADÍSTICAS Y ANÁLISIS
                // ============================================================
                case 13 -> { // stats por clave
                    String k = leerLinea(sc, "Clave: ");
                    List<Integer> list = mapa.get(k);
                    if (list == null || list.isEmpty()) { System.out.println("No hay datos."); break; }
                    IntSummaryStatistics stats = list.stream().mapToInt(Integer::intValue).summaryStatistics();
                    System.out.println("count=" + stats.getCount() + ", sum=" + stats.getSum() + ", avg=" + stats.getAverage()
                            + ", min=" + stats.getMin() + ", max=" + stats.getMax());
                }

                case 14 -> { // stats globales
                    List<Integer> all = mapa.values().stream().flatMap(List::stream).toList();
                    if (all.isEmpty()) { System.out.println("No hay datos."); break; }
                    IntSummaryStatistics stats = all.stream().mapToInt(Integer::intValue).summaryStatistics();
                    System.out.println("GLOBAL -> count=" + stats.getCount() + ", sum=" + stats.getSum()
                            + ", avg=" + stats.getAverage() + ", min=" + stats.getMin() + ", max=" + stats.getMax());
                }

                case 15 -> { // Top-N por promedio
                    int n = leerEntero(sc, "¿Top-N por promedio? ");
                    List<Map.Entry<String, Double>> top = mapa.entrySet().stream()
                            .map(e -> Map.entry(e.getKey(),
                                    e.getValue().isEmpty() ? Double.NaN :
                                            e.getValue().stream().mapToInt(Integer::intValue).average().orElse(Double.NaN)))
                            .sorted((a, b) -> {
                                double va = a.getValue(), vb = b.getValue();
                                if (Double.isNaN(va) && Double.isNaN(vb)) return 0;
                                if (Double.isNaN(va)) return 1;
                                if (Double.isNaN(vb)) return -1;
                                return Double.compare(vb, va);
                            })
                            .limit(n)
                            .toList();
                    System.out.println("Top-" + n + " por promedio: " + top);
                }

                case 16 -> { // Top-N por suma
                    int n = leerEntero(sc, "¿Top-N por suma? ");
                    List<Map.Entry<String, Integer>> top = mapa.entrySet().stream()
                            .map(e -> Map.entry(e.getKey(), e.getValue().stream().mapToInt(Integer::intValue).sum()))
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .limit(n)
                            .toList();
                    System.out.println("Top-" + n + " por suma: " + top);
                }

                // ============================================================
                // 🧠 PATRÓN 6: SUBMAPAS Y NAVEGACIÓN ORDENADA
                // ============================================================
                case 17 -> { // submap por prefijo
                    String pref = leerLinea(sc, "Prefijo: ");
                    if (pref == null) pref = "";
                    String hi = pref + '\uffff';
                    SortedMap<String, List<Integer>> sub = mapa.subMap(pref, hi);
                    System.out.println("SubMap por prefijo '" + pref + "': " + sub);
                }

                case 18 -> { // rango min..max con inclusividad
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    String min = leerLinea(sc, "Clave mínima: ");
                    String max = leerLinea(sc, "Clave máxima: ");
                    boolean incMin = leerBoolean(sc, "¿Incluir mínima? (true/false): ");
                    boolean incMax = leerBoolean(sc, "¿Incluir máxima? (true/false): ");
                    NavigableMap<String, List<Integer>> sub = mapa.subMap(min, incMin, max, incMax);
                    System.out.println("subMap " + (incMin ? "[" : "(") + min + " .. " + max + (incMax ? "]" : ")") + ": " + sub);
                }

                case 19 -> { // headMap / tailMap con inclusividad
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    String piv = leerLinea(sc, "Clave pivote: ");
                    boolean inc = leerBoolean(sc, "¿Incluir pivote? (true/false): ");
                    System.out.println("headMap: " + mapa.headMap(piv, inc));
                    System.out.println("tailMap: " + mapa.tailMap(piv, inc));
                }

                case 20 -> { // descendentes
                    System.out.println("descendingMap: " + mapa.descendingMap());
                    System.out.println("descendingKeySet: " + mapa.descendingKeySet());
                }

                case 21 -> { // poll extremos
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    String extremo = leerLinea(sc, "¿'FIRST' o 'LAST'?: ").trim().toUpperCase();
                    Map.Entry<String, List<Integer>> polled = switch (extremo) {
                        case "FIRST" -> mapa.pollFirstEntry();
                        case "LAST"  -> mapa.pollLastEntry();
                        default -> null;
                    };
                    System.out.println("Extraído: " + polled);
                    System.out.println("Mapa ahora: " + mapa);
                }

                // ============================================================
                // 🧠 PATRÓN 7: TRANSFORMACIONES MASIVAS
                // ============================================================
                case 22 -> { // sumar delta
                    int delta = leerEntero(sc, "Delta a sumar a cada elemento: ");
                    mapa.replaceAll((k, list) -> {
                        if (list == null) return new ArrayList<>();
                        List<Integer> out = new ArrayList<>(list.size());
                        for (int x : list) out.add(x + delta);
                        return out;
                    });
                    System.out.println("OK. Listas transformadas (+ " + delta + ").");
                }

                case 23 -> { // multiplicar factor
                    int factor = leerEntero(sc, "Factor multiplicativo: ");
                    mapa.replaceAll((k, list) -> {
                        if (list == null) return new ArrayList<>();
                        List<Integer> out = new ArrayList<>(list.size());
                        for (int x : list) out.add(x * factor);
                        return out;
                    });
                    System.out.println("OK. Listas transformadas (* " + factor + ").");
                }

                // ============================================================
                // 🧠 PATRÓN 8: IMPORTACIÓN / EXPORTACIÓN
                // ============================================================
                case 24 -> { // merge desde CSV
                    String csv = leerLinea(sc, "CSV (ej: ana:10|12|15,bob:20|18): ");
                    NavigableMap<String, List<Integer>> otro = parseCSVMapList(csv);
                    for (var e : otro.entrySet()) {
                        mapa.merge(e.getKey(), e.getValue(), (l1, l2) -> {
                            List<Integer> merged = new ArrayList<>(l1.size() + l2.size());
                            merged.addAll(l1);
                            merged.addAll(l2);
                            return merged;
                        });
                    }
                    System.out.println("Merge OK. Mapa: " + mapa);
                }

                case 25 -> { // reemplazar (putAll)
                    String csv = leerLinea(sc, "CSV (ej: ana:10|12|15,bob:20|18): ");
                    NavigableMap<String, List<Integer>> otro = parseCSVMapList(csv);
                    mapa.putAll(otro);
                    System.out.println("putAll OK. Mapa: " + mapa);
                }

                case 26 -> { // exportar CSV
                    String export = mapa.entrySet().stream()
                            .map(e -> e.getKey() + ":" +
                                    e.getValue().stream().map(String::valueOf).collect(Collectors.joining("|")))
                            .collect(Collectors.joining(","));
                    System.out.println("CSV: " + export);
                }

                // ============================================================
                // 🧠 PATRÓN 9: CONSULTAS BOOLEANAS
                // ============================================================
                case 27 -> {
                    String k = leerLinea(sc, "Clave: ");
                    int v = leerEntero(sc, "Valor a buscar en su lista: ");
                    boolean hasKey = mapa.containsKey(k);
                    boolean hasVal = hasKey && mapa.get(k).contains(v);
                    System.out.println("containsKey? " + hasKey + " | contiene valor? " + hasVal);
                }

                // ============================================================
                // 🧠 PATRÓN 10: SALIDA
                // ============================================================
                case 30 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != OPCION_SALIR);

        sc.close();
    }

    // ============================================================
    // 🧩 HELPERS — Entrada y parseo CSV
    // ============================================================

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ TREEMAP (String -> List<Integer>) ---");
        System.out.println(" 1 . Agregar un entero a clave (append)");
        System.out.println(" 2 . Agregar varios enteros CSV a clave");
        System.out.println(" 3 . Reemplazar lista completa de una clave");
        System.out.println(" 4 . Eliminar clave");
        System.out.println(" 5 . Eliminar primera ocurrencia de valor en clave");
        System.out.println(" 6 . Eliminar TODAS las ocurrencias de valor en clave");
        System.out.println(" 7 . Consultar lista por clave");
        System.out.println(" 8 . Listar todo (orden natural)");
        System.out.println(" 9 . Vistas: keySet/values/entrySet (resumen)");
        System.out.println("10 . Ordenar lista de una clave (ASC/DESC)");
        System.out.println("11 . Normalizar lista de una clave (orden asc + únicos)");
        System.out.println("12 . Normalizar TODAS las listas");
        System.out.println("13 . Estadísticas de una clave");
        System.out.println("14 . Estadísticas globales");
        System.out.println("15 . Top-N por promedio");
        System.out.println("16 . Top-N por suma");
        System.out.println("17 . SubMap por prefijo (truco \\uffff)");
        System.out.println("18 . subMap(min..max) con inclusividad");
        System.out.println("19 . headMap / tailMap con inclusividad");
        System.out.println("20 . Descending map / descendingKeySet");
        System.out.println("21 . pollFirstEntry / pollLastEntry");
        System.out.println("22 . Transformar: sumar delta a TODAS las listas");
        System.out.println("23 . Transformar: multiplicar por factor TODAS las listas");
        System.out.println("24 . Importar y MERGE desde CSV k:v1|v2|...");
        System.out.println("25 . Importar y REEMPLAZAR (putAll) desde CSV");
        System.out.println("26 . Exportar a CSV k:v1|v2|...");
        System.out.println("27 . containsKey / contiene valor en lista de clave");
        System.out.println("28 . Limpiar mapa");
        System.out.println("29 . size / isEmpty");
        System.out.println("30 . Salir");
    }

    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = sc.nextLine();
            try {
                return Integer.parseInt(v.trim());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Intenta de nuevo.");
            }
        }
    }

    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Fuera de rango [" + min + ", " + max + "].");
                continue;
            }
            return n;
        }
    }

    static boolean leerBoolean(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = sc.nextLine().trim().toLowerCase();
            if ("true".equals(v) || "false".equals(v)) return Boolean.parseBoolean(v);
            System.out.println("Ingresa true o false.");
        }
    }

    static String leerLinea(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    // CSV simple: "10,12,15" -> [10,12,15]
    static List<Integer> parseCSVIntSimple(String csv) {
        List<Integer> out = new ArrayList<>();
        if (csv == null || csv.isBlank()) return out;
        for (String t : csv.split(",")) {
            if (t == null) continue;
            String s = t.trim();
            if (s.isEmpty()) continue;
            try {
                out.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido '" + s + "' (ignorado).");
            }
        }
        return out;
    }

    // CSV de mapa: "ana:10|12|15,bob:20|18" -> TreeMap<String,List<Integer>>
    static NavigableMap<String, List<Integer>> parseCSVMapList(String csv) {
        NavigableMap<String, List<Integer>> res = new TreeMap<>();
        if (csv == null || csv.isBlank()) return res;

        for (String token : csv.split(",")) {
            if (token == null || token.isBlank()) continue;
            String[] kv = token.trim().split(":", 2);
            if (kv.length != 2) continue;
            String k = kv[0].trim();
            String vs = kv[1].trim();
            if (k.isEmpty()) continue;

            List<Integer> lista = new ArrayList<>();
            if (!vs.isEmpty()) {
                for (String part : vs.split("\\|")) {
                    String s = part.trim();
                    if (s.isEmpty()) continue;
                    try {
                        lista.add(Integer.parseInt(s));
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido '" + s + "' en clave '" + k + "' (ignorado).");
                    }
                }
            }
            res.put(k, lista);
        }
        return res;
    }
}
