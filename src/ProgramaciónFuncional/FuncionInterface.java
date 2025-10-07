package ProgramaciónFuncional;

import java.util.Scanner;
import java.util.function.Function;

public class FuncionInterface {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        Function<String,Integer> longitud = x -> x.length();
        System.out.println("Longitud: "+ longitud.apply("Percy"));

        Function<String,String> mayus = x-> x.toUpperCase();
        System.out.println("Palabra mayuscula: "+ mayus.apply("percy"));

        // 2) andThen(): primero ejecuta la función actual, luego la que pasas
        Function<Integer, Integer> duplicar = x -> x * 2;
        Function<Integer, Integer> cuadrado = x -> x * x;
        Function<Integer, Integer> duplicarYCuadrar = duplicar.andThen(cuadrado);
        System.out.println("andThen (duplicar y luego cuadrar 3): " + duplicarYCuadrar.apply(3));
        // ((3 * 2) ^ 2) = 36

        // 3) compose(): primero ejecuta la función que pasas, luego la actual
        Function<Integer, Integer> cuadrarYDuplicar = duplicar.compose(cuadrado);
        System.out.println("compose (cuadrar y luego duplicar 3): " + cuadrarYDuplicar.apply(3));
        // ((3 ^ 2) * 2) = 18

        Function<Integer, Integer> sumar2 = x -> x + 2;
        Function<Integer, Integer> multiplicar3 = x -> x * 3;
        Function<Integer, Integer> procesar = sumar2.andThen(multiplicar3).andThen(sumar2).andThen(cuadrado);
        System.out.println(procesar.apply(2));

        Function<Integer, Integer> procesarCompose = cuadrado.compose(sumar2).compose(multiplicar3);
        System.out.println(procesarCompose.apply(2));
    }

}
