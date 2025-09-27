import java.util.Arrays;

public class Main {

    // O(n): búsqueda lineal — MEMORIZAR
    // Devuelve el índice de la primera coincidencia o -1 si no está.
    static int linearSearch(int[] a, int x) {
        for (int i = 0; i < a.length; i++) if (a[i] == x) return i;
        return -1;
    }

    // O(log n): búsqueda binaria iterativa — MEMORIZAR
    // PRECONDICIÓN: a está ORDENADO en forma no decreciente.
    static int binarySearch(int[] a, int x) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1); // evita overflow
            if (a[mid] == x) return mid;
            if (a[mid] < x) lo = mid + 1; else hi = mid - 1;
        }
        return -1;
    }

    // O(log n) tiempo, O(log n) espacio (por recursión) — MEMORIZAR
    // PRECONDICIÓN: a ordenado (igual que arriba). Rango [lo, hi] INCLUSIVO.
    static int binarySearchRec(int[] a, int lo, int hi, int x) {
        if (lo > hi) return -1;
        int mid = lo + ((hi - lo) >>> 1);
        if (a[mid] == x) return mid;
        return (a[mid] < x)
                ? binarySearchRec(a, mid + 1, hi, x)
                : binarySearchRec(a, lo, mid - 1, x);
    }

    // Variantes típicas: primera y última ocurrencia (útiles con duplicados)
    static int binarySearchFirst(int[] a, int x) {
        int lo = 0, hi = a.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            if (a[mid] >= x) { if (a[mid] == x) ans = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        return ans;
    }
    static int binarySearchLast(int[] a, int x) {
        int lo = 0, hi = a.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            if (a[mid] <= x) { if (a[mid] == x) ans = mid; lo = mid + 1; }
            else hi = mid - 1;
        }
        return ans;
    }

    // O(n^2): ejemplo de bucle doble (cuenta pares con suma <= t)
    static int countPairsLE_ON2(int[] a, int t) {
        int c = 0;
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[i] + a[j] <= t) c++;
        return c;
    }

    // O(n log n): versión óptima con sort + dos punteros
    static int countPairsLE(int[] a, int t) {
        int[] b = Arrays.copyOf(a, a.length);
        Arrays.sort(b);
        int i = 0, j = b.length - 1, c = 0;
        while (i < j) {
            if (b[i] + b[j] <= t) {
                c += (j - i); // todos los pares (i, i+1..j-1) valen con j
                i++;
            } else {
                j--;
            }
        }
        return c;
    }

    // O(n log n): Merge Sort (rango [l, r) — semicluso)
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

    // O(n) tiempo, O(n) espacio por recursión (peligro de stack en arreglos muy grandes)
    static int sumRec(int[] a, int i) {
        if (i == a.length) return 0;
        return a[i] + sumRec(a, i + 1);
    }
    // Alternativa iterativa O(1) espacio (segura para tamaños grandes)
    static int sumIter(int[] a) {
        int s = 0;
        for (int v : a) s += v;
        return s;
    }

    // ---- MAIN PARA PRUEBAS ----
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 11};

        System.out.println("Lineal -> " + linearSearch(arr, 7));
        System.out.println("Binaria iterativa -> " + binarySearch(arr, 7));
        System.out.println("Binaria recursiva -> " + binarySearchRec(arr, 0, arr.length - 1, 7));

        // Duplicados para probar first/last
        int[] arrDup = {1, 3, 3, 3, 7, 9};
        System.out.println("First(3) -> " + binarySearchFirst(arrDup, 3));
        System.out.println("Last(3)  -> " + binarySearchLast(arrDup, 3));

        int[] nums = {1, 2, 3, 4, 5};
        System.out.println("Pares <= 5 (O(n^2)) -> " + countPairsLE_ON2(nums, 5));
        System.out.println("Pares <= 5 (O(n log n)) -> " + countPairsLE(nums, 5));

        int[] toSort = {5, 1, 4, 2, 8};
        mergeSort(toSort);
        System.out.println("MergeSort -> " + Arrays.toString(toSort));

        int[] sumArr = {1, 2, 3, 4};
        System.out.println("Suma recursiva -> " + sumRec(sumArr, 0));
        System.out.println("Suma iterativa -> " + sumIter(sumArr));
    }
}
