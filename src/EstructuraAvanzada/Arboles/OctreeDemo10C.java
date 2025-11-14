package EstructuraAvanzada.Arboles;

import java.util.*;

public class OctreeDemo10C {

    // ===== Octree (3D) =====
    static class Octree {
        static final class AABB3D {
            final double cx, cy, cz, hx, hy, hz;
            AABB3D(double cx,double cy,double cz,double hx,double hy,double hz){
                this.cx=cx; this.cy=cy; this.cz=cz; this.hx=hx; this.hy=hy; this.hz=hz;
            }
            boolean contains(double x,double y,double z){
                return Math.abs(x-cx)<=hx && Math.abs(y-cy)<=hy && Math.abs(z-cz)<=hz;
            }
            boolean intersects(AABB3D o){
                return Math.abs(cx - o.cx) <= (hx + o.hx)
                        && Math.abs(cy - o.cy) <= (hy + o.hy)
                        && Math.abs(cz - o.cz) <= (hz + o.hz);
            }
            @Override public String toString(){ return "AABB3D[c=(" + cx + "," + cy + "," + cz + "), h=(" + hx + "," + hy + "," + hz + ")]"; }
        }

        static final class Node {
            final AABB3D box;
            final int capacity;
            final List<double[]> points = new ArrayList<>();
            boolean divided;
            Node[] child = new Node[8]; // 8 octantes
            Node(AABB3D box, int capacity){ this.box=box; this.capacity=capacity; }
        }

        private final Node root;

        public Octree(double cx,double cy,double cz,double hx,double hy,double hz,int capacity){
            this.root = new Node(new AABB3D(cx,cy,cz,hx,hy,hz), capacity);
        }

        public boolean insert(double x,double y,double z){ return insert(root,x,y,z); }
        private boolean insert(Node n,double x,double y,double z){
            if(!n.box.contains(x,y,z)) return false;
            if(n.points.size() < n.capacity && !n.divided){
                n.points.add(new double[]{x,y,z});
                return true;
            }
            if(!n.divided) subdivide(n);
            for(Node c : n.child) if(insert(c,x,y,z)) return true;
            return false;
        }

        private void subdivide(Node n){
            double cx=n.box.cx, cy=n.box.cy, cz=n.box.cz;
            double hx=n.box.hx/2, hy=n.box.hy/2, hz=n.box.hz/2;
            int i=0;
            for(int dx=-1; dx<=1; dx+=2)
                for(int dy=-1; dy<=1; dy+=2)
                    for(int dz=-1; dz<=1; dz+=2)
                        n.child[i++] = new Node(new AABB3D(cx+dx*hx, cy+dy*hy, cz+dz*hz, hx, hy, hz), n.capacity);
            n.divided = true;
            for(double[] p : n.points){
                for(Node c : n.child) if(insert(c,p[0],p[1],p[2])) break;
            }
            n.points.clear();
        }

        public List<double[]> queryRange(double cx,double cy,double cz,double hx,double hy,double hz){
            ArrayList<double[]> out = new ArrayList<>();
            query(root, new AABB3D(cx,cy,cz,hx,hy,hz), out);
            return out;
        }
        private void query(Node n, AABB3D range, List<double[]> out){
            if(n==null || !n.box.intersects(range)) return;
            if(!n.divided){
                for(double[] p : n.points) if(range.contains(p[0],p[1],p[2])) out.add(p);
            }else{
                for(Node c : n.child) query(c, range, out);
            }
        }

        public String dump(){ StringBuilder sb=new StringBuilder(); dump(root,0,sb); return sb.toString(); }
        private void dump(Node n,int d,StringBuilder sb){
            if(n==null) return;
            sb.append("  ".repeat(d)).append(n.box).append(" :: ");
            if(!n.divided) sb.append(n.points);
            sb.append("\n");
            if(n.divided) for(Node c : n.child) dump(c,d+1,sb);
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        Octree oct = new Octree(0,0,0, 16,16,16, 4); // cubo desde -16..16 en cada eje
        Random rnd = new Random(7);
        for(int i=0;i<60;i++){
            double x = -15 + rnd.nextInt(31);
            double y = -15 + rnd.nextInt(31);
            double z = -15 + rnd.nextInt(31);
            oct.insert(x,y,z);
        }
        System.out.println("Octree:\n" + oct.dump());

        System.out.println("Query AABB3D c=(0,0,0), h=(6,6,6):");
        for(double[] p : oct.queryRange(0,0,0, 6,6,6)) System.out.println(Arrays.toString(p));
    }
}
