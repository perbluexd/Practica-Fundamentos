package EstructuraAvanzada;

public class DoublyCircularLinkedListDemo {
    static class DoublyCircularLinkedList<T> {
        private Node<T> head;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next, prev;
            Node(T data) { this.data = data; }
        }

        // Inserta al final
        public void add(T data) {
            Node<T> node = new Node<>(data);
            if (head == null) {
                head = node;
                head.next = head.prev = head;
            } else {
                Node<T> tail = head.prev;
                tail.next = node;
                node.prev = tail;
                node.next = head;
                head.prev = node;
            }
            size++;
        }

        public void printForward() {
            if (head == null) {
                System.out.println("[]");
                return;
            }
            Node<T> curr = head;
            System.out.print("[");
            do {
                System.out.print(curr.data);
                curr = curr.next;
                if (curr != head) System.out.print(", ");
            } while (curr != head);
            System.out.println("]");
        }

        public void printBackward() {
            if (head == null) {
                System.out.println("[]");
                return;
            }
            Node<T> curr = head.prev;
            System.out.print("[");
            do {
                System.out.print(curr.data);
                curr = curr.prev;
                if (curr != head.prev) System.out.print(", ");
            } while (curr != head.prev);
            System.out.println("]");
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
    }

    // ==== MAIN ====
    public static void main(String[] args) {
        DoublyCircularLinkedList<Integer> list = new DoublyCircularLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        System.out.println("Lista doblemente circular:");
        list.printForward();   // [1, 2, 3, 4]
        list.printBackward();  // [4, 3, 2, 1]
        System.out.println("Tamaño: " + list.size());
    }
}
