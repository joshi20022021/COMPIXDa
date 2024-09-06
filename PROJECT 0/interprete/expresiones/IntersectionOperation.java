package interprete.expresiones;

import java.util.Set;
import interprete.InterpreterContext;

public class IntersectionOperation implements IExpression {
    private IExpression izquierda;
    private IExpression derecha;

    public IntersectionOperation(IExpression izquierda, IExpression derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public Set<String> interpretar(InterpreterContext context) {
        Set<String> resultadoIzquierda = izquierda.interpretar(context);
        Set<String> resultadoDerecha = derecha.interpretar(context);
        resultadoIzquierda.retainAll(resultadoDerecha);  // Realizamos la intersección
        return resultadoIzquierda;
    }
}
