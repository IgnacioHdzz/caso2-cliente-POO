package cliente.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import comun.modelo.AnimalBase;
import comun.modelo.BanderaBase;
import comun.modelo.Posicion;
import comun.modelo.TazaBase;
import comun.protocolo.datos.DatosEstado;

// El último estado del juego recibido del servidor, ya convertido a clases del cliente. 
public class EstadoJuegoCliente {

    private int idZonaVisible;
    private List<Integer> zonasConAnimales;
    private List<AnimalVista> animales;
    private List<TazaVista> tazas;
    private List<BanderaVista> banderas;
    private int capacidadDanio;
    private int tazasConsumidas;
    private int botsEliminados;
    private long momentoRecepcionMs;

    public EstadoJuegoCliente() {
        this.zonasConAnimales = new ArrayList<>();
        this.animales = new ArrayList<>();
        this.tazas = new ArrayList<>();
        this.banderas = new ArrayList<>();
    }

    public void actualizarDesde(DatosEstado pDatos, long pAhoraMs) {
        // 1. Recordar dónde estaba cada animal en la foto anterior (id -> posición).
        Map<Integer, Posicion> posicionesAnteriores = new HashMap<>();
        for (AnimalVista animal : animales) {
            posicionesAnteriores.put(animal.getId(), animal.getPosicion());
        }

        // 2. Convertir lo que llegó en clases del cliente.
        List<AnimalVista> animalesNuevos = new ArrayList<>();
        for (AnimalBase datos : pDatos.getAnimales()) {
            animalesNuevos.add(new AnimalVista(datos, posicionesAnteriores.get(datos.getId())));
        }
        List<TazaVista> tazasNuevas = new ArrayList<>();
        for (TazaBase datos : pDatos.getTazas()) {
            tazasNuevas.add(new TazaVista(datos));
        }
        List<BanderaVista> banderasNuevas = new ArrayList<>();
        for (BanderaBase datos : pDatos.getBanderas()) {
            banderasNuevas.add(new BanderaVista(datos));
        }

        // 3. Reemplazar la foto vieja por la nueva.
        this.animales = animalesNuevos;
        this.tazas = tazasNuevas;
        this.banderas = banderasNuevas;
        this.idZonaVisible = pDatos.getIdZonaVisible();
        this.zonasConAnimales = pDatos.getZonasConAnimales();
        this.capacidadDanio = pDatos.getCapacidadDanio();
        this.tazasConsumidas = pDatos.getTazasConsumidas();
        this.botsEliminados = pDatos.getBotsEliminados();
        this.momentoRecepcionMs = pAhoraMs;
    }

    public List<IDibujable> getElementosDibujables() {
        List<IDibujable> elementos = new ArrayList<>();
        elementos.addAll(tazas);
        elementos.addAll(banderas);
        elementos.addAll(animales);
        return elementos;
    }

    // El animal que está cerca de ese punto (o null). Sirve para saber sobre qué se hizo clic. 
    public AnimalVista buscarAnimalEn(Posicion pPunto, double pRadio) {
        AnimalVista masCercano = null;
        double menorDistancia = pRadio;
        for (AnimalVista animal : animales) {
            double distancia = animal.getPosicion().distanciaA(pPunto);
            if (animal.estaVivo() && distancia <= menorDistancia) {
                masCercano = animal;
                menorDistancia = distancia;
            }
        }
        return masCercano;
    }

    public List<Integer> getIdsAnimalesDeManada(int pIdManada) {
        List<Integer> ids = new ArrayList<>();
        for (AnimalVista animal : animales) {
            if (animal.getIdManada() == pIdManada && animal.estaVivo()) {
                ids.add(animal.getId());
            }
        }
        return ids;
    }

    public int getIdZonaVisible() {
        return idZonaVisible;
    }

    public List<Integer> getZonasConAnimales() {
        return zonasConAnimales;
    }

    public List<AnimalVista> getAnimales() {
        return animales;
    }

    public int getCapacidadDanio() {
        return capacidadDanio;
    }

    public int getTazasConsumidas() {
        return tazasConsumidas;
    }

    public int getBotsEliminados() {
        return botsEliminados;
    }

    public long getMomentoRecepcionMs() {
        return momentoRecepcionMs;
    }
} 