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

                case 1 -> {
                    // put (sobrescribe) una lista completa a partir de CSV de valores "a|b|c"
                    int k = leer_entero(sc, "Ingresa la clave (entera): ");
                    List<String> vals = leer_lista(sc, "Ingresa valores separados por '|', ej: a|b|c: ");
                    mapa.put(k, vals);
                    System.out.println("Asignado: " + k + " -> " + vals);
                }

                case 2 -> {
                    // putIfAbsent: si no existe, inicializa con lista; si existe, no toca
                    int k = leer_entero(sc, "Ingresa la clave (entera): ");
                    List<String> vals = leer_lista(sc, "Valores iniciales '|', ej: x|y|z: ");
                    List<String> previo = mapa.putIfAbsent(k, vals);
                    System.out.println(previo == null
                            ? "Clave nueva creada."
                            : "La clave ya existía con: " + previo);
                }

                case 3 -> {
                    // get / getOrDefault
                    int k = leer_entero(sc, "Clave a consultar: ");
                    List<String> encontrado = mapa.get(k);
                    System.out.println(encontrado != null ? "Encontrado." : "No existe la clave.");
                    System.out.println("Valor (o vacío por defecto): " + mapa.getOrDefault(k, List.of()));
                }

                case 4 -> {
                    // remove por clave / remove por (clave, valor-específico)
                    int k = leer_entero(sc, "Clave a eliminar completa: ");
                    List<String> quitado = mapa.remove(k);
                    System.out.println(quitado != null ? "Eliminado: " + quitado : "Clave no encontrada.");

                    int k2 = leer_entero(sc, "Clave para eliminar un valor específico: ");
                    String v = leer_linea(sc, "Valor a eliminar: ").trim();
                    boolean ok = removeValue(mapa, k2, v);
                    System.out.println(ok ? "Valor eliminado de la lista." : "No se encontró valor/clave.");
                }

                case 5 -> {
                    // replace de lista completa / replace condicional por comparación de lista actual
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

                case 6 -> {
                    // compute: añade un elemento (o crea lista) para la clave
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
                        // Variante C: agregar "BONUS" solo si ya tiene al menos 1 notificación y evitar duplicados
                        if (lista.isEmpty()) return lista;              // sin notificaciones -> no agregar
                        if (lista.contains("BONUS")) return lista;      // ya lo tiene -> no duplicar

                        // Opción no-mutable: crear copia solo si vamos a modificar
                        List<String> nueva = new ArrayList<>(lista);
                        nueva.add("BONUS");
                        return nueva;

                        // Si prefieres mutar en sitio (más simple pero toca el objeto):
                        // lista.add("BONUS");
                        // return lista;
                    });
                    System.out.println("Lista tras computeIfPresent (o null si no aplicó): " + val2);
                }

                case 8 -> {
                    // merge: fusiona listas (uniendo elementos, evitando duplicados opcionalmente)
                    int k = leer_entero(sc, "Clave a fusionar: ");
                    List<String> nuevos = leer_lista(sc, "Valores a fusionar '|': ");
                    List<String> res = mapa.merge(k, nuevos, (oldL, newL) -> {
                        // Unión evitando duplicados preservando orden
                        LinkedHashSet<String> set = new LinkedHashSet<>(oldL);
                        set.addAll(newL);
                        return new ArrayList<>(set);
                    });
                    System.out.println("Lista resultante: " + res);
                }

                case 9 -> {
                    // size / isEmpty / containsKey / containsValue (busca un string en alguna lista)
                    System.out.println("Tamaño (claves): " + mapa.size());
                    System.out.println(mapa.isEmpty() ? "Está vacío" : "Tiene elementos");
                    int k = leer_entero(sc, "Clave para containsKey: ");
                    System.out.println("containsKey? " + mapa.containsKey(k));
                    String v = leer_linea(sc, "Valor para containsValue (busca en TODAS las listas): ").trim();
                    boolean found = mapa.values().stream().anyMatch(lst -> lst != null && lst.contains(v));
                    System.out.println("¿Alguna lista contiene '" + v + "'? " + found);
                }

                case 10 -> {
                    // Listar entrySet
                    for (Map.Entry<Integer, List<String>> e : mapa.entrySet()) {
                        System.out.println("Clave: " + e.getKey() + " | Lista: " + e.getValue());
                    }
                }

                case 11 -> {
                    // Vistas keySet / values (solo cuenta de valores)
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Listas (solo tamaños): " +
                            mapa.values().stream().map(l -> (l == null ? 0 : l.size())).collect(Collectors.toList()));
                }

                case 12 -> {
                    // forEach
                    mapa.forEach((k, v) -> System.out.println("[" + k + "] = " + v));
                }

                case 13 -> {
                    // Ordenar por clave (ASC/DESC)
                    List<Map.Entry<Integer, List<String>>> arr = new ArrayList<>(mapa.entrySet());
                    String orden = leer_linea(sc, "Orden por clave: 'ASC' o 'DESC': ").trim().toUpperCase();
                    if ("DESC".equals(orden)) {
                        arr.sort(Map.Entry.<Integer, List<String>>comparingByKey().reversed());
                    } else {
                        arr.sort(Map.Entry.comparingByKey());
                    }
                    System.out.println("Entradas ordenadas por clave (" + orden + "): " + arr);
                }

                case 14 -> {
                    // Ordenar por tamaño de lista (ASC/DESC)
                    List<Map.Entry<Integer, List<String>>> arr = new ArrayList<>(mapa.entrySet());
                    String orden = leer_linea(sc, "Orden por tamaño: 'ASC' o 'DESC': ").trim().toUpperCase();
                    Comparator<Map.Entry<Integer, List<String>>> cmp =
                            Comparator.comparingInt(e -> (e.getValue() == null ? 0 : e.getValue().size()));
                    if ("DESC".equals(orden)) cmp = cmp.reversed();
                    arr.sort(cmp);
                    System.out.println("Entradas por tamaño (" + orden + "): " + arr);
                }

                case 15 -> {
                    // replaceAll: normaliza strings (trim + a minúsculas) en todas las listas
                    System.out.println("Normalizando todas las listas (trim + toLowerCase)...");
                    mapa.replaceAll((k, v) -> {
                        if (v == null) return new ArrayList<>();
                        List<String> out = new ArrayList<>();
                        for (String s : v) {
                            if (s != null) out.add(s.trim().toLowerCase());
                        }
                        return out;
                    });
                    System.out.println("Mapa: " + mapa);
                }

                case 16 -> {
                    // Estadísticas (totales, promedio, max/min tamaño de lista)
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

                case 17 -> {
                    // Top-N por tamaño de lista (desc)
                    if (mapa.isEmpty()) {
                        System.out.println("Mapa vacío.");
                        break;
                    }
                    int n = leer_entero(sc, "¿Cuántos elementos (Top-N)? ");
                    List<Map.Entry<Integer, List<String>>> top = mapa.entrySet().stream()
                            .sorted((a, b) -> Integer.compare(
                                    b.getValue() == null ? 0 : b.getValue().size(),
                                    a.getValue() == null ? 0 : a.getValue().size()))
                            .limit(n)
                            .collect(Collectors.toList());
                    System.out.println("Top-" + n + " por tamaño de lista: " + top);
                }

                case 18 -> {
                    // Invertir: valor -> lista de claves (cada string a qué ids pertenece)
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
                    // Filtrar por rango de claves [min, max]
                    int min = leer_entero(sc, "Mín clave: ");
                    int max = leer_entero(sc, "Máx clave: ");
                    Map<Integer, List<String>> filtrado = mapa.entrySet().stream()
                            .filter(e -> e.getKey() >= min && e.getKey() <= max)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    System.out.println("Filtrado por rango [" + min + ", " + max + "]: " + filtrado);
                }

                case 20 -> {
                    // Cargar CSV "id:val1|val2, id2:x|y" y fusionar (unión sin duplicados)
                    Map<Integer, List<String>> otro = leerCSVIdLista(sc,
                            "CSV (ej: 1:a|b|c, 2:x|y, 3:p): ");
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
                    // putAll desde CSV (sobrescribe listas completas)
                    Map<Integer, List<String>> otro = leerCSVIdLista(sc,
                            "CSV (sobrescribe) ej: 10:a|b, 11:c|d: ");
                    mapa.putAll(otro);
                    System.out.println("Mapa tras putAll: " + mapa);
                }

                case 22 -> {
                    // equals / hashCode / copia superficial
                    Map<Integer, List<String>> copia = new HashMap<>(mapa);
                    System.out.println("Copia creada. equals(copia)? " + mapa.equals(copia));
                    System.out.println("hashCode original: " + mapa.hashCode());
                    System.out.println("hashCode copia: " + copia.hashCode());
                }

                case 23 -> {
                    // Proyección: Map<Integer, Integer> con el tamaño de cada lista
                    Map<Integer, Integer> proyeccion = mapa.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> e.getValue() == null ? 0 : e.getValue().size()
                            ));
                    System.out.println("Proyección (clave -> tamaño_lista): " + proyeccion);
                }

                case 24 -> {
                    // clear
                    mapa.clear();
                    System.out.println("Mapa limpiado.");
                }

                case 25 -> {
                    // Estado rápido
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
        System.out.println("\n--- MENÚ MULTIMAP (Integer -> List<String>) ---");
        System.out.println(" 1 . put (sobrescribir lista completa)");
        System.out.println(" 2 . putIfAbsent (inicializar lista si no existe)");
        System.out.println(" 3 . get / getOrDefault");
        System.out.println(" 4 . remove (por clave y por valor puntual)");
        System.out.println(" 5 . replace lista / replace condicional");
        System.out.println(" 6 . compute (append a lista)");
        System.out.println(" 7 . computeIfAbsent / computeIfPresent");
        System.out.println(" 8 . merge (unión sin duplicados)");
        System.out.println(" 9 . size / isEmpty / containsKey / containsValue");
        System.out.println("10 . Listar entrySet");
        System.out.println("11 . Vistas keySet / tamaños de values");
        System.out.println("12 . forEach");
        System.out.println("13 . Ordenar por clave (ASC/DESC)");
        System.out.println("14 . Ordenar por tamaño de lista (ASC/DESC)");
        System.out.println("15 . replaceAll (normalizar strings)");
        System.out.println("16 . Estadísticas (totales/promedio/max/min)");
        System.out.println("17 . Top-N por tamaño de lista");
        System.out.println("18 . Invertir (valor -> lista de claves)");
        System.out.println("19 . Filtrar por rango de claves");
        System.out.println("20 . Cargar CSV id:val1|val2 y FUSIONAR (unión)");
        System.out.println("21 . putAll desde CSV (sobrescribir)");
        System.out.println("22 . equals / hashCode / copia");
        System.out.println("23 . Proyección a Map<Integer,Integer> (tamaños)");
        System.out.println("24 . clear");
        System.out.println("25 . Estado rápido");
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

        for (String token : csv.split(",", -1)) { // -1 para preservar vacíos si te interesa diagnosticarlos
            if (token.isBlank()) continue;

            String[] arr = token.trim().split(":", 2); // 2 = a la primera “:” partimos y listo
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
                // Si quieres sobrescribir: result.put(k, vals);
                // Si prefieres fusionar sin duplicados:
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
