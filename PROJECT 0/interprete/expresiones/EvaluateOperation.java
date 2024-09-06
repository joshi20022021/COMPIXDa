package interprete.expresiones;

import java.util.Set;
import interprete.InterpreterContext;

public class EvaluateOperation implements IExpression {
    private Set<String> elementosAEvaluar;
    private IExpression operacion;

    public EvaluateOperation(Set<String> elementosAEvaluar, IExpression operacion) {
        this.elementosAEvaluar = elementosAEvaluar;
        this.operacion = operacion;
    }

    @Override
    public Set<String> interpretar(InterpreterContext context) {
        Set<String> resultadoOperacion = operacion.interpretar(context);
        for (String elemento : elementosAEvaluar) {
            if (resultadoOperacion.contains(elemento)) {
                System.out.println(elemento + " -> exitoso");
            } else {
                System.out.println(elemento + " -> falló");
            }
        }
        return resultadoOperacion;
    }
}
