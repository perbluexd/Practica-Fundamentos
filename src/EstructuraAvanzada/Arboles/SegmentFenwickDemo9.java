package EstructuraAvanzada.Arboles;

import java.util.Arrays;

/**
 * n.º 9 — Segment Tree / Fenwick Tree (BIT)
 * Árboles para consultas por rango y actualizaciones en arreglos.
 *
 * Este archivo incluye:
 *  - SegmentTree: soporte para sumas por rango y actualización puntual.
 *  - FenwickTree (BIT): sumas prefix/rango y actualización puntual.
 *
 * Complejidades:
 *  - SegmentTree: build O(n), query O(log n), update O(log n).
 *  - FenwickTree: build O(n), prefix O(log n), update O(log n).
 */
public class SegmentFenwickDemo9 {

    /* ===================== SEGMENT TREE (Sumas) ===================== */
    static class SegmentTree {
        private final int n;      // tamaño lógico del arreglo
        private final long[] st;  // árbol de segmentos (potencia de 2 interna)

        public SegmentTree(int[] arr) {
            this.n = arr.length;
            // tamaño máximo ~ 4n para almacenamiento seguro
            this.st = new long[4 * Math.max(1, n)];
            if (n > 0) build(arr, 1, 0, n - 1);
        }

        private void build(int[] a, int p, int l, int r) {
            if (l == r) { st[p] = a[l]; return; }
            int m = (l + r) >>> 1;
            build(a, p << 1, l, m);
            build(a, p << 1 | 1, m + 1, r);
            st[p] = st[p << 1] + st[p << 1 | 1];
        }

        /** Suma en rango [L, R] inclusive. */
        public long rangeSum(int L, int R) {
            if (L < 0 || R >= n || L > R) throw new IllegalArgumentException();
            return query(1, 0, n - 1, L, R);
        }

        private long query(int p, int l, int r, int L, int R) {
            if (R < l || r < L) return 0L;    // fuera
            if (L <= l && r <= R) return st[p]; // completamente dentro
            int m = (l + r) >>> 1;
            long left = query(p << 1, l, m, L, R);
            long right = query(p << 1 | 1, m + 1, r, L, R);
            return left + right;
        }

        /** Actualización puntual: a[idx] = newVal */
        public void pointSet(int idx, int newVal) {
            if (idx < 0 || idx >= n) throw new IllegalArgumentException();
            pointSet(1, 0, n - 1, idx, newVal);
        }

        private void pointSet(int p, int l, int r, int idx, int v) {
            if (l == r) { st[p] = v; return; }
            int m = (l + r) >>> 1;
            if (idx <= m) pointSet(p << 1, l, m, idx, v);
            else pointSet(p << 1 | 1, m + 1, r, idx, v);
            st[p] = st[p << 1] + st[p << 1 | 1];
        }

        /** Actualización puntual por delta: a[idx] += delta */
        public void pointAdd(int idx, int delta) {
            if (idx < 0 || idx >= n) throw new IllegalArgumentException();
            pointAdd(1, 0, n - 1, idx, delta);
        }

        private void pointAdd(int p, int l, int r, int idx, int delta) {
            if (l == r) { st[p] += delta; return; }
            int m = (l + r) >>> 1;
            if (idx <= m) pointAdd(p << 1, l, m, idx, delta);
            else pointAdd(p << 1 | 1, m + 1, r, idx, delta);
            st[p] = st[p << 1] + st[p << 1 | 1];
        }
    }

    /* ===================== FENWICK TREE (BIT) ===================== */
    static class FenwickTree {
        private final int n;
        private final long[] bit; // 1-indexed internamente

        public FenwickTree(int n) {
            this.n = n;
            this.bit = new long[n + 1];
        }

        public FenwickTree(int[] arr) {
            this(arr.length);
            // build O(n) — suma en BIT[i] las contribuciones del rango (i - LSB(i) + 1 .. i)
            for (int i = 1; i <= n; i++) {
                bit[i] += arr[i - 1];
                int j = i + (i & -i);
                if (j <= n) bit[j] += bit[i];
            }
        }

        /** a[idx] += delta (0-indexed). */
        public void add(int idx, long delta) {
            for (int i = idx + 1; i <= n; i += (i & -i)) bit[i] += delta;
        }

        /** prefix sum [0..idx] (0-indexed). */
        public long prefixSum(int idx) {
            long res = 0L;
            for (int i = idx + 1; i > 0; i -= (i & -i)) res += bit[i];
            return res;
        }

        /** range sum [L..R] (0-indexed, inclusivo). */
        public long rangeSum(int L, int R) {
            if (L < 0 || R >= n || L > R) throw new IllegalArgumentException();
            return prefixSum(R) - (L == 0 ? 0 : prefixSum(L - 1));
        }

        /** Establecer a[idx] = newVal (requiere conocer valor actual). */
        public void set(int idx, int currentVal, int newVal) {
            add(idx, newVal - currentVal);
        }

        public int size() { return n; }
    }

    /* ===================== DEMO MAIN ===================== */
    public static void main(String[] args) {
        int[] arr = {5, -3, 7, 2, 9, 1, 4}; // n = 7
        System.out.println("Array base: " + Arrays.toString(arr));

        // ---- Segment Tree (sumas) ----
        SegmentTree st = new SegmentTree(arr);
        System.out.println("\n[SegmentTree] Sumas por rango:");
        System.out.println("sum(0, 3) = " + st.rangeSum(0, 3)); // 5 + (-3) + 7 + 2 = 11
        System.out.println("sum(2, 6) = " + st.rangeSum(2, 6)); // 7 + 2 + 9 +1 +4 = 23

        System.out.println("pointAdd idx=1, +5  (arr[1] pasa de -3 a 2)");
        st.pointAdd(1, 5);
        System.out.println("sum(0, 3) = " + st.rangeSum(0, 3)); // ahora 16

        System.out.println("pointSet idx=4, =0  (arr[4] pasa de 9 a 0)");
        st.pointSet(4, 0);
        System.out.println("sum(2, 6) = " + st.rangeSum(2, 6)); // ahora 14

        // ---- Fenwick Tree (BIT) ----
        FenwickTree ft = new FenwickTree(arr);
        System.out.println("\n[FenwickTree] Sumas por rango:");
        System.out.println("sum(0, 3) = " + ft.rangeSum(0, 3)); // 11
        System.out.println("sum(2, 6) = " + ft.rangeSum(2, 6)); // 23

        System.out.println("add idx=1, +5  (arr[1] pasa de -3 a 2)");
        ft.add(1, 5);
        System.out.println("sum(0, 3) = " + ft.rangeSum(0, 3)); // 16

        System.out.println("set idx=4, =0  (de 9 a 0)");
        ft.set(4, 9, 0); // si no conoces currentVal, calcula: currentVal = rangeSum(4,4)
        System.out.println("sum(2, 6) = " + ft.rangeSum(2, 6)); // 14

        // Nota: SegmentTree sirve para más operaciones (min, max, gcd, lazy propagation).
        // El BIT es compacto y muy rápido para sumas y algunas extensiones (rango-update / punto-query, etc.).
    }
}
