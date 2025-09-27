import java.util.Arrays;

public class ArrayInteger {
    public static void main(String[] args) {
        int[] numeros = {50, 10, 30, 40, 20};

        // Longitud del array
        System.out.println("Longitud: " + numeros.length); // 5

        // Convertir a String legible
        System.out.println("toString: " + Arrays.toString(numeros));
        // [50, 10, 30, 40, 20]

        // Ordenar el array
        Arrays.sort(numeros);
        System.out.println("sort: " + Arrays.toString(numeros));
        // [10, 20, 30, 40, 50]

        // Búsqueda binaria (requiere array ordenado)
        int idx = Arrays.binarySearch(numeros, 30);
        System.out.println("binarySearch 30: " + idx); // 2

        // Copiar array a nuevo tamaño
        int[] copia = Arrays.copyOf(numeros, 7);
        System.out.println("copyOf: " + Arrays.toString(copia));
        // [10, 20, 30, 40, 50, 0, 0]

        // Copiar un rango
        int[] rango = Arrays.copyOfRange(numeros, 1, 4);
        System.out.println("copyOfRange (1,4): " + Arrays.toString(rango));
        // [20, 30, 40]

        // Comparar igualdad
        System.out.println("equals con copia: " + Arrays.equals(numeros, copia)); // false

        // Comparar igualdad profunda (para arrays anidados)
        int[][] matriz1 = {{1, 2}, {3, 4}};
        int[][] matriz2 = {{1, 2}, {3, 4}};
        System.out.println("deepEquals: " + Arrays.deepEquals(matriz1, matriz2)); // true

        // Rellenar con un valor
        Arrays.fill(copia, 99);
        System.out.println("fill con 99: " + Arrays.toString(copia));
        // [99, 99, 99, 99, 99, 99, 99]

        // HashCode del array (NO es único, puede haber colisiones)
        System.out.println("hashCode: " + Arrays.hashCode(numeros));

        // Comparación lexicográfica
        int comp = Arrays.compare(numeros, new int[]{10, 20, 30, 40, 50});
        System.out.println("compare con [10,20,30,40,50]: " + comp); // 0

        // Mismatch (primer índice distinto)
        int mismatch = Arrays.mismatch(numeros, new int[]{10, 20, 35, 40, 50});
        System.out.println("mismatch con [10,20,35,40,50]: " + mismatch); // 2

        // ---- Extras modernos ----
        // setAll: inicializa con función (ejemplo: cuadrados)
        int[] cuadrados = new int[5];
        Arrays.setAll(cuadrados, i -> i * i);
        System.out.println("setAll cuadrados: " + Arrays.toString(cuadrados));
        // [0, 1, 4, 9, 16]

        // parallelSort: ordena en paralelo (eficiente en arrays grandes)
        int[] grandes = {99, 12, 58, 32, 7, 101, 4, 65};
        Arrays.parallelSort(grandes);
        System.out.println("parallelSort: " + Arrays.toString(grandes));
        // [4, 7, 12, 32, 58, 65, 99, 101]

        // stream: cálculos rápidos
        int suma = Arrays.stream(numeros).sum();
        double promedio = Arrays.stream(numeros).average().orElse(0);
        int max = Arrays.stream(numeros).max().orElse(Integer.MIN_VALUE);

        System.out.println("Stream suma: " + suma);        // 150
        System.out.println("Stream promedio: " + promedio); // 30.0
        System.out.println("Stream max: " + max);           // 50
    }
}
