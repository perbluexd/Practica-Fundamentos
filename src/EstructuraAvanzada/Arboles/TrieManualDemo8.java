package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * Trie (Prefix Tree) para cadenas Unicode (usa HashMap por nodo).
 * Funciones: insert, contains (palabra exacta), startsWith, delete,
 * countWords, countPrefixes, autocompletar (wordsWithPrefix).
 */
public class TrieManualDemo8 {

    static class Trie {
        private static final class Node {
            Map<Character, Node> next = new HashMap<>();
            boolean isWord;      // marca fin de palabra
            int pass;            // cuántas palabras pasan por este nodo (prefijos)
        }

        private final Node root = new Node();
        private int words; // número de palabras distintas almacenadas

        /** Inserta palabra; ignora inserción duplicada sobre la misma palabra. */
        public void insert(String word) {
            Objects.requireNonNull(word, "word");
            Node x = root;
            x.pass++;
            boolean newWord = false;
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                Node nxt = x.next.get(ch);
                if (nxt == null) {
                    nxt = new Node();
                    x.next.put(ch, nxt);
                    newWord = true; // al menos un nodo nuevo
                }
                x = nxt;
                x.pass++;
            }
            if (!x.isWord) { // si no existía exacta
                x.isWord = true;
                words++;
            } else if (newWord) {
                // Palabra ya marcada pero se creó rama (raro si paths comparten)
                // no ajustamos 'words' porque no es palabra nueva
            }
        }

        /** ¿Existe la palabra exacta? */
        public boolean contains(String word) {
            Node x = walk(word);
            return x != null && x.isWord;
        }

        /** ¿Existe alguna palabra que comience con el prefijo? */
        public boolean startsWith(String prefix) {
            return walk(prefix) != null;
        }

        /** Elimina una palabra exacta si existe. Devuelve true si la borró. */
        public boolean delete(String word) {
            if (!contains(word)) return false;
            deleteRec(root, word, 0);
            words--;
            return true;
        }

        private boolean deleteRec(Node node, String w, int i) {
            node.pass--;
            if (i == w.length()) {
                node.isWord = false;
                // Si no tiene hijos y no es palabra, indicar al padre que lo elimine
                return node.next.isEmpty();
            }
            char ch = w.charAt(i);
            Node child = node.next.get(ch);
            boolean removeChild = deleteRec(child, w, i + 1);
            if (removeChild) node.next.remove(ch);
            // borrar este nodo si no es raíz, no es palabra y no tiene hijos
            return !node.isWord && node.next.isEmpty() && node != root;
        }

        /** Número de palabras almacenadas. */
        public int size() { return words; }

        /** Número de palabras que tienen el prefijo dado. */
        public int countPrefixes(String prefix) {
            Node x = walk(prefix);
            return x == null ? 0 : x.pass - (x.isWord ? 0 : 0); // pass cuenta prefijos (incluye palabra si esWord)
        }

        /** Devuelve hasta 'limit' sugerencias que comienzan con 'prefix' (lexicográfico por carácter). */
        public List<String> wordsWithPrefix(String prefix, int limit) {
            List<String> out = new ArrayList<>();
            Node start = walk(prefix);
            if (start == null || limit <= 0) return out;
            StringBuilder sb = new StringBuilder(prefix);
            dfs(start, sb, out, limit);
            return out;
        }

        /** Todas las palabras (cuidado: puede ser grande). */
        public List<String> allWords() {
            List<String> out = new ArrayList<>(words);
            dfs(root, new StringBuilder(), out, Integer.MAX_VALUE);
            return out;
        }

        // ---- Internos ----
        private Node walk(String s) {
            Objects.requireNonNull(s, "string");
            Node x = root;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                x = x.next.get(ch);
                if (x == null) return null;
            }
            return x;
        }

        private void dfs(Node node, StringBuilder path, List<String> out, int limit) {
            if (out.size() >= limit) return;
            if (node.isWord) {
                out.add(path.toString());
                if (out.size() >= limit) return;
            }
            // Para orden determinista, iteramos claves ordenadas
            List<Character> ks = new ArrayList<>(node.next.keySet());
            Collections.sort(ks);
            for (char ch : ks) {
                path.append(ch);
                dfs(node.next.get(ch), path, out, limit);
                path.deleteCharAt(path.length() - 1);
                if (out.size() >= limit) return;
            }
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        Trie trie = new Trie();

        // Inserciones
        trie.insert("hola");
        trie.insert("hola"); // duplicada (ignorará el conteo de palabras)
        trie.insert("holanda");
        trie.insert("holístico");
        trie.insert("hondo");
        trie.insert("honor");
        trie.insert("hoja");
        trie.insert("hotel");
        trie.insert("bot");
        trie.insert("bota");
        trie.insert("botón");

        System.out.println("size (palabras distintas): " + trie.size()); // esperado: 10

        // contains / startsWith
        System.out.println("contains('hola'): " + trie.contains("hola"));       // true
        System.out.println("contains('hole'): " + trie.contains("hole"));       // false
        System.out.println("startsWith('hol'): " + trie.startsWith("hol"));     // true
        System.out.println("startsWith('hz'): " + trie.startsWith("hz"));       // false

        // Autocompletado
        System.out.println("\nAutocomplete 'ho' (lim=6): " + trie.wordsWithPrefix("ho", 6));
        System.out.println("Autocomplete 'bot' (lim=5): " + trie.wordsWithPrefix("bot", 5));

        // Borrado
        System.out.println("\ndelete('hotel'): " + trie.delete("hotel")); // true
        System.out.println("delete('hotel'): " + trie.delete("hotel"));   // false
        System.out.println("contains('hotel'): " + trie.contains("hotel"));// false
        System.out.println("size: " + trie.size());

        // Todas las palabras (orden lexicográfico por carácter):
        System.out.println("\nTodas las palabras:");
        System.out.println(trie.allWords());
    }
}
