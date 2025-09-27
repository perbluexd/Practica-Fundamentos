import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * DEMO de LinkedList usada como Deque<Integer> (cola doble):
 *
 * - Usamos la INTERFAZ Deque<Integer> con IMPLEMENTACIÓN LinkedList<>.
 * - Métodos típicos:
 *    addFirst/addLast       → encolar al inicio/final (lanza excepción si falla)
 *    offerFirst/offerLast   → encolar (retorna boolean, no lanza excepción)
 *    pollFirst/pollLast     → desencolar (retorna null si vacío)
 *    peekFirst/peekLast     → ver extremos sin quitar (retorna null si vacío)
 * - También aprovechamos métodos propios de LinkedList (como listIterator(idx) y descendingIterator()).
 *
 * Complejidad:
 * - Inserciones/eliminaciones en extremos: O(1).
 * - Acceso por índice: O(n) (LinkedList no es aleatorio).
 */
public class LinkedListInteger {
    private static final int OPCION_SALIR = 9;

    public static void main(String[] args) {
        // try-with-resources: cierra el Scanner automáticamente al salir.
        try (Scanner sc = new Scanner(System.in)) {
            // Estructura base: Deque con implementación LinkedList (doble extremo).
            Deque<Integer> numeros = new LinkedList<>();
            int opcion;

            do {
                mostrarMenu();
                // Lee opción y valida en rango [1..OPCION_SALIR]
                opcion = leerEntero(sc, "Ingresa la opción que desees: ", 1, OPCION_SALIR);

                switch (opcion) {

                    case 1 -> {
                        // Encolar al final (cola FIFO típica)
                        int n = leerEntero(sc, "Nuevo número: ");
                        // addLast lanza excepción en estructuras acotadas; aquí no aplica (LinkedList sin límite).
                        numeros.addLast(n);
                        System.out.println("Encolado el número (al final): " + n);
                    }

                    case 2 -> {
                        // Desencolar del inicio (FIFO)
                        // pollFirst retorna null si la deque está vacía (no lanza excepción).
                        Integer quitado = numeros.pollFirst();
                        System.out.println(quitado == null ? "La cola está vacía." : "Desencolado (inicio): " + quitado);
                    }

                    case 3 -> {
                        // Consultar extremos sin quitar
                        if (numeros.isEmpty()) {
                            System.out.println("Lista de números vacía.");
                        } else {
                            // peekFirst/peekLast retornan null si están vacías (aquí ya validamos).
                            System.out.println("Primero: " + numeros.peekFirst());
                            System.out.println("Último:  " + numeros.peekLast());
                        }
                    }

                    case 4 -> {
                        // Insertar por índice arbitrario usando ListIterator (API específica de LinkedList)
                        int max = numeros.size(); // Permitimos insertar al final (índice == size)
                        int idx = leerEntero(sc, "Índice (0 - " + max + "): ", 0, max);
                        int valor = leerEntero(sc, "Número a insertar: ");

                        // Para usar listIterator(idx) necesitamos el tipo LinkedList concreto
                        ListIterator<Integer> it = ((LinkedList<Integer>) numeros).listIterator(idx);
                        it.add(valor); // Inserta ANTES de la posición actual del iterador
                        System.out.println("Número insertado. Índice: " + idx + " | Valor: " + valor);
                    }

                    case 5 -> {
                        // Eliminar por valor (la primera ocurrencia)
                        int eliminado = leerEntero(sc, "Número a eliminar (exacto): ");
                        // Deque hereda remove(Object) -> elimina primera aparición del elemento
                        boolean ok = numeros.remove(eliminado);
                        System.out.println(ok ? "Número eliminado." : "Número no encontrado.");
                    }

                    case 6 -> {
                        // Listar en orden de inicio → fin
                        if (numeros.isEmpty()) {
                            System.out.println("No hay números para listar.");
                        } else {
                            System.out.println("Listado de números (inicio → fin):");
                            int i = 1;
                            for (int n : numeros) {
                                System.out.println(i++ + ". " + n);
                            }
                        }
                    }

                    case 7 -> {
                        // Listar en orden inverso (fin → inicio)
                        if (numeros.isEmpty()) {
                            System.out.println("No hay números para listar en reversa.");
                        } else {
                            System.out.println("Listado inverso (fin → inicio):");
                            // descendingIterator() es propio de LinkedList (no está en Deque genérico)
                            Iterator<Integer> it = ((LinkedList<Integer>) numeros).descendingIterator();
                            int i = numeros.size();
                            while (it.hasNext()) {
                                System.out.println(i-- + ". " + it.next());
                            }
                        }
                    }

                    case 8 -> {
                        // Limpiar toda la lista
                        numeros.clear();
                        System.out.println("Lista limpiada. (0 elementos)");
                    }

                    case OPCION_SALIR -> {
                        // Salir del programa
                        System.out.println("Saliendo... ¡Gracias por usar el programa!");
                    }

                    default -> System.out.println("Opción inválida.");
                }
            } while (opcion != OPCION_SALIR);
        }
    }

    /** Muestra el menú principal de operaciones sobre la Deque. */
    static void mostrarMenu() {
        System.out.println("""
                
                --- MENÚ LINKEDLIST / DEQUE (Números) ---
                1. Encolar número (al final)
                2. Desencolar número (del inicio)
                3. Ver primero y último (sin quitar)
                4. Insertar en posición (índice)
                5. Eliminar por valor (exacto)
                6. Listar (inicio → fin)
                7. Listar inverso (fin → inicio)
                8. Limpiar todos
                9. Salir
                """);
    }

    // ----------------- Helpers de entrada -----------------

    /** Lee entero sin rango (reintenta hasta que sea válido). */
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

    /** Lee entero validado en el rango inclusivo [min, max]. */
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
}
