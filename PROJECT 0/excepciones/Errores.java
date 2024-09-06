package excepciones;

public class Errores {
    private final String tipo;
    private final String descripcion;
    private final int linea;
    private final int columna;
    private final String caracterError;

    // Constructor completo
    public Errores(String tipo, String descripcion, int linea, int columna, String caracterError) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
        this.caracterError = caracterError;
    }

    // Constructor simplificado si no se necesita 'caracterError'
    public Errores(String tipo, String descripcion, int linea, int columna) {
        this(tipo, descripcion, linea, columna, null);
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getCaracterError() {
        return caracterError;
    }

    @Override
    public String toString() {
        return tipo + " en línea " + linea + ", columna " + columna + ": " + descripcion + 
               (caracterError != null ? " ('" + caracterError + "')" : "");
    }
}
