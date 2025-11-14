package EstructuraAvanzada;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Pila (LIFO) genérica implementada manualmente con nodos enlazados.
 * Operaciones principales: push, pop, peek, size, isEmpty, clear.
 * Itera de tope → fondo.
 */
public class StackLinkedDemo {

    // ===== Implementación de la Pila =====
    static class Stack<T> implements Iterable<T> {

        // ----- Nodo -----
        private static final class Node<E> {
            E item;
            Node<E> next;
            Node(E item, Node<E> next) {
                this.item = item;
                this.next = next;
            }
        }

        // ----- Campos -----
        private Node<T> top;  // tope de la pila
        private int size;

        // ----- Operaciones básicas -----
        // O(1)
        public void push(T value) {
            top = new Node<>(value, top);
            size++;
        }

        // O(1)
        public T pop() {
            if (top == null) throw new NoSuchElementException("Stack is empty");
            T v = top.item;
            Node<T> next = top.next;
            // help GC
            top.item = null;
            top.next = null;
            top = next;
            size--;
            return v;
        }

        // O(1)
        public T peek() {
            if (top == null) throw new NoSuchElementException("Stack is empty");
            return top.item;
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        // O(n)
        public void clear() {
            Node<T> x = top;
            while (x != null) {
                Node<T> n = x.next;
                x.item = null; x.next = null;
                x = n;
            }
            top = null;
            size = 0;
        }

        // ----- Utilidad -----
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[top -> ");
            for (Node<T> x = top; x != null; x = x.next) {
                sb.append(x.item);
                if (x.next != null) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }

        // Itera desde el tope hacia el fondo.
        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private Node<T> cursor = top;
                @Override public boolean hasNext() { return cursor != null; }
                @Override public T next() {
                    if (cursor == null) throw new NoSuchElementException();
                    T v = cursor.item;
                    cursor = cursor.next;
                    return v;
                }
            };
        }
    }

    // ===== Demo en main =====
    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();

        System.out.println("PUSH 3, 5, 7");
        s.push(3); s.push(5); s.push(7);
        System.out.println(s); // [top -> 7, 5, 3]
        System.out.println("peek = " + s.peek()); // 7
        System.out.println("size = " + s.size()); // 3

        System.out.println("\nPOP -> " + s.pop()); // 7
        System.out.println(s); // [top -> 5, 3]

        System.out.println("\nIterando de tope a fondo:");
        for (int v : s) System.out.print(v + " "); // 5 3
        System.out.println();

        System.out.println("\nPUSH 9, 11");
        s.push(9); s.push(11);
        System.out.println(s); // [top -> 11, 9, 5, 3]

        System.out.println("\nPOP consecutivos:");
        while (!s.isEmpty()) System.out.print(s.pop() + " "); // 11 9 5 3
        System.out.println("\nempty? " + s.isEmpty()); // true
    }
}
