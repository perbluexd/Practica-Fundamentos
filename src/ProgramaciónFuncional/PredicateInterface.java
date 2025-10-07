package ProgramaciónFuncional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PredicateInterface {
    public static void main(String[] args) {

        // 1) test(): predicado simple
        Predicate<Integer> esPar = x -> x % 2 == 0;
        System.out.println("¿4 es par?: " + esPar.test(4)); // true
        System.out.println("¿7 es par?: " + esPar.test(7)); // false
        List<Integer> lista = new ArrayList<>(List.of(1,2,3,4,5));
        List<Integer> listaenteros = new ArrayList<>();

        for(int i = 0;i<lista.size();i++){
            if(esPar.test(i)){
                listaenteros.add(lista.get(i));
            }

        }

        // 2) and(): ambos predicados deben ser verdaderos
        Predicate<Integer> mayorQueDiez = x -> x > 10;
        Predicate<Integer> parYMayorQueDiez = esPar.and(mayorQueDiez);
        System.out.println("¿12 es par y > 10?: " + parYMayorQueDiez.test(12)); // true
        System.out.println("¿7 es par y > 10?: " + parYMayorQueDiez.test(7));   // false

        // 3) or(): al menos uno debe ser verdadero
        Predicate<Integer> menorQueCero = x -> x < 0;
        Predicate<Integer> mayorQueCien = x -> x > 100;
        Predicate<Integer> fueraDeRango = menorQueCero.or(mayorQueCien);
        System.out.println("¿-5 está fuera de rango?: " + fueraDeRango.test(-5));   // true
        System.out.println("¿50 está fuera de rango?: " + fueraDeRango.test(50));   // false
        System.out.println("¿150 está fuera de rango?: " + fueraDeRango.test(150)); // true

        // 4) negate(): invierte el resultado
        Predicate<String> noVacio = s -> !s.isEmpty();
        Predicate<String> vacio = noVacio.negate();
        System.out.println("¿\"Hola\" no está vacío?: " + noVacio.test("Hola")); // true
        System.out.println("¿\"\" está vacío?: " + vacio.test(""));              // true

        // 5) Uso típico en filter() con streams
        List<Integer> numeros = List.of(5, 12, 20, 7, 3, 40);
        List<Integer> pares = numeros.stream()
                .filter(esPar)
                .toList();
        System.out.println("Números pares en la lista: " + pares); // [12, 20, 40]
    }
}
