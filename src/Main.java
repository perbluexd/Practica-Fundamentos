import java.util.Arrays;

/**
 * DEMO DE ALGORITMOS CLÁSICOS — AGRUPADOS POR PATRONES MENTALES
 *
 * 🔹 Se abordan patrones de búsqueda, conteo, ordenamiento y recursión.
 * 🔹 Incluye variantes iterativas y recursivas.
 * 🔹 Indica complejidad temporal y espacial en cada bloque.
 */
public class Main {

    // ============================================================
    // 🧠 PATRÓN 1: BÚSQUEDA LINEAL — O(n)
    // ============================================================
    // Recorre secuencialmente hasta hallar el valor.
    // Devuelve índice o -1 si no existe.
    static int linearSearch(int[] a, int x) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == x) return i;
        return -1;
    }

    // ============================================================
    // 🧠 PATRÓN 2: BÚSQUEDA BINARIA ITERATIVA — O(log n)
    // ============================================================
    // PRECONDICIÓN: el arreglo debe estar ORDENADO.
    static int binarySearch(int[] a, int x) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1); // evita overflow
            if (a[mid] == x) return mid;
            if (a[mid] < x) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    // ============================================================
    // 🧠 PATRÓN 3: BÚSQUEDA BINARIA RECURSIVA — O(log n) tiempo / O(log n) espacio
    // ============================================================
    static int binarySearchRec(int[] a, int lo, int hi, int x) {
        if (lo > hi) return -1;
        int mid = lo + ((hi - lo) >>> 1);
        if (a[mid] == x) return mid;
        return (a[mid] < x)
                ? binarySearchRec(a, mid + 1, hi, x)
                : binarySearchRec(a, lo, mid - 1, x);
    }

    // ============================================================
    // 🧠 PATRÓN 4: VARIANTES BINARIAS — PRIMERA Y ÚLTIMA OCURRENCIA
    // ============================================================
    static int binarySearchFirst(int[] a, int x) {
        int lo = 0, hi = a.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            if (a[mid] >= x) {
                if (a[mid] == x) ans = mid;
                hi = mid - 1;
            } else lo = mid + 1;
        }
        return ans;
    }

    static int binarySearchLast(int[] a, int x) {
        int lo = 0, hi = a.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            if (a[mid] <= x) {
                if (a[mid] == x) ans = mid;
                lo = mid + 1;
            } else hi = mid - 1;
        }
        return ans;
    }

    // ============================================================
    // 🧠 PATRÓN 5: DOBLE BUCLE — CONTEO DE PARES (O(n²))
    // ============================================================
    // Ejemplo de algoritmo cuadrático.
    // Cuenta cuántos pares (i, j) cumplen a[i] + a[j] <= t.
    static int countPairsLE_ON2(int[] a, int t) {
        int c = 0;
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[i] + a[j] <= t) c++;
        return c;
    }

    // ============================================================
    // 🧠 PATRÓN 6: OPTIMIZACIÓN CON DOS PUNTEROS — O(n log n)
    // ============================================================
    // Primero ordena y luego usa dos punteros para reducir complejidad.
    static int countPairsLE(int[] a, int t) {
        int[] b = Arrays.copyOf(a, a.length);
        Arrays.sort(b);
        int i = 0, j = b.length - 1, c = 0;
        while (i < j) {
            if (b[i] + b[j] <= t) {
                c += (j - i);
                i++;
            } else j--;
        }
        return c;
    }

    // ============================================================
    // 🧠 PATRÓN 7: ORDENAMIENTO — MERGE SORT (DIVIDE Y VENCERÁS)
    // ============================================================
    // O(n log n) tiempo — O(n) espacio auxiliar
    static void mergeSort(int[] a) { mergeSort(a, 0, a.length); }

    static void mergeSort(int[] a, int l, int r) {
        if (r - l <= 1) return;
        int m = (l + r) >>> 1;
        mergeSort(a, l, m);
        mergeSort(a, m, r);
        merge(a, l, m, r);
    }

    static void merge(int[] a, int l, int m, int r) {
        int[] tmp = new int[r - l];
        int i = l, j = m, k = 0;
        while (i < m && j < r) tmp[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        while (i < m) tmp[k++] = a[i++];
        while (j < r) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, l, tmp.length);
    }

    // ============================================================
    // 🧠 PATRÓN 8: SUMATORIA — RECURSIVA E ITERATIVA
    // ============================================================
    // Recursiva → O(n) tiempo y O(n) espacio (por pila)
    static int sumRec(int[] a, int i) {
        if (i == a.length) return 0;
        return a[i] + sumRec(a, i + 1);
    }

    // Iterativa → O(n) tiempo y O(1) espacio
    static int sumIter(int[] a) {
        int s = 0;
        for (int v : a) s += v;
        return s;
    }

    // ============================================================
    // 🧪 MAIN DE PRUEBAS
    // ============================================================
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 11};

        System.out.println("🔍 Lineal → " + linearSearch(arr, 7));
        System.out.println("🔍 Binaria iterativa → " + binarySearch(arr, 7));
        System.out.println("🔍 Binaria recursiva → " + binarySearchRec(arr, 0, arr.length - 1, 7));

        int[] arrDup = {1, 3, 3, 3, 7, 9};
        System.out.println("↔ First(3) → " + binarySearchFirst(arrDup, 3));
        System.out.println("↔ Last(3)  → " + binarySearchLast(arrDup, 3));

        int[] nums = {1, 2, 3, 4, 5};
        System.out.println("👥 Pares <= 5 (O(n²)) → " + countPairsLE_ON2(nums, 5));
        System.out.println("⚡ Pares <= 5 (O(n log n)) → " + countPairsLE(nums, 5));

        int[] toSort = {5, 1, 4, 2, 8};
        mergeSort(toSort);
        System.out.println("📊 MergeSort → " + Arrays.toString(toSort));

        int[] sumArr = {1, 2, 3, 4};
        System.out.println("Σ Suma recursiva → " + sumRec(sumArr, 0));
        System.out.println("Σ Suma iterativa → " + sumIter(sumArr));
    }
}
