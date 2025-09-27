import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * DEMO de LinkedList usada como Deque<String> (cola doble) pero ahora con PALABRAS:
 *
 * - Estructura base: Deque<String> implementada con LinkedList.
 * - Permite trabajar con palabras encoladas (FIFO) o insertadas en cualquier índice.
 * - Soporta recorrer en orden normal o inverso, borrar elementos y limpiar la lista completa.
 *
 * Complejidad:
 *   - Inserción/eliminación en extremos: O(1).
 *   - Acceso por índice: O(n).
 *   - Mantiene el orden de inserción (a diferencia de HashSet, que no tiene orden).
 */
public class LinkedListString {
    private static final int OPCION_SALIR = 9;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Deque<String> palabras = new LinkedList<>();
            int opcion;

            do {
                mostrarMenu();
                // Lee opción con validación [1..9]
                opcion = leerEntero(sc, "Ingresa la opción que desees: ", 1, OPCION_SALIR);

                switch (opcion) {

                    case 1 -> {
                        // Encolar palabra al final (típico comportamiento de una cola FIFO)
                        String s = leerLineaNoVacia(sc, "Nueva palabra: ");
                        palabras.addLast(s); // encolamos al final
                        System.out.println("Encolada la palabra: " + s);
                    }

                    case 2 -> {
                        // Desencolar palabra del inicio
                        String quitado = palabras.pollFirst(); // retorna null si está vacía
                        System.out.println(quitado == null ? "La cola está vacía." : "Desencolada: " + quitado);
                    }

                    case 3 -> {
                        // Consultar extremos sin quitarlos
                        if (palabras.isEmpty()) {
                            System.out.println("Lista de palabras vacía.");
                        } else {
                            System.out.println("Primero: " + palabras.peekFirst()); // primera palabra
                            System.out.println("Último:  " + palabras.peekLast());  // última palabra
                        }
                    }

                    case 4 -> {
                        // Insertar en posición arbitraria (índice) usando ListIterator
                        int max = palabras.size(); // se permite insertar al final
                        int idx = leerEntero(sc, "Índice (0 - " + max + "): ", 0, max);
                        String valor = leerLineaNoVacia(sc, "Palabra a insertar: ");

                        // Para acceder a listIterator necesitamos el tipo concreto LinkedList
                        ListIterator<String> it = ((LinkedList<String>) palabras).listIterator(idx);
                        it.add(valor); // inserta en esa posición
                        System.out.println("Palabra insertada. Índice: " + idx + " | Valor: " + valor);
                    }

                    case 5 -> {
                        // Eliminar por valor exacto
                        String eliminado = leerLineaNoVacia(sc, "Palabra a eliminar (exacta): ");
                        boolean ok = palabras.remove(eliminado); // elimina primera ocurrencia
                        System.out.println(ok ? "Palabra eliminada." : "Palabra no encontrada.");
                    }

                    case 6 -> {
                        // Listar en orden normal (inicio → fin)
                        if (palabras.isEmpty()) {
                            System.out.println("No hay palabras para listar.");
                        } else {
                            System.out.println("Listado de palabras (inicio → fin):");
                            int i = 1;
                            for (String s : palabras) {
                                System.out.println(i++ + ". " + s);
                            }
                        }
                    }

                    case 7 -> {
                        // Listar en orden inverso (fin → inicio)
                        if (palabras.isEmpty()) {
                            System.out.println("No hay palabras para listar en reversa.");
                        } else {
                            System.out.println("Listado inverso (fin → inicio):");
                            // descendingIterator recorre en sentido inverso
                            Iterator<String> it = ((LinkedList<String>) palabras).descendingIterator();
                            int i = palabras.size();
                            while (it.hasNext()) {
                                System.out.println(i-- + ". " + it.next());
                            }
                        }
                    }

                    case 8 -> {
                        // Limpiar toda la lista
                        palabras.clear();
                        System.out.println("Lista limpiada. (0 elementos)");
                    }

                    case OPCION_SALIR -> {
                        // Salida del programa
                        System.out.println("Saliendo... ¡Gracias por usar el programa!");
                    }

                    default -> System.out.println("Opción inválida.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    /** Muestra el menú principal con todas las opciones. */
    static void mostrarMenu() {
        System.out.println("""
                
                --- MENÚ LINKEDLIST / DEQUE (Palabras) ---
                1. Encolar palabra (al final)
                2. Desencolar palabra (del inicio)
                3. Ver primera y última (sin quitar)
                4. Insertar en posición (índice)
                5. Eliminar por valor (exacto)
                6. Listar (inicio → fin)
                7. Listar inverso (fin → inicio)
                8. Limpiar todos
                9. Salir
                """);
    }

    // -------- Helpers --------

    /** Lee entero sin rango (valida entrada). */
    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Dato inválido. Inténtalo de nuevo.");
            }
        }
    }

    /** Lee entero con rango inclusivo [min, max]. */
    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Fuera de rango (" + min + " - " + max + "). Intenta de nuevo.");
                continue;
            }
            return n;
        }
    }

    /** Lee una línea no vacía (para Strings). */
    static String leerLineaNoVacia(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("La entrada no puede estar vacía. Inténtalo otra vez.");
        }
    }
}
