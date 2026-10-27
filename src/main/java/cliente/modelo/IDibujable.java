package cliente.modelo;

/**
 * Patrón Visitor (elemento): algo que se puede dibujar. Es interfaz porque
 * AnimalVista, TazaVista y BanderaVista ya heredan de sus clases base comunes.
 */
public interface IDibujable {

    void aceptar(IVisitanteElemento pVisitante);
}
