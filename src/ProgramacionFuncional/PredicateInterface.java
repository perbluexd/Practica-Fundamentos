package ProgramacionFuncional;

import java.util.*;
import java.util.function.Predicate;

/**
 * INTERFAZ PREDICATE<T> – Guía por Patrones
 * -----------------------------------------
 * Patrones cubiertos:
 * 1️⃣ test(): Evaluación simple
 * 2️⃣ and(): Composición lógica (Y)
 * 3️⃣ or(): Composición lógica (O)
 * 4️⃣ negate(): Negación de condición
 * 5️⃣ Uso funcional en Streams (filter)
 * 6️⃣ Combinaciones avanzadas (predicados múltiples)
 *
 * Predicate<T> → recibe un valor y devuelve boolean.
 * Es la base de la lógica condicional en programación funcional.
 */
public class PredicateInterface {

    public static void main(String[] args) {
        patron1_testBasico();
        patron2_and();
        patron3_or();
        patron4_negate();
        patron5_streamFilter();
        patron6_combinado();
    }

    // ============================================================
    // 🧠 PATRÓN 1: test() → Evaluación simple
    // ============================================================
    static void patron1_testBasico() {
        System.out.println("\n[PATRÓN 1] test(): Evaluación simple");

        Predicate<Integer> esPar = x -> x % 2 == 0;

        System.out.println("¿4 es par? " + esPar.test(4)); // true
        System.out.println("¿7 es par? " + esPar.test(7)); // false

        // Ejemplo aplicado sobre lista
        List<Integer> lista = List.of(1, 2, 3, 4, 5);
        List<Integer> pares = new ArrayList<>();

        for (Integer n : lista) {
            if (esPar.test(n)) pares.add(n);
        }

        System.out.println("Elementos pares en la lista: " + pares); // [2,4]
    }

    // ============================================================
    // 🧠 PATRÓN 2: and() → ambos predicados deben cumplirse
    // ============================================================
    static void patron2_and() {
        System.out.println("\n[PATRÓN 2] and(): Composición lógica (Y)");

        Predicate<Integer> esPar = x -> x % 2 == 0;
        Predicate<Integer> mayorQueDiez = x -> x > 10;

        Predicate<Integer> parYMayorQueDiez = esPar.and(mayorQueDiez);

        System.out.println("¿12 es par y >10? " + parYMayorQueDiez.test(12)); // true
        System.out.println("¿7 es par y >10? " + parYMayorQueDiez.test(7));   // false
    }

    // ============================================================
    // 🧠 PATRÓN 3: or() → al menos un predicado debe cumplirse
    // ============================================================
    static void patron3_or() {
        System.out.println("\n[PATRÓN 3] or(): Composición lógica (O)");

        Predicate<Integer> menorQueCero = x -> x < 0;
        Predicate<Integer> mayorQueCien = x -> x > 100;

        Predicate<Integer> fueraDeRango = menorQueCero.or(mayorQueCien);

        System.out.println("¿-5 fuera de rango? " + fueraDeRango.test(-5));   // true
        System.out.println("¿50 fuera de rango? " + fueraDeRango.test(50));   // false
        System.out.println("¿150 fuera de rango? " + fueraDeRango.test(150)); // true
    }

    // ============================================================
    // 🧠 PATRÓN 4: negate() → invierte el resultado lógico
    // ============================================================
    static void patron4_negate() {
        System.out.println("\n[PATRÓN 4] negate(): Negación de condición");

        Predicate<String> noVacio = s -> !s.isEmpty();
        Predicate<String> vacio = noVacio.negate();

        System.out.println("\"Hola\" no vacío? " + noVacio.test("Hola")); // true
        System.out.println("\"\" vacío? " + vacio.test(""));              // true
    }

    // ============================================================
    // 🧠 PATRÓN 5: Uso funcional con filter() en Streams
    // ============================================================
    static void patron5_streamFilter() {
        System.out.println("\n[PATRÓN 5] filter() con Stream");

        Predicate<Integer> esPar = x -> x % 2 == 0;
        List<Integer> numeros = List.of(5, 12, 20, 7, 3, 40);

        List<Integer> pares = numeros.stream()
                .filter(esPar)
                .toList();

        System.out.println("Números pares filtrados: " + pares); // [12, 20, 40]
    }

    // ============================================================
    // 🧠 PATRÓN 6: Combinaciones avanzadas (and + or + negate)
    // ============================================================
    static void patron6_combinado() {
        System.out.println("\n[PATRÓN 6] Combinaciones lógicas avanzadas");

        Predicate<Integer> esPositivo = n -> n > 0;
        Predicate<Integer> esPar = n -> n % 2 == 0;

        Predicate<Integer> parOPositivo = esPar.or(esPositivo);     // verdadero si cumple alguno
        Predicate<Integer> noParNiPositivo = parOPositivo.negate(); // falso si cumple alguno

        System.out.println("¿2 es par o positivo? " + parOPositivo.test(2));   // true
        System.out.println("¿-3 no par ni positivo? " + noParNiPositivo.test(-3)); // true
        System.out.println("¿4 no par ni positivo? " + noParNiPositivo.test(4));   // false
    }
}
