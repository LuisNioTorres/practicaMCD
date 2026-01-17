package src;

import java.io.Serializable;

/**
 * Objeto compartido distribuido (DSM basado en objetos)
 */
public class ContadorCompartido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int valor;

    public ContadorCompartido(int valorInicial) {
        this.valor = valorInicial;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int nuevoValor) {
        this.valor = nuevoValor;
    }
}
