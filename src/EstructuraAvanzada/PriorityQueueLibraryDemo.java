package EstructuraAvanzada;

import java.util.PriorityQueue;
import java.util.Comparator;

public class PriorityQueueLibraryDemo {

    // Un tipo propio para mostrar uso con Comparator
    static class Task {
        final int priority; // menor = más urgente
        final String name;
        Task(int priority, String name) { this.priority = priority; this.name = name; }
        @Override public String toString() { return "(" + priority + ", " + name + ")"; }
    }

    public static void main(String[] args) {
        // ===== 1) Min-heap por defecto (orden natural) =====
        PriorityQueue<Integer> minpq = new PriorityQueue<>();
        minpq.offer(5);
        minpq.offer(2);
        minpq.offer(9);
        minpq.offer(1);
        minpq.offer(7);
        System.out.println("Min-heap (Integer, natural): peek=" + minpq.peek()); // 1
        System.out.print("polls: ");
        while (!minpq.isEmpty()) System.out.print(minpq.poll() + " "); // 1 2 5 7 9
        System.out.println();

        // ===== 2) Max-heap usando Comparator inverso =====
        PriorityQueue<Integer> maxpq = new PriorityQueue<>(Comparator.reverseOrder());
        maxpq.offer(5);
        maxpq.offer(2);
        maxpq.offer(9);
        maxpq.offer(1);
        maxpq.offer(7);
        System.out.println("Max-heap (Integer): peek=" + maxpq.peek()); // 9
        System.out.print("polls: ");
        while (!maxpq.isEmpty()) System.out.print(maxpq.poll() + " "); // 9 7 5 2 1
        System.out.println();

        // ===== 3) Objetos propios con Comparator (menor prioridad primero) =====
        PriorityQueue<Task> tasks = new PriorityQueue<>(Comparator.comparingInt(t -> t.priority));
        tasks.offer(new Task(3, "escribir documentación"));
        tasks.offer(new Task(1, "arreglar bug crítico"));
        tasks.offer(new Task(2, "revisar PRs"));
        System.out.println("Tasks (min-heap por prioridad): peek=" + tasks.peek());
        System.out.print("polls: ");
        while (!tasks.isEmpty()) System.out.print(tasks.poll() + " ");
        System.out.println();

        // ===== 4) Max-heap de objetos (mayor prioridad primero) =====
        PriorityQueue<Task> tasksMax =
                new PriorityQueue<>(Comparator.comparingInt((Task t) -> t.priority).reversed());
        tasksMax.offer(new Task(5, "backup semanal"));
        tasksMax.offer(new Task(10, "hotfix producción"));
        tasksMax.offer(new Task(7, "deploy staging"));
        System.out.println("Tasks (max-heap por prioridad): peek=" + tasksMax.peek());
        System.out.print("polls: ");
        while (!tasksMax.isEmpty()) System.out.print(tasksMax.poll() + " ");
        System.out.println();
    }
}
