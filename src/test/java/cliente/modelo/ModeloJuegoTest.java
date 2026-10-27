package cliente.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ModeloJuegoTest {

    /** Un observador de prueba que solo cuenta cuántas veces lo avisaron. */
    private static class ObservadorContador implements IObservadorModelo {
        private int avisos;

        @Override
        public void actualizar(ModeloJuego pModelo) {
            avisos++;
        }
    }

    @Test
    void alCrearLaPartidaCambiaDeFaseYAvisaALosObservadores() {
        ModeloJuego modelo = new ModeloJuego();
        ObservadorContador observador = new ObservadorContador();
        modelo.agregarObservador(observador);

        modelo.registrarPartidaCreada("A7K2");

        assertEquals(FaseCliente.ESPERANDO_RIVAL, modelo.getFase());
        assertEquals("A7K2", modelo.getCodigo());
        assertEquals(1, observador.avisos);
    }

    @Test
    void siElRivalAbandonaVuelveAlInicioConUnAvisoQueSeMuestraUnaSolaVez() {
        ModeloJuego modelo = new ModeloJuego();
        modelo.registrarPartidaCreada("A7K2");

        modelo.rivalAbandono("Tu rival abandonó la partida.");

        assertEquals(FaseCliente.INICIO, modelo.getFase());
        assertEquals("Tu rival abandonó la partida.", modelo.consumirAviso());
        assertTrue(modelo.consumirAviso() == null);
    }
}