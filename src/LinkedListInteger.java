import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * DEMO de LinkedList usada como Deque<Integer> (cola doble):
 *
 * 🔹 Usamos la INTERFAZ Deque<Integer> con IMPLEMENTACIÓN LinkedList<>.
 * 🔹 Métodos típicos:
 *    addFirst/addLast       → encolar al inicio/final (lanza excepción si falla)
 *    offerFirst/offerLast   → encolar (retorna boolean)
 *    pollFirst/pollLast     → desencolar (retorna null si vacío)
 *    peekFirst/peekLast     → ver extremos sin quitar
 *
 * 🔹 También aprovechamos métodos propios de LinkedList (listIterator, descendingIterator).
 *
 * Complejidad:
 * - Inserciones/eliminaciones en extremos: O(1)
 * - Acceso por índice: O(n)
 */
public class LinkedListInteger {

    private static final int OPCION_SALIR = 9;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            Deque<Integer> numeros = new LinkedList<>();
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Ingresa la opción que desees: ", 1, OPCION_SALIR);

                switch (opcion) {

                    // ====================================================
                    // 🧠 PATRÓN: CONSTRUIR / AGREGAR
                    // ====================================================
                    case 1 -> {
                        int n = leerEntero(sc, "Nuevo número: ");
                        numeros.addLast(n); // Encolar al final (FIFO)
                        System.out.println("✅ Encolado el número (al final): " + n);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: ELIMINAR / DESENCOLAR
                    // ====================================================
                    case 2 -> {
                        Integer quitado = numeros.pollFirst(); // Desencolar del inicio
                        System.out.println(quitado == null
                                ? "⚠️ La cola está vacía."
                                : "🗑️ Desencolado (inicio): " + quitado);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: CONSULTAR / EXTREMOS
                    // ====================================================
                    case 3 -> {
                        if (numeros.isEmpty()) {
                            System.out.println("⚠️ Lista vacía.");
                        } else {
                            System.out.println("Primero: " + numeros.peekFirst());
                            System.out.println("Último:  " + numeros.peekLast());
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: INSERCIÓN ESPECÍFICA (ÍNDICE)
                    // ====================================================
                    case 4 -> {
                        int max = numeros.size();
                        int idx = leerEntero(sc, "Índice (0 - " + max + "): ", 0, max);
                        int valor = leerEntero(sc, "Número a insertar: ");

                        ListIterator<Integer> it = ((LinkedList<Integer>) numeros).listIterator(idx);
                        it.add(valor); // Inserta antes de la posición actual
                        System.out.println("✅ Insertado en índice " + idx + ": " + valor);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: ELIMINAR POR VALOR
                    // ====================================================
                    case 5 -> {
                        int eliminado = leerEntero(sc, "Número a eliminar (exacto): ");
                        boolean ok = numeros.remove(eliminado);
                        System.out.println(ok ? "🗑️ Número eliminado." : "❌ Número no encontrado.");
                    }

                    // ====================================================
                    // 🧠 PATRÓN: RECORRER / LISTAR
                    // ====================================================
                    case 6 -> {
                        if (numeros.isEmpty()) {
                            System.out.println("⚠️ No hay números para listar.");
                        } else {
                            System.out.println("Listado (inicio → fin):");
                            int i = 1;
                            for (int n : numeros) {
                                System.out.println(i++ + ". " + n);
                            }
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: RECORRER INVERSO
                    // ====================================================
                    case 7 -> {
                        if (numeros.isEmpty()) {
                            System.out.println("⚠️ No hay números para listar en reversa.");
                        } else {
                            System.out.println("Listado inverso (fin → inicio):");
                            Iterator<Integer> it = ((LinkedList<Integer>) numeros).descendingIterator();
                            int i = numeros.size();
                            while (it.hasNext()) {
                                System.out.println(i-- + ". " + it.next());
                            }
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: LIMPIEZA / RESET
                    // ====================================================
                    case 8 -> {
                        numeros.clear();
                        System.out.println("🧹 Lista limpiada. (0 elementos)");
                    }

                    // ====================================================
                    // 🏁 SALIR
                    // ====================================================
                    case OPCION_SALIR -> System.out.println("👋 Saliendo... ¡Gracias por usar el programa!");

                    default -> System.out.println("Opción inválida.");
                }

            } while (opcion != OPCION_SALIR);
        }
    }

    // ==========================================================
    // 🔧 UTILIDADES / INPUT HELPERS
    // ==========================================================

    static void mostrarMenu() {
        System.out.println("\n--- MENÚ LINKEDLIST / DEQUE (Integer) — AGRUPADO POR PATRONES ---");
        System.out.println(" 1. Encolar número (CONSTRUIR/AGREGAR)");
        System.out.println(" 2. Desencolar (ELIMINAR)");
        System.out.println(" 3. Ver primero y último (CONSULTAR)");
        System.out.println(" 4. Insertar en posición (INSERCIÓN ESPECÍFICA)");
        System.out.println(" 5. Eliminar por valor (ELIMINAR)");
        System.out.println(" 6. Listar inicio → fin (RECORRER)");
        System.out.println(" 7. Listar fin → inicio (RECORRER INVERSO)");
        System.out.println(" 8. Limpiar todos (RESET)");
        System.out.println(" 9. Salir");
    }

    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Dato inválido. Inténtalo de nuevo.");
            }
        }
    }

    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("⚠️ Fuera de rango (" + min + " - " + max + ").");
                continue;
            }
            return n;
        }
    }
}
