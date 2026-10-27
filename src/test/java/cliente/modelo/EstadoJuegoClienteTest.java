package cliente.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import comun.modelo.AnimalBase;
import comun.modelo.Especie;
import comun.modelo.Posicion;
import comun.protocolo.datos.DatosEstado;

class EstadoJuegoClienteTest {

    private DatosEstado fotoCon(AnimalBase... pAnimales) {
        return new DatosEstado(0, List.of(0), List.of(pAnimales), List.of(), List.of(), 2, 0, 0, List.of());
    }

    private AnimalBase animal(int pId, double pX, int pIdManada, int pEnergia) {
        return new AnimalBase(pId, new Posicion(pX, 100), 0, Especie.JAGUAR, pEnergia, pIdManada, false);
    }

    @Test
    void cadaAnimalRecuerdaDondeEstabaEnLaFotoAnterior() {
        EstadoJuegoCliente estado = new EstadoJuegoCliente();
        estado.actualizarDesde(fotoCon(animal(7, 100, 1, 20)), 1000);
        estado.actualizarDesde(fotoCon(animal(7, 110, 1, 20)), 1050);

        AnimalVista vista = estado.getAnimales().get(0);
        assertEquals(110, vista.getPosicion().getX(), 0.0001);
        assertEquals(100, vista.getPosicionAnterior().getX(), 0.0001);
    }

    @Test
    void encuentraElAnimalMasCercanoAlClic() {
        EstadoJuegoCliente estado = new EstadoJuegoCliente();
        estado.actualizarDesde(fotoCon(animal(1, 100, 1, 20), animal(2, 130, 1, 20)), 1000);

        assertEquals(2, estado.buscarAnimalEn(new Posicion(125, 100), 25).getId());
        assertTrue(estado.buscarAnimalEn(new Posicion(400, 100), 25) == null);
    }

    @Test
    void soloDevuelveLosAnimalesVivosDeEsaManada() {
        EstadoJuegoCliente estado = new EstadoJuegoCliente();
        estado.actualizarDesde(fotoCon(animal(1, 100, 1, 20), animal(2, 130, 1, 0), animal(3, 160, 5, 10)), 1000);

        assertEquals(List.of(1), estado.getIdsAnimalesDeManada(1));
    }
}