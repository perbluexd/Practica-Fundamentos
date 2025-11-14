package EstructuraAvanzada;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class DoublyLinkedListDemo {

    // ===== Lista doblemente enlazada GENÉRICA =====
    static class DoublyLinkedList<T> implements Iterable<T> {

        // ----- Nodo -----
        private static final class Node<E> {
            E item;
            Node<E> prev;
            Node<E> next;
            Node(E item, Node<E> prev, Node<E> next) {
                this.item = item;
                this.prev = prev;
                this.next = next;
            }
        }

        // ----- Campos -----
        private Node<T> head;
        private Node<T> tail;
        private int size;
        private int modCount; // para iteradores fail-fast simples

        // ----- Constructores -----
        public DoublyLinkedList() {}

        // ----- Métricas -----
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        // ----- Helpers internos -----
        private void checkElementIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }
        private void checkPositionIndex(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }
        private Node<T> nodeAt(int index) {
            // Optimización: si está en la primera mitad, recorremos desde head; si no, desde tail.
            if (index < (size >> 1)) {
                Node<T> x = head;
                for (int i = 0; i < index; i++) x = x.next;
                return x;
            } else {
                Node<T> x = tail;
                for (int i = size - 1; i > index; i--) x = x.prev;
                return x;
            }
        }

        // ----- Inserciones -----
        public void addFirst(T item) {
            Node<T> h = head;
            Node<T> newNode = new Node<>(item, null, h);
            head = newNode;
            if (h == null) {
                tail = newNode;
            } else {
                h.prev = newNode;
            }
            size++; modCount++;
        }

        public void addLast(T item) {
            Node<T> t = tail;
            Node<T> newNode = new Node<>(item, t, null);
            tail = newNode;
            if (t == null) {
                head = newNode;
            } else {
                t.next = newNode;
            }
            size++; modCount++;
        }

        // Inserta en una posición [0..size]; index==size es equivalente a addLast
        public void add(int index, T item) {
            checkPositionIndex(index);
            if (index == size) { addLast(item); return; }
            if (index == 0)    { addFirst(item); return; }
            Node<T> succ = nodeAt(index);
            Node<T> pred = succ.prev;
            Node<T> newNode = new Node<>(item, pred, succ);
            pred.next = newNode;
            succ.prev = newNode;
            size++; modCount++;
        }

        // ----- Acceso / actualización -----
        public T get(int index) {
            checkElementIndex(index);
            return nodeAt(index).item;
        }

        public T set(int index, T newValue) {
            checkElementIndex(index);
            Node<T> x = nodeAt(index);
            T old = x.item;
            x.item = newValue;
            return old;
        }

        // ----- Eliminaciones -----
        public T removeFirst() {
            if (head == null) throw new NoSuchElementException("List is empty");
            Node<T> h = head;
            T value = h.item;
            Node<T> next = h.next;
            h.item = null; h.next = null; // help GC
            head = next;
            if (next == null) {
                tail = null;
            } else {
                next.prev = null;
            }
            size--; modCount++;
            return value;
        }

        public T removeLast() {
            if (tail == null) throw new NoSuchElementException("List is empty");
            Node<T> t = tail;
            T value = t.item;
            Node<T> prev = t.prev;
            t.item = null; t.prev = null; // help GC
            tail = prev;
            if (prev == null) {
                head = null;
            } else {
                prev.next = null;
            }
            size--; modCount++;
            return value;
        }

        public T removeAt(int index) {
            checkElementIndex(index);
            if (index == 0)  return removeFirst();
            if (index == size - 1) return removeLast();
            Node<T> x = nodeAt(index);
            T value = x.item;
            Node<T> p = x.prev, n = x.next;
            // desvincular
            p.next = n; n.prev = p;
            x.item = null; x.prev = x.next = null; // help GC
            size--; modCount++;
            return value;
        }

        // Elimina la primera aparición de 'o' (== null-safe equals)
        public boolean remove(Object o) {
            for (Node<T> x = head; x != null; x = x.next) {
                if (o == null ? x.item == null : o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
            return false;
        }

        private T unlink(Node<T> x) {
            Node<T> p = x.prev, n = x.next;
            if (p == null) head = n; else p.next = n;
            if (n == null) tail = p; else n.prev = p;
            T v = x.item;
            x.item = null; x.prev = x.next = null;
            size--; modCount++;
            return v;
        }

        public void clear() {
            // Desvincular todos los nodos para ayudar al GC
            Node<T> x = head;
            while (x != null) {
                Node<T> next = x.next;
                x.item = null; x.prev = x.next = null;
                x = next;
            }
            head = tail = null;
            size = 0; modCount++;
        }

        // ----- Búsquedas -----
        public int indexOf(Object o) {
            int i = 0;
            for (Node<T> x = head; x != null; x = x.next, i++) {
                if (o == null ? x.item == null : o.equals(x.item)) return i;
            }
            return -1;
        }
        public boolean contains(Object o) { return indexOf(o) >= 0; }

        // ----- Iteradores -----
        @Override
        public Iterator<T> iterator() {
            return new DLLIterator(true);
        }

        public Iterator<T> descendingIterator() {
            return new DLLIterator(false);
        }

        private final class DLLIterator implements Iterator<T> {
            private Node<T> nextNode;
            private Node<T> lastReturned;
            private final int expectedModCount;
            private final boolean forward;

            DLLIterator(boolean forward) {
                this.forward = forward;
                this.expectedModCount = modCount;
                this.nextNode = forward ? head : tail;
            }

            private void checkForComodification() {
                if (expectedModCount != modCount) {
                    throw new IllegalStateException("List modified during iteration");
                }
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public T next() {
                checkForComodification();
                if (nextNode == null) throw new NoSuchElementException();
                lastReturned = nextNode;
                nextNode = forward ? nextNode.next : nextNode.prev;
                return lastReturned.item;
            }
        }

        // ----- toString -----
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            Node<T> x = head;
            while (x != null) {
                sb.append(x.item);
                x = x.next;
                if (x != null) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    // ===== Demo rápida en main =====
    public static void main(String[] args) {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();

        // Inserciones básicas
        list.addFirst(2);           // [2]
        list.addFirst(1);           // [1, 2]
        list.addLast(3);            // [1, 2, 3]
        list.add(3, 5);             // [1, 2, 3, 5]
        list.add(3, 4);             // [1, 2, 3, 4, 5]
        System.out.println("Inicial: " + list); // [1, 2, 3, 4, 5]

        // Accesos
        System.out.println("get(0)=" + list.get(0)); // 1
        System.out.println("get(4)=" + list.get(4)); // 5

        // set
        list.set(2, 99);            // [1, 2, 99, 4, 5]
        System.out.println("set(2,99): " + list);

        // remove por índice
        list.removeAt(2);           // [1, 2, 4, 5]
        System.out.println("removeAt(2): " + list);

        // remove por valor
        list.remove((Integer) 4);   // [1, 2, 5]
        System.out.println("remove(4): " + list);

        // pops
        System.out.println("removeFirst(): " + list.removeFirst()); // 1 -> [2,5]
        System.out.println("removeLast(): " + list.removeLast());   // 5 -> [2]
        System.out.println("Actual: " + list + " size=" + list.size());

        // Iteración normal
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);           // [2, 10, 20, 30]
        System.out.print("Iterator forward: ");
        for (Integer v : list) System.out.print(v + " ");
        System.out.println();

        // Iteración inversa
        System.out.print("Iterator backward: ");
        Iterator<Integer> it = list.descendingIterator();
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();

        // contains / indexOf
        System.out.println("contains(20): " + list.contains(20));
        System.out.println("indexOf(10): " + list.indexOf(10));

        // clear
        list.clear();
        System.out.println("clear -> " + list + " (isEmpty=" + list.isEmpty() + ")");
    }
}
