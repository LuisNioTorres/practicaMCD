package src;

// ===============================
// ContadorCompartido.java (ACTUALIZADA)
// ===============================
import java.io.Serializable;
import java.time.LocalDateTime;

public class ContadorCompartido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int valor;
    private int version;
    private int idCreador;
    private LocalDateTime timestamp;

    public ContadorCompartido(int valorInicial) {
        this.valor = valorInicial;
        this.version = 0;
        this.idCreador = 0;
        this.timestamp = null;
    }

    // ===== ACTUALIZACIÓN LOCAL (SECCIÓN CRÍTICA) =====
    public void actualizar(int nuevoValor, int idNodo) {
        this.valor = nuevoValor;
        this.version++;
        this.idCreador = idNodo;
        this.timestamp = LocalDateTime.now();
    }

    // ===== GETTERS =====
    public int getValor() {
        return valor;
    }

    public int getVersion() {
        return version;
    }

    public int getIdCreador() {
        return idCreador;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
