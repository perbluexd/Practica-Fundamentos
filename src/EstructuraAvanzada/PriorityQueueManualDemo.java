package EstructuraAvanzada;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * Cola de prioridad genérica (min-heap por defecto).
 * - offer(e): O(log n)
 * - poll(): O(log n)
 * - peek(): O(1)
 * Permite pasar un Comparator para cambiar el orden (p. ej., max-heap).
 */
public class PriorityQueueManualDemo {

    // ===== Implementación =====
    static class PriorityQueueManual<T> {

        private Object[] heap;        // almacenamiento (heap binario)
        private int size;             // número de elementos
        private final Comparator<? super T> cmp; // comparador (null => Comparable)

        private static final int DEFAULT_CAPACITY = 16;

        public PriorityQueueManual() {
            this(null, DEFAULT_CAPACITY);
        }

        public PriorityQueueManual(Comparator<? super T> comparator) {
            this(comparator, DEFAULT_CAPACITY);
        }

        public PriorityQueueManual(Comparator<? super T> comparator, int capacity) {
            if (capacity < 1) capacity = DEFAULT_CAPACITY;
            this.heap = new Object[capacity];
            this.cmp = comparator;
        }

        // ---- API principal ----

        /** Inserta un elemento en O(log n). */
        public void offer(T e) {
            if (e == null) throw new NullPointerException("Null elements not allowed");
            ensureCapacity(size + 1);
            heap[size] = e;
            siftUp(size);
            size++;
        }

        /** Devuelve el mínimo (o máximo si comparator invierte) sin quitar. O(1) */
        @SuppressWarnings("unchecked")
        public T peek() {
            return size == 0 ? null : (T) heap[0];
        }

        /** Extrae y devuelve la raíz (mínimo por defecto). O(log n). */
        @SuppressWarnings("unchecked")
        public T poll() {
            if (size == 0) return null;
            T root = (T) heap[0];
            int last = --size;
            Object x = heap[last];
            heap[last] = null;
            if (last != 0) {
                heap[0] = x;
                siftDown(0);
            }
            return root;
        }

        /** Versión que lanza excepción si está vacía. */
        public T remove() {
            T v = poll();
            if (v == null) throw new NoSuchElementException("PriorityQueue is empty");
            return v;
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        /** Limpia la estructura en O(n). */
        public void clear() {
            Arrays.fill(heap, 0, size, null);
            size = 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(heap[i]);
                if (i + 1 < size) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }

        // ---- Internos de heap ----

        private void ensureCapacity(int minCapacity) {
            if (minCapacity <= heap.length) return;
            int newCap = heap.length + (heap.length >> 1) + 1; // ~1.5x
            if (newCap < minCapacity) newCap = minCapacity;
            heap = Arrays.copyOf(heap, newCap);
        }

        @SuppressWarnings("unchecked")
        private int compare(T a, T b) {
            if (cmp != null) return cmp.compare(a, b);
            // Comparable natural
            return ((Comparable<? super T>) a).compareTo(b);
        }

        @SuppressWarnings("unchecked")
        private void siftUp(int idx) {
            Object[] h = heap;
            Object x = h[idx];
            while (idx > 0) {
                int parent = (idx - 1) >>> 1;
                Object p = h[parent];
                if (compare((T) x, (T) p) >= 0) break; // min-heap: x >= p => OK
                h[idx] = p;
                idx = parent;
            }
            h[idx] = x;
        }

        @SuppressWarnings("unchecked")
        private void siftDown(int idx) {
            Object[] h = heap;
            int half = size >>> 1; // nodos internos tienen hijo izquierdo
            Object x = h[idx];
            while (idx < half) {
                int left = (idx << 1) + 1;
                int right = left + 1;
                int small = left;

                if (right < size && compare((T) h[right], (T) h[left]) < 0) {
                    small = right;
                }
                if (compare((T) h[small], (T) x) >= 0) break; // ya está en lugar
                h[idx] = h[small];
                idx = small;
            }
            h[idx] = x;
        }
    }

    // ===== Demo en main =====
    public static void main(String[] args) {
        // --- Min-heap por defecto (orden natural) ---
        PriorityQueueManual<Integer> minq = new PriorityQueueManual<>();
        minq.offer(5);
        minq.offer(2);
        minq.offer(9);
        minq.offer(1);
        minq.offer(7);
        System.out.println("Min-heap -> " + minq); // (forma interna, no ordenada)
        System.out.println("peek = " + minq.peek()); // 1
        System.out.print("polls: ");
        while (!minq.isEmpty()) System.out.print(minq.poll() + " "); // 1 2 5 7 9
        System.out.println();

        // --- Max-heap pasando Comparator inverso ---
        PriorityQueueManual<Integer> maxq =
                new PriorityQueueManual<>((a, b) -> Integer.compare(b, a));
        maxq.offer(5);
        maxq.offer(2);
        maxq.offer(9);
        maxq.offer(1);
        maxq.offer(7);
        System.out.println("Max-heap -> " + maxq);
        System.out.println("peek = " + maxq.peek()); // 9
        System.out.print("polls: ");
        while (!maxq.isEmpty()) System.out.print(maxq.poll() + " "); // 9 7 5 2 1
        System.out.println();

        // --- Ejemplo con Strings (orden natural = alfabético) ---
        PriorityQueueManual<String> words = new PriorityQueueManual<>();
        words.offer("zorro");
        words.offer("alpha");
        words.offer("kilo");
        words.offer("bravo");
        System.out.print("strings (min-heap): ");
        while (!words.isEmpty()) System.out.print(words.poll() + " "); // alpha bravo kilo zorro
        System.out.println();
    }
}
