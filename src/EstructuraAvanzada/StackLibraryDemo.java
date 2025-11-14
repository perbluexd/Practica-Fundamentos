package EstructuraAvanzada;

import java.util.ArrayDeque;
import java.util.Deque;

public class StackLibraryDemo {

    public static void main(String[] args) {
        // ArrayDeque implementa Deque, que permite usarlo como Pila (LIFO)
        Deque<Integer> stack = new ArrayDeque<>();

        System.out.println("=== PUSH ===");
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Stack: " + stack); // [30, 20, 10] (tope = 30)

        System.out.println("\n=== PEEK ===");
        System.out.println("peek() -> " + stack.peek()); // 30
        System.out.println("Stack sigue igual: " + stack);

        System.out.println("\n=== POP ===");
        System.out.println("pop() -> " + stack.pop()); // 30
        System.out.println("Stack ahora: " + stack);   // [20, 10]

        System.out.println("\n=== MÁS OPERACIONES ===");
        stack.push(40);
        stack.push(50);
        System.out.println("Stack: " + stack); // [50, 40, 20, 10]

        System.out.println("\nRecorriendo la pila:");
        for (int value : stack) {
            System.out.print(value + " "); // 50 40 20 10
        }

        System.out.println("\n\nVaciar pila:");
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " "); // 50 40 20 10
        }
        System.out.println("\nVacía? " + stack.isEmpty());
    }
}
