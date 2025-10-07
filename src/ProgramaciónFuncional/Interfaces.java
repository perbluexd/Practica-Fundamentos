package ProgramaciónFuncional;

public class Interfaces {

    /**
     * La interfaz Instrumento define el contrato mínimo
     * que todo instrumento debe cumplir: saber "tocar".
     */
    interface Instrumento {
        void tocar();
    }

    /**
     * La interfaz Afinable define una capacidad adicional
     * que algunos instrumentos pueden tener: "afinar".
     * No todos los instrumentos implementarán esta interfaz.
     */
    interface Afinable {
        void afinar();
    }

    /**
     * Clase abstracta que sirve como base para todos los instrumentos.
     * Implementa la interfaz Instrumento, pero no define la lógica de "tocar".
     * Además, aporta atributos comunes a todos los instrumentos:
     * nombre y familia.
     */
    abstract class InstrumentoBase implements Instrumento {
        protected String nombre;   // accesible en subclases
        protected String familia;

        // Constructor que inicializa los atributos comunes
        public InstrumentoBase(String nombre, String familia) {
            this.nombre = nombre;
            this.familia = familia;
        }

        // Método concreto opcional que da información común
        public void mostrarInfo() {
            System.out.println("Instrumento: " + nombre + " | Familia: " + familia);
        }
    }

    /**
     * Clase concreta Guitarra:
     * - Hereda de InstrumentoBase (tiene nombre y familia).
     * - Implementa Afinable (porque puede afinarse).
     * - Está obligada a implementar "tocar()" (de Instrumento).
     * - También está obligada a implementar "afinar()" (de Afinable).
     */
    class Guitarra extends InstrumentoBase implements Afinable {
        public Guitarra() {
            super("Guitarra", "Cuerda"); // inicializa los atributos de la clase base
        }

        @Override
        public void tocar() {
            System.out.println("Rasgueando la guitarra 🎸");
        }

        @Override
        public void afinar() {
            System.out.println("Afinando cuerdas de la guitarra 🔧");
        }
    }

    /**
     * Clase concreta Triángulo:
     * - Hereda de InstrumentoBase.
     * - No implementa Afinable (porque no se afina).
     * - Está obligada a implementar "tocar()".
     */
    class Triangulo extends InstrumentoBase {
        public Triangulo() {
            super("Triángulo", "Percusión");
        }

        @Override
        public void tocar() {
            System.out.println("Sonando el triángulo 🔔");
        }
    }

    /**
     * Método main para probar el funcionamiento.
     */
    public static void main(String[] args) {
        Interfaces programa = new Interfaces();

        // Crear una guitarra
        Guitarra guitarra = programa.new Guitarra();
        guitarra.mostrarInfo();
        guitarra.tocar();
        guitarra.afinar();

        // Crear un triángulo
        Triangulo triangulo = programa.new Triangulo();
        triangulo.mostrarInfo();
        triangulo.tocar();
    }
}
