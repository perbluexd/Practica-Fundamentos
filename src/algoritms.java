public class algoritms {

    // 1. Búsqueda Binaria
    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // 2. Factorial (Recursivo e Iterativo)
    public static int factorialRec(int n) {
        if (n <= 1) return 1;
        return n * factorialRec(n - 1);
    }
    public static int factorialIter(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    // 3. Fibonacci (Recursivo e Iterativo)
    public static int fibRec(int n) {
        if (n <= 1) return n;
        return fibRec(n - 1) + fibRec(n - 2);
    }
    public static int fibIter(int n) {
        if (n <= 1) return n;
        int a = 0, b = 1, c = 0;
        for (int i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    // 4. Números Primos
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // 5. Invertir una cadena
    public static String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    // 6. Palíndromo
    public static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    // 7. Ordenamiento Bubble Sort
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // 7b. QuickSort
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
        }
        int temp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = temp;
        return i + 1;
    }

    // 8. Torres de Hanói (ejemplo de recursividad clásica)
    public static void hanoi(int n, char from, char aux, char to) {
        if (n == 1) {
            System.out.println("Mover disco 1 de " + from + " a " + to);
            return;
        }
        hanoi(n - 1, from, to, aux);
        System.out.println("Mover disco " + n + " de " + from + " a " + to);
        hanoi(n - 1, aux, from, to);
    }

    // ---- MAIN DE PRUEBA ----
    public static void main(String[] args) {
        // Búsqueda binaria
        int[] nums = {1, 3, 5, 7, 9};
        System.out.println("Binary Search (buscar 5): " + binarySearch(nums, 5));

        // Factorial
        System.out.println("Factorial rec(5): " + factorialRec(5));
        System.out.println("Factorial iter(5): " + factorialIter(5));

        // Fibonacci
        System.out.println("Fibonacci rec(6): " + fibRec(6));
        System.out.println("Fibonacci iter(6): " + fibIter(6));

        // Primos
        System.out.println("¿Es primo 29? " + isPrime(29));

        // Cadenas
        System.out.println("Invertir 'hola': " + reverseString("hola"));
        System.out.println("¿Es 'reconocer' palíndromo? " + isPalindrome("reconocer"));

        // Ordenamiento
        int[] arr = {5, 2, 9, 1, 3};
        bubbleSort(arr);
        System.out.print("Bubble Sort: ");
        for (int n : arr) System.out.print(n + " ");
        System.out.println();

        int[] arr2 = {8, 4, 7, 3, 2, 9};
        quickSort(arr2, 0, arr2.length - 1);
        System.out.print("QuickSort: ");
        for (int n : arr2) System.out.print(n + " ");
        System.out.println();

        // Torres de Hanói
        System.out.println("Torres de Hanói (3 discos):");
        hanoi(3, 'A', 'B', 'C');
    }
}
