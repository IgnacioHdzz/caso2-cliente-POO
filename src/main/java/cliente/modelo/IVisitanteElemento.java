package cliente.modelo;

/** Patrón Visitor (visitante): una operación distinta para cada tipo de elemento. */
public interface IVisitanteElemento {

    void visitar(AnimalVista pAnimal);

    void visitar(TazaVista pTaza);

    void visitar(BanderaVista pBandera);
}
