import java.util.ArrayList;
import java.util.Scanner;

public class ArrayListDemo {
    public static void main(String[] args) {
        // ====================================================
        // 🧱 SETUP / I/O (infra de la demo)
        // ====================================================
        try (Scanner sc = new Scanner(System.in)) {
            ArrayList<String> nombres = new ArrayList<>();
            int opcion;

            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Elige una opción: ", 1, 5);

                switch (opcion) {
                    // ====================================================
                    // 🧠 PATRÓN: CONSTRUIR / AGREGAR
                    // Objetivo: incorporar elementos a la colección
                    // Métodos: add, addAll (no usado aquí)
                    // ====================================================
                    case 1 -> {
                        System.out.print("Ingresa un nombre: ");
                        String nombre = sc.nextLine().trim();
                        if (nombre.isBlank()) {
                            System.out.println("❌ Nombre vacío, no se agregó.");
                        } else {
                            nombres.add(nombre);
                            System.out.println("✅ Nombre agregado correctamente.");
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: EXPLORAR / INSPECCIONAR
                    // Objetivo: ver el contenido y estado de la lista
                    // Métodos: isEmpty, size, iteración/forEach
                    // ====================================================
                    case 2 -> {
                        if (nombres.isEmpty()) {
                            System.out.println("La lista está vacía.");
                        } else {
                            System.out.println("Lista de nombres (size=" + nombres.size() + "):");
                            int i = 1;
                            for (String n : nombres) {
                                System.out.println(i++ + ". " + n);
                            }
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: BUSCAR
                    // Objetivo: comprobar existencia / localizar elementos
                    // Métodos: contains, indexOf/lastIndexOf (no usados aquí)
                    // ====================================================
                    case 3 -> {
                        System.out.print("Ingresa el nombre a buscar: ");
                        String buscar = sc.nextLine().trim();
                        if (nombres.contains(buscar)) {
                            System.out.println("✅ El nombre '" + buscar + "' SÍ está en la lista.");
                        } else {
                            System.out.println("❌ El nombre '" + buscar + "' NO está en la lista.");
                        }
                    }

                    // ====================================================
                    // 🧠 PATRÓN: TRANSFORMAR / FILTRAR (ELIMINAR)
                    // Objetivo: modificar el contenido removiendo elementos
                    // Métodos: remove(Object), removeIf (no usado aquí)
                    // ====================================================
                    case 4 -> {
                        System.out.print("Ingresa el nombre a eliminar: ");
                        String eliminar = sc.nextLine().trim();
                        if (nombres.remove(eliminar)) {
                            System.out.println("🗑️ Nombre eliminado correctamente.");
                        } else {
                            System.out.println("❌ Ese nombre no existe en la lista.");
                        }
                    }

                    // ====================================================
                    // 🏁 SALIR
                    // ====================================================
                    case 5 -> System.out.println("👋 Saliendo del programa...");

                    default -> System.out.println("Opción inválida.");
                }
            } while (opcion != 5);
        }
    }

    static void mostrarMenu() {
        System.out.println("""
                
                --- MENÚ ARRAYLIST ---
                1. Agregar nombre        (CONSTRUIR / AGREGAR)
                2. Mostrar todos         (EXPLORAR / INSPECCIONAR)
                3. Buscar nombre         (BUSCAR)
                4. Eliminar nombre       (TRANSFORMAR / FILTRAR)
                5. Salir
                """);
    }

    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                int n = Integer.parseInt(s.trim());
                if (n < min || n > max) {
                    System.out.println("Número fuera de rango (" + min + "-" + max + ").");
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Intenta de nuevo.");
            }
        }
    }
}
