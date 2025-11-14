package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * Binary Heap (Montículo Binario) genérico sobre arreglo.
 * - Por defecto es MIN-HEAP (raíz = mínimo). Para MAX-HEAP pasar Comparator inverso.
 * - Operaciones: offer O(log n), poll O(log n), peek O(1), build-heap O(n).
 * - Base de PriorityQueue y HeapSort.
 */
public class BinaryHeapManualDemo6 {

    // ===== Implementación =====
    static class BinaryHeap<T> {

        private Object[] a;                     // almacenamiento
        private int size;                       // número de elementos
        private final Comparator<? super T> c;  // null => Comparable natural

        private static final int DEFAULT_CAP = 16;

        public BinaryHeap() { this(null, DEFAULT_CAP); }
        public BinaryHeap(Comparator<? super T> cmp) { this(cmp, DEFAULT_CAP); }

        public BinaryHeap(Comparator<? super T> cmp, int capacity) {
            if (capacity < 1) capacity = DEFAULT_CAP;
            this.a = new Object[capacity];
            this.c = cmp;
        }

        /** Construye el heap a partir de una colección en O(n). */
        public BinaryHeap(Collection<? extends T> items, Comparator<? super T> cmp) {
            this.c = cmp;
            this.a = items.toArray();
            this.size = a.length;
            heapify();
        }

        // ===== API =====
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        /** Inserta elemento en O(log n). */
        public void offer(T x) {
            Objects.requireNonNull(x, "element is null");
            ensureCapacity(size + 1);
            a[size] = x;
            siftUp(size++);
        }

        /** Devuelve el mínimo (o máximo si comparator lo invierte) sin extraer. */
        @SuppressWarnings("unchecked")
        public T peek() { return size == 0 ? null : (T) a[0]; }

        /** Extrae la raíz en O(log n). */
        @SuppressWarnings("unchecked")
        public T poll() {
            if (size == 0) return null;
            T root = (T) a[0];
            int last = --size;
            Object x = a[last];
            a[last] = null;
            if (last != 0) {
                a[0] = x;
                siftDown(0);
            }
            return root;
        }

        /** Elimina todo. */
        public void clear() {
            Arrays.fill(a, 0, size, null);
            size = 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(a[i]);
                if (i + 1 < size) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }

        // ===== Internos =====
        @SuppressWarnings("unchecked")
        private int cmp(T x, T y) {
            if (c != null) return c.compare(x, y);
            return ((Comparable<? super T>) x).compareTo(y);
        }

        private void ensureCapacity(int min) {
            if (min <= a.length) return;
            int newCap = a.length + (a.length >> 1) + 1;
            if (newCap < min) newCap = min;
            a = Arrays.copyOf(a, newCap);
        }

        /** Construye heap en O(n) (siftDown desde el último padre hacia la raíz). */
        private void heapify() {
            if (size <= 1) return;
            for (int i = (size >>> 1) - 1; i >= 0; i--) siftDown(i);
        }

        @SuppressWarnings("unchecked")
        private void siftUp(int i) {
            Object[] arr = a;
            Object x = arr[i];
            while (i > 0) {
                int p = (i - 1) >>> 1;
                Object parent = arr[p];
                if (cmp((T) x, (T) parent) >= 0) break; // para min-heap: x >= parent -> ok
                arr[i] = parent;
                i = p;
            }
            arr[i] = x;
        }

        @SuppressWarnings("unchecked")
        private void siftDown(int i) {
            Object[] arr = a;
            Object x = arr[i];
            int half = size >>> 1; // nodos con al menos hijo izquierdo
            while (i < half) {
                int left = (i << 1) + 1;
                int right = left + 1;
                int best = left;
                if (right < size && cmp((T) arr[right], (T) arr[left]) < 0) best = right;
                if (cmp((T) arr[best], (T) x) >= 0) break;
                arr[i] = arr[best];
                i = best;
            }
            arr[i] = x;
        }

        // ===== Utilidades extra =====

        /** Construye un heap MIN a partir de una colección (atajo). */
        public static <E extends Comparable<? super E>> BinaryHeap<E> minHeapOf(Collection<? extends E> items) {
            return new BinaryHeap<>(new ArrayList<>(items), null);
        }

        /** Construye un heap MAX a partir de una colección (atajo). */
        public static <E> BinaryHeap<E> maxHeapOf(Collection<? extends E> items, Comparator<? super E> cmp) {
            return new BinaryHeap<>(new ArrayList<>(items), cmp);
        }

        /** Heap sort que retorna una lista ordenada ascendente usando este heap. */
        public List<T> drainSorted() {
            ArrayList<T> out = new ArrayList<>(size);
            while (!isEmpty()) out.add(poll());
            return out;
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        // ---- Min-heap (por defecto, orden natural) ----
        BinaryHeap<Integer> minHeap = new BinaryHeap<>();
        minHeap.offer(7);
        minHeap.offer(3);
        minHeap.offer(10);
        minHeap.offer(1);
        minHeap.offer(5);

        System.out.println("Min-heap interno : " + minHeap); // representación del array-heap
        System.out.println("peek (min)       : " + minHeap.peek()); // 1
        System.out.print("polls asc        : ");
        while (!minHeap.isEmpty()) System.out.print(minHeap.poll() + " "); // 1 3 5 7 10
        System.out.println();

        // ---- Max-heap (pasando comparator inverso) ----
        BinaryHeap<Integer> maxHeap = new BinaryHeap<>(Comparator.reverseOrder());
        for (int v : new int[]{7,3,10,1,5}) maxHeap.offer(v);
        System.out.println("\nMax-heap interno : " + maxHeap);
        System.out.println("peek (max)       : " + maxHeap.peek()); // 10
        System.out.print("polls desc       : ");
        while (!maxHeap.isEmpty()) System.out.print(maxHeap.poll() + " "); // 10 7 5 3 1
        System.out.println();

        // ---- Build-heap O(n) desde colección ----
        List<Integer> data = Arrays.asList(12, 4, 9, 2, 20, 15, 7);
        BinaryHeap<Integer> built = new BinaryHeap<>(data, null); // min-heap
        System.out.println("\nHeapify desde lista: " + built);
        System.out.println("ordenado (asc)     : " + built.drainSorted()); // 2 4 7 9 12 15 20

        // ---- Heap sort demo (con Strings) ----
        List<String> words = Arrays.asList("zorro", "beta", "alfa", "delta", "kilo");
        BinaryHeap<String> heapWords = new BinaryHeap<>();
        for (String w : words) heapWords.offer(w);
        System.out.println("\nHeapSort asc palabras: " + heapWords.drainSorted()); // alfa beta delta kilo zorro

        // ---- Max-heap de objetos (comparador por campo) ----
        record Task(int prio, String name) {}
        BinaryHeap<Task> tasks = new BinaryHeap<>((a, b) -> Integer.compare(b.prio, a.prio)); // MAX por prioridad
        tasks.offer(new Task(5, "backup"));
        tasks.offer(new Task(10, "hotfix"));
        tasks.offer(new Task(7, "deploy"));
        System.out.print("\nTareas por prioridad (desc): ");
        while (!tasks.isEmpty()) System.out.print(tasks.poll().name() + " "); // hotfix deploy backup
        System.out.println();
    }
}
