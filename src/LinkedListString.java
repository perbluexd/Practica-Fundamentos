import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * DEMO de LinkedList usada como Deque<String> (cola doble) para trabajar con PALABRAS.
 *
 * 🔹 Estructura base: Deque<String> implementada con LinkedList.
 * 🔹 Permite encolar (FIFO), insertar en índices, recorrer en ambos sentidos, eliminar y limpiar.
 *
 * Complejidad:
 * - Inserción/eliminación en extremos: O(1)
 * - Acceso por índice: O(n)
 * - Mantiene el orden de inserción.
 */
public class LinkedListString {

    private static final int OPCION_SALIR = 9;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            Deque<String> palabras = new LinkedList<>();
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Ingresa la opción que desees: ", 1, OPCION_SALIR);

                switch (opcion) {

                    // ====================================================
                    // 🧠 PATRÓN: CONSTRUIR / AGREGAR
                    // ====================================================
                    case 1 -> {
                        String s = leerLineaNoVacia(sc, "Nueva palabra: ");
                        palabras.addLast(s);
                        System.out.println("✅ Encolada la palabra: " + s);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: ELIMINAR / DESENCOLAR
                    // ====================================================
                    case 2 -> {
                        String quitado = palabras.pollFirst();
                        System.out.println(quitado == null
                                ? "⚠️ La cola está vacía."
                                : "🗑️ Desencolada: " + quitado);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: CONSULTAR / EXTREMOS
                    // ====================================================
                    case 3 -> {
                        if (palabras.isEmpty()) {
                            System.out.println("⚠️ Lista vacía.");
                        } else {
                            System.out.println("Primera palabra: " + palabras.peekFirst());
                            System.out.println("Última palabra:  " + palabras.peekLast());
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: INSERCIÓN ESPECÍFICA (ÍNDICE)
                    // ====================================================
                    case 4 -> {
                        int max = palabras.size();
                        int idx = leerEntero(sc, "Índice (0 - " + max + "): ", 0, max);
                        String valor = leerLineaNoVacia(sc, "Palabra a insertar: ");

                        ListIterator<String> it = ((LinkedList<String>) palabras).listIterator(idx);
                        it.add(valor);
                        System.out.println("✅ Palabra insertada. Índice: " + idx + " | Valor: " + valor);
                    }

                    // ====================================================
                    // 🧠 PATRÓN: ELIMINAR POR VALOR
                    // ====================================================
                    case 5 -> {
                        String eliminado = leerLineaNoVacia(sc, "Palabra a eliminar (exacta): ");
                        boolean ok = palabras.remove(eliminado);
                        System.out.println(ok ? "🗑️ Palabra eliminada." : "❌ Palabra no encontrada.");
                    }

                    // ====================================================
                    // 🧠 PATRÓN: RECORRER / LISTAR
                    // ====================================================
                    case 6 -> {
                        if (palabras.isEmpty()) {
                            System.out.println("⚠️ No hay palabras para listar.");
                        } else {
                            System.out.println("Listado (inicio → fin):");
                            int i = 1;
                            for (String s : palabras) {
                                System.out.println(i++ + ". " + s);
                            }
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: RECORRER INVERSO
                    // ====================================================
                    case 7 -> {
                        if (palabras.isEmpty()) {
                            System.out.println("⚠️ No hay palabras para listar en reversa.");
                        } else {
                            System.out.println("Listado inverso (fin → inicio):");
                            Iterator<String> it = ((LinkedList<String>) palabras).descendingIterator();
                            int i = palabras.size();
                            while (it.hasNext()) {
                                System.out.println(i-- + ". " + it.next());
                            }
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: LIMPIEZA / RESET
                    // ====================================================
                    case 8 -> {
                        palabras.clear();
                        System.out.println("🧹 Lista limpiada (0 elementos).");
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
        System.out.println("\n--- MENÚ LINKEDLIST / DEQUE (Strings) — AGRUPADO POR PATRONES ---");
        System.out.println(" 1. Encolar palabra (CONSTRUIR/AGREGAR)");
        System.out.println(" 2. Desencolar palabra (ELIMINAR)");
        System.out.println(" 3. Ver primera y última (CONSULTAR)");
        System.out.println(" 4. Insertar en posición (INSERCIÓN ESPECÍFICA)");
        System.out.println(" 5. Eliminar por valor (ELIMINAR)");
        System.out.println(" 6. Listar (inicio → fin) (RECORRER)");
        System.out.println(" 7. Listar inverso (fin → inicio) (RECORRER INVERSO)");
        System.out.println(" 8. Limpiar todos (LIMPIEZA)");
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

    static String leerLineaNoVacia(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("⚠️ La entrada no puede estar vacía.");
        }
    }
}
