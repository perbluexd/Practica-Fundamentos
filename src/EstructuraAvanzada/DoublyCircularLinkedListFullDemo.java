package EstructuraAvanzada;

public class DoublyCircularLinkedListFullDemo {
    static class DoublyCircularLinkedList<T> {
        private Node<T> head;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next, prev;
            Node(T data) { this.data = data; }
        }

        public void addFirst(T data) {
            Node<T> node = new Node<>(data);
            if (head == null) {
                head = node;
                head.next = head.prev = head;
            } else {
                Node<T> tail = head.prev;
                node.next = head;
                node.prev = tail;
                head.prev = node;
                tail.next = node;
                head = node;
            }
            size++;
        }

        public void addLast(T data) {
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

        public boolean remove(T data) {
            if (head == null) return false;
            Node<T> current = head;
            do {
                if ((current.data == null && data == null) ||
                        (current.data != null && current.data.equals(data))) {
                    if (current.next == current) {
                        head = null;
                    } else {
                        Node<T> prev = current.prev;
                        Node<T> next = current.next;
                        prev.next = next;
                        next.prev = prev;
                        if (current == head)
                            head = next;
                    }
                    size--;
                    return true;
                }
                current = current.next;
            } while (current != head);
            return false;
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
        public void clear() { head = null; size = 0; }
    }

    // ==== MAIN ====
    public static void main(String[] args) {
        DoublyCircularLinkedList<Integer> list = new DoublyCircularLinkedList<>();

        System.out.println("=== Inserciones ===");
        list.addFirst(10);
        list.addLast(20);
        list.addLast(30);
        list.addFirst(5);
        list.printForward();   // [5, 10, 20, 30]
        list.printBackward();  // [30, 20, 10, 5]
        System.out.println("Tamaño: " + list.size());

        System.out.println("\n=== Eliminaciones ===");
        list.remove(10);
        list.printForward();   // [5, 20, 30]
        list.remove(30);
        list.printForward();   // [5, 20]
        list.remove(5);
        list.printForward();   // [20]
        System.out.println("Tamaño: " + list.size());

        System.out.println("\n=== Limpieza ===");
        list.clear();
        list.printForward();   // []
        System.out.println("Vacía: " + list.isEmpty());
    }
}
