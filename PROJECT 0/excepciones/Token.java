package excepciones;

public class Token {
    private final String lexema;
    private final String tipo;
    private final int linea;
    private final int columna;

    public Token(String lexema, String tipo, int linea, int columna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
    }

    // Getters
    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    // Método para verificar si el token es de un tipo específico
    public boolean esTipo(String tipo) {
        return this.tipo.equals(tipo);
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexema='" + lexema + '\'' +
                ", tipo='" + tipo + '\'' +
                ", linea=" + linea +
                ", columna=" + columna +
                '}';
    }
}
