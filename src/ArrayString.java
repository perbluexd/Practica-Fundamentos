import java.util.Arrays;

public class ArrayString {
    public static void main(String[] args) {
        String[] nombres = {"Pedro", "Ana", "Luis", "Maria"};

        // Longitud
        System.out.println("Longitud: " + nombres.length); // 4

        // toString
        System.out.println("toString: " + Arrays.toString(nombres));
        // [Pedro, Ana, Luis, Maria]

        // Ordenar (alfabéticamente)
        Arrays.sort(nombres);
        System.out.println("sort: " + Arrays.toString(nombres));
        // [Ana, Luis, Maria, Pedro]

        // binarySearch (PRE: ordenado)
        int idx = Arrays.binarySearch(nombres, "Maria");
        System.out.println("binarySearch 'Maria': " + idx); // 2

        // copyOf
        String[] copia = Arrays.copyOf(nombres, 6);
        System.out.println("copyOf: " + Arrays.toString(copia));
        // [Ana, Luis, Maria, Pedro, null, null]

        // copyOfRange
        String[] rango = Arrays.copyOfRange(nombres, 1, 3);
        System.out.println("copyOfRange (1,3): " + Arrays.toString(rango));
        // [Luis, Maria]

        // equals
        System.out.println("equals con copia: " + Arrays.equals(nombres, copia)); // false

        // deepEquals (para arreglos multidimensionales)
        String[][] m1 = {{"Ana", "Luis"}, {"Pedro", "Maria"}};
        String[][] m2 = {{"Ana", "Luis"}, {"Pedro", "Maria"}};
        System.out.println("deepEquals: " + Arrays.deepEquals(m1, m2)); // true

        // fill
        Arrays.fill(copia, "Vacío");
        System.out.println("fill con 'Vacío': " + Arrays.toString(copia));
        // [Vacío, Vacío, Vacío, Vacío, Vacío, Vacío]

        // hashCode
        System.out.println("hashCode: " + Arrays.hashCode(nombres));

        // compare
        int comp = Arrays.compare(nombres, new String[]{"Ana", "Luis", "Maria", "Pedro"});
        System.out.println("compare con [Ana,Luis,Maria,Pedro]: " + comp); // 0

        // mismatch
        int mismatch = Arrays.mismatch(nombres, new String[]{"Ana", "Luis", "Mario", "Pedro"});
        System.out.println("mismatch con [Ana,Luis,Mario,Pedro]: " + mismatch); // 2

        // ---- Extras modernos ----
        // setAll: inicializa con función
        String[] extras = new String[4];
        Arrays.setAll(extras, i -> "Item" + (i + 1));
        System.out.println("setAll: " + Arrays.toString(extras));
        // [Item1, Item2, Item3, Item4]

        // parallelSort (más rápido en arrays grandes)
        String[] grandes = {"Zoe", "Ana", "Pedro", "Luis", "Maria"};
        Arrays.parallelSort(grandes);
        System.out.println("parallelSort: " + Arrays.toString(grandes));
        // [Ana, Luis, Maria, Pedro, Zoe]

        // stream sobre arrays
        long countM = Arrays.stream(nombres).filter(s -> s.startsWith("M")).count();
        System.out.println("Stream count nombres con M: " + countM); // 1 (Maria)
    }
}
