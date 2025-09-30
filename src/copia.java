import java.util.*;

public class copia {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        HashMap<Integer, List<String>> mapa = new HashMap<>();


        System.out.println("Ingresa la clave a buscar: ");
        int s = sc.nextInt();
        sc.nextLine();
        String clase = leer_linea(sc,"Ingresa la notificación: ");

        List<String> res = mapa.computeIfAbsent(s,k -> new ArrayList<>());

        res.add(clase);
        System.out.println("se agrego la nueva clase: "+ res);


        List<String> nuevos = leer_lista(sc,"Ingresa la lista: ");
        List<String> res2 = mapa.merge(s,nuevos,(viejo,nuevo) -> {
            LinkedHashSet<String> set = new LinkedHashSet<>(viejo);
            set.addAll(nuevo);
            return new ArrayList<>(set);
        });

        List<String> us = mapa.computeIfPresent(s,(k,lista) -> {
            if(lista == null) return null;
            lista = new ArrayList<>(lista);
            lista.add("bonus");
            return lista;

        });




    }







    static String leer_linea(Scanner sc, String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    static List<String> leer_lista(Scanner sc, String prompt) {
        String linea = leer_linea(sc, prompt);
        if (linea == null || linea.isBlank()) return new ArrayList<>();
        List<String> out = new ArrayList<>();
        for (String t : linea.split("\\|")) {
            if (t == null) continue;
            String s = t.trim();
            if (!s.isEmpty()) out.add(s);
        }
        return out;
    }
}
