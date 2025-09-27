import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Demo integral de HashSet<Integer> con menú interactivo.
 * - Muestra operaciones básicas (add, remove, contains, size, isEmpty).
 * - Recorridos (for-each, Iterator).
 * - Conjuntos: unión, intersección, diferencia.
 * - Utilidades: toArray, containsAll, equals/hashCode, clone.
 * - Streams: filter/map/sorted/collect, any/all/noneMatch, groupingBy, toMap.
 *
 * NOTA: HashSet NO garantiza orden de iteración.
 */
public class HashSetDemoInteger {

    // Opción para salir del menú
    public static final int OPCION_SALIR = 22;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        // Conjunto base donde trabajamos. No admite duplicados. Orden no garantizado.
        HashSet<Integer> numeros = new HashSet<>();

        do {
            mostrarMenu();
            // Lee opción validada en rango [1..OPCION_SALIR]
            opcion = leerEntero(sc, "Ingresa la opción que deseas: ", 1, OPCION_SALIR);

            switch (opcion) {

                case 1 -> { // add
                    /**
                     * add(E e): Inserta el número si NO estaba presente.
                     * Retorna true si el conjunto cambió (se agregó),
                     * false si ya existía (no hay duplicados).
                     */
                    int n = leerEntero(sc, "Número a agregar: ");
                    boolean added = numeros.add(n);
                    System.out.println(added ? "Agregado correctamente." : "Ya existía.");
                }

                case 2 -> { // remove
                    /**
                     * remove(Object o): Elimina el elemento si existe.
                     * Retorna true si se eliminó, false si no estaba.
                     */
                    int n = leerEntero(sc, "Número a eliminar: ");
                    boolean removed = numeros.remove(n);
                    System.out.println(removed ? "Eliminado." : "No encontrado.");
                }

                case 3 -> { // contains
                    /**
                     * contains(Object o): Verifica pertenencia en O(1) promedio.
                     */
                    int n = leerEntero(sc, "Número a buscar: ");
                    System.out.println(numeros.contains(n) ? "Sí está." : "No está.");
                }

                case 4 -> { // size / isEmpty
                    /**
                     * size(): cantidad de elementos.
                     * isEmpty(): true si size()==0.
                     */
                    System.out.println("size(): " + numeros.size());
                    System.out.println("isEmpty(): " + numeros.isEmpty());
                }

                case 5 -> { // listar for-each (sin orden garantizado)
                    /**
                     * Recorre el set con for-each. El orden es arbitrario (propio de HashSet).
                     */
                    if (numeros.isEmpty()) {
                        System.out.println("Conjunto vacío.");
                    } else {
                        for (int n : numeros) {
                            System.out.println("Elemento: " + n);
                        }
                    }
                }

                case 6 -> { // Iterator (sin orden garantizado)
                    /**
                     * Recorre con Iterator manualmente (útil si quieres remove() seguro durante iteración).
                     */
                    if (numeros.isEmpty()) {
                        System.out.println("Conjunto vacío.");
                    } else {
                        Iterator<Integer> it = numeros.iterator();
                        int i = 1;
                        while (it.hasNext()) {
                            System.out.println(i++ + ": " + it.next());
                        }
                    }
                }

                case 7 -> { // unión
                    /**
                     * Unión: A ∪ B = elementos en A o en B (sin duplicados).
                     * addAll(Collection): retorna true si el set cambió.
                     */
                    Set<Integer> otro = csvInt(sc, "Números (sep. por comas): ");
                    Set<Integer> union = new HashSet<>(numeros); // copia para no alterar 'numeros'
                    boolean cambio = union.addAll(otro);
                    System.out.println("Otro: " + otro);
                    System.out.println("Unión: " + union);
                    System.out.println("¿Cambió?: " + cambio);
                }

                case 8 -> { // intersección
                    /**
                     * Intersección: A ∩ B = elementos comunes a ambos.
                     * retainAll(Collection): keep-only elementos presentes en el argumento.
                     * Retorna true si el set cambió (se eliminaron elementos).
                     */
                    Set<Integer> otro = csvInt(sc, "Números (sep. por comas): ");
                    Set<Integer> inter = new HashSet<>(numeros);
                    boolean cambio = inter.retainAll(otro);
                    System.out.println("Otro: " + otro);
                    System.out.println("Intersección: " + inter);
                    System.out.println("¿Cambió?: " + cambio);
                }

                case 9 -> { // diferencia
                    /**
                     * Diferencia: A \ B = elementos en A que NO están en B.
                     * removeAll(Collection): elimina todos los que aparezcan en el argumento.
                     * Retorna true si se eliminaron elementos.
                     */
                    Set<Integer> otro = csvInt(sc, "Números (sep. por comas): ");
                    Set<Integer> dif = new HashSet<>(numeros);
                    boolean cambio = dif.removeAll(otro);
                    System.out.println("Otro: " + otro);
                    System.out.println("Diferencia (A\\B): " + dif);
                    System.out.println("¿Se eliminaron elementos?: " + cambio);
                }

                case 10 -> { // toArray
                    /**
                     * toArray(IntFunction<T[]> generator): crea un array tipado.
                     * Ventaja: evita castings y arrays de Object[].
                     */
                    Integer[] arr = numeros.toArray(Integer[]::new);
                    System.out.println("Array: " + Arrays.toString(arr));
                }

                case 11 -> { // containsAll
                    /**
                     * containsAll(Collection<?> c): true si TODOS los elementos de c están en el set.
                     * Útil para probar si 'numeros' es un superconjunto de 'otro'.
                     */
                    Set<Integer> otro = csvInt(sc, "Números (sep. por comas): ");
                    System.out.println("¿numeros contiene a 'otro'?: " + numeros.containsAll(otro));
                }

                case 12 -> { // removeIf
                    /**
                     * removeIf(Predicate): elimina todos los elementos que cumplan el predicado.
                     * Retorna true si cambió el set.
                     */
                    int limite = leerEntero(sc, "Eliminar números menores a: ");
                    Predicate<Integer> pred = n -> n < limite;
                    boolean cambio = numeros.removeIf(pred);
                    System.out.println("¿Cambió?: " + cambio);
                    System.out.println("Restante: " + numeros);
                }

                case 13 -> { // equals / hashCode
                    /**
                     * equals(Object): true si dos sets contienen EXACTAMENTE los mismos elementos (orden irrelevante).
                     * hashCode(): consistente con equals (mismos elementos => mismo hash).
                     */
                    Set<Integer> otro = csvInt(sc, "Números (sep. por comas): ");
                    System.out.println("equals?: " + numeros.equals(otro));
                    System.out.println("hashCode numeros: " + numeros.hashCode());
                    System.out.println("hashCode otro: " + otro.hashCode());
                }

                case 14 -> { // clone
                    /**
                     * clone(): copia superficial del HashSet (nueva instancia, mismos elementos).
                     * Para tipos inmutables como Integer, es suficiente.
                     */
                    @SuppressWarnings("unchecked")
                    HashSet<Integer> copia = (HashSet<Integer>) numeros.clone();
                    System.out.println("clone(): " + copia);
                    System.out.println("¿Misma instancia? " + (copia == numeros));
                    System.out.println("equals?: " + copia.equals(numeros));
                }

                case 15 -> { // stream: conteo / orden natural
                    /**
                     * Ejemplos con Streams:
                     * - count filtrado
                     * - sorted(): orden natural ascendente (crea stream ordenado, NO cambia el set)
                     */
                    int limite = leerEntero(sc, "Contar números mayores a: ");
                    long conteo = numeros.stream().filter(n -> n > limite).count();
                    System.out.println("Coincidencias: " + conteo);

                    List<Integer> ordenados = numeros.stream().sorted().toList();
                    System.out.println("Ordenados (asc): " + ordenados);
                }

                case 16 -> { // map
                    /**
                     * map(Function): transforma cada elemento y devuelve un stream del nuevo tipo/valor.
                     * toList(): crea lista inmutable (desde Java 16); si usas Java 8-15, usa collect(Collectors.toList()).
                     */
                    var dobles = numeros.stream().map(n -> n * 2).toList();
                    System.out.println("Dobles: " + dobles);

                    var cuadrados = numeros.stream().map(n -> n * n).toList();
                    System.out.println("Cuadrados: " + cuadrados);
                }

                case 17 -> { // sorted con Comparator (reverse / custom)
                    /**
                     * sorted(Comparator): orden con criterio:
                     * - reverseOrder(): descendente
                     * - comparingInt + thenComparingInt: criterio compuesto
                     */
                    var reverso = numeros.stream().sorted(Comparator.reverseOrder()).toList();
                    System.out.println("Orden inverso (desc): " + reverso);

                    var porResiduo = numeros.stream()
                            .sorted(
                                    Comparator
                                            .comparingInt((Integer n) -> n % 10)      // 1° criterio: residuo mod 10
                                            .thenComparingInt(Integer::intValue)      // 2° criterio: orden natural
                            )
                            .toList();
                    System.out.println("Orden por residuo (y natural si empatan): " + porResiduo);
                }

                case 18 -> { // collect
                    /**
                     * collect(Collectors.toSet()): colecciona a Set (NO garantiza tipo específico).
                     * joining(): une strings con separador.
                     */
                    int limite = leerEntero(sc, "Filtrar números mayores a: ");
                    Set<Integer> filtrado = numeros.stream().filter(n -> n > limite).collect(Collectors.toSet());
                    System.out.println("Filtrado→Set (> " + limite + "): " + filtrado);

                    String unidos = numeros.stream().sorted()
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "));
                    System.out.println("joining: " + unidos);
                }

                case 19 -> { // anyMatch / allMatch / noneMatch
                    /**
                     * anyMatch: ¿existe algún elemento que cumpla?
                     * allMatch: ¿todos cumplen?
                     * noneMatch: ¿ninguno cumple?
                     */
                    int limite = leerEntero(sc, "Límite: ");
                    boolean alguno = numeros.stream().anyMatch(n -> n > limite);
                    boolean todos = numeros.stream().allMatch(n -> n > limite);
                    boolean ninguno = numeros.stream().noneMatch(n -> n > limite);
                    System.out.println("anyMatch: " + alguno + " | allMatch: " + todos + " | noneMatch: " + ninguno);
                }

                case 20 -> { // groupingBy / toMap
                    /**
                     * groupingBy: agrupa en Map<claveDeGrupo, List<elem>>
                     *   Aquí agrupamos por paridad: 0 = pares, 1 = impares.
                     * toMap: crea un mapa con función clave y función valor (ojo con claves repetidas).
                     */
                    Map<Integer, List<Integer>> porParidad = numeros.stream()
                            .collect(Collectors.groupingBy(n -> n % 2)); // 0: pares, 1: impares
                    System.out.println("groupingBy(paridad): " + porParidad);

                    Map<Integer, Integer> mapa = numeros.stream()
                            .collect(Collectors.toMap(n -> n, n -> n * n));
                    System.out.println("toMap(n -> n^2): " + mapa);
                }

                case 21 -> { // clear
                    /**
                     * clear(): elimina TODOS los elementos del set.
                     */
                    numeros.clear();
                    System.out.println("Conjunto limpiado.");
                }

                case OPCION_SALIR -> {
                    System.out.println("Gracias por usar el programa. ¡Hasta luego!");
                }

                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != OPCION_SALIR);

        sc.close();
    }

    // ----------------------
    // Helpers de interacción
    // ----------------------

    /** Muestra el menú principal de opciones. */
    static void mostrarMenu() {
        System.out.println("\n--- MENÚ HASHSET (ENTEROS) ---");
        System.out.println("1  . Agregar");
        System.out.println("2  . Eliminar");
        System.out.println("3  . Buscar");
        System.out.println("4  . Tamaño/Vacío");
        System.out.println("5  . Listar (for-each)");
        System.out.println("6  . Listar con Iterator");
        System.out.println("7  . Unión");
        System.out.println("8  . Intersección");
        System.out.println("9  . Diferencia");
        System.out.println("10 . Convertir a array");
        System.out.println("11 . Subconjunto (containsAll)");
        System.out.println("12 . Borrado condicional (removeIf)");
        System.out.println("13 . Igualdad/Hash (equals/hashCode)");
        System.out.println("14 . Clonar (clone)");
        System.out.println("15 . Stream: conteo / orden");
        System.out.println("16 . Stream: map (dobles / cuadrados)");
        System.out.println("17 . Stream: sorted (reverso / residuo)");
        System.out.println("18 . Stream: collect (toSet / joining)");
        System.out.println("19 . Stream: anyMatch / allMatch / noneMatch");
        System.out.println("20 . Stream: groupingBy / toMap");
        System.out.println("21 . Limpiar");
        System.out.println("22 . Salir");
    }

    /**
     * Lee un entero desde consola con reintentos hasta que sea válido.
     */
    static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, intenta de nuevo.");
            }
        }
    }

    /**
     * Lee un entero validado en un rango [min..max].
     */
    static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int n = leerEntero(sc, prompt);
            if (n < min || n > max) {
                System.out.println("Fuera de rango [" + min + " .. " + max + "], intenta de nuevo.");
                continue;
            }
            return n;
        }
    }

    /**
     * Parsea una línea CSV de enteros "1,2,3" -> Set<Integer>{1,2,3}.
     * Ignora tokens vacíos y valores no numéricos.
     */
    static Set<Integer> csvInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String csv = sc.nextLine();
        Set<Integer> set = new HashSet<>();
        if (csv == null || csv.isBlank()) return set;

        for (String p : csv.split(",")) {
            String t = p.trim();
            if (t.isEmpty()) continue;
            try {
                set.add(Integer.parseInt(t));
            } catch (NumberFormatException ignored) {
                // Si algún token no es número, se ignora silenciosamente (o podrías avisar).
            }
        }
        return set;
    }
}
