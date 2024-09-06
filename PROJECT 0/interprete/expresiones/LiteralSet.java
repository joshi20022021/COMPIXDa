package interprete.expresiones;

import java.util.Set;
import interprete.InterpreterContext;

public class LiteralSet implements IExpression {
    private String nombreConjunto;

    public LiteralSet(String nombreConjunto) {
        this.nombreConjunto = nombreConjunto;
    }

    @Override
    public Set<String> interpretar(InterpreterContext context) {
        return context.obtenerConjunto(nombreConjunto);  // Obtiene el conjunto desde el contexto
    }
}
