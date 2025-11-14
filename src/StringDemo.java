public class StringDemo {
    public static void main(String[] args) {
        String texto = "  Hola Mundo  "; // Incluye espacios al inicio y al final

        // ============================================================
        // 🧠 PATRÓN 1: INFORMACIÓN BÁSICA
        // ============================================================
        System.out.println("length(): " + texto.length()); // 14
        System.out.println("charAt(2): " + texto.charAt(2)); // 'H'
        System.out.println("substring(2, 6): " + texto.substring(2, 6)); // "Hola"
        System.out.println("indexOf('M'): " + texto.indexOf('M')); // 7
        System.out.println("lastIndexOf('o'): " + texto.lastIndexOf('o')); // 11


        // ============================================================
        // 🧠 PATRÓN 2: COMPARACIONES
        // ============================================================
        System.out.println("equals(\"  Hola Mundo  \"): " + texto.equals("  Hola Mundo  "));
        System.out.println("equalsIgnoreCase(\"  hola mundo  \"): " + texto.equalsIgnoreCase("  hola mundo  "));


        // ============================================================
        // 🧠 PATRÓN 3: CAMBIO DE CASO (mayúsculas/minúsculas)
        // ============================================================
        System.out.println("toUpperCase(): " + texto.toUpperCase()); // "  HOLA MUNDO  "
        System.out.println("toLowerCase(): " + texto.toLowerCase()); // "  hola mundo  "


        // ============================================================
        // 🧠 PATRÓN 4: MANEJO DE ESPACIOS
        // ============================================================
        System.out.println("trim(): '" + texto.trim() + "'");             // "Hola Mundo"
        System.out.println("strip(): '" + texto.strip() + "'");           // "Hola Mundo" (Unicode-aware)
        System.out.println("stripLeading(): '" + texto.stripLeading() + "'"); // "Hola Mundo  "
        System.out.println("stripTrailing(): '" + texto.stripTrailing() + "'"); // "  Hola Mundo"


        // ============================================================
        // 🧠 PATRÓN 5: REEMPLAZO DE CARACTERES Y SUBCADENAS
        // ============================================================
        System.out.println("replace('o','0'): " + texto.replace('o','0')); // "  H0la Mund0  "
        System.out.println("replace(\"Mundo\",\"Java\"): " + texto.replace("Mundo", "Java")); // "  Hola Java  "


        // ============================================================
        // 🧠 PATRÓN 6: BÚSQUEDA Y EVALUACIÓN DE CONTENIDO
        // ============================================================
        System.out.println("contains(\"Mundo\"): " + texto.contains("Mundo")); // true
        System.out.println("startsWith(\"  Ho\"): " + texto.startsWith("  Ho")); // true
        System.out.println("endsWith(\"ndo  \"): " + texto.endsWith("ndo  "));   // true


        // ============================================================
        // 🧠 PATRÓN 7: DIVISIÓN / SPLIT
        // ============================================================
        // split usa regex — "\\s+" = uno o más espacios
        String[] partes = texto.strip().split("\\s+");
        System.out.println("split(\"\\\\s+\"): " + java.util.Arrays.toString(partes)); // ["Hola", "Mundo"]


        // ============================================================
        // 🧠 PATRÓN 8: CONVERSIÓN A ARREGLO
        // ============================================================
        System.out.println("toCharArray(): " + java.util.Arrays.toString(texto.toCharArray()));
        // [' ', ' ', 'H', 'o', 'l', 'a', ' ', 'M', 'u', 'n', 'd', 'o', ' ', ' ']


        // ============================================================
        // 🧠 PATRÓN 9: NOVEDADES JAVA 11+
        // ============================================================
        System.out.println("isBlank(): " + "   \t".isBlank()); // true
        System.out.println("repeat(3): " + "Ja".repeat(3)); // "JaJaJa"
        System.out.println("lines().count(): " + "a\nb\nc".lines().count()); // 3


        // ============================================================
        // 🧠 PATRÓN 10: FORMATEO Y TRANSFORMACIÓN FUNCIONAL (Java 12-15)
        // ============================================================
        System.out.println("formatted(): " + "Hola, %s. Tienes %d ms".formatted("Percy", 42));
        // "Hola, Percy. Tienes 42 ms"

        String procesado = texto
                .transform(String::strip)
                .transform(s -> s.replace("Mundo", "Java"));
        System.out.println("transform(...): " + procesado); // "Hola Java"


        // ============================================================
        // 🧠 PATRÓN 11: TRADUCCIÓN DE ESCAPES Y FORMATEO VISUAL
        // ============================================================
        String crudo = "Linea1\\n\\tLinea2";
        System.out.print("translateEscapes():\n" + crudo.translateEscapes().indent(2));
        // Muestra:
        //   Linea1
        //     Linea2
    }
}
