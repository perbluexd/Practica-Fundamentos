package ProgramaciónFuncional;

import java.util.Optional;

public class OptionalInterface {
    public static void main(String[] args) {

        // 1️⃣ CREACIÓN DE OPTIONAL
        Optional<String> opt1 = Optional.of("Hola");
        Optional<String> opt2 = Optional.empty();            // Se infiere <String> por la variable
        Optional<String> opt3 = Optional.ofNullable(null);   // Vacío porque el valor es null

        System.out.println("opt1 = " + opt1);  // Optional[Hola]
        System.out.println("opt2 = " + opt2);  // Optional.empty
        System.out.println("opt3 = " + opt3);  // Optional.empty


        // 2️⃣ MAP → transformar el valor si existe
        Optional<String> nombre = Optional.of("Ana");
        Optional<Integer> longitud = nombre.map(String::length);
        System.out.println("\nmap(): " + longitud); // Optional[3]

        Optional<String> vacio = Optional.empty();
        Optional<Integer> longitudVacia = vacio.map(String::length);
        System.out.println("map() en vacío: " + longitudVacia); // Optional.empty


        // 3️⃣ FILTER → mantener el valor solo si cumple la condición
        Optional<Integer> edad = Optional.of(20);
        Optional<Integer> mayorDeEdad = edad.filter(e -> e >= 18);
        Optional<Integer> menorDeEdad = edad.filter(e -> e < 18);

        System.out.println("\nfilter() mayor: " + mayorDeEdad); // Optional[20]
        System.out.println("filter() menor: " + menorDeEdad);   // Optional.empty


        // 4️⃣ OR ELSE → devuelve el valor o un valor por defecto
        String valor1 = Optional.of("Texto").orElse("Defecto");
        String valor2 = Optional.<String>empty().orElse("Defecto");  // 👈 Tipo explícito para evitar Object
        System.out.println("\norElse(): " + valor1); // Texto
        System.out.println("orElse(): " + valor2);   // Defecto


        // 5️⃣ OR ELSE GET → igual que orElse, pero usa una función para generar el valor
        String valor3 = Optional.<String>empty().orElseGet(() -> "Generado dinámicamente"); // 👈
        System.out.println("\norElseGet(): " + valor3);


        // 6️⃣ IF PRESENT → ejecuta algo solo si hay valor
        Optional<String> saludo = Optional.of("Hola mundo");
        System.out.println("\nifPresent(): ");
        saludo.ifPresent(s -> System.out.println("El valor es: " + s));
        Optional.<String>empty().ifPresent(s -> System.out.println("No se imprime esto"));   // 👈


        // 7️⃣ FLAT MAP → igual que map, pero evita anidación de Optionals
        Optional<String> palabra = Optional.of("Java");
        Optional<Optional<Integer>> anidado = palabra.map(s -> Optional.of(s.length())); // Optional[Optional[4]]
        Optional<Integer> plano = palabra.flatMap(s -> Optional.of(s.length()));         // Optional[4]

        System.out.println("\nmap() produce anidado: " + anidado);
        System.out.println("flatMap() produce plano: " + plano);


        // 8️⃣ OR ELSE THROW → lanza excepción si está vacío
        try {
            String dato = Optional.<String>empty().orElseThrow(() -> new IllegalStateException("No hay valor")); // 👈
        } catch (Exception e) {
            System.out.println("\norElseThrow(): " + e.getMessage());
        }

        // 9️⃣ IS PRESENT / IS EMPTY → verificar si hay o no valor
        Optional<String> texto = Optional.of("Hola");
        System.out.println("\nisPresent(): " + texto.isPresent()); // true
        System.out.println("isEmpty(): " + texto.isEmpty());       // false
    }
}
