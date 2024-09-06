package interprete.expresiones;

import java.util.List;
import java.util.Set;
import interprete.InterpreterContext;

public class CompositeOperation implements IExpression {
    private List<IExpression> operaciones;

    public CompositeOperation(List<IExpression> operaciones) {
        this.operaciones = operaciones;
    }

    @Override
    public Set<String> interpretar(InterpreterContext context) {
        Set<String> resultado = operaciones.get(0).interpretar(context);  // Cambiado a "interpretar"
        for (int i = 1; i < operaciones.size(); i++) {
            resultado.addAll(operaciones.get(i).interpretar(context));  // Cambiado a "interpretar"
        }
        return resultado;
    }
}
