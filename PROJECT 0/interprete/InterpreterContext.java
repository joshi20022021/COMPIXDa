package interprete;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InterpreterContext {
    // Almacena los conjuntos definidos y el universo de caracteres ASCII
    private Map<String, Set<String>> conjuntos = new HashMap<>();
    private Set<String> universo;

    public InterpreterContext() {
        universo = generarUniverso();
    }

    // Define un conjunto con un nombre
    public void definirConjunto(String nombre, Set<String> elementos) {
        conjuntos.put(nombre, elementos);
    }

    // Obtiene un conjunto por su nombre
    public Set<String> obtenerConjunto(String nombre) {
        return conjuntos.get(nombre);
    }

    // Obtiene el universo de caracteres ASCII
    public Set<String> obtenerUniverso() {
        return universo;
    }

    // Genera el universo de caracteres entre ! y ~ en ASCII
    private Set<String> generarUniverso() {
        Set<String> universo = new HashSet<>();
        for (int i = 33; i <= 126; i++) {
            universo.add(Character.toString((char) i));
        }
        return universo;
    }
}
