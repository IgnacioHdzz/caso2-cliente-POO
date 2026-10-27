package cliente.modelo;

/** Patrón Observer: quien quiere enterarse cuando el modelo cambia. */
public interface IObservadorModelo {

    void actualizar(ModeloJuego pModelo);
}
