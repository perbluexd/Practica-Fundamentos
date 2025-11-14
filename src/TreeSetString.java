import java.util.*;

public class TreeSetString {

    // =========================
    // Constantes / Config
    // =========================
    public static final int OPCION_SALIR = 12;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            TreeSet<String> palabras = new TreeSet<>(); // orden natural (alfabético)
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

                switch (opcion) {

                    // ============================================================
                    // 🧠 PATRÓN 1: INSERCIÓN / POBLACIÓN
                    // ============================================================
                    case 1 -> {
                        String s = leerLineaNoVacia(sc, "Ingresa la palabra a agregar: ");
                        boolean ok = palabras.add(s); // false si ya existía
                        System.out.println(ok ? "Agregado." : "Ya existía (no se repite).");
                    }

                    // ============================================================
                    // 🧠 PATRÓN 2: CONSULTAS BÁSICAS (LISTADO / EXTREMOS)
                    // ============================================================
                    case 2 -> { // listar en orden natural
                        System.out.println(palabras.isEmpty() ? "Conjunto vacío." : "Orden natural: " + palabras);
                    }

                    case 3 -> { // first / last
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.println("first(): " + palabras.first());
                            System.out.println("last():  " + palabras.last());
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 3: VECINOS / BÚSQUEDA POR PROXIMIDAD
                    // ============================================================
                    case 4 -> { // lower / higher / ceiling / floor
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String clave = leerLineaNoVacia(sc, "Elemento de referencia: ");
                            System.out.println("lower(" + clave + "):   " + palabras.lower(clave));    // < clave
                            System.out.println("higher(" + clave + "):  " + palabras.higher(clave));  // > clave
                            System.out.println("ceiling(" + clave + "): " + palabras.ceiling(clave)); // >= clave
                            System.out.println("floor(" + clave + "):   " + palabras.floor(clave));   // <= clave
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 4: VISTAS POR RANGO (HEAD/TAIL/SUBSET)
                    // ============================================================
                    case 5 -> { // headSet / tailSet
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String hasta = leerLineaNoVacia(sc, "headSet hasta (exclusivo): ");
                            String desde = leerLineaNoVacia(sc, "tailSet desde (inclusivo): ");
                            System.out.println("headSet(< " + hasta + "): " + palabras.headSet(hasta));
                            System.out.println("tailSet(>= " + desde + "): " + palabras.tailSet(desde));
                        }
                    }

                    case 6 -> { // subSet exclusivo e inclusivo
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String from = leerLineaNoVacia(sc, "subSet desde: ");
                            String to   = leerLineaNoVacia(sc, "subSet hasta: ");
                            SortedSet<String> subEx = palabras.subSet(from, to); // [from, to)
                            System.out.println("subSet exclusivo [" + from + ", " + to + "): " + subEx);

                            NavigableSet<String> subInc = palabras.subSet(from, true, to, true); // [from, to]
                            System.out.println("subSet inclusivo [" + from + ", " + to + "]: " + subInc);
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 5: ÓRDENES INVERSOS / RECORRIDOS
                    // ============================================================
                    case 7 -> { // descendingSet
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else System.out.println("Vista descendente: " + palabras.descendingSet());
                    }

                    case 8 -> { // descendingIterator
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.print("descendingIterator(): ");
                            Iterator<String> itDesc = palabras.descendingIterator();
                            while (itDesc.hasNext()) System.out.print(itDesc.next() + " ");
                            System.out.println();
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 6: EXTRACCIÓN DE EXTREMOS (MUTABLE)
                    // ============================================================
                    case 9 -> { // pollFirst / pollLast
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String first = palabras.pollFirst(); // elimina el primero
                            System.out.println("pollFirst() → " + first);
                            if (!palabras.isEmpty()) {
                                String last = palabras.pollLast(); // elimina el último
                                System.out.println("pollLast()  → " + last);
                            }
                            System.out.println("Después: " + palabras);
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 7: COMPARADORES
                    // ============================================================
                    case 10 -> { // ver comparator usado
                        Comparator<? super String> comp = palabras.comparator();
                        System.out.println("Comparator usado: " + (comp == null ? "orden natural" : comp));
                    }

                    case 11 -> { // demo comparator personalizado (longitud)
                        System.out.println("Creando vista POR LONGITUD (Comparator)...");
                        TreeSet<String> porLongitud = new TreeSet<>(
                                Comparator.comparingInt(String::length)
                                        .thenComparing(Comparator.naturalOrder())
                        );
                        porLongitud.addAll(palabras);
                        System.out.println("Orden por longitud: " + porLongitud);
                    }

                    // ============================================================
                    // 🧠 PATRÓN 8: SALIR
                    // ============================================================
                    case 12 -> System.out.println("Saliendo...");

                    default -> System.out.println("Ingresa un valor válido.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    // =======================
    // Helpers (I/O)
    // =======================
    static void mostrarMenu() {
        System.out.println("""
                
                --- MENÚ TREESET (sólo funciones que NO tienen HashSet/LinkedHashSet) ---
                1  . Agregar (add)  *solo para poblar
                2  . Listar (orden natural)
                3  . first / last
                4  . lower / higher / ceiling / floor
                5  . headSet / tailSet
                6  . subSet (exclusivo e inclusivo)
                7  . descendingSet (vista inversa)
                8  . descendingIterator
                9  . pollFirst / pollLast
                10 . Ver comparator usado
                11 . Demo: TreeSet con Comparator (longitud)
                12 . Salir
                """);
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
            int v = leerEntero(sc, prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Ingresa un número entre " + min + " y " + max + ".");
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
}
