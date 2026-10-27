package cliente.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;

class SeleccionTest {

    @Test
    void alternarDosVecesDejaElAnimalSinSeleccionar() {
        Seleccion seleccion = new Seleccion();
        seleccion.alternar(5);
        assertTrue(seleccion.contiene(5));
        seleccion.alternar(5);
        assertFalse(seleccion.contiene(5));
    }

    @Test
    void seleccionarTodosYLimpiar() {
        Seleccion seleccion = new Seleccion();
        seleccion.seleccionarTodos(List.of(1, 2, 3));
        assertEquals(List.of(1, 2, 3), seleccion.getIds());
        seleccion.limpiar();
        assertTrue(seleccion.estaVacia());
    }

    @Test
    void unAnimalNoSeRepiteAunqueSeSeleccioneDosVeces() {
        Seleccion seleccion = new Seleccion();
        seleccion.seleccionarTodos(List.of(4, 4, 7));
        assertEquals(List.of(4, 7), seleccion.getIds());
    }
}