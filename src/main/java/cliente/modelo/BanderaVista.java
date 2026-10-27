package cliente.modelo;

import comun.modelo.BanderaBase;

// Una bandera en el cliente. 
public class BanderaVista extends BanderaBase implements IDibujable {

    public BanderaVista(BanderaBase pDatos) {
        super(pDatos);
    }

    @Override
    public void aceptar(IVisitanteElemento pVisitante) {
        pVisitante.visitar(this);
    }
}
