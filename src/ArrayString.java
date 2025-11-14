import java.util.Arrays;

public class ArrayString {
    public static void main(String[] args) {
        String[] nombres = {"Pedro", "Ana", "Luis", "Maria"};

        // ====================================================
        // 🧠 PATRÓN MENTAL 1: EXPLORAR / INSPECCIONAR
        // Objetivo: entender estructura, tamaño y contenido textual
        // ====================================================

        // Longitud
        System.out.println("Longitud: " + nombres.length); // 4

        // toString
        System.out.println("toString: " + Arrays.toString(nombres));
        // [Pedro, Ana, Luis, Maria]

        // hashCode
        System.out.println("hashCode: " + Arrays.hashCode(nombres));


        // ====================================================
        // 🧠 PATRÓN MENTAL 2: ORDENAR / ORGANIZAR
        // Objetivo: ordenar elementos alfabéticamente o compararlos
        // ====================================================

        // Ordenar (alfabéticamente)
        Arrays.sort(nombres);
        System.out.println("sort: " + Arrays.toString(nombres));
        // [Ana, Luis, Maria, Pedro]

        // Ordenar en paralelo (útil para arrays grandes)
        String[] grandes = {"Zoe", "Ana", "Pedro", "Luis", "Maria"};
        Arrays.parallelSort(grandes);
        System.out.println("parallelSort: " + Arrays.toString(grandes));
        // [Ana, Luis, Maria, Pedro, Zoe]

        // Comparación lexicográfica entre arrays
        int comp = Arrays.compare(nombres, new String[]{"Ana", "Luis", "Maria", "Pedro"});
        System.out.println("compare con [Ana,Luis,Maria,Pedro]: " + comp); // 0


        // ====================================================
        // 🧠 PATRÓN MENTAL 3: BUSCAR Y COMPARAR
        // Objetivo: encontrar coincidencias o diferencias
        // ====================================================

        // binarySearch (PRE: ordenado)
        int idx = Arrays.binarySearch(nombres, "Maria");
        System.out.println("binarySearch 'Maria': " + idx); // 2

        // equals
        System.out.println("equals con copia: " + Arrays.equals(nombres, new String[]{"Ana", "Luis", "Maria", "Pedro"}));

        // deepEquals (para arreglos multidimensionales)
        String[][] m1 = {{"Ana", "Luis"}, {"Pedro", "Maria"}};
        String[][] m2 = {{"Ana", "Luis"}, {"Pedro", "Maria"}};
        System.out.println("deepEquals: " + Arrays.deepEquals(m1, m2)); // true

        // mismatch (primer índice distinto)
        int mismatch = Arrays.mismatch(nombres, new String[]{"Ana", "Luis", "Mario", "Pedro"});
        System.out.println("mismatch con [Ana,Luis,Mario,Pedro]: " + mismatch); // 2


        // ====================================================
        // 🧠 PATRÓN MENTAL 4: COPIAR / CLONAR
        // Objetivo: crear duplicados o subarreglos
        // ====================================================

        // copyOf
        String[] copia = Arrays.copyOf(nombres, 6);
        System.out.println("copyOf: " + Arrays.toString(copia));
        // [Ana, Luis, Maria, Pedro, null, null]

        // copyOfRange
        String[] rango = Arrays.copyOfRange(nombres, 1, 3);
        System.out.println("copyOfRange (1,3): " + Arrays.toString(rango));
        // [Luis, Maria]


        // ====================================================
        // 🧠 PATRÓN MENTAL 5: TRANSFORMAR / RELLENAR
        // Objetivo: modificar el contenido o generar nuevos valores
        // ====================================================

        // fill
        Arrays.fill(copia, "Vacío");
        System.out.println("fill con 'Vacío': " + Arrays.toString(copia));
        // [Vacío, Vacío, Vacío, Vacío, Vacío, Vacío]

        // setAll: inicializa con función
        String[] extras = new String[4];
        Arrays.setAll(extras, i -> "Item" + (i + 1));
        System.out.println("setAll: " + Arrays.toString(extras));
        // [Item1, Item2, Item3, Item4]


        // ====================================================
        // 🧠 PATRÓN MENTAL 6: CALCULAR / ANALIZAR CON STREAMS
        // Objetivo: aplicar funciones de conteo, filtrado o reducción
        // ====================================================

        long countM = Arrays.stream(nombres)
                .filter(s -> s.startsWith("M"))
                .count();
        System.out.println("Stream count nombres con M: " + countM); // 1 (Maria)
    }
}
