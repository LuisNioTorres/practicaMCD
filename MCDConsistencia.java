import java.util.Scanner;

// Clase que representa los datos en la Memoria Compartida Distribuida
/*
Se instancian NODOS en cada TERMINAL.
Cada NODO puede actualizar el CONTADOR.
Despues de un tiempo X los nodos restantes deben actualizar el valor a ese CONTADOR.
PRUEBA ADICIONAL:
En el momento que NODOA actualiza el contador , y otro NODOB intenta leer el contador de OTRO NODOC
¿Seguira viendo el CONTADOR ANTIGUO o el ACTUALIZADO?
*/

class DatosMCD {
    int contador;
    String ultimoEditor;

    public DatosMCD(int contador, String ultimoEditor) {
        this.contador = contador;
        this.ultimoEditor = ultimoEditor;
    }

    @Override
    public String toString() {
        return "Contador: " + contador + " | Ultimo Editor: " + ultimoEditor;
    }
}

// Clase Nodo que simula un computador en la red
class Nodo {
    private String nombre;
    private DatosMCD memoriaLocal;

    // Simula el bus de red (memoria compartida distribuida)
    private static DatosMCD busGlobal = new DatosMCD(0, "Sistema");

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.memoriaLocal = new DatosMCD(0, "Sistema");
    }

    // Operación ACQUIRE: sincroniza desde la red hacia el nodo
    public void acquire() {
        synchronized (busGlobal) {
            memoriaLocal.contador = busGlobal.contador;
            memoriaLocal.ultimoEditor = busGlobal.ultimoEditor;
            System.out.println("[ACQUIRE - " + nombre + "] Memoria local actualizada.");
        }
    }

    // Operación RELEASE: publica cambios del nodo hacia la red
    public void release(int nuevoValor) {
        synchronized (busGlobal) {
            memoriaLocal.contador = nuevoValor;
            memoriaLocal.ultimoEditor = nombre;

            busGlobal.contador = nuevoValor;
            busGlobal.ultimoEditor = nombre;

            System.out.println("[RELEASE - " + nombre + "] Valor publicado: " + nuevoValor);
        }
    }

    public void mostrarMemoria() {
        System.out.println(nombre + " -> " + memoriaLocal);
    }
}

public class MCDConsistencia {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("--- SIMULACION MCD: CONSISTENCIA LIBERADA ---");

        // Crear nodos
        Nodo[] nodos = {
                new Nodo("Nodo-1"),
                new Nodo("Nodo-2"),
                new Nodo("Nodo-3"),
                new Nodo("Nodo-4"),
                new Nodo("Nodo-5")
        };

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Ver memoria local de todos los nodos");
            System.out.println("2. Nodo-1 modifica contador (RELEASE)");
            System.out.println("3. Otros nodos sincronizan (ACQUIRE)");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    for (Nodo n : nodos) {
                        n.mostrarMemoria();
                    }
                    break;

                case 2:
                    System.out.print("Ingrese nuevo valor del contador: ");
                    int valor = sc.nextInt();
                    nodos[0].release(valor);
                    break;

                case 3:
                    for (int i = 1; i < nodos.length; i++) {
                        nodos[i].acquire();
                    }
                    break;

                case 4:
                    salir = true;
                    break;

                default:
                    System.out.println("Opcion no válida.");
            }
        }

        sc.close();
        System.out.println("Simulacion finalizada.");
    }
}
