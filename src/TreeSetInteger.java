import java.util.*;

public class TreeSetInteger {
    // Constante que define la opción para salir del menú
    public static final int OPCION_SALIR = 12;

    public static void main(String[] args) {
        // Uso de try-with-resources para cerrar automáticamente el Scanner
        try (Scanner sc = new Scanner(System.in)) {
            // TreeSet para almacenar enteros en orden natural (ascendente y sin repetidos)
            TreeSet<Integer> numeros = new TreeSet<>();
            int opcion;

            // Ciclo principal del menú
            do {
                mostrarMenu(); // Muestra las opciones
                // Lee la opción del usuario validando rango
                opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

                // Según la opción seleccionada, se ejecuta un bloque de código
                switch (opcion) {
                    case 1 -> { // add
                        // Agregar número al conjunto
                        int n = leerEntero(sc, "Ingresa el número a agregar: ");
                        boolean ok = numeros.add(n); // add devuelve false si ya existe
                        System.out.println(ok ? "Agregado." : "Ya existía (no se repite).");
                    }

                    case 2 -> { // Listar en orden natural
                        // Imprime el conjunto en orden ascendente
                        System.out.println(numeros.isEmpty() ? "Conjunto vacío." : "Orden natural: " + numeros);
                    }

                    case 3 -> { // first / last
                        // Muestra el primer y último elemento
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.println("first(): " + numeros.first()); // menor
                            System.out.println("last():  " + numeros.last());  // mayor
                        }
                    }

                    case 4 -> { // lower / higher / ceiling / floor
                        // Busca vecinos alrededor de un número de referencia
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            int clave = leerEntero(sc, "Elemento de referencia: ");
                            System.out.println("lower(" + clave + "):   " + numeros.lower(clave));    // menor estrictamente
                            System.out.println("higher(" + clave + "):  " + numeros.higher(clave));  // mayor estrictamente
                            System.out.println("ceiling(" + clave + "): " + numeros.ceiling(clave)); // mayor o igual
                            System.out.println("floor(" + clave + "):   " + numeros.floor(clave));   // menor o igual
                        }
                    }

                    case 5 -> { // headSet / tailSet
                        // Muestra subconjuntos con base en rangos
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            int hasta = leerEntero(sc, "headSet hasta (exclusivo): ");
                            int desde = leerEntero(sc, "tailSet desde (inclusivo): ");
                            System.out.println("headSet(< " + hasta + "): " + numeros.headSet(hasta));   // elementos < hasta
                            System.out.println("tailSet(>= " + desde + "): " + numeros.tailSet(desde)); // elementos >= desde
                        }
                    }

                    case 6 -> { // subSet
                        // Muestra subconjuntos exclusivos e inclusivos
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            int from = leerEntero(sc, "subSet desde: ");
                            int to   = leerEntero(sc, "subSet hasta: ");
                            // Subconjunto exclusivo [from, to)
                            SortedSet<Integer> subEx = numeros.subSet(from, to);
                            System.out.println("subSet exclusivo [" + from + ", " + to + "): " + subEx);

                            // Subconjunto inclusivo [from, to]
                            NavigableSet<Integer> subInc = numeros.subSet(from, true, to, true);
                            System.out.println("subSet inclusivo [" + from + ", " + to + "]: " + subInc);
                        }
                    }

                    case 7 -> { // descendingSet
                        // Vista descendente del conjunto
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            NavigableSet<Integer> desc = numeros.descendingSet();
                            System.out.println("Vista descendente: " + desc);
                        }
                    }

                    case 8 -> { // descendingIterator
                        // Recorre el conjunto en orden descendente con un iterador
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            System.out.print("descendingIterator(): ");
                            Iterator<Integer> itDesc = numeros.descendingIterator();
                            while (itDesc.hasNext()) System.out.print(itDesc.next() + " ");
                            System.out.println();
                        }
                    }

                    case 9 -> { // pollFirst / pollLast
                        // Extrae y elimina el primer y último elemento
                        if (numeros.isEmpty()) System.out.println("Conjunto vacío.");
                        else {
                            Integer first = numeros.pollFirst(); // elimina el menor
                            System.out.println("pollFirst() → " + first);
                            if (!numeros.isEmpty()) {
                                Integer last = numeros.pollLast(); // elimina el mayor
                                System.out.println("pollLast()  → " + last);
                            }
                            System.out.println("Después: " + numeros);
                        }
                    }

                    case 10 -> { // comparator
                        // Muestra el comparador usado por el TreeSet
                        Comparator<? super Integer> comp = numeros.comparator();
                        System.out.println("Comparator usado: " + (comp == null ? "orden natural" : comp));
                    }

                    case 11 -> { // Comparator personalizado
                        // Crea un TreeSet ordenado por el último dígito de cada número
                        System.out.println("Creando vista POR ÚLTIMO DÍGITO (Comparator)...");
                        Comparator<Integer> compUltimoDigito =
                                Comparator.comparingInt((Integer i) -> i % 10) // ordenar por último dígito
                                        .thenComparingInt(i -> i);            // desempatar por valor natural
                        // Nuevo TreeSet con ese criterio
                        TreeSet<Integer> porUltimoDigito = new TreeSet<>(compUltimoDigito);
                        porUltimoDigito.addAll(numeros); // copiamos todos los números
                        System.out.println("Orden por último dígito: " + porUltimoDigito);
                    }

                    case 12 -> System.out.println("Saliendo..."); // Finaliza el programa

                    default -> System.out.println("Ingresa un valor válido."); // Manejo de error
                }

            } while (opcion != OPCION_SALIR); // Repite hasta que se elija salir
        }
    }

    // --------- Helpers ---------

    // Muestra las opciones del menú
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

    // Lee un entero sin restricciones
    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim()); // Convierte el texto a int
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
            }
        }
    }

    // Lee un entero dentro de un rango [min, max]
    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int v = leerEntero(sc, prompt); // reutiliza el método anterior
            if (v >= min && v <= max) return v;
            System.out.println("Ingresa un número entre " + min + " y " + max + ".");
        }
    }
}
