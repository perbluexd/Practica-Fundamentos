import java.util.*;
import java.util.function.Predicate;

public class TreeSetString {
    // Constante para indicar la opción de salir
    public static final int OPCION_SALIR = 12;

    public static void main(String[] args) {
        // try-with-resources: cierra el Scanner automáticamente al salir
        try (Scanner sc = new Scanner(System.in)) {
            // TreeSet para almacenar palabras ordenadas alfabéticamente (orden natural de String)
            TreeSet<String> palabras = new TreeSet<>();
            int opcion;

            // Bucle principal del menú
            do {
                mostrarMenu(); // muestra opciones
                opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

                switch (opcion) {
                    case 1 -> { // add
                        // Agregar una palabra al conjunto
                        var s = leerLineaNoVacia(sc, "Ingresa la palabra a agregar: ");
                        boolean ok = palabras.add(s); // false si ya existía
                        System.out.println(ok ? "Agregado." : "Ya existía (no se repite).");
                    }

                    case 2 -> { // Listar en orden natural
                        // Muestra todas las palabras en orden alfabético
                        System.out.println(palabras.isEmpty() ? "Conjunto vacío." : "Orden natural: " + palabras);
                    }

                    case 3 -> { // first / last
                        // Obtiene la primera y última palabra del TreeSet
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.println("first(): " + palabras.first()); // menor (alfabéticamente)
                            System.out.println("last():  " + palabras.last());  // mayor (alfabéticamente)
                        }
                    }

                    case 4 -> { // lower / higher / ceiling / floor
                        // Métodos de navegación relativos a un elemento dado
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String clave = leerLineaNoVacia(sc, "Elemento de referencia: ");
                            System.out.println("lower(" + clave + "):   " + palabras.lower(clave));    // inmediatamente menor
                            System.out.println("higher(" + clave + "):  " + palabras.higher(clave));  // inmediatamente mayor
                            System.out.println("ceiling(" + clave + "): " + palabras.ceiling(clave)); // igual o mayor
                            System.out.println("floor(" + clave + "):   " + palabras.floor(clave));   // igual o menor
                        }
                    }

                    case 5 -> { // headSet / tailSet
                        // Subconjuntos basados en rangos
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String hasta = leerLineaNoVacia(sc, "headSet hasta (exclusivo): ");
                            String desde = leerLineaNoVacia(sc, "tailSet desde (inclusivo): ");
                            System.out.println("headSet(< " + hasta + "): " + palabras.headSet(hasta));   // todo lo menor que "hasta"
                            System.out.println("tailSet(>= " + desde + "): " + palabras.tailSet(desde)); // todo lo mayor o igual que "desde"
                        }
                    }

                    case 6 -> { // subSet exclusivo e inclusivo
                        // Muestra subconjuntos con distintos límites
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            String from = leerLineaNoVacia(sc, "subSet desde: ");
                            String to   = leerLineaNoVacia(sc, "subSet hasta: ");

                            // Subconjunto exclusivo: [from, to)
                            SortedSet<String> subEx = palabras.subSet(from, to);
                            System.out.println("subSet exclusivo [" + from + ", " + to + "): " + subEx);

                            // Subconjunto inclusivo: [from, to]
                            NavigableSet<String> subInc = palabras.subSet(from, true, to, true);
                            System.out.println("subSet inclusivo [" + from + ", " + to + "]: " + subInc);
                        }
                    }

                    case 7 -> { // descendingSet
                        // Vista invertida del TreeSet (orden descendente)
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            NavigableSet<String> desc = palabras.descendingSet();
                            // ¡Ojo! Esta vista está vinculada al set original, no es copia independiente
                            System.out.println("Vista descendente: " + desc);
                        }
                    }

                    case 8 -> { // descendingIterator
                        // Iterador para recorrer en orden descendente
                        if (palabras.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.print("descendingIterator(): ");
                            Iterator<String> itDesc = palabras.descendingIterator();
                            while (itDesc.hasNext()) System.out.print(itDesc.next() + " ");
                            System.out.println();
                        }
                    }

                    case 9 -> { // pollFirst / pollLast
                        // Extrae y elimina los extremos
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

                    case 10 -> { // comparator
                        // Muestra el comparador usado en el TreeSet
                        Comparator<? super String> comp = palabras.comparator();
                        System.out.println("Comparator usado: " + (comp == null ? "orden natural" : comp));
                    }

                    case 11 -> { // Comparator personalizado
                        // Nuevo TreeSet ordenado por longitud de palabra
                        System.out.println("Creando vista POR LONGITUD (Comparator)...");
                        TreeSet<String> porLongitud = new TreeSet<>(Comparator
                                .comparingInt(String::length)                 // primero por longitud
                                .thenComparing(Comparator.naturalOrder()));  // en caso de empate, por orden natural
                        porLongitud.addAll(palabras); // se copian todas las palabras
                        System.out.println("Orden por longitud: " + porLongitud);
                    }

                    case 12 -> System.out.println("Saliendo..."); // Fin del programa

                    default -> System.out.println("Ingresa un valor válido.");
                }

            } while (opcion != OPCION_SALIR); // ciclo hasta que se elija salir
        }
    }

    // --------- Helpers ---------

    // Imprime el menú con todas las opciones disponibles
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

    // Lee un entero desde consola, validando que sea un número
    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim()); // intenta convertir
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
            }
        }
    }

    // Lee un entero restringido a un rango [min, max]
    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int v = leerEntero(sc, prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Ingresa un número entre " + min + " y " + max + ".");
        }
    }

    // Lee una cadena que no esté vacía
    static String leerLineaNoVacia(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.isBlank()) return s.trim(); // valida que no sea vacío
            System.out.println("Entrada vacía. Intenta de nuevo.");
        }
    }
}
