package ProgramacionFuncional;

import java.util.*;
import java.util.function.*;

/**
 * SUPPLIER & CONSUMER – Patrones funcionales esenciales
 * -----------------------------------------------------
 * Patrones cubiertos:
 * 1️⃣ Consumer simple (uso directo)
 * 2️⃣ Consumer aplicado sobre colecciones (forEach)
 * 3️⃣ Supplier simple (valor fijo)
 * 4️⃣ Supplier con valor dinámico o aleatorio
 * 5️⃣ Supplier + Consumer trabajando juntos
 * 6️⃣ Generación de listas con Supplier + procesamiento con Consumer
 *
 * Concepto base:
 * - Supplier<T> → “fábrica”: produce valores (método .get())
 * - Consumer<T> → “consumidor”: usa valores (método .accept())
 * - Se complementan: uno genera, el otro procesa.
 */
public class SupplierConsumerInterface {

    public static void main(String[] args) {

        patron1_consumerSimple();
        patron2_consumerForEach();
        patron3_supplierSimple();
        patron4_supplierAleatorio();
        patron5_supplierConsumerJuntos();
        patron6_supplierGeneraConsumerProcesa();
    }

    // ============================================================
    // 1️⃣ Consumer simple: actúa sobre un único valor
    // ============================================================
    static void patron1_consumerSimple() {
        Consumer<String> imprimir = s -> System.out.println("Elemento: " + s);
        imprimir.accept("Percy"); // Uso directo
    }

    // ============================================================
    // 2️⃣ Consumer aplicado con forEach sobre una lista
    // ============================================================
    static void patron2_consumerForEach() {
        List<String> nombres = List.of("Percy", "Ana", "Luis");
        Consumer<String> imprimir = s -> System.out.println("Nombre: " + s);
        nombres.forEach(imprimir); // Equivale a un bucle con imprimir.accept()
    }

    // ============================================================
    // 3️⃣ Supplier simple: devuelve un valor constante
    // ============================================================
    static void patron3_supplierSimple() {
        Supplier<String> saludar = () -> "Hola desde Supplier";
        System.out.println(saludar.get());
    }

    // ============================================================
    // 4️⃣ Supplier con valor aleatorio (dinámico)
    // ============================================================
    static void patron4_supplierAleatorio() {
        Supplier<Integer> generarNumero = () -> new Random().nextInt(100);
        System.out.println("Número aleatorio 1: " + generarNumero.get());
        System.out.println("Número aleatorio 2: " + generarNumero.get());
    }

    // ============================================================
    // 5️⃣ Supplier + Consumer: flujo completo generar → consumir
    // ============================================================
    static void patron5_supplierConsumerJuntos() {
        Supplier<Integer> generarEdad = () -> new Random().nextInt(50) + 1; // 1–50
        Consumer<Integer> mostrarEdad = e -> System.out.println("Edad generada: " + e);

        for (int i = 0; i < 5; i++) {
            Integer edad = generarEdad.get(); // Supplier genera
            mostrarEdad.accept(edad);         // Consumer consume
        }
    }

    // ============================================================
    // 6️⃣ Crear lista con Supplier + procesar con Consumer
    // ============================================================
    static void patron6_supplierGeneraConsumerProcesa() {
        Supplier<Integer> generarPar = () -> new Random().nextInt(50) * 2; // genera pares
        List<Integer> numeros = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            numeros.add(generarPar.get());
        }

        Consumer<Integer> mostrarNumero = n -> System.out.println("Número par: " + n);
        numeros.forEach(mostrarNumero);
    }
}
