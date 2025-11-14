package EstructuraAvanzada.Arboles;

import java.util.*;

public class KDTree2DManualDemo10A {

    // ===== KD-Tree 2D =====
    static class KDTree2D {
        static final class Node {
            double x, y;
            Node left, right;
            int axis; // 0 = x, 1 = y
            Node(double x, double y, int axis) { this.x = x; this.y = y; this.axis = axis; }
            @Override public String toString(){ return "(" + x + "," + y + ")[" + (axis==0?"x":"y") + "]"; }
        }

        private Node root;
        private int size;

        public int size(){ return size; }
        public boolean isEmpty(){ return size==0; }

        public void insert(double x, double y){ root = insert(root, x, y, 0); }
        private Node insert(Node n, double x, double y, int axis){
            if(n==null){ size++; return new Node(x,y,axis); }
            double key = (axis==0? x : y);
            double cur = (axis==0? n.x : n.y);
            if(key < cur) n.left = insert(n.left, x, y, axis^1);
            else           n.right = insert(n.right, x, y, axis^1);
            return n;
        }

        // Nearest Neighbor (1-NN)
        public double[] nearest(double x, double y){
            if(root==null) return null;
            Best best = new Best();
            nn(root, x, y, best);
            return new double[]{best.bestX, best.bestY};
        }

        // k-Nearest Neighbors (kNN)
        public List<double[]> kNearest(double x, double y, int k){
            if(k<=0 || root==null) return List.of();
            PriorityQueue<Best> pq = new PriorityQueue<>((a,b)->Double.compare(b.bestDist2, a.bestDist2)); // max-heap por distancia
            knn(root, x, y, k, pq);
            ArrayList<double[]> out = new ArrayList<>(pq.size());
            while(!pq.isEmpty()){
                Best b = pq.poll();
                out.add(new double[]{b.bestX, b.bestY});
            }
            Collections.reverse(out);
            return out;
        }

        // Range search en rectángulo [xmin,xmax]×[ymin,ymax]
        public List<double[]> range(double xmin, double ymin, double xmax, double ymax){
            ArrayList<double[]> out = new ArrayList<>();
            range(root, xmin,ymin,xmax,ymax, out);
            return out;
        }

        // ----- Internos -----
        private static final class Best {
            double bestX, bestY, bestDist2 = Double.POSITIVE_INFINITY;
            Best(){}
            Best(double x,double y,double d2){ bestX=x; bestY=y; bestDist2=d2; }
        }
        private static double d2(double x1,double y1,double x2,double y2){
            double dx=x1-x2, dy=y1-y2; return dx*dx+dy*dy;
        }

        private void nn(Node n, double qx, double qy, Best best){
            if(n==null) return;
            double dist2 = d2(qx,qy,n.x,n.y);
            if(dist2 < best.bestDist2){ best.bestDist2 = dist2; best.bestX = n.x; best.bestY = n.y; }
            // decidir rama primaria según eje
            double q = (n.axis==0? qx : qy);
            double p = (n.axis==0? n.x : n.y);
            Node first = (q < p) ? n.left : n.right;
            Node second= (q < p) ? n.right: n.left;
            nn(first, qx, qy, best);
            // posible cruce de hiperplano
            double planeDist2 = (q - p)*(q - p);
            if(planeDist2 <= best.bestDist2) nn(second, qx, qy, best);
        }

        private void knn(Node n, double qx, double qy, int k, PriorityQueue<Best> pq){
            if(n==null) return;
            double dist2 = d2(qx,qy,n.x,n.y);
            if(pq.size()<k) pq.offer(new Best(n.x,n.y,dist2));
            else if(dist2 < pq.peek().bestDist2){ pq.poll(); pq.offer(new Best(n.x,n.y,dist2)); }

            double q = (n.axis==0? qx : qy);
            double p = (n.axis==0? n.x : n.y);
            Node first = (q < p) ? n.left : n.right;
            Node second= (q < p) ? n.right: n.left;

            knn(first, qx, qy, k, pq);
            double limit = pq.size()<k ? Double.POSITIVE_INFINITY : pq.peek().bestDist2;
            double planeDist2 = (q - p)*(q - p);
            if(planeDist2 <= limit) knn(second, qx, qy, k, pq);
        }

        private void range(Node n, double xmin,double ymin,double xmax,double ymax, List<double[]> out){
            if(n==null) return;
            if(n.x>=xmin && n.x<=xmax && n.y>=ymin && n.y<=ymax) out.add(new double[]{n.x,n.y});
            // decidir por eje
            if(n.axis==0){ // comparar por x
                if(xmin <= n.x) range(n.left, xmin,ymin,xmax,ymax,out);
                if(xmax >= n.x) range(n.right,xmin,ymin,xmax,ymax,out);
            }else{ // por y
                if(ymin <= n.y) range(n.left, xmin,ymin,xmax,ymax,out);
                if(ymax >= n.y) range(n.right,xmin,ymin,xmax,ymax,out);
            }
        }

        public String toPretty(){ StringBuilder sb=new StringBuilder(); pretty(root,0,sb); return sb.toString(); }
        private void pretty(Node n,int d,StringBuilder sb){
            if(n==null) return;
            sb.append("  ".repeat(d)).append("- ").append(n).append("\n");
            pretty(n.left,d+1,sb); pretty(n.right,d+1,sb);
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        KDTree2D kd = new KDTree2D();
        double[][] pts = { {2,3},{5,4},{9,6},{4,7},{8,1},{7,2},{6,3},{3,5} };
        for (double[] p : pts) kd.insert(p[0], p[1]);

        System.out.println("KD-Tree 2D (x/y alternados):\n" + kd.toPretty());
        System.out.println("size = " + kd.size());

        double[] q = {6.5, 2.4};
        System.out.println("\nNN de " + Arrays.toString(q) + " -> " + Arrays.toString(kd.nearest(q[0], q[1])));
        System.out.println("kNN (k=3) -> ");
        for (double[] p : kd.kNearest(q[0], q[1], 3)) System.out.println(Arrays.toString(p));

        System.out.println("\nRange [x:3..7, y:2..5]:");
        for (double[] p : kd.range(3,2,7,5)) System.out.println(Arrays.toString(p));
    }
}
