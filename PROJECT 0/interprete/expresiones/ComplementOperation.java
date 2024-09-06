package interprete.expresiones;

import java.util.Set;
import interprete.InterpreterContext;

public class ComplementOperation implements IExpression {
    private IExpression conjunto;

    public ComplementOperation(IExpression conjunto) {
        this.conjunto = conjunto;
    }

    @Override
    public Set<String> interpretar(InterpreterContext context) {
        Set<String> universo = context.obtenerUniverso();
        Set<String> resultado = conjunto.interpretar(context);
        universo.removeAll(resultado);  // Realizamos el complemento
        return universo;
    }
}
