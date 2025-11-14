package ProgramacionFuncional;

import java.util.Scanner;
import java.util.function.Function;

/**
 * INTERFAZ FUNCTION<T,R> – Guía por Patrones
 * ------------------------------------------
 * Patrones cubiertos:
 * 1) Funciones básicas (apply)
 * 2) Composición secuencial (andThen)
 * 3) Composición inversa (compose)
 * 4) Pipelines complejos (andThen + compose encadenados)
 * 5) Comparación final de flujos funcionales
 *
 * Cada patrón ilustra cómo construir transformaciones de datos puras
 * sin efectos secundarios, en estilo declarativo.
 */
public class FuncionInterface {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        patron1_basico();
        patron2_andThen();
        patron3_compose();
        patron4_pipeline();
        patron5_diferenciasAndThenCompose();
    }

    // ============================================================
    // 🧠 PATRÓN 1: FUNCIONES BÁSICAS (apply)
    // ============================================================
    static void patron1_basico() {
        System.out.println("\n[PATRÓN 1] Funciones básicas (apply)");

        Function<String, Integer> longitud = s -> s.length();
        System.out.println("Longitud(\"Percy\") = " + longitud.apply("Percy"));

        Function<String, String> mayus = s -> s.toUpperCase();
        System.out.println("Mayúsculas(\"percy\") = " + mayus.apply("percy"));
    }

    // ============================================================
    // 🧠 PATRÓN 2: andThen() → primero actual, luego la siguiente
    // ============================================================
    static void patron2_andThen() {
        System.out.println("\n[PATRÓN 2] andThen(): primero actual → luego la pasada");

        Function<Integer, Integer> duplicar = x -> x * 2;
        Function<Integer, Integer> cuadrado = x -> x * x;

        Function<Integer, Integer> duplicarYCuadrar = duplicar.andThen(cuadrado);
        System.out.println("andThen(3): ((3*2)^2) = " + duplicarYCuadrar.apply(3)); // 36
    }

    // ============================================================
    // 🧠 PATRÓN 3: compose() → primero la pasada, luego actual
    // ============================================================
    static void patron3_compose() {
        System.out.println("\n[PATRÓN 3] compose(): primero la pasada → luego la actual");

        Function<Integer, Integer> duplicar = x -> x * 2;
        Function<Integer, Integer> cuadrado = x -> x * x;

        Function<Integer, Integer> cuadrarYDuplicar = duplicar.compose(cuadrado);
        System.out.println("compose(3): ((3^2)*2) = " + cuadrarYDuplicar.apply(3)); // 18
    }

    // ============================================================
    // 🧠 PATRÓN 4: PIPELINES COMPLEJOS (andThen + compose encadenados)
    // ============================================================
    static void patron4_pipeline() {
        System.out.println("\n[PATRÓN 4] Pipelines combinados (andThen + compose)");

        Function<Integer, Integer> sumar2 = x -> x + 2;
        Function<Integer, Integer> multiplicar3 = x -> x * 3;
        Function<Integer, Integer> cuadrado = x -> x * x;

        // ( ( ((x + 2) * 3) + 2 ) ^ 2 )
        Function<Integer, Integer> procesar =
                sumar2.andThen(multiplicar3).andThen(sumar2).andThen(cuadrado);

        System.out.println("Pipeline andThen (x=2): " + procesar.apply(2));
    }

    // ============================================================
    // 🧠 PATRÓN 5: DIFERENCIAS ENTRE andThen() y compose()
    // ============================================================
    static void patron5_diferenciasAndThenCompose() {
        System.out.println("\n[PATRÓN 5] Diferencias entre andThen y compose");

        Function<Integer, Integer> sumar2 = x -> x + 2;
        Function<Integer, Integer> multiplicar3 = x -> x * 3;
        Function<Integer, Integer> cuadrado = x -> x * x;

        // andThen: se lee izquierda → derecha
        Function<Integer, Integer> f1 =
                sumar2.andThen(multiplicar3).andThen(cuadrado);
        System.out.println("andThen: ((x+2)*3)^2 → " + f1.apply(2));

        // compose: se lee derecha → izquierda
        Function<Integer, Integer> f2 =
                cuadrado.compose(multiplicar3).compose(sumar2);
        System.out.println("compose: ((x+2)*3)^2 → " + f2.apply(2));

        // Nota: ambos producen la misma transformación si el orden lógico coincide,
        // pero su interpretación cambia. AndThen encadena de forma natural como “pipeline”.
    }
}
