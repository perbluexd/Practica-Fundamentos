import java.util.*;

public class AlgorithmsInterviewPlus {

    // ─────────────────────────────
    // 1) TORRES DE HANÓI (recursión)
    // ─────────────────────────────
    public static void hanoi(int n, char from, char aux, char to) {
        if (n == 1) {
            System.out.println("Mover disco 1 de " + from + " a " + to);
            return;
        }
        hanoi(n - 1, from, to, aux);
        System.out.println("Mover disco " + n + " de " + from + " a " + to);
        hanoi(n - 1, aux, from, to);
    }

    // ─────────────────────────────
    // 2) SUMA DE DÍGITOS (rec/iter)
    // ─────────────────────────────
    public static int sumaDigitosRec(int n) {
        n = Math.abs(n);
        if (n < 10) return n;
        return (n % 10) + sumaDigitosRec(n / 10);
    }
    public static int sumaDigitosIter(int n) {
        n = Math.abs(n);
        int s = 0;
        while (n > 0) { s += n % 10; n /= 10; }
        return s;
    }

    // ─────────────────────────────
    // 3) PERMUTACIONES DE UNA CADENA (backtracking)
    // ─────────────────────────────
    public static List<String> permutaciones(String s) {
        List<String> res = new ArrayList<>();
        boolean[] usado = new boolean[s.length()];
        backtrackPerm("", s, usado, res);
        return res;
    }
    private static void backtrackPerm(String actual, String s, boolean[] usado, List<String> res) {
        if (actual.length() == s.length()) {
            res.add(actual);
            return;
        }
        for (int i = 0; i < s.length(); i++) {
            if (usado[i]) continue;
            usado[i] = true;
            backtrackPerm(actual + s.charAt(i), s, usado, res);
            usado[i] = false;
        }
    }

    // (Variante con duplicados: usa ordenamiento + saltos)
    public static List<String> permutacionesUnicas(String s) {
        char[] arr = s.toCharArray();
        Arrays.sort(arr);
        List<String> res = new ArrayList<>();
        boolean[] usado = new boolean[arr.length];
        backtrackPermUnique(new StringBuilder(), arr, usado, res);
        return res;
    }
    private static void backtrackPermUnique(StringBuilder sb, char[] arr, boolean[] usado, List<String> res) {
        if (sb.length() == arr.length) { res.add(sb.toString()); return; }
        for (int i = 0; i < arr.length; i++) {
            if (usado[i]) continue;
            if (i > 0 && arr[i] == arr[i-1] && !usado[i-1]) continue; // evita duplicados
            usado[i] = true;
            sb.append(arr[i]);
            backtrackPermUnique(sb, arr, usado, res);
            sb.deleteCharAt(sb.length()-1);
            usado[i] = false;
        }
    }

    // ─────────────────────────────
    // 4) SUBCONJUNTOS (Power Set, backtracking)
    // ─────────────────────────────
    public static List<List<Integer>> subconjuntos(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        backtrackSubconjuntos(0, nums, new ArrayList<>(), res);
        return res;
    }
    private static void backtrackSubconjuntos(int idx, int[] nums, List<Integer> cur, List<List<Integer>> res) {
        res.add(new ArrayList<>(cur));
        for (int i = idx; i < nums.length; i++) {
            cur.add(nums[i]);
            backtrackSubconjuntos(i + 1, nums, cur, res);
            cur.remove(cur.size() - 1);
        }
    }

    // (Variante sin duplicados en presencia de repetidos)
    public static List<List<Integer>> subconjuntosUnicos(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        backtrackSubconjuntosUnicos(0, nums, new ArrayList<>(), res);
        return res;
    }
    private static void backtrackSubconjuntosUnicos(int start, int[] nums, List<Integer> cur, List<List<Integer>> res) {
        res.add(new ArrayList<>(cur));
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i-1]) continue; // evita duplicados
            cur.add(nums[i]);
            backtrackSubconjuntosUnicos(i + 1, nums, cur, res);
            cur.remove(cur.size() - 1);
        }
    }

    // ─────────────────────────────
    // 5) TWO SUM (HashMap O(n))
    // ─────────────────────────────
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> pos = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int faltante = target - nums[i];
            if (pos.containsKey(faltante)) {
                return new int[]{pos.get(faltante), i};
            }
            pos.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    // ─────────────────────────────
    // 6) GENERAR PARÉNTESIS VÁLIDOS (backtracking)
    // ─────────────────────────────
    public static List<String> generarParentesis(int n) {
        List<String> res = new ArrayList<>();
        backtrackParentesis("", 0, 0, n, res);
        return res;
    }
    private static void backtrackParentesis(String cur, int open, int close, int n, List<String> res) {
        if (cur.length() == 2*n) { res.add(cur); return; }
        if (open < n)  backtrackParentesis(cur + "(", open + 1, close, n, res);
        if (close < open) backtrackParentesis(cur + ")", open, close + 1, n, res);
    }

    // ─────────────────────────────
    // 7) SUBSET SUM / COMBINATIONS SUM (backtracking clásico)
    // ─────────────────────────────
    // Dado nums y target, retornar combinaciones (permite reutilizar elementos)
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        backtrackCombSum(0, candidates, target, new ArrayList<>(), res);
        return res;
    }
    private static void backtrackCombSum(int start, int[] cand, int target, List<Integer> cur, List<List<Integer>> res) {
        if (target == 0) { res.add(new ArrayList<>(cur)); return; }
        for (int i = start; i < cand.length; i++) {
            if (cand[i] > target) break;
            cur.add(cand[i]);
            backtrackCombSum(i, cand, target - cand[i], cur, res); // i (no i+1) para reutilizar
            cur.remove(cur.size() - 1);
        }
    }

    // ─────────────────────────────
    // 8) COIN CHANGE (DP – mínima cantidad de monedas)
    // ─────────────────────────────
    public static int coinChangeMinCoins(int[] coins, int amount) {
        int INF = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) {
                if (a - c >= 0) dp[a] = Math.min(dp[a], dp[a - c] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // ─────────────────────────────
    // 9) KADANE – MÁXIMO SUBARRAY (O(n))
    // ─────────────────────────────
    public static int maxSubArray(int[] nums) {
        int best = nums[0], cur = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(nums[i], cur + nums[i]);
            best = Math.max(best, cur);
        }
        return best;
    }

    // ─────────────────────────────
    // 10) ANAGRAMAS (frecuencia de caracteres)
    // ─────────────────────────────
    public static boolean sonAnagramas(String a, String b) {
        if (a.length() != b.length()) return false;
        int[] freq = new int[256];
        for (char c : a.toCharArray()) freq[c]++;
        for (char c : b.toCharArray()) {
            if (--freq[c] < 0) return false;
        }
        return true;
    }

    // ─────────────────────────────
    // 11) GCD / LCM (Euclides + derivado)
    // ─────────────────────────────
    public static int gcd(int a, int b) {
        a = Math.abs(a); b = Math.abs(b);
        while (b != 0) { int t = a % b; a = b; b = t; }
        return a;
    }
    public static long lcm(int a, int b) {
        return Math.abs((long)a / gcd(a, b) * b);
    }

    // ─────────────────────────────
    // 12) EXPONENCIACIÓN RÁPIDA (binaria, O(log n))
    // ─────────────────────────────
    public static long fastPow(long base, long exp) {
        long res = 1;
        long x = base;
        long e = exp;
        while (e > 0) {
            if ((e & 1) == 1) res *= x;
            x *= x;
            e >>= 1;
        }
        return res;
    }

    // ─────────────────────────────
    // 13) VALID PARENTHESES (Stack)
    // ─────────────────────────────
    public static boolean validParentheses(String s) {
        Map<Character,Character> map = Map.of(')', '(', '}', '{', ']', '[');
        Deque<Character> st = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (map.containsValue(c)) st.push(c);
            else if (map.containsKey(c)) {
                if (st.isEmpty() || st.pop() != map.get(c)) return false;
            }
        }
        return st.isEmpty();
    }

    // ─────────────────────────────
    // 14) ROTAR MATRIZ NxN 90° (in-place)
    // ─────────────────────────────
    public static void rotateMatrix90(int[][] m) {
        int n = m.length;
        // transponer
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                int tmp = m[i][j]; m[i][j] = m[j][i]; m[j][i] = tmp;
            }
        }
        // reflejar horizontal
        for (int i = 0; i < n; i++) {
            for (int j = 0, k = n-1; j < k; j++, k--) {
                int tmp = m[i][j]; m[i][j] = m[i][k]; m[i][k] = tmp;
            }
        }
    }

    // ─────────────────────────────
    // 15) BÚSQUEDA EN MATRIZ ORDENADA (fila y col ascendente)
    // ─────────────────────────────
    public static boolean searchMatrix(int[][] matrix, int target) {
        int r = 0, c = matrix[0].length - 1;
        while (r < matrix.length && c >= 0) {
            if (matrix[r][c] == target) return true;
            else if (matrix[r][c] > target) c--;
            else r++;
        }
        return false;
    }

    // ─────────────────────────────
    // MAIN: ejemplos cortos
    // ─────────────────────────────
    public static void main(String[] args) {
        // Hanoi
        System.out.println("Hanoi(3):");
        hanoi(3, 'A', 'B', 'C');

        // Suma de dígitos
        System.out.println("Suma dígitos rec(9872): " + sumaDigitosRec(9872));

        // Permutaciones
        System.out.println("Permutaciones de 'abc': " + permutaciones("abc"));

        // Subconjuntos
        System.out.println("Subconjuntos de [1,2,3]: " + subconjuntos(new int[]{1,2,3}));

        // Two Sum
        System.out.println("TwoSum [2,7,11,15], 9 -> " + Arrays.toString(twoSum(new int[]{2,7,11,15}, 9)));

        // Generar paréntesis
        System.out.println("Paréntesis n=3: " + generarParentesis(3));

        // Combination Sum
        System.out.println("CombSum [2,3,6,7], target=7: " + combinationSum(new int[]{2,3,6,7}, 7));

        // Coin Change
        System.out.println("Min coins [1,2,5] amount=11: " + coinChangeMinCoins(new int[]{1,2,5}, 11));

        // Kadane
        System.out.println("MaxSubArray [-2,1,-3,4,-1,2,1,-5,4]: " + maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));

        // Anagramas
        System.out.println("¿'listen' y 'silent' anagramas?: " + sonAnagramas("listen","silent"));

        // GCD/LCM
        System.out.println("gcd(48,18)=" + gcd(48,18) + ", lcm(21,6)=" + lcm(21,6));

        // Pow rápido
        System.out.println("fastPow(3,13)=" + fastPow(3,13));

        // Valid Parentheses
        System.out.println("Valid '({[]})': " + validParentheses("({[]})"));

        // Rotar matriz
        int[][] M = {{1,2,3},{4,5,6},{7,8,9}};
        rotateMatrix90(M);
        System.out.println("Matriz rotada 90°: " + Arrays.deepToString(M));

        // Buscar en matriz ordenada
        int[][] S = {{1,4,7},{8,11,15},{18,21,24}};
        System.out.println("Buscar 11 en matriz: " + searchMatrix(S, 11));
    }
}
