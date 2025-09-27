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

                case 1 -> {
                    // put: inserta o reemplaza el valor de la clave. Retorna el valor previo (o null si no existía).
                    String v = leer_linea(sc, "Ingresa la clave: ");
                    Integer n = leer_entero(sc, "Ingresa el valor: ");
                    Integer previo = mapa.put(v, n);
                    // Si previo == null, no existía (se agregó). Si no, se reemplazó y mostramos el valor anterior.
                    System.out.println(previo == null
                            ? "Agregado nuevo correctamente"
                            : "Valor reemplazado. Valor anterior: " + previo);
                }

                case 2 -> {
                    // putIfAbsent: inserta solo si la clave no existe o está asociada a null. Retorna el valor anterior (o null si insertó).
                    var v = leer_linea(sc, "Ingresa la clave: ");
                    var n = leer_entero(sc, "Ingresa el valor: ");
                    Integer previo = mapa.putIfAbsent(v, n);
                    // Si previo == null, insertó; si no, ya había un valor no nulo y no hizo nada.
                    System.out.println(previo == null
                            ? "Valor agregado (clave no existía o estaba en null)."
                            : "La clave ya tenía valor y es: " + previo);
                }

                case 3 -> {
                    // get / getOrDefault: consulta un valor por clave, con opción de valor por defecto si no existe.
                    var v = leer_linea(sc, "Ingresa la clave a consultar: ");
                    Integer encontrado = mapa.get(v);
                    System.out.println(encontrado != null ? "Sí se encontró el valor." : "No se encontró valor.");
                    // getOrDefault no altera el mapa; solo retorna un fallback si la clave no existe o está en null.
                    System.out.println("Valor (o por defecto -1): " + mapa.getOrDefault(v, -1));
                }

                case 4 -> {
                    // remove por clave / remove por (clave, valor)
                    var v = leer_linea(sc, "Ingresa la clave a eliminar: ");
                    Integer valor = mapa.remove(v); // elimina si la clave existía. Retorna el valor eliminado o null.
                    System.out.println(valor != null ? "Valor eliminado: " + valor : "Clave no encontrada.");

                    var v2 = leer_linea(sc, "Ingresa la clave a eliminar (condicional): ");
                    var v3 = leer_entero(sc, "Ingresa el valor de la clave a eliminar (condicional): ");
                    boolean ok = mapa.remove(v2, v3); // elimina solo si coincide exactamente clave y valor
                    System.out.println(ok ? "Clave y valor coincidían, entrada eliminada." : "No se encontró coincidencia clave-valor.");
                }

                case 5 -> {
                    // replace (simple y condicional)
                    var v1 = leer_linea(sc, "Ingresa la clave a reemplazar: ");
                    var v2 = leer_entero(sc, "Ingresa el valor de reemplazo: ");
                    Integer previo = mapa.replace(v1, v2); // reemplaza si la clave existe. Retorna valor anterior o null si no existía.
                    System.out.println(previo != null ? "Valor antiguo reemplazado: " + previo : "Clave no encontrada (no se reemplazó).");

                    var v3 = leer_linea(sc, "Ingresa la clave a reemplazar (condicional): ");
                    var v4 = leer_entero(sc, "Ingresa el valor de coincidencia: ");
                    var v5 = leer_entero(sc, "Ingresa valor de reemplazo: ");
                    boolean ok = mapa.replace(v3, v4, v5); // reemplaza solo si coincide valor actual == v4
                    System.out.println(ok ? "Se encontró la coincidencia y se reemplazó." : "No se encontró la coincidencia (no se reemplazó).");
                }

                case 6 -> {
                    // compute: aplica una BiFunction (clave, valorActual) => nuevoValor.
                    // Si la clave no existe o está en null, val llega null. Si la lambda retorna null, se elimina la clave.
                    var v1 = leer_linea(sc, "Ingresa la clave: ");
                    // En este caso: si no existe o está en null -> 1; si existe con valor -> incrementa en 1
                    Integer res = mapa.compute(v1, (key, val) -> (val == null) ? 1 : val + 1);
                    System.out.println("Valor resultante para '" + v1 + "': " + res);
                }

                case 7 -> {
                    // computeIfAbsent: si clave no existe o está en null, calcula e inserta un valor inicial.
                    String v1 = leer_linea(sc, "Ingresa la clave para computeIfAbsent: ");
                    Integer valor = mapa.computeIfAbsent(v1, key -> 0);
                    System.out.println("Valor tras computeIfAbsent: " + valor + " (para clave '" + v1 + "')");

                    // computeIfPresent: solo actúa si la clave existe y su valor es != null. Si lambda retorna null, elimina la clave.
                    String v2 = leer_linea(sc, "Ingresa la clave para computeIfPresent: ");
                    Integer valor2 = mapa.computeIfPresent(v2, (key, val) -> val + 10);
                    System.out.println("Valor tras computeIfPresent (o null si no aplicó): " + valor2 + " (para clave '" + v2 + "')");
                }

                case 8 -> {
                    // merge: combina valor nuevo con valor existente mediante una BiFunction (oldVal, newVal) => resultado.
                    // Si la clave no existe o está en null, asigna directamente el valor nuevo.
                    String k = leer_linea(sc, "Ingresa la clave: ");
                    Integer v = leer_entero(sc, "Ingresa el valor: ");
                    // Aquí usamos suma: si existe, suma old + v; si no, guarda v.
                    Integer res = mapa.merge(k, v, Integer::sum);
                    System.out.println("Valor resultante para '" + k + "': " + res);
                }

                case 9 -> {
                    // Información básica y consultas
                    System.out.println("Tamaño: " + mapa.size());
                    System.out.println(mapa.isEmpty() ? "Está vacío" : "Tiene elementos");

                    var k = leer_linea(sc, "Clave para containsKey: ");
                    System.out.println("containsKey? " + mapa.containsKey(k));

                    var v = leer_entero(sc, "Valor para containsValue: ");
                    System.out.println("containsValue? " + mapa.containsValue(v));
                }

                case 10 -> {
                    // Listar pares clave-valor usando entrySet()
                    for (Map.Entry<String, Integer> e : mapa.entrySet()) {
                        System.out.println("Clave: " + e.getKey() + " | Valor: " + e.getValue());
                    }
                }

                case 11 -> {
                    // Vistas keySet / values: colecciones "en vivo" (reflejan cambios en el mapa)
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Valores: " + mapa.values());
                }

                case 12 -> {
                    // forEach con lambda (K,V)
                    mapa.forEach((k, v) -> System.out.println("[" + k + "] = " + v));
                }

                case 13 -> {
                    // Ordenar por CLAVE (ASC/DESC) convirtiendo las entradas a lista.
                    // 1) Convertimos el entrySet (Set<Entry<K,V>>) en una lista mutable
                    List<Map.Entry<String, Integer>> arr = new ArrayList<>(mapa.entrySet());

                    // Preguntamos el orden
                    String orden = leer_linea(sc, "Orden por clave: escribe 'ASC' o 'DESC': ").trim().toUpperCase();

                    if ("DESC".equals(orden)) {
                        // Descendente por clave
                        arr.sort(Map.Entry.<String, Integer>comparingByKey().reversed());
                    } else {
                        // Ascendente por clave (por defecto)
                        arr.sort(Map.Entry.comparingByKey());
                    }

                    // Mostramos el resultado ordenado (esto NO cambia el mapa, solo la lista)
                    System.out.println("Entradas ordenadas por clave (" + orden + "): " + arr);

                    // Si quisieras reconstruir un mapa ordenado, podrías usar LinkedHashMap:
                    // Map<String,Integer> ordenado = new LinkedHashMap<>();
                    // for (var e : arr) ordenado.put(e.getKey(), e.getValue());
                }

                case 14 -> {
                    // Ordenar por VALOR (ASC/DESC) convirtiendo las entradas a lista.
                    List<Map.Entry<String, Integer>> arr = new ArrayList<>(mapa.entrySet());

                    String orden = leer_linea(sc, "Orden por valor: escribe 'ASC' o 'DESC': ").trim().toUpperCase();

                    if ("DESC".equals(orden)) {
                        arr.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
                    } else {
                        arr.sort(Map.Entry.comparingByValue());
                    }

                    System.out.println("Entradas ordenadas por valor (" + orden + "): " + arr);
                }

                case 15 -> {
                    // replaceAll: transforma en bloque TODOS los valores aplicando una BiFunction (k,v) => nuevoV
                    System.out.println("Sumando +1 a todos los valores (si v == null, poner 0)...");
                    mapa.replaceAll((k, v) -> v == null ? 0 : v + 1); // siempre espera una lambda (BiFunction)
                    System.out.println("Mapa: " + mapa);
                }

                case 16 -> {
                    // Estadísticas (suma/promedio/max/min) – opcional simple
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

                case 17 -> {
                    // Top-N por valor (desc) – simple
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

                case 18 -> {
                    // Invertir: valor -> lista de claves (puede haber múltiples claves con el mismo valor)
                    Map<Integer, List<String>> invertido = new HashMap<>();
                    for (var e : mapa.entrySet()) {
                        invertido.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
                    }
                    System.out.println("Invertido (valor -> claves): " + invertido);
                }

                case 19 -> {
                    // Filtrar por prefijo de clave (stream)
                    String pref = leer_linea(sc, "Prefijo de clave: ");
                    Map<String, Integer> filtrado = mapa.entrySet().stream()
                            .filter(e -> e.getKey() != null && e.getKey().startsWith(pref))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    System.out.println("Filtrado por prefijo '" + pref + "': " + filtrado);
                }

                case 20 -> {
                    // Cargar CSV k:v y fusionar (merge-sum) en el mapa actual
                    Map<String, Integer> otro = otro(sc, "Ingresa CSV k:v (separado por comas, ej: a:1,b:2,c:3): ");
                    // Fusiona sumando valores por clave si ya existen
                    for (var e : otro.entrySet()) {
                        mapa.merge(e.getKey(), e.getValue(), Integer::sum);
                    }
                    System.out.println("Mapa tras fusionar (sumando): " + mapa);
                }

                case 21 -> {
                    // putAll: cargar CSV k:v directo (sin fusionar; simplemente pone/reescribe)
                    Map<String, Integer> otro = otro(sc, "Ingresa CSV k:v (separado por comas, ej: a:1,b:2,c:3): ");
                    mapa.putAll(otro);
                    System.out.println("Mapa tras putAll: " + mapa);
                }

                case 22 -> {
                    // equals / hashCode / copia superficial
                    HashMap<String, Integer> copia = new HashMap<>(mapa);
                    System.out.println("Copia creada. equals(copia)? " + mapa.equals(copia));
                    System.out.println("hashCode original: " + mapa.hashCode());
                    System.out.println("hashCode copia: " + copia.hashCode());
                }

                case 23 -> {
                    // Proyección a Map<String,String> (ejemplo)
                    Map<String, String> proyeccion = mapa.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> "Valor=" + e.getValue()
                            ));
                    System.out.println("Proyección (String->String): " + proyeccion);
                }

                case 24 -> {
                    // clear: vacía el mapa
                    mapa.clear();
                    System.out.println("Mapa limpiado.");
                }

                case 25 -> {
                    // Estado rápido
                    System.out.println("Estado rápido:");
                    System.out.println("Tamaño: " + mapa.size());
                    System.out.println("Claves: " + mapa.keySet());
                    System.out.println("Valores: " + mapa.values());
                    System.out.println("Entradas: " + mapa.entrySet());
                }

                case 26 -> {
                    // Salir
                    System.out.println("Saliendo...");
                }

                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != OPCION_SALIR);

        sc.close();
    }

    // ---------------------------
    //  Funciones de utilidad I/O
    // ---------------------------

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ HASHMAP (String -> Integer) V2 ---");
        System.out.println(" 1 . put (insertar/reemplazar)");
        System.out.println(" 2 . putIfAbsent (insertar si no existe)");
        System.out.println(" 3 . get / getOrDefault");
        System.out.println(" 4 . remove (por clave y condicional clave-valor)");
        System.out.println(" 5 . replace / replace condicional");
        System.out.println(" 6 . compute (incrementar/crear)");
        System.out.println(" 7 . computeIfAbsent / computeIfPresent");
        System.out.println(" 8 . merge (sumar valores por clave)");
        System.out.println(" 9 . size / isEmpty / containsKey / containsValue");
        System.out.println("10 . Listar entrySet");
        System.out.println("11 . Vistas keySet / values");
        System.out.println("12 . forEach");
        System.out.println("13 . Ordenar por clave (ASC/DESC)");
        System.out.println("14 . Ordenar por valor (ASC/DESC)");
        System.out.println("15 . replaceAll (transformación en bloque)");
        System.out.println("16 . Estadísticas (suma/promedio/max/min)");
        System.out.println("17 . Top-N por valor");
        System.out.println("18 . Invertir (valor -> lista de claves)");
        System.out.println("19 . Filtrar por prefijo de clave (stream)");
        System.out.println("20 . Cargar CSV k:v y fusionar (merge-sum)");
        System.out.println("21 . putAll (cargar CSV k:v directo)");
        System.out.println("22 . equals / hashCode / copia");
        System.out.println("23 . Proyección a Map<String,String>");
        System.out.println("24 . clear");
        System.out.println("25 . Estado rápido");
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
