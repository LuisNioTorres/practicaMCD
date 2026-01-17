package src;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodoRemoto extends Remote {

    // Obtener el OBJETO contador
    ContadorCompartido getContador() throws RemoteException;

    // Recibir UPDATE del OBJETO completo (WRITE-UPDATE)
    void recibirUpdate(ContadorCompartido contador) throws RemoteException;
}
