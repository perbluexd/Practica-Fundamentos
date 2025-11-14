import java.util.*;
import java.util.stream.Collectors;

public class TreeMapMenuString {

    private static final int OPCION_SALIR = 30;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NavigableMap<Integer, List<String>> mapa = new TreeMap<>();
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Elige opción: ", 1, OPCION_SALIR);

            switch (opcion) {

                // ============================================================
                // 🧠 PATRÓN 1: INSERCIÓN Y REEMPLAZO
                // ============================================================
                case 1 -> { // append a una clave
                    int k = leerEntero(sc, "Clave (entero): ");
                    String v = leerLinea(sc, "Valor (string): ");
                    mapa.computeIfAbsent(k, kk -> new ArrayList<>()).add(v);
                    System.out.println("OK. " + k + " -> " + mapa.get(k));
                }

                case 2 -> { // append CSV
                    int k = leerEntero(sc, "Clave (entero): ");
                    String csv = leerLinea(sc, "Valores CSV (ej: a,b,c): ");
                    List<String> vals = parseCSVStrSimple(csv);
                    mapa.computeIfAbsent(k, kk -> new ArrayList<>()).addAll(vals);
                    System.out.println("OK. " + k + " -> " + mapa.get(k));
                }

                case 3 -> { // reemplazar lista completa
                    int k = leerEntero(sc, "Clave (entero): ");
                    String csv = leerLinea(sc, "Nueva lista CSV (ej: a,b,c): ");
                    List<String> vals = parseCSVStrSimple(csv);
                    mapa.put(k, new ArrayList<>(vals));
                    System.out.println("Reemplazado. " + k + " -> " + mapa.get(k));
                }

                // ============================================================
                // 🧠 PATRÓN 2: ELIMINACIÓN Y LIMPIEZA
                // ============================================================
                case 4 -> { // eliminar clave
                    int k = leerEntero(sc, "Clave a eliminar (entero): ");
                    List<String> removed = mapa.remove(k);
                    System.out.println(removed != null ? "Eliminado: " + k + " -> " + removed : "No existía la clave.");
                }

                case 5 -> { // eliminar primera ocurrencia en clave
                    int k = leerEntero(sc, "Clave (entero): ");
                    String v = leerLinea(sc, "Valor a eliminar (primera ocurrencia): ");
                    List<String> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    boolean ok = list.remove(v);
                    System.out.println(ok ? "Eliminado \"" + v + "\" en " + k + ". Lista: " + list : "Valor no encontrado.");
                }

                case 6 -> { // eliminar todas las ocurrencias en clave
                    int k = leerEntero(sc, "Clave (entero): ");
                    String v = leerLinea(sc, "Valor a eliminar (todas las ocurrencias): ");
                    List<String> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    boolean ok = list.removeIf(x -> Objects.equals(x, v));
                    System.out.println(ok ? "Eliminadas todas las ocurrencias. Lista: " + list : "No había ocurrencias.");
                }

                case 28 -> { // limpiar mapa completo
                    mapa.clear();
                    System.out.println("Mapa limpiado.");
                }

                // ============================================================
                // 🧠 PATRÓN 3: CONSULTAS Y VISTAS
                // ============================================================
                case 7 -> { // obtener lista por clave
                    int k = leerEntero(sc, "Clave (entero): ");
                    System.out.println(k + " -> " + mapa.get(k));
                }

                case 8 -> { // listar todo
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    mapa.forEach((k, v) -> System.out.println(k + " -> " + v));
                }

                case 9 -> { // vistas / tamaños
                    System.out.println("Claves: " + mapa.navigableKeySet());
                    System.out.println("Tamaño del mapa: " + mapa.size());
                    int totalValores = mapa.values().stream().mapToInt(List::size).sum();
                    System.out.println("Total de strings en listas: " + totalValores);
                }

                case 29 -> { // size / isEmpty rápido
                    System.out.println("size=" + mapa.size() + " | isEmpty=" + mapa.isEmpty());
                }

                // ============================================================
                // 🧠 PATRÓN 4: ORDENAMIENTO Y NORMALIZACIÓN
                // ============================================================
                case 10 -> { // ordenar lista de una clave (ASC/DESC)
                    int k = leerEntero(sc, "Clave (entero): ");
                    String ord = leerLinea(sc, "Orden (ASC/DESC): ").trim().toUpperCase();
                    List<String> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    list.sort(Comparator.naturalOrder());
                    if ("DESC".equals(ord)) Collections.reverse(list);
                    System.out.println("OK. " + k + " -> " + list);
                }

                case 11 -> { // normalizar una clave (orden + únicos)
                    int k = leerEntero(sc, "Clave (entero): ");
                    List<String> list = mapa.get(k);
                    if (list == null) { System.out.println("No existe la clave."); break; }
                    list.sort(Comparator.naturalOrder());
                    List<String> norm = list.stream().distinct().collect(Collectors.toList());
                    mapa.put(k, norm);
                    System.out.println("Normalizada. " + k + " -> " + norm);
                }

                case 12 -> { // normalizar todas (orden + únicos)
                    mapa.replaceAll((kk, list) -> {
                        if (list == null) return new ArrayList<>();
                        List<String> cp = new ArrayList<>(list);
                        cp.sort(Comparator.naturalOrder());
                        return cp.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                    });
                    System.out.println("Todas las listas normalizadas.");
                }

                // ============================================================
                // 🧠 PATRÓN 5: ESTADÍSTICAS Y TOP-N
                // ============================================================
                case 13 -> { // stats por clave (longitudes)
                    int k = leerEntero(sc, "Clave (entero): ");
                    List<String> list = mapa.get(k);
                    if (list == null || list.isEmpty()) { System.out.println("No hay datos."); break; }
                    IntSummaryStatistics stats = list.stream().mapToInt(s -> s == null ? 0 : s.length()).summaryStatistics();
                    System.out.println("count=" + stats.getCount() + ", total_len=" + stats.getSum() + ", avg_len=" + stats.getAverage()
                            + ", min_len=" + stats.getMin() + ", max_len=" + stats.getMax());
                }

                case 14 -> { // stats globales (longitudes)
                    List<String> all = mapa.values().stream().flatMap(List::stream).toList();
                    if (all.isEmpty()) { System.out.println("No hay datos."); break; }
                    IntSummaryStatistics stats = all.stream().mapToInt(s -> s == null ? 0 : s.length()).summaryStatistics();
                    System.out.println("GLOBAL -> count=" + stats.getCount() + ", total_len=" + stats.getSum()
                            + ", avg_len=" + stats.getAverage() + ", min_len=" + stats.getMin() + ", max_len=" + stats.getMax());
                }

                case 15 -> { // Top-N por tamaño de lista
                    int n = leerEntero(sc, "¿Top-N por tamaño de lista? ");
                    List<Map.Entry<Integer, Integer>> top = mapa.entrySet().stream()
                            .map(e -> Map.entry(e.getKey(), e.getValue().size()))
                            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                            .limit(n)
                            .toList();
                    System.out.println("Top-" + n + " por tamaño de lista: " + top);
                }

                case 16 -> { // Top-N por promedio de longitud
                    int n = leerEntero(sc, "¿Top-N por promedio de longitud? ");
                    List<Map.Entry<Integer, Double>> top = mapa.entrySet().stream()
                            .map(e -> {
                                List<String> lst = e.getValue();
                                double avg = lst.isEmpty() ? Double.NaN :
                                        lst.stream().mapToInt(s -> s == null ? 0 : s.length()).average().orElse(Double.NaN);
                                return Map.entry(e.getKey(), avg);
                            })
                            .sorted((a, b) -> {
                                double va = a.getValue(), vb = b.getValue();
                                if (Double.isNaN(va) && Double.isNaN(vb)) return 0;
                                if (Double.isNaN(va)) return 1;
                                if (Double.isNaN(vb)) return -1;
                                return Double.compare(vb, va); // desc
                            })
                            .limit(n)
                            .toList();
                    System.out.println("Top-" + n + " por promedio de longitud: " + top);
                }

                // ============================================================
                // 🧠 PATRÓN 6: SUBMAPAS Y NAVEGACIÓN ORDENADA
                // ============================================================
                case 17 -> { // subMap(min..max) con inclusividad
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    int min = leerEntero(sc, "Clave mínima (entero): ");
                    int max = leerEntero(sc, "Clave máxima (entero): ");
                    boolean incMin = leerBoolean(sc, "¿Incluir mínima? (true/false): ");
                    boolean incMax = leerBoolean(sc, "¿Incluir máxima? (true/false): ");
                    NavigableMap<Integer, List<String>> sub = mapa.subMap(min, incMin, max, incMax);
                    System.out.println("subMap " + (incMin ? "[" : "(") + min + " .. " + max + (incMax ? "]" : ")") + ": " + sub);
                }

                case 18 -> { // headMap / tailMap con inclusividad
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    int piv = leerEntero(sc, "Clave pivote (entero): ");
                    boolean inc = leerBoolean(sc, "¿Incluir pivote? (true/false): ");
                    System.out.println("headMap: " + mapa.headMap(piv, inc));
                    System.out.println("tailMap: " + mapa.tailMap(piv, inc));
                }

                case 19 -> { // vistas descendentes
                    System.out.println("descendingMap: " + mapa.descendingMap());
                    System.out.println("descendingKeySet: " + mapa.descendingKeySet());
                }

                case 20 -> { // poll extremos
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    String extremo = leerLinea(sc, "¿'FIRST' o 'LAST'?: ").trim().toUpperCase();
                    Map.Entry<Integer, List<String>> polled = switch (extremo) {
                        case "FIRST" -> mapa.pollFirstEntry();
                        case "LAST"  -> mapa.pollLastEntry();
                        default -> null;
                    };
                    System.out.println("Extraído: " + polled);
                    System.out.println("Mapa ahora: " + mapa);
                }

                // ============================================================
                // 🧠 PATRÓN 7: TRANSFORMACIONES MASIVAS (STRING→STRING)
                // ============================================================
                case 21 -> { // uppercase todas las listas
                    mapa.replaceAll((k, list) -> mapStrings(list, s -> s == null ? null : s.toUpperCase()));
                    System.out.println("OK. Transformación a MAYÚSCULAS aplicada.");
                }

                case 22 -> { // lowercase todas las listas
                    mapa.replaceAll((k, list) -> mapStrings(list, s -> s == null ? null : s.toLowerCase()));
                    System.out.println("OK. Transformación a minúsculas aplicada.");
                }

                case 23 -> { // trim todas las listas
                    mapa.replaceAll((k, list) -> mapStrings(list, s -> s == null ? null : s.trim()));
                    System.out.println("OK. Trim aplicado.");
                }

                // ============================================================
                // 🧠 PATRÓN 8: IMPORTACIÓN / EXPORTACIÓN
                // ============================================================
                case 24 -> { // MERGE desde CSV k:v1|v2|...
                    String csv = leerLinea(sc, "CSV (ej: 2024:a|b|c,2025:x|y): ");
                    NavigableMap<Integer, List<String>> otro = parseCSVMapList(csv);
                    for (var e : otro.entrySet()) {
                        mapa.merge(e.getKey(), e.getValue(), (l1, l2) -> {
                            List<String> merged = new ArrayList<>(l1.size() + l2.size());
                            merged.addAll(l1);
                            merged.addAll(l2);
                            return merged;
                        });
                    }
                    System.out.println("Merge OK. Mapa: " + mapa);
                }

                case 25 -> { // REEMPLAZAR (putAll) desde CSV
                    String csv = leerLinea(sc, "CSV (ej: 2024:a|b|c,2025:x|y): ");
                    NavigableMap<Integer, List<String>> otro = parseCSVMapList(csv);
                    mapa.putAll(otro);
                    System.out.println("putAll OK. Mapa: " + mapa);
                }

                case 26 -> { // exportar a CSV
                    String export = mapa.entrySet().stream()
                            .map(e -> e.getKey() + ":" +
                                    e.getValue().stream().map(s -> s == null ? "null" : s).collect(Collectors.joining("|")))
                            .collect(Collectors.joining(","));
                    System.out.println("CSV: " + export);
                }

                // ============================================================
                // 🧠 PATRÓN 9: CONSULTAS BOOLEANAS
                // ============================================================
                case 27 -> { // containsKey / contiene valor
                    int k = leerEntero(sc, "Clave (entero): ");
                    String v = leerLinea(sc, "Valor (string) a buscar en su lista: ");
                    boolean hasKey = mapa.containsKey(k);
                    boolean hasVal = hasKey && mapa.get(k).contains(v);
                    System.out.println("containsKey? " + hasKey + " | contiene valor? " + hasVal);
                }

                // ============================================================
                // 🧠 PATRÓN 10: SALIR
                // ============================================================
                case 30 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != OPCION_SALIR);

        sc.close();
    }

    // =======================
    // Helpers de I/O y parse
    // =======================

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ TREEMAP (Integer -> List<String>) ---");
        System.out.println(" 1 . Agregar un string a clave (append)");
        System.out.println(" 2 . Agregar varios strings CSV a clave");
        System.out.println(" 3 . Reemplazar lista completa de una clave");
        System.out.println(" 4 . Eliminar clave");
        System.out.println(" 5 . Eliminar primera ocurrencia de valor en clave");
        System.out.println(" 6 . Eliminar TODAS las ocurrencias de valor en clave");
        System.out.println(" 7 . Consultar lista por clave");
        System.out.println(" 8 . Listar todo (orden natural)");
        System.out.println(" 9 . Vistas: keySet/size/total elementos");
        System.out.println("10 . Ordenar lista de una clave (ASC/DESC)");
        System.out.println("11 . Normalizar lista de una clave (orden asc + únicos)");
        System.out.println("12 . Normalizar TODAS las listas");
        System.out.println("13 . Estadísticas de una clave (longitud de strings)");
        System.out.println("14 . Estadísticas globales (longitud de strings)");
        System.out.println("15 . Top-N por tamaño de lista");
        System.out.println("16 . Top-N por promedio de longitud");
        System.out.println("17 . subMap(min..max) con inclusividad");
        System.out.println("18 . headMap / tailMap con inclusividad");
        System.out.println("19 . Descending map / descendingKeySet");
        System.out.println("20 . pollFirstEntry / pollLastEntry");
        System.out.println("21 . Transformar: MAYÚSCULAS todas las listas");
        System.out.println("22 . Transformar: minúsculas todas las listas");
        System.out.println("23 . Transformar: trim todas las listas");
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

    // CSV simple: "a,b,c" -> ["a","b","c"]
    static List<String> parseCSVStrSimple(String csv) {
        List<String> out = new ArrayList<>();
        if (csv == null || csv.isBlank()) return out;
        for (String t : csv.split(",")) {
            if (t == null) continue;
            String s = t.trim();
            if (s.isEmpty()) continue;
            out.add(s);
        }
        return out;
    }

    // CSV de mapa: "2024:a|b|c,2025:x|y" -> TreeMap<Integer,List<String>>
    static NavigableMap<Integer, List<String>> parseCSVMapList(String csv) {
        NavigableMap<Integer, List<String>> res = new TreeMap<>();
        if (csv == null || csv.isBlank()) return res;

        for (String token : csv.split(",")) {
            if (token == null || token.isBlank()) continue;
            String[] kv = token.trim().split(":", 2);
            if (kv.length != 2) continue;

            String kraw = kv[0].trim();
            String vs = kv[1].trim();
            if (kraw.isEmpty()) continue;

            try {
                int k = Integer.parseInt(kraw);
                List<String> lista = new ArrayList<>();
                if (!vs.isEmpty()) {
                    for (String part : vs.split("\\|")) {
                        String s = part.trim();
                        if (!s.isEmpty()) lista.add(s);
                    }
                }
                res.put(k, lista);
            } catch (NumberFormatException e) {
                System.out.println("Clave inválida '" + kraw + "' (ignorada).");
            }
        }
        return res;
    }

    static List<String> mapStrings(List<String> list, java.util.function.UnaryOperator<String> f) {
        if (list == null) return new ArrayList<>();
        List<String> out = new ArrayList<>(list.size());
        for (String s : list) out.add(f.apply(s));
        return out;
    }
}
