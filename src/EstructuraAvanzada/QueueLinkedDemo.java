package EstructuraAvanzada;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Cola genérica (FIFO) implementada manualmente con nodos enlazados.
 * Operaciones principales: enqueue, dequeue, peek, size, isEmpty, clear.
 */
public class QueueLinkedDemo {

    // ===== Implementación de la Cola =====
    static class Queue<T> implements Iterable<T> {

        // ----- Nodo -----
        private static final class Node<E> {
            E item;
            Node<E> next;
            Node(E item) { this.item = item; }
        }

        // ----- Campos -----
        private Node<T> head; // frente
        private Node<T> tail; // final
        private int size;

        // ----- Operaciones principales -----

        // Enqueue -> inserta al final
        public void enqueue(T value) {
            Node<T> node = new Node<>(value);
            if (tail == null) { // cola vacía
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
            size++;
        }

        // Dequeue -> quita del frente
        public T dequeue() {
            if (head == null) throw new NoSuchElementException("Queue is empty");
            T value = head.item;
            head = head.next;
            if (head == null) tail = null; // si vacía
            size--;
            return value;
        }

        // Peek -> devuelve el primero sin quitarlo
        public T peek() {
            if (head == null) throw new NoSuchElementException("Queue is empty");
            return head.item;
        }

        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }

        public void clear() {
            Node<T> x = head;
            while (x != null) {
                Node<T> next = x.next;
                x.item = null;
                x.next = null;
                x = next;
            }
            head = tail = null;
            size = 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[front -> ");
            Node<T> curr = head;
            while (curr != null) {
                sb.append(curr.item);
                curr = curr.next;
                if (curr != null) sb.append(", ");
            }
            sb.append(" <- rear]");
            return sb.toString();
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private Node<T> cursor = head;
                @Override public boolean hasNext() { return cursor != null; }
                @Override public T next() {
                    if (cursor == null) throw new NoSuchElementException();
                    T val = cursor.item;
                    cursor = cursor.next;
                    return val;
                }
            };
        }
    }

    // ===== Demo en main =====
    public static void main(String[] args) {
        Queue<Integer> queue = new Queue<>();

        System.out.println("=== ENQUEUE ===");
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println(queue); // [front -> 10, 20, 30 <- rear]
        System.out.println("size = " + queue.size());

        System.out.println("\n=== PEEK ===");
        System.out.println("peek() -> " + queue.peek()); // 10

        System.out.println("\n=== DEQUEUE ===");
        System.out.println("dequeue() -> " + queue.dequeue()); // 10
        System.out.println(queue); // [front -> 20, 30 <- rear]

        System.out.println("\n=== ITERACIÓN ===");
        for (int v : queue) System.out.print(v + " "); // 20 30
        System.out.println();

        System.out.println("\n=== MÁS ELEMENTOS ===");
        queue.enqueue(40);
        queue.enqueue(50);
        System.out.println(queue); // [front -> 20, 30, 40, 50 <- rear]

        System.out.println("\n=== VACIAR COLA ===");
        while (!queue.isEmpty()) System.out.print(queue.dequeue() + " ");
        System.out.println("\nCola vacía? " + queue.isEmpty());
    }
}
