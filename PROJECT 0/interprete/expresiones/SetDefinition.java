package interprete.expresiones;

import java.util.Set;
import interprete.InterpreterContext;

public class SetDefinition implements IExpression {
    private String nombre;
    private Set<String> elementos;

    public SetDefinition(String nombre, Set<String> elementos) {
        this.nombre = nombre;
        this.elementos = elementos;
    }

    @Override
    public Set<String> interpretar(InterpreterContext context) {
        context.definirConjunto(nombre, elementos);
        return elementos;
    }
}
