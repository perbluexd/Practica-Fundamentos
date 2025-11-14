package ProgramacionFuncional;

import java.util.Optional;

/**
 * INTERFAZ OPTIONAL<T> – Guía por Patrones
 * -----------------------------------------
 * Patrones cubiertos:
 * 1️⃣ Creación y naturaleza del Optional
 * 2️⃣ Transformaciones seguras (map / flatMap)
 * 3️⃣ Filtrado condicional (filter)
 * 4️⃣ Valores por defecto (orElse / orElseGet / orElseThrow)
 * 5️⃣ Presencia y ausencia (isPresent / isEmpty / ifPresent)
 *
 * Enfocado en evitar NullPointerException y modelar "ausencia de valor"
 * como parte del flujo funcional.
 */
public class OptionalInterface {

    public static void main(String[] args) {
        patron1_creacion();
        patron2_transformaciones();
        patron3_filter();
        patron4_valoresPorDefecto();
        patron5_presenciaAusencia();
    }

    // ============================================================
    // 🧠 PATRÓN 1: CREACIÓN Y NATURALEZA DE OPTIONAL
    // ============================================================
    static void patron1_creacion() {
        System.out.println("\n[PATRÓN 1] Creación de Optional");

        Optional<String> opt1 = Optional.of("Hola");         // ✅ no puede ser null
        Optional<String> opt2 = Optional.empty();            // vacío explícitamente
        Optional<String> opt3 = Optional.ofNullable(null);   // vacío si el valor es null

        System.out.println("opt1 = " + opt1);  // Optional[Hola]
        System.out.println("opt2 = " + opt2);  // Optional.empty
        System.out.println("opt3 = " + opt3);  // Optional.empty
    }

    // ============================================================
    // 🧠 PATRÓN 2: TRANSFORMACIONES SEGURAS (map / flatMap)
    // ============================================================
    static void patron2_transformaciones() {
        System.out.println("\n[PATRÓN 2] Transformaciones (map / flatMap)");

        // --- map() transforma el valor si existe ---
        Optional<String> nombre = Optional.of("Ana");
        Optional<Integer> longitud = nombre.map(String::length);
        System.out.println("map(): " + longitud); // Optional[3]

        Optional<String> vacio = Optional.empty();
        Optional<Integer> longitudVacia = vacio.map(String::length);
        System.out.println("map() en vacío: " + longitudVacia); // Optional.empty

        // --- flatMap() evita Optional anidado ---
        Optional<String> palabra = Optional.of("Java");
        Optional<Optional<Integer>> anidado = palabra.map(s -> Optional.of(s.length())); // Optional[Optional[4]]
        Optional<Integer> plano = palabra.flatMap(s -> Optional.of(s.length()));         // Optional[4]

        System.out.println("\nmap() produce anidado: " + anidado);
        System.out.println("flatMap() produce plano: " + plano);
    }

    // ============================================================
    // 🧠 PATRÓN 3: FILTRADO CONDICIONAL (filter)
    // ============================================================
    static void patron3_filter() {
        System.out.println("\n[PATRÓN 3] Filtrado (filter)");

        Optional<Integer> edad = Optional.of(20);
        Optional<Integer> mayorDeEdad = edad.filter(e -> e >= 18);
        Optional<Integer> menorDeEdad = edad.filter(e -> e < 18);

        System.out.println("filter() mayor: " + mayorDeEdad); // Optional[20]
        System.out.println("filter() menor: " + menorDeEdad);   // Optional.empty
    }

    // ============================================================
    // 🧠 PATRÓN 4: VALORES POR DEFECTO (orElse / orElseGet / orElseThrow)
    // ============================================================
    static void patron4_valoresPorDefecto() {
        System.out.println("\n[PATRÓN 4] Valores por defecto");

        // --- orElse() ---
        String valor1 = Optional.of("Texto").orElse("Defecto");
        String valor2 = Optional.<String>empty().orElse("Defecto");
        System.out.println("orElse(): " + valor1);
        System.out.println("orElse(): " + valor2);

        // --- orElseGet() ---
        String valor3 = Optional.<String>empty().orElseGet(() -> "Generado dinámicamente");
        System.out.println("orElseGet(): " + valor3);

        // --- orElseThrow() ---
        try {
            String dato = Optional.<String>empty()
                    .orElseThrow(() -> new IllegalStateException("No hay valor"));
        } catch (Exception e) {
            System.out.println("orElseThrow(): " + e.getMessage());
        }
    }

    // ============================================================
    // 🧠 PATRÓN 5: PRESENCIA Y AUSENCIA (isPresent / isEmpty / ifPresent)
    // ============================================================
    static void patron5_presenciaAusencia() {
        System.out.println("\n[PATRÓN 5] Presencia / Ausencia");

        Optional<String> saludo = Optional.of("Hola mundo");

        // ifPresent() → ejecuta acción si hay valor
        System.out.println("ifPresent():");
        saludo.ifPresent(s -> System.out.println("El valor es: " + s));

        // Caso vacío → no ejecuta nada
        Optional.<String>empty().ifPresent(s -> System.out.println("No se imprime esto"));

        // Comprobaciones manuales
        Optional<String> texto = Optional.of("Hola");
        System.out.println("isPresent(): " + texto.isPresent()); // true
        System.out.println("isEmpty(): " + texto.isEmpty());     // false
    }
}
