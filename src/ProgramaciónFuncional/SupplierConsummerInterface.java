package ProgramaciónFuncional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.Random;
import java.util.ArrayList;

public class SupplierConsummerInterface {
    public static void main(String[] args) {

        // 1) Consumer simple: imprime un String
        Consumer<String> imprimir = s -> System.out.println("Elemento: " + s);
        imprimir.accept("Percy"); // uso directo con accept

        // 2) Consumer aplicado con forEach sobre una lista
        List<String> nombres = List.of("Percy", "Ana", "Luis");
        nombres.forEach(imprimir); // equivalente a un bucle con imprimir.accept()

        // 3) Supplier simple: siempre devuelve un valor fijo
        Supplier<String> saludar = () -> "Hola desde Supplier";
        System.out.println(saludar.get());

        // 4) Supplier con valor aleatorio
        Supplier<Integer> generarNumero = () -> new Random().nextInt(100);
        System.out.println("Número aleatorio 1: " + generarNumero.get());
        System.out.println("Número aleatorio 2: " + generarNumero.get());

        // 5) Usar Supplier y Consumer juntos
        Supplier<Integer> generarEdad = () -> new Random().nextInt(50) + 1; // edad entre 1 y 50
        Consumer<Integer> mostrarEdad = e -> System.out.println("Edad generada: " + e);

        for (int i = 0; i < 5; i++) {
            Integer edad = generarEdad.get(); // Supplier genera
            mostrarEdad.accept(edad);         // Consumer consume
        }

        // 6) Crear lista con Supplier y procesarla con Consumer
        Supplier<Integer> generarPar = () -> (new Random().nextInt(50)) * 2; // genera pares
        List<Integer> numeros = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            numeros.add(generarPar.get());
        }

        Consumer<Integer> mostrarNumero = n -> System.out.println("Número par: " + n);
        numeros.forEach(mostrarNumero); // Consumer aplicado con forEach
    }
}
