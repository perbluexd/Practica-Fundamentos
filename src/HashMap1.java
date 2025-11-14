import java.util.*;
import java.util.stream.Collectors;

public class HashMap1 {

    // Límite del menú (incluye "Salir")
    private static final int OPCION_SALIR = 26;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, Integer> mapa = new HashMap<>();
        int opcion;

        do {
            mostrarMenu();
            opcion = leer_entero(sc, "Ingresa la opción que deseas: ", 0, OPCION_SALIR);

            switch (opcion) {

                // ====================================================
                // 🧠 PATRÓN: CONSTRUIR / AGREGAR (crear entradas)
                // ====================================================
                case 1 -> {
                    // put: inserta o reemplaza; retorna valor previo (o null)
                    String v = leer_linea(sc, "Ingresa la clave: ");
                    Integer n = leer_entero(sc, "Ingresa el valor: ");
                    Integer previo = mapa.put(v, n);
                    System.out.println(previo == null
                            ? "Agregado nuevo correctamente"
                            : "Valor reemplazado. Valor anterior: " + previo);
                }
                case 2 -> {
                    // putIfAbsent: inserta solo si no existe (o está en null)
                    var v = leer_linea(sc, "Ingresa la clave: ");
                    var n = leer_entero(sc, "Ingresa el valor: ");
                    Integer previo = mapa.putIfAbsent(v, n);
                    System.out.println(previo == null
                            ? "Valor agregado (clave no existía o estaba en null)."
                            : "La clave ya tenía valor y es: " + previo);
                }

                // ====================================================
                // 🧠 PATRÓN: CONSULTAR / BUSCAR (lecturas)
                // ====================================================
                case 3 -> {
                    // get / getOrDefault
                    var v = leer_linea(sc, "Ingresa la clave a consultar: ");
                    Integer encontrado = mapa.get(v);
                    System.out.println(encontrado != null ? "Sí se encontró el valor." : "No se encontró valor.");
                    System.out.println("Valor (o por defecto -1): " + mapa.getOrDefault(v, -1));
                }
                case 9 -> {
                    // size / isEmpty / contains
                    System.out.println("Tamaño: " + mapa.size());
                    System.out.println(mapa.isEmpty() ? "Está vacío" : "Tiene elementos");
                    var k = leer_linea(sc, "Clave para containsKey: ");
                    System.out.println("containsKey? " + mapa.containsKey(k));
                    var v = leer_entero(sc, "Valor para containsValue: ");
                    System.out.println("containsValue? " + mapa.containsValue(v));
                }
                case 10 -> {
                    // entrySet (listar pares)
                    for (Map.Entry<String, Integer> e : mapa.entrySet()) {
                        System.out.println("Clave: " + e.getKey() + " | Valor: " + e.getValue());
                    }
                }
                case 11 -> {
                    // vistas en vivo
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Valores: " + mapa.values());
                }
                case 12 -> {
                    // forEach (K,V)
                    mapa.forEach((k, v) -> System.out.println("[" + k + "] = " + v));
                }

                // ====================================================
                // 🧠 PATRÓN: ELIMINAR (borrar seguro)
                // ====================================================
                case 4 -> {
                    // remove por clave / clave+valor
                    var v = leer_linea(sc, "Ingresa la clave a eliminar: ");
                    Integer valor = mapa.remove(v);
                    System.out.println(valor != null ? "Valor eliminado: " + valor : "Clave no encontrada.");

                    var v2 = leer_linea(sc, "Ingresa la clave a eliminar (condicional): ");
                    var v3 = leer_entero(sc, "Ingresa el valor de la clave a eliminar (condicional): ");
                    boolean ok = mapa.remove(v2, v3);
                    System.out.println(ok ? "Clave y valor coincidían, entrada eliminada." : "No se encontró coincidencia clave-valor.");
                }

                // ====================================================
                // 🧠 PATRÓN: ACTUALIZAR (reemplazos puntuales)
                // ====================================================
                case 5 -> {
                    // replace simple y condicional
                    var v1 = leer_linea(sc, "Ingresa la clave a reemplazar: ");
                    var v2 = leer_entero(sc, "Ingresa el valor de reemplazo: ");
                    Integer previo = mapa.replace(v1, v2);
                    System.out.println(previo != null ? "Valor antiguo reemplazado: " + previo : "Clave no encontrada (no se reemplazó).");

                    var v3 = leer_linea(sc, "Ingresa la clave a reemplazar (condicional): ");
                    var v4 = leer_entero(sc, "Ingresa el valor de coincidencia: ");
                    var v5 = leer_entero(sc, "Ingresa valor de reemplazo: ");
                    boolean ok = mapa.replace(v3, v4, v5);
                    System.out.println(ok ? "Se encontró la coincidencia y se reemplazó." : "No se encontró la coincidencia (no se reemplazó).");
                }

                // ====================================================
                // 🧠 PATRÓN: CONTAR / AGRUPAR (computar y fusionar)
                // ====================================================
                case 6 -> {
                    // compute (crear/incrementar; null => crea; lambda null => elimina)
                    var v1 = leer_linea(sc, "Ingresa la clave: ");
                    Integer res = mapa.compute(v1, (key, val) -> (val == null) ? 1 : val + 1);
                    System.out.println("Valor resultante para '" + v1 + "': " + res);
                }
                case 7 -> {
                    // computeIfAbsent / computeIfPresent
                    String v1 = leer_linea(sc, "Ingresa la clave para computeIfAbsent: ");
                    Integer valor = mapa.computeIfAbsent(v1, key -> 0);
                    System.out.println("Valor tras computeIfAbsent: " + valor + " (para clave '" + v1 + "')");

                    String v2 = leer_linea(sc, "Ingresa la clave para computeIfPresent: ");
                    Integer valor2 = mapa.computeIfPresent(v2, (key, val) -> val + 10);
                    System.out.println("Valor tras computeIfPresent (o null si no aplicó): " + valor2 + " (para clave '" + v2 + "')");
                }
                case 8 -> {
                    // merge (combinar valores por clave)
                    String k = leer_linea(sc, "Ingresa la clave: ");
                    Integer v = leer_entero(sc, "Ingresa el valor: ");
                    Integer res = mapa.merge(k, v, Integer::sum);
                    System.out.println("Valor resultante para '" + k + "': " + res);
                }
                case 20 -> {
                    // cargar CSV y fusionar (merge-sum)
                    Map<String, Integer> otro = otro(sc, "Ingresa CSV k:v (ej: a:1,b:2,c:3): ");
                    for (var e : otro.entrySet()) {
                        mapa.merge(e.getKey(), e.getValue(), Integer::sum);
                    }
                    System.out.println("Mapa tras fusionar (sumando): " + mapa);
                }
                case 21 -> {
                    // putAll (carga directa, reescribe)
                    Map<String, Integer> otro = otro(sc, "Ingresa CSV k:v (ej: a:1,b:2,c:3): ");
                    mapa.putAll(otro);
                    System.out.println("Mapa tras putAll: " + mapa);
                }

                // ====================================================
                // 🧠 PATRÓN: TRANSFORMAR EN BLOQUE (mapa completo)
                // ====================================================
                case 15 -> {
                    // replaceAll sobre todos los valores
                    System.out.println("Sumando +1 a todos los valores (si v == null, poner 0)...");
                    mapa.replaceAll((k, v) -> v == null ? 0 : v + 1);
                    System.out.println("Mapa: " + mapa);
                }

                // ====================================================
                // 🧠 PATRÓN: ORDENAR / RANKEAR (listas de entries)
                // ====================================================
                case 13 -> {
                    // ordenar por CLAVE (ASC/DESC)
                    List<Map.Entry<String, Integer>> arr = new ArrayList<>(mapa.entrySet());
                    String orden = leer_linea(sc, "Orden por clave: escribe 'ASC' o 'DESC': ").trim().toUpperCase();
                    if ("DESC".equals(orden)) {
                        arr.sort(Map.Entry.<String, Integer>comparingByKey().reversed());
                    } else {
                        arr.sort(Map.Entry.comparingByKey());
                    }
                    System.out.println("Entradas ordenadas por clave (" + orden + "): " + arr);
                }
                case 14 -> {
                    // ordenar por VALOR (ASC/DESC)
                    List<Map.Entry<String, Integer>> arr = new ArrayList<>(mapa.entrySet());
                    String orden = leer_linea(sc, "Orden por valor: escribe 'ASC' o 'DESC': ").trim().toUpperCase();
                    if ("DESC".equals(orden)) {
                        arr.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
                    } else {
                        arr.sort(Map.Entry.comparingByValue());
                    }
                    System.out.println("Entradas ordenadas por valor (" + orden + "): " + arr);
                }
                case 17 -> {
                    // Top-N por valor (desc)
                    if (mapa.isEmpty()) {
                        System.out.println("Mapa vacío.");
                        break;
                    }
                    int n = leer_entero(sc, "¿Cuántos elementos (Top-N)? ");
                    List<Map.Entry<String, Integer>> top = mapa.entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .limit(n)
                            .collect(Collectors.toList());
                    System.out.println("Top-" + n + " por valor: " + top);
                }
                case 16 -> {
                    // estadísticas rápidas sobre valores
                    if (mapa.isEmpty()) {
                        System.out.println("Mapa vacío.");
                        break;
                    }
                    IntSummaryStatistics stats = mapa.values().stream().mapToInt(Integer::intValue).summaryStatistics();
                    System.out.println("Suma: " + stats.getSum());
                    System.out.println("Promedio: " + stats.getAverage());
                    System.out.println("Máximo: " + stats.getMax());
                    System.out.println("Mínimo: " + stats.getMin());
                }

                // ====================================================
                // 🧠 PATRÓN: PROYECCIONES / FILTROS (streams)
                // ====================================================
                case 18 -> {
                    // invertir: valor -> lista de claves
                    Map<Integer, List<String>> invertido = new HashMap<>();
                    for (var e : mapa.entrySet()) {
                        invertido.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
                    }
                    System.out.println("Invertido (valor -> claves): " + invertido);
                }
                case 19 -> {
                    // filtrar por prefijo en clave
                    String pref = leer_linea(sc, "Prefijo de clave: ");
                    Map<String, Integer> filtrado = mapa.entrySet().stream()
                            .filter(e -> e.getKey() != null && e.getKey().startsWith(pref))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    System.out.println("Filtrado por prefijo '" + pref + "': " + filtrado);
                }
                case 23 -> {
                    // proyección a Map<String,String>
                    Map<String, String> proyeccion = mapa.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> "Valor=" + e.getValue()
                            ));
                    System.out.println("Proyección (String->String): " + proyeccion);
                }

                // ====================================================
                // 🧠 PATRÓN: COPIAS / IGUALDAD / LIMPIEZA / ESTADO
                // ====================================================
                case 22 -> {
                    // equals / hashCode / copia
                    HashMap<String, Integer> copia = new HashMap<>(mapa);
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
                    System.out.println("Tamaño: " + mapa.size());
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Valores: " + mapa.values());
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
    //  Funciones de utilidad I/O
    // ---------------------------

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ HASHMAP (String -> Integer) — agrupado por patrones ---");
        System.out.println(" 1 . put                         (CONSTRUIR/AGREGAR)");
        System.out.println(" 2 . putIfAbsent                 (CONSTRUIR/AGREGAR)");
        System.out.println(" 3 . get / getOrDefault          (CONSULTAR/BUSCAR)");
        System.out.println(" 4 . remove                      (ELIMINAR)");
        System.out.println(" 5 . replace / replace cond.     (ACTUALIZAR)");
        System.out.println(" 6 . compute                     (CONTAR/AGRUPAR)");
        System.out.println(" 7 . computeIfAbsent/Present     (CONTAR/AGRUPAR)");
        System.out.println(" 8 . merge                       (CONTAR/AGRUPAR)");
        System.out.println(" 9 . size/isEmpty/contains*      (CONSULTAR/BUSCAR)");
        System.out.println("10 . Listar entrySet             (CONSULTAR/BUSCAR)");
        System.out.println("11 . keySet / values             (CONSULTAR/BUSCAR)");
        System.out.println("12 . forEach                     (CONSULTAR/BUSCAR)");
        System.out.println("13 . Ordenar por clave           (ORDENAR/RANKEAR)");
        System.out.println("14 . Ordenar por valor           (ORDENAR/RANKEAR)");
        System.out.println("15 . replaceAll                  (TRANSFORMAR BLOQUE)");
        System.out.println("16 . Estadísticas                (ORDENAR/RANKEAR)");
        System.out.println("17 . Top-N por valor             (ORDENAR/RANKEAR)");
        System.out.println("18 . Invertir valor->claves      (PROYECCIÓN)");
        System.out.println("19 . Filtrar por prefijo (stream)(FILTRAR)");
        System.out.println("20 . CSV fusionar (merge-sum)    (CONTAR/AGRUPAR)");
        System.out.println("21 . putAll CSV directo          (CONSTRUIR/AGREGAR)");
        System.out.println("22 . equals / hashCode / copia   (COPIAS/IGUALDAD)");
        System.out.println("23 . Proyección Map<String,String>(PROYECCIÓN)");
        System.out.println("24 . clear                       (LIMPIEZA)");
        System.out.println("25 . Estado rápido               (ESTADO)");
        System.out.println("26 . Salir");
    }

    static int leer_entero(Scanner sc, String prompt) {
        while (true) {
            System.out.println(prompt);
            String v = sc.nextLine();
            try {
                return Integer.parseInt(v);
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

    /**
     * Parsea un CSV de la forma "k1:v1,k2:v2,..."
     * Ignora tokens vacíos o mal formados y líneas con valores no numéricos.
     * Retorna un nuevo HashMap<String,Integer> con lo leído.
     */
    static Map<String, Integer> otro(Scanner sc, String prompt) {
        String csv = leer_linea(sc, prompt);
        HashMap<String, Integer> otromapa = new HashMap<>();
        if (csv == null || csv.isBlank()) return otromapa;

        for (String token : csv.split(",")) {
            if (token == null || token.isBlank()) continue;
            String[] arr = token.trim().split(":");
            if (arr.length != 2) continue;

            String a = arr[0].trim();
            String b = arr[1].trim();
            if (a.isEmpty()) continue;

            try {
                int n = Integer.parseInt(b);
                otromapa.put(a, n);
            } catch (NumberFormatException e) {
                System.out.println("Error de valores en token: '" + token + "'. Se ignora.");
            }
        }
        return otromapa;
    }
}
