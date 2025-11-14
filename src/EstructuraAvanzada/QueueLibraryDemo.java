package EstructuraAvanzada;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueLibraryDemo {

    public static void main(String[] args) {

        // ---- Creación de la cola ----
        // ArrayDeque implementa la interfaz Queue
        Queue<Integer> queue = new ArrayDeque<>();

        System.out.println("=== ENQUEUE ===");
        queue.offer(10);
        queue.offer(20);
        queue.offer(30);
        System.out.println("Cola: " + queue); // [10, 20, 30]
        System.out.println("size = " + queue.size());

        System.out.println("\n=== PEEK ===");
        System.out.println("peek() -> " + queue.peek()); // 10 (no lo elimina)
        System.out.println("Cola: " + queue);

        System.out.println("\n=== DEQUEUE ===");
        System.out.println("poll() -> " + queue.poll()); // 10 (lo elimina)
        System.out.println("Cola: " + queue); // [20, 30]

        System.out.println("\n=== MÁS ELEMENTOS ===");
        queue.offer(40);
        queue.offer(50);
        System.out.println("Cola: " + queue); // [20, 30, 40, 50]

        System.out.println("\n=== ITERAR ===");
        for (int v : queue) System.out.print(v + " "); // 20 30 40 50
        System.out.println();

        System.out.println("\n=== VACIAR COLA ===");
        while (!queue.isEmpty()) System.out.print(queue.poll() + " "); // 20 30 40 50
        System.out.println("\nCola vacía? " + queue.isEmpty());
    }
}
