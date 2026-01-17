
package src;

// ===============================
// Nodo.java (ACTUALIZADA)
// ===============================
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Nodo extends UnicastRemoteObject implements NodoRemoto {

    private static final long serialVersionUID = 1L;

    private static final int[] NODOS = { 1, 2, 3, 4 };
    private static final int BASE_PORT = 1099;

    private int idNodo;
    private ContadorCompartido contadorLocal;

    protected Nodo(int idNodo) throws RemoteException {
        this.idNodo = idNodo;
        this.contadorLocal = new ContadorCompartido(0);
    }

    @Override
    public synchronized ContadorCompartido getContador() throws RemoteException {
        return contadorLocal;
    }

    @Override
    public synchronized void recibirUpdate(ContadorCompartido contador) throws RemoteException {
        this.contadorLocal = contador;
        mostrarEstadoObjeto("[UPDATE] Objeto ContadorCompartido recibido : { ", contadorLocal);
    }

    private void hacerRelease() {
        for (int nodo : NODOS) {
            if (nodo == idNodo)
                continue;

            try {
                String url = "rmi://localhost:" + (BASE_PORT + nodo) + "/Nodo" + nodo;
                NodoRemoto remoto = (NodoRemoto) Naming.lookup(url);
                remoto.recibirUpdate(contadorLocal);
            } catch (Exception e) {
                System.out.println("No se pudo contactar al Nodo " + nodo);
            }
        }
        mostrarEstadoObjeto("RELEASE completado. Objeto propagado", contadorLocal);
    }

    private void consultarOtroNodo(int nodoDestino) {
        try {
            String url = "rmi://localhost:" + (BASE_PORT + nodoDestino) + "/Nodo" + nodoDestino;
            NodoRemoto remoto = (NodoRemoto) Naming.lookup(url);
            ContadorCompartido contador = remoto.getContador();
            System.out.println("Objeto consultado del nodo: " + nodoDestino);
            recibirUpdate(contador);
        } catch (Exception e) {
            System.out.println("No se pudo consultar al Nodo " + nodoDestino);
        }
    }

    private void mostrarEstadoObjeto(String titulo, ContadorCompartido c) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.println("\n" + titulo);
        System.out.println("Valor: " + c.getValor());
        System.out.println("Creado por Nodo: " + c.getIdCreador());
        System.out.println("Versión: " + c.getVersion());
        System.out.println(
                "Timestamp: " + (c.getTimestamp() == null ? "N/A" : c.getTimestamp().format(fmt)) + " \n " + "}");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Nodo <idNodo>");
            return;
        }

        try {
            int idNodo = Integer.parseInt(args[0]);
            int puerto = BASE_PORT + idNodo;

            LocateRegistry.createRegistry(puerto);

            Nodo nodo = new Nodo(idNodo);
            Naming.rebind("rmi://localhost:" + puerto + "/Nodo" + idNodo, nodo);

            System.out.println("Nodo " + idNodo + " iniciado");
            System.out.println("Valor inicial del contador: 0");

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n1) Actualizar contador");
                System.out.println("2) Consultar contador en otro nodo");
                System.out.println("3) Salir");
                System.out.print("Opción: ");

                int opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.print("Nuevo valor del contador: ");
                        int nuevoValor = Integer.parseInt(sc.nextLine());

                        // actualización LOCAL del OBJETO
                        nodo.contadorLocal.actualizar(nuevoValor, idNodo);

                        System.out.println("Contador actualizado localmente, AUN NO se ha hecho RELEASE");
                        System.out.println("Presiona ENTER para hacer RELEASE (simulación RC)");
                        sc.nextLine();

                        nodo.hacerRelease();
                        break;

                    case 2:
                        System.out.print("ID del nodo a consultar: ");
                        int destino = Integer.parseInt(sc.nextLine());
                        nodo.consultarOtroNodo(destino);
                        break;

                    case 3:
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Opción inválida");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
