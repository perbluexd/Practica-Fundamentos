import java.util.*;

public class TreeSetInteger {

    // =========================
    // Constantes / Config
    // =========================
    public static final int OPCION_SALIR = 12;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            TreeSet<Integer> numeros = new TreeSet<>();
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

                switch (opcion) {

                    // ============================================================
                    // 🧠 PATRÓN 1: INSERCIÓN / POBLACIÓN
                    // ============================================================
                    case 1 -> { // add
                        int n = leerEntero(sc, "Ingresa el número a agregar: ");
                        boolean ok = numeros.add(n); // false si ya existía
                        System.out.println(ok ? "Agregado." : "Ya existía (no se repite).");
                    }

                    // ============================================================
                    // 🧠 PATRÓN 2: CONSULTAS BÁSICAS (LISTADO / EXTREMOS)
                    // ============================================================
                    case 2 -> { // listar en orden natural
                        System.out.println(numeros.isEmpty() ? "Conjunto vacío." : "Orden natural: " + numeros);
                    }

                    case 3 -> { // first / last
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.println("first(): " + numeros.first());
                            System.out.println("last():  " + numeros.last());
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 3: VECINOS / BÚSQUEDA POR PROXIMIDAD
                    // ============================================================
                    case 4 -> { // lower / higher / ceiling / floor
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            int clave = leerEntero(sc, "Elemento de referencia: ");
                            System.out.println("lower(" + clave + "):   " + numeros.lower(clave));    // < clave
                            System.out.println("higher(" + clave + "):  " + numeros.higher(clave));  // > clave
                            System.out.println("ceiling(" + clave + "): " + numeros.ceiling(clave)); // >= clave
                            System.out.println("floor(" + clave + "):   " + numeros.floor(clave));   // <= clave
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 4: VISTAS POR RANGO (HEAD/TAIL/SUBSET)
                    // ============================================================
                    case 5 -> { // headSet / tailSet
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            int hasta = leerEntero(sc, "headSet hasta (exclusivo): ");
                            int desde = leerEntero(sc, "tailSet desde (inclusivo): ");
                            System.out.println("headSet(< " + hasta + "): " + numeros.headSet(hasta));
                            System.out.println("tailSet(>= " + desde + "): " + numeros.tailSet(desde));
                        }
                    }

                    case 6 -> { // subSet exclusivo e inclusivo
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            int from = leerEntero(sc, "subSet desde: ");
                            int to   = leerEntero(sc, "subSet hasta: ");
                            SortedSet<Integer> subEx = numeros.subSet(from, to); // [from, to)
                            System.out.println("subSet exclusivo [" + from + ", " + to + "): " + subEx);

                            NavigableSet<Integer> subInc = numeros.subSet(from, true, to, true); // [from, to]
                            System.out.println("subSet inclusivo [" + from + ", " + to + "]: " + subInc);
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 5: ÓRDENES INVERSOS / RECORRIDOS
                    // ============================================================
                    case 7 -> { // descendingSet
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else System.out.println("Vista descendente: " + numeros.descendingSet());
                    }

                    case 8 -> { // descendingIterator
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.print("descendingIterator(): ");
                            Iterator<Integer> itDesc = numeros.descendingIterator();
                            while (itDesc.hasNext()) System.out.print(itDesc.next() + " ");
                            System.out.println();
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 6: EXTRACCIÓN DE EXTREMOS (MUTABLE)
                    // ============================================================
                    case 9 -> { // pollFirst / pollLast
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            Integer first = numeros.pollFirst(); // elimina menor
                            System.out.println("pollFirst() → " + first);
                            if (!numeros.isEmpty()) {
                                Integer last = numeros.pollLast(); // elimina mayor
                                System.out.println("pollLast()  → " + last);
                            }
                            System.out.println("Después: " + numeros);
                        }
                    }

                    // ============================================================
                    // 🧠 PATRÓN 7: COMPARADORES
                    // ============================================================
                    case 10 -> { // ver comparator usado
                        Comparator<? super Integer> comp = numeros.comparator();
                        System.out.println("Comparator usado: " + (comp == null ? "orden natural" : comp));
                    }

                    case 11 -> { // demo comparator personalizado (último dígito)
                        System.out.println("Creando vista POR ÚLTIMO DÍGITO (Comparator)...");
                        Comparator<Integer> compUltimoDigito =
                                Comparator.comparingInt((Integer i) -> i % 10)
                                        .thenComparingInt(i -> i); // desempate por orden natural
                        TreeSet<Integer> porUltimoDigito = new TreeSet<>(compUltimoDigito);
                        porUltimoDigito.addAll(numeros);
                        System.out.println("Orden por último dígito: " + porUltimoDigito);
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
                
                --- MENÚ TREESET INTEGER ---
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
                11 . Demo: TreeSet con Comparator (último dígito)
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
}
