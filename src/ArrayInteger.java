import java.util.Arrays;

public class ArrayInteger {
    public static void main(String[] args) {
        int[] numeros = {50, 10, 30, 40, 20};

        // ====================================================
        // 🧠 PATRÓN MENTAL 1: EXPLORAR / INSPECCIONAR
        // Objetivo: comprender el contenido y tamaño del array
        // ====================================================

        // Longitud del array
        System.out.println("Longitud: " + numeros.length); // 5

        // Convertir a String legible
        System.out.println("toString: " + Arrays.toString(numeros));
        // [50, 10, 30, 40, 20]

        // HashCode del array (identificador no único)
        System.out.println("hashCode: " + Arrays.hashCode(numeros));


        // ====================================================
        // 🧠 PATRÓN MENTAL 2: ORDENAR Y ORGANIZAR
        // Objetivo: ordenar los elementos de menor a mayor
        // ====================================================

        // Ordenar el array
        Arrays.sort(numeros);
        System.out.println("sort: " + Arrays.toString(numeros));
        // [10, 20, 30, 40, 50]

        // Ordenar en paralelo (útil para arrays grandes)
        int[] grandes = {99, 12, 58, 32, 7, 101, 4, 65};
        Arrays.parallelSort(grandes);
        System.out.println("parallelSort: " + Arrays.toString(grandes));
        // [4, 7, 12, 32, 58, 65, 99, 101]

        // Comparación lexicográfica entre dos arrays
        int comp = Arrays.compare(numeros, new int[]{10, 20, 30, 40, 50});
        System.out.println("compare con [10,20,30,40,50]: " + comp); // 0


        // ====================================================
        // 🧠 PATRÓN MENTAL 3: BUSCAR Y COMPARAR
        // Objetivo: encontrar elementos o diferencias
        // ====================================================

        // Búsqueda binaria (requiere array ordenado)
        int idx = Arrays.binarySearch(numeros, 30);
        System.out.println("binarySearch 30: " + idx); // 2

        // Comparar igualdad superficial
        System.out.println("equals con copia: " + Arrays.equals(numeros, new int[]{10, 20, 30, 40, 50}));

        // Comparar igualdad profunda (para arrays anidados)
        int[][] matriz1 = {{1, 2}, {3, 4}};
        int[][] matriz2 = {{1, 2}, {3, 4}};
        System.out.println("deepEquals: " + Arrays.deepEquals(matriz1, matriz2)); // true

        // Mismatch (primer índice distinto)
        int mismatch = Arrays.mismatch(numeros, new int[]{10, 20, 35, 40, 50});
        System.out.println("mismatch con [10,20,35,40,50]: " + mismatch); // 2


        // ====================================================
        // 🧠 PATRÓN MENTAL 4: COPIAR Y CLONAR
        // Objetivo: duplicar o extender arrays de forma controlada
        // ====================================================

        // Copiar array a nuevo tamaño
        int[] copia = Arrays.copyOf(numeros, 7);
        System.out.println("copyOf: " + Arrays.toString(copia));
        // [10, 20, 30, 40, 50, 0, 0]

        // Copiar un rango específico
        int[] rango = Arrays.copyOfRange(numeros, 1, 4);
        System.out.println("copyOfRange (1,4): " + Arrays.toString(rango));
        // [20, 30, 40]


        // ====================================================
        // 🧠 PATRÓN MENTAL 5: TRANSFORMAR / RELLENAR
        // Objetivo: modificar valores o inicializar arrays
        // ====================================================

        // Rellenar con un valor fijo
        Arrays.fill(copia, 99);
        System.out.println("fill con 99: " + Arrays.toString(copia));
        // [99, 99, 99, 99, 99, 99, 99]

        // Inicializar dinámicamente (ejemplo: cuadrados)
        int[] cuadrados = new int[5];
        Arrays.setAll(cuadrados, i -> i * i);
        System.out.println("setAll cuadrados: " + Arrays.toString(cuadrados));
        // [0, 1, 4, 9, 16]


        // ====================================================
        // 🧠 PATRÓN MENTAL 6: CALCULAR CON STREAMS
        // Objetivo: obtener información numérica o resumir datos
        // ====================================================

        int suma = Arrays.stream(numeros).sum();
        double promedio = Arrays.stream(numeros).average().orElse(0);
        int max = Arrays.stream(numeros).max().orElse(Integer.MIN_VALUE);

        System.out.println("Stream suma: " + suma);        // 150
        System.out.println("Stream promedio: " + promedio); // 30.0
        System.out.println("Stream max: " + max);           // 50
    }
}
