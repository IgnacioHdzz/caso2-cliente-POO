package cliente.modelo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Los animales propios que el jugador tiene seleccionados. */
public class Seleccion {

    private final Set<Integer> idsSeleccionados; // Set es una colección que no permite duplicados y  linkedHashSet recuerda el orden de inserción.

    public Seleccion() {
        this.idsSeleccionados = new LinkedHashSet<>();
    }

    // Si no estaba, lo agrega y si ya estaba, lo quita.
    public void alternar(int pIdAnimal) {
        if (idsSeleccionados.contains(pIdAnimal)) {
            idsSeleccionados.remove(pIdAnimal);
        } else {
            idsSeleccionados.add(pIdAnimal);
        }
    }

    public void seleccionarTodos(List<Integer> pIdsAnimales) {
        idsSeleccionados.addAll(pIdsAnimales);
    }

    public void limpiar() {
        idsSeleccionados.clear();
    }

    public boolean contiene(int pIdAnimal) {
        return idsSeleccionados.contains(pIdAnimal);
    }

    public boolean estaVacia() {
        return idsSeleccionados.isEmpty();
    }

    // Una copia, para que nadie modifique la selección desde afuera. 
    public List<Integer> getIds() {
        return new ArrayList<Integer>(idsSeleccionados);
    }
}
