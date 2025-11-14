import java.util.*;
import java.util.stream.Collectors;

public class HashMap2 {

    private static final int OPCION_SALIR = 26;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<Integer, List<String>> mapa = new HashMap<>();
        int opcion;

        do {
            mostrarMenu();
            opcion = leer_entero(sc, "Ingresa la opción que deseas: ", 0, OPCION_SALIR);

            switch (opcion) {

                // ====================================================
                // 🧠 PATRÓN: CONSTRUIR / AGREGAR (crear entradas)
                // ====================================================
                case 1 -> {
                    // put: asigna lista completa desde CSV "a|b|c" (sobrescribe)
                    int k = leer_entero(sc, "Ingresa la clave (entera): ");
                    List<String> vals = leer_lista(sc, "Ingresa valores separados por '|', ej: a|b|c: ");
                    mapa.put(k, vals);
                    System.out.println("Asignado: " + k + " -> " + vals);
                }
                case 2 -> {
                    // putIfAbsent: inicializa lista solo si no existe
                    int k = leer_entero(sc, "Ingresa la clave (entera): ");
                    List<String> vals = leer_lista(sc, "Valores iniciales '|', ej: x|y|z: ");
                    List<String> previo = mapa.putIfAbsent(k, vals);
                    System.out.println(previo == null ? "Clave nueva creada." : "La clave ya existía con: " + previo);
                }

                // ====================================================
                // 🧠 PATRÓN: CONSULTAR / BUSCAR (lecturas)
                // ====================================================
                case 3 -> {
                    // get / getOrDefault
                    int k = leer_entero(sc, "Clave a consultar: ");
                    List<String> encontrado = mapa.get(k);
                    System.out.println(encontrado != null ? "Encontrado." : "No existe la clave.");
                    System.out.println("Valor (o vacío por defecto): " + mapa.getOrDefault(k, List.of()));
                }
                case 9 -> {
                    // size / isEmpty / contains*
                    System.out.println("Tamaño (claves): " + mapa.size());
                    System.out.println(mapa.isEmpty() ? "Está vacío" : "Tiene elementos");
                    int k = leer_entero(sc, "Clave para containsKey: ");
                    System.out.println("containsKey? " + mapa.containsKey(k));
                    String v = leer_linea(sc, "Valor para containsValue (busca en TODAS las listas): ").trim();
                    boolean found = mapa.values().stream().anyMatch(lst -> lst != null && lst.contains(v));
                    System.out.println("¿Alguna lista contiene '" + v + "'? " + found);
                }
                case 10 -> {
                    // entrySet (listar pares)
                    for (Map.Entry<Integer, List<String>> e : mapa.entrySet()) {
                        System.out.println("Clave: " + e.getKey() + " | Lista: " + e.getValue());
                    }
                }
                case 11 -> {
                    // vistas (keySet / sizes de values)
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Listas (solo tamaños): " +
                            mapa.values().stream().map(l -> (l == null ? 0 : l.size())).collect(Collectors.toList()));
                }
                case 12 -> {
                    // forEach
                    mapa.forEach((k, v) -> System.out.println("[" + k + "] = " + v));
                }

                // ====================================================
                // 🧠 PATRÓN: ELIMINAR (borrado seguro)
                // ====================================================
                case 4 -> {
                    // remove por clave completa y por valor puntual
                    int k = leer_entero(sc, "Clave a eliminar completa: ");
                    List<String> quitado = mapa.remove(k);
                    System.out.println(quitado != null ? "Eliminado: " + quitado : "Clave no encontrada.");

                    int k2 = leer_entero(sc, "Clave para eliminar un valor específico: ");
                    String v = leer_linea(sc, "Valor a eliminar: ").trim();
                    boolean ok = removeValue(mapa, k2, v);
                    System.out.println(ok ? "Valor eliminado de la lista." : "No se encontró valor/clave.");
                }

                // ====================================================
                // 🧠 PATRÓN: ACTUALIZAR (reemplazos puntuales)
                // ====================================================
                case 5 -> {
                    // replace de lista completa / replace condicional
                    int k = leer_entero(sc, "Clave a reemplazar: ");
                    List<String> nueva = leer_lista(sc, "Nueva lista '|': ");
                    List<String> previo = mapa.replace(k, nueva);
                    System.out.println(previo != null ? "Lista anterior: " + previo : "Clave no encontrada.");

                    int k2 = leer_entero(sc, "Clave a reemplazar (condicional): ");
                    List<String> actual = leer_lista(sc, "Lista esperada (para coincidir) '|': ");
                    List<String> repl = leer_lista(sc, "Lista de reemplazo '|': ");
                    boolean ok = mapa.replace(k2, actual, repl);
                    System.out.println(ok ? "Reemplazo condicional aplicado." : "No coincidió la lista actual.");
                }

                // ====================================================
                // 🧠 PATRÓN: CONTAR / AGRUPAR (computar y fusionar)
                // ====================================================
                case 6 -> {
                    // compute: agrega un elemento (o crea lista)
                    int k = leer_entero(sc, "Clave: ");
                    String nuevo = leer_linea(sc, "Elemento a agregar: ").trim();
                    List<String> res = mapa.compute(k, (key, val) -> {
                        List<String> lista = (val == null) ? new ArrayList<>() : new ArrayList<>(val);
                        lista.add(nuevo);
                        return lista;
                    });
                    System.out.println("Resultado en " + k + ": " + res);
                }
                case 7 -> {
                    // computeIfAbsent / computeIfPresent
                    int k1 = leer_entero(sc, "Clave computeIfAbsent: ");
                    List<String> val1 = mapa.computeIfAbsent(k1, kk -> new ArrayList<>());
                    System.out.println("Lista tras computeIfAbsent: " + val1);

                    int k2 = leer_entero(sc, "Clave computeIfPresent: ");
                    List<String> val2 = mapa.computeIfPresent(k2, (kk, lista) -> {
                        if (lista.isEmpty()) return lista;
                        if (lista.contains("BONUS")) return lista;
                        List<String> nueva = new ArrayList<>(lista);
                        nueva.add("BONUS");
                        return nueva;
                    });
                    System.out.println("Lista tras computeIfPresent (o null si no aplicó): " + val2);
                }
                case 8 -> {
                    // merge: unión de listas evitando duplicados
                    int k = leer_entero(sc, "Clave a fusionar: ");
                    List<String> nuevos = leer_lista(sc, "Valores a fusionar '|': ");
                    List<String> res = mapa.merge(k, nuevos, (oldL, newL) -> {
                        LinkedHashSet<String> set = new LinkedHashSet<>(oldL);
                        set.addAll(newL);
                        return new ArrayList<>(set);
                    });
                    System.out.println("Lista resultante: " + res);
                }
                case 20 -> {
                    // cargar CSV id:val1|val2 y FUSIONAR (unión)
                    Map<Integer, List<String>> otro = leerCSVIdLista(sc, "CSV (ej: 1:a|b|c, 2:x|y, 3:p): ");
                    for (var e : otro.entrySet()) {
                        mapa.merge(e.getKey(), e.getValue(), (oldL, newL) -> {
                            LinkedHashSet<String> set = new LinkedHashSet<>(oldL);
                            set.addAll(newL);
                            return new ArrayList<>(set);
                        });
                    }
                    System.out.println("Mapa tras fusionar (unión): " + mapa);
                }
                case 21 -> {
                    // putAll desde CSV (sobrescribe listas)
                    Map<Integer, List<String>> otro = leerCSVIdLista(sc, "CSV (sobrescribe) ej: 10:a|b, 11:c|d: ");
                    mapa.putAll(otro);
                    System.out.println("Mapa tras putAll: " + mapa);
                }

                // ====================================================
                // 🧠 PATRÓN: TRANSFORMAR EN BLOQUE (todo el mapa)
                // ====================================================
                case 15 -> {
                    // replaceAll: normaliza strings (trim + lower)
                    System.out.println("Normalizando todas las listas (trim + toLowerCase)...");
                    mapa.replaceAll((k, v) -> {
                        if (v == null) return new ArrayList<>();
                        List<String> out = new ArrayList<>();
                        for (String s : v) if (s != null) out.add(s.trim().toLowerCase());
                        return out;
                    });
                    System.out.println("Mapa: " + mapa);
                }

                // ====================================================
                // 🧠 PATRÓN: ORDENAR / RANKEAR
                // ====================================================
                case 13 -> {
                    // ordenar por CLAVE (ASC/DESC)
                    List<Map.Entry<Integer, List<String>>> arr = new ArrayList<>(mapa.entrySet());
                    String orden = leer_linea(sc, "Orden por clave: 'ASC' o 'DESC': ").trim().toUpperCase();
                    if ("DESC".equals(orden)) arr.sort(Map.Entry.<Integer, List<String>>comparingByKey().reversed());
                    else arr.sort(Map.Entry.comparingByKey());
                    System.out.println("Entradas ordenadas por clave (" + orden + "): " + arr);
                }
                case 14 -> {
                    // ordenar por TAMAÑO de lista (ASC/DESC)
                    List<Map.Entry<Integer, List<String>>> arr = new ArrayList<>(mapa.entrySet());
                    String orden = leer_linea(sc, "Orden por tamaño: 'ASC' o 'DESC': ").trim().toUpperCase();
                    Comparator<Map.Entry<Integer, List<String>>> cmp =
                            Comparator.comparingInt(e -> (e.getValue() == null ? 0 : e.getValue().size()));
                    if ("DESC".equals(orden)) cmp = cmp.reversed();
                    arr.sort(cmp);
                    System.out.println("Entradas por tamaño (" + orden + "): " + arr);
                }
                case 17 -> {
                    // Top-N por tamaño de lista (desc)
                    if (mapa.isEmpty()) { System.out.println("Mapa vacío."); break; }
                    int n = leer_entero(sc, "¿Cuántos elementos (Top-N)? ");
                    List<Map.Entry<Integer, List<String>>> top = mapa.entrySet().stream()
                            .sorted((a, b) -> Integer.compare(
                                    b.getValue() == null ? 0 : b.getValue().size(),
                                    a.getValue() == null ? 0 : a.getValue().size()))
                            .limit(n).collect(Collectors.toList());
                    System.out.println("Top-" + n + " por tamaño de lista: " + top);
                }
                case 16 -> {
                    // estadísticas de tamaños
                    int totalClaves = mapa.size();
                    int totalItems = mapa.values().stream().filter(Objects::nonNull).mapToInt(List::size).sum();
                    double prom = totalClaves == 0 ? 0.0 : (double) totalItems / totalClaves;
                    int max = mapa.values().stream().filter(Objects::nonNull).mapToInt(List::size).max().orElse(0);
                    int min = mapa.values().stream().filter(Objects::nonNull).mapToInt(List::size).min().orElse(0);
                    System.out.println("Total claves: " + totalClaves);
                    System.out.println("Total items: " + totalItems);
                    System.out.println("Promedio items por clave: " + prom);
                    System.out.println("Máximo tamaño: " + max);
                    System.out.println("Mínimo tamaño: " + min);
                }

                // ====================================================
                // 🧠 PATRÓN: PROYECCIONES / FILTROS (streams)
                // ====================================================
                case 18 -> {
                    // invertir: valor -> ids
                    Map<String, List<Integer>> invertido = new HashMap<>();
                    for (var e : mapa.entrySet()) {
                        Integer id = e.getKey();
                        List<String> lista = e.getValue();
                        if (lista == null) continue;
                        for (String s : lista) {
                            if (s == null) continue;
                            invertido.computeIfAbsent(s, k -> new ArrayList<>()).add(id);
                        }
                    }
                    System.out.println("Invertido (valor -> ids): " + invertido);
                }
                case 19 -> {
                    // filtrar por rango de claves [min, max]
                    int min = leer_entero(sc, "Mín clave: ");
                    int max = leer_entero(sc, "Máx clave: ");
                    Map<Integer, List<String>> filtrado = mapa.entrySet().stream()
                            .filter(e -> e.getKey() >= min && e.getKey() <= max)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    System.out.println("Filtrado por rango [" + min + ", " + max + "]: " + filtrado);
                }
                case 23 -> {
                    // proyección: clave -> tamaño de su lista
                    Map<Integer, Integer> proyeccion = mapa.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> e.getValue() == null ? 0 : e.getValue().size()
                            ));
                    System.out.println("Proyección (clave -> tamaño_lista): " + proyeccion);
                }

                // ====================================================
                // 🧠 PATRÓN: COPIAS / IGUALDAD / LIMPIEZA / ESTADO
                // ====================================================
                case 22 -> {
                    // equals / hashCode / copia superficial
                    Map<Integer, List<String>> copia = new HashMap<>(mapa);
                    System.out.println("Copia creada. equals(copia)? " + mapa.equals(copia));
                    System.out.println("hashCode original: " + mapa.hashCode());
                    System.out.println("hashCode copia: " + copia.hashCode());
                }
                case 24 -> {
                    // clear
                    mapa.clear();
                    System.out.println("Mapa limpiado.");
                }
                case 25 -> {
                    // estado rápido
                    System.out.println("Estado rápido:");
                    System.out.println("Total claves: " + mapa.size());
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Tamaños por clave: " +
                            mapa.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            e -> e.getValue() == null ? 0 : e.getValue().size()
                                    )));
                    System.out.println("Entradas: " + mapa.entrySet());
                }

                // ====================================================
                // 🏁 SALIR
                // ====================================================
                case 26 -> System.out.println("Saliendo...");

                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != OPCION_SALIR);

        sc.close();
    }

    // ---------------------------
    //  Utilidades de entrada/salida
    // ---------------------------

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ MULTIMAP (Integer -> List<String>) — agrupado por patrones ---");
        System.out.println(" 1 . put                         (CONSTRUIR/AGREGAR)");
        System.out.println(" 2 . putIfAbsent                 (CONSTRUIR/AGREGAR)");
        System.out.println(" 3 . get / getOrDefault          (CONSULTAR/BUSCAR)");
        System.out.println(" 4 . remove / removeValue        (ELIMINAR)");
        System.out.println(" 5 . replace / replace cond.     (ACTUALIZAR)");
        System.out.println(" 6 . compute (append)            (CONTAR/AGRUPAR)");
        System.out.println(" 7 . computeIfAbsent/Present     (CONTAR/AGRUPAR)");
        System.out.println(" 8 . merge (unión sin duplicados)(CONTAR/AGRUPAR)");
        System.out.println(" 9 . size/isEmpty/contains*      (CONSULTAR/BUSCAR)");
        System.out.println("10 . Listar entrySet             (CONSULTAR/BUSCAR)");
        System.out.println("11 . keySet / tamaños values     (CONSULTAR/BUSCAR)");
        System.out.println("12 . forEach                     (CONSULTAR/BUSCAR)");
        System.out.println("13 . Ordenar por clave           (ORDENAR/RANKEAR)");
        System.out.println("14 . Ordenar por tamaño lista    (ORDENAR/RANKEAR)");
        System.out.println("15 . replaceAll normalizar       (TRANSFORMAR BLOQUE)");
        System.out.println("16 . Estadísticas de tamaños     (ORDENAR/RANKEAR)");
        System.out.println("17 . Top-N por tamaño lista      (ORDENAR/RANKEAR)");
        System.out.println("18 . Invertir valor->ids         (PROYECCIÓN)");
        System.out.println("19 . Filtrar por rango de claves (FILTRAR)");
        System.out.println("20 . CSV fusionar (unión)        (CONTAR/AGRUPAR)");
        System.out.println("21 . putAll desde CSV            (CONSTRUIR/AGREGAR)");
        System.out.println("22 . equals / hashCode / copia   (COPIAS/IGUALDAD)");
        System.out.println("23 . Proyección clave->tamaño    (PROYECCIÓN)");
        System.out.println("24 . clear                       (LIMPIEZA)");
        System.out.println("25 . Estado rápido               (ESTADO)");
        System.out.println("26 . Salir");
    }

    static int leer_entero(Scanner sc, String prompt) {
        while (true) {
            System.out.println(prompt);
            String v = sc.nextLine();
            try {
                return Integer.parseInt(v.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor ingresado incorrecto, vuelve a intentarlo.");
            }
        }
    }

    static int leer_entero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leer_entero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Fuera de rango [" + min + ", " + max + "].");
                continue;
            }
            return n;
        }
    }

    static String leer_linea(Scanner sc, String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    /** Lee una línea tipo "a|b|c" y la convierte a lista. Quita vacíos y trim. */
    static List<String> leer_lista(Scanner sc, String prompt) {
        String linea = leer_linea(sc, prompt);
        if (linea == null || linea.isBlank()) return new ArrayList<>();
        List<String> out = new ArrayList<>();
        for (String t : linea.split("\\|")) {
            if (t == null) continue;
            String s = t.trim();
            if (!s.isEmpty()) out.add(s);
        }
        return out;
    }

    /**
     * Parsea CSV tipo: "1:a|b|c, 2:x|y, 3:p"
     * Retorna Map<Integer, List<String>>. Ignora tokens mal formados.
     */
    static Map<Integer, List<String>> leerCSVIdLista(Scanner sc, String prompt) {
        String csv = leer_linea(sc, prompt);
        Map<Integer, List<String>> result = new LinkedHashMap<>(); // preserva orden
        if (csv == null || csv.isBlank()) return result;

        for (String token : csv.split(",", -1)) {
            if (token.isBlank()) continue;

            String[] arr = token.trim().split(":", 2);
            if (arr.length != 2) {
                System.out.println("Token inválido (falta ':'): '" + token + "'. Se ignora.");
                continue;
            }

            String kStr = arr[0].trim();
            String valsStr = arr[1].trim();

            try {
                int k = Integer.parseInt(kStr);
                List<String> vals = new ArrayList<>();
                if (!valsStr.isEmpty()) {
                    for (String v : valsStr.split("\\|")) {
                        String s = v.trim();
                        if (!s.isEmpty()) vals.add(s);
                    }
                }
                // Fusión sin duplicados (si llega repetido en el CSV)
                result.merge(k, vals, (oldL, newL) -> {
                    LinkedHashSet<String> set = new LinkedHashSet<>(oldL);
                    set.addAll(newL);
                    return new ArrayList<>(set);
                });
            } catch (NumberFormatException e) {
                System.out.println("Clave inválida en token: '" + token + "'. Se ignora.");
            }
        }
        return result;
    }

    /** Elimina un valor dentro de la lista de una clave. Si la lista queda vacía, elimina la clave. */
    static boolean removeValue(Map<Integer, List<String>> mapa, int key, String val) {
        List<String> lista = mapa.get(key);
        if (lista == null) return false;
        boolean removed = lista.remove(val);
        if (lista.isEmpty()) mapa.remove(key);
        return removed;
    }
}
