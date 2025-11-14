package EstructuraAvanzada.Arboles;

import java.util.*;

public class QuadTreeDemo10B {

    // ===== QuadTree (2D) =====
    static class QuadTree {
        static final class AABB {
            final double cx, cy, hw, hh; // centro (cx,cy), half-width, half-height
            AABB(double cx,double cy,double hw,double hh){ this.cx=cx; this.cy=cy; this.hw=hw; this.hh=hh; }
            boolean contains(double x,double y){
                return x>=cx-hw && x<=cx+hw && y>=cy-hh && y<=cy+hh;
            }
            boolean intersects(AABB o){
                return Math.abs(cx - o.cx) <= (hw + o.hw) && Math.abs(cy - o.cy) <= (hh + o.hh);
            }
            @Override public String toString(){ return "AABB[c=(" + cx + "," + cy + "), hw=" + hw + ", hh=" + hh + "]"; }
        }

        static final class Node {
            final AABB box;
            final int capacity;
            final List<double[]> points = new ArrayList<>();
            boolean divided;
            Node nw, ne, sw, se;

            Node(AABB box, int capacity){ this.box=box; this.capacity=capacity; }
        }

        private final Node root;

        public QuadTree(double cx,double cy,double hw,double hh,int capacity){
            this.root = new Node(new AABB(cx,cy,hw,hh), capacity);
        }

        public boolean insert(double x,double y){ return insert(root, x, y); }
        private boolean insert(Node n, double x, double y){
            if(!n.box.contains(x,y)) return false;
            if(n.points.size() < n.capacity && !n.divided){
                n.points.add(new double[]{x,y});
                return true;
            }
            if(!n.divided) subdivide(n);
            return insert(n.nw,x,y) || insert(n.ne,x,y) || insert(n.sw,x,y) || insert(n.se,x,y);
        }

        private void subdivide(Node n){
            double cx=n.box.cx, cy=n.box.cy, hw=n.box.hw/2, hh=n.box.hh/2;
            n.nw = new Node(new AABB(cx-hw, cy+hh, hw, hh), n.capacity);
            n.ne = new Node(new AABB(cx+hw, cy+hh, hw, hh), n.capacity);
            n.sw = new Node(new AABB(cx-hw, cy-hh, hw, hh), n.capacity);
            n.se = new Node(new AABB(cx+hw, cy-hh, hw, hh), n.capacity);
            n.divided = true;
            // Reinsertar puntos existentes en hijos
            for(double[] p : n.points){
                if(!insert(n.nw,p[0],p[1]) && !insert(n.ne,p[0],p[1]) &&
                        !insert(n.sw,p[0],p[1]) && !insert(n.se,p[0],p[1])) {
                    // En teoría no debería pasar si subdivisión coincide
                }
            }
            n.points.clear();
        }

        // Consulta por rango (AABB)
        public List<double[]> queryRange(double cx,double cy,double hw,double hh){
            ArrayList<double[]> out = new ArrayList<>();
            query(root, new AABB(cx,cy,hw,hh), out);
            return out;
        }
        private void query(Node n, AABB range, List<double[]> out){
            if(n==null || !n.box.intersects(range)) return;
            if(!n.divided){
                for(double[] p : n.points) if(range.contains(p[0],p[1])) out.add(p);
            }else{
                query(n.nw,range,out); query(n.ne,range,out); query(n.sw,range,out); query(n.se,range,out);
            }
        }

        // Consulta por círculo (centro, radio)
        public List<double[]> queryCircle(double cx,double cy,double r){
            double hw=r, hh=r;
            AABB bounds = new AABB(cx,cy,hw,hh);
            ArrayList<double[]> out = new ArrayList<>();
            queryCircle(root, bounds, cx, cy, r*r, out);
            return out;
        }
        private void queryCircle(Node n, AABB bounds, double cx,double cy,double r2, List<double[]> out){
            if(n==null || !n.box.intersects(bounds)) return;
            if(!n.divided){
                for(double[] p : n.points){
                    double dx=p[0]-cx, dy=p[1]-cy;
                    if(dx*dx + dy*dy <= r2) out.add(p);
                }
            }else{
                queryCircle(n.nw,bounds,cx,cy,r2,out);
                queryCircle(n.ne,bounds,cx,cy,r2,out);
                queryCircle(n.sw,bounds,cx,cy,r2,out);
                queryCircle(n.se,bounds,cx,cy,r2,out);
            }
        }

        public String dump(){ StringBuilder sb=new StringBuilder(); dump(root,0,sb); return sb.toString(); }
        private void dump(Node n,int d,StringBuilder sb){
            if(n==null) return;
            sb.append("  ".repeat(d)).append(n.box).append(" :: ");
            if(!n.divided) sb.append(n.points);
            sb.append("\n");
            if(n.divided){ dump(n.nw,d+1,sb); dump(n.ne,d+1,sb); dump(n.sw,d+1,sb); dump(n.se,d+1,sb); }
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        QuadTree qt = new QuadTree(0,0,16,16,4); // mundo [-16..16]x[-16..16], cap=4 por nodo
        Random rnd = new Random(42);
        for(int i=0;i<40;i++){
            double x = -15 + rnd.nextInt(31);
            double y = -15 + rnd.nextInt(31);
            qt.insert(x,y);
        }
        System.out.println("QuadTree:\n" + qt.dump());

        System.out.println("Query AABB cx=0,cy=0, hw=5,hh=5:");
        for(double[] p : qt.queryRange(0,0,5,5)) System.out.println(Arrays.toString(p));

        System.out.println("\nQuery Círculo c=(3,3), r=6:");
        for(double[] p : qt.queryCircle(3,3,6)) System.out.println(Arrays.toString(p));
    }
}
