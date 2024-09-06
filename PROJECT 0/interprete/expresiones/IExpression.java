package interprete.expresiones;

import java.util.Set;
import interprete.InterpreterContext;

public interface IExpression {
    Set<String> interpretar(InterpreterContext context);  // Este método debe existir
}
