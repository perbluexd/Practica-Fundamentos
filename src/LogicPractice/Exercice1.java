package LogicPractice;

import java.util.*;

public class Exercice1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Integer> lista = new ArrayList<>();

        System.out.print("Ingresa la cantidad de datos a evaluar: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("Ingresa el " + (i + 1) + " valor: ");
            int valor = sc.nextInt();
            lista.add(valor);
        }

        List<Integer> solitarios = encontrados(lista);

        if (solitarios.isEmpty()) {
            System.out.println("No hay números solitarios.");
        } else {
            System.out.println("Números solitarios encontrados: " + solitarios);
        }
    }

    public static List<Integer> encontrados(List<Integer> lista) {
        HashMap<Integer, Integer> mapa = new HashMap<>();

        // Contamos ocurrencias
        for (Integer elemento : lista) {
            mapa.compute(elemento, (key, val) -> (val == null) ? 1 : val + 1);
        }

        // Creamos una lista con los números que aparecen solo una vez
        List<Integer> solitarios = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entrada : mapa.entrySet()) {
            if (entrada.getValue() == 1) {
                solitarios.add(entrada.getKey());
            }
        }

        return solitarios;
    }
}
