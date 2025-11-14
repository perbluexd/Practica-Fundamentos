package EstructuraAvanzada;

public class CircularLinkedListDemo {

    static class CircularLinkedList<T> {
        private Node<T> tail;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // Inserta al final (por simplicidad)
        public void add(T data) {
            Node<T> node = new Node<>(data);
            if (tail == null) {
                tail = node;
                tail.next = tail; // se apunta a sí mismo
            } else {
                node.next = tail.next; // apunta al head
                tail.next = node;      // el anterior último apunta al nuevo
                tail = node;           // el nuevo se convierte en tail
            }
            size++;
        }

        // Muestra todos los elementos
        public void print() {
            if (tail == null) {
                System.out.println("[]");
                return;
            }
            Node<T> current = tail.next; // head
            System.out.print("[");
            do {
                System.out.print(current.data);
                current = current.next;
                if (current != tail.next) System.out.print(", ");
            } while (current != tail.next);
            System.out.println("]");
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
    }

    // ==== MAIN ====
    public static void main(String[] args) {
        CircularLinkedList<Integer> list = new CircularLinkedList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        System.out.println("Lista circular simple:");
        list.print(); // [10, 20, 30, 40]
        System.out.println("Tamaño: " + list.size());
    }
}
