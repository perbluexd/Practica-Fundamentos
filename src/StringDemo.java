public class StringDemo {
    public static void main(String[] args) {
        String texto = "  Hola Mundo  "; // Incluye espacios al inicio y al final

        // --- MÉTODOS BÁSICOS ---
        System.out.println("length(): " + texto.length());
        // Cuenta caracteres (incluyendo espacios). Resultado: 14

        System.out.println("charAt(2): " + texto.charAt(2));
        // Devuelve el carácter en el índice 2 (empezando en 0). Aquí es 'H'.

        System.out.println("substring(2, 6): " + texto.substring(2, 6));
        // Devuelve el substring entre índices [2, 6). Resultado: "Hola"

        System.out.println("indexOf('M'): " + texto.indexOf('M'));
        // Busca la primera ocurrencia de 'M'. Resultado: 7

        System.out.println("lastIndexOf('o'): " + texto.lastIndexOf('o'));
        // Busca la última ocurrencia de 'o'. Resultado: 11


        // --- COMPARACIONES ---
        System.out.println("equals(\"  Hola Mundo  \"): " + texto.equals("  Hola Mundo  "));
        // equals compara exactamente con otro String (espacios incluidos). true

        System.out.println("equalsIgnoreCase(\"  hola mundo  \"): " + texto.equalsIgnoreCase("  hola mundo  "));
        // Igual que equals, pero ignora diferencias entre mayúsculas/minúsculas. true


        // --- MAYÚSCULAS Y MINÚSCULAS ---
        System.out.println("toUpperCase(): " + texto.toUpperCase()); // "  HOLA MUNDO  "
        System.out.println("toLowerCase(): " + texto.toLowerCase()); // "  hola mundo  "


        // --- MANEJO DE ESPACIOS ---
        System.out.println("trim(): '" + texto.trim() + "'");
        // trim elimina SOLO espacios en blanco ASCII. Resultado: "Hola Mundo"

        System.out.println("strip(): '" + texto.strip() + "'");
        // strip es Unicode-aware (funciona con espacios no-ASCII también). Resultado: "Hola Mundo"

        System.out.println("stripLeading(): '" + texto.stripLeading() + "'");
        // Quita solo espacios iniciales: "Hola Mundo  "

        System.out.println("stripTrailing(): '" + texto.stripTrailing() + "'");
        // Quita solo espacios finales: "  Hola Mundo"


        // --- REEMPLAZOS ---
        System.out.println("replace('o','0'): " + texto.replace('o','0'));
        // Reemplaza caracteres: "  H0la Mund0  "

        System.out.println("replace(\"Mundo\",\"Java\"): " + texto.replace("Mundo", "Java"));
        // Reemplaza cadenas: "  Hola Java  "


        // --- BÚSQUEDAS ---
        System.out.println("contains(\"Mundo\"): " + texto.contains("Mundo")); // true
        System.out.println("startsWith(\"  Ho\"): " + texto.startsWith("  Ho")); // true
        System.out.println("endsWith(\"ndo  \"): " + texto.endsWith("ndo  "));   // true


        // --- SPLIT ---
        // split usa regex → "\\s+" significa "1 o más espacios"
        String[] partes = texto.strip().split("\\s+");
        System.out.println("split(\"\\\\s+\"): " + java.util.Arrays.toString(partes));
        // Resultado: ["Hola", "Mundo"]


        // --- CONVERTIR A ARREGLO DE CHARS ---
        System.out.println("toCharArray(): " + java.util.Arrays.toString(texto.toCharArray()));
        // Devuelve [' ', ' ', 'H', 'o', 'l', 'a', ' ', 'M', 'u', 'n', 'd', 'o', ' ', ' ']


        // --- NOVEDADES JAVA 11+ ---
        System.out.println("isBlank(): " + "   \t".isBlank());
        // isBlank → true si es vacío o solo espacios en blanco (incluye tab, Unicode). true

        System.out.println("repeat(3): " + "Ja".repeat(3));
        // Repite la cadena 3 veces: "JaJaJa"

        System.out.println("lines().count(): " + "a\nb\nc".lines().count());
        // "lines" devuelve un Stream de líneas. Aquí hay 3.


        // --- FORMATTED (Java 15) ---
        System.out.println("formatted(): " + "Hola, %s. Tienes %d ms".formatted("Percy", 42));
        // Similar a String.format pero más conciso. Resultado: "Hola, Percy. Tienes 42 ms"


        // --- TRANSFORM (Java 12) ---
        String procesado = texto.transform(s -> s.strip())
                .transform(s -> s.replace("Mundo", "Java"));
        System.out.println("transform(...): " + procesado);
        // Se pueden encadenar transformaciones legibles → "Hola Java"


        // --- INDENT y TRANSLATEESCAPES ---
        String crudo = "Linea1\\n\\tLinea2";
        // translateEscapes → interpreta \n, \t, etc.
        // indent(2) → agrega 2 espacios de indentación a cada línea
        System.out.print("translateEscapes():\n" + crudo.translateEscapes().indent(2));
        // Muestra:
        //   Linea1
        //     Linea2
    }
}
