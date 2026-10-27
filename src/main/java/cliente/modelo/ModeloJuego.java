package cliente.modelo;

import java.util.ArrayList;
import java.util.List;
import comun.modelo.AnimalBase;
import comun.modelo.Especie;
import comun.modelo.MapaBase;
import comun.protocolo.datos.DatosEstado;
import comun.protocolo.datos.DatosFinJuego;
import comun.protocolo.datos.DatosInicioJuego;
import comun.protocolo.datos.DatosPartidaUnida;
import comun.protocolo.datos.DatosSeleccionEspecies;

// Todo lo que el cliente sabe. 
// Patrón Observer, cada vez que cambia, avisa a las pantallas. Lo modifican solo los procesadores y el controlador, siempre desde el hilo de Swing.

public class ModeloJuego {

    private static final int SIN_MANADA = -1; // Valor que indica que todavía no se sabe el id de la manada propia.

    private final List<IObservadorModelo> observadores;
    private EstadoJuegoCliente estadoJuego;
    private final Seleccion seleccion;
    private FaseCliente fase;
    private String nickname;
    private String codigo;
    private String nicknameRival;
    private List<Especie> especiesDisponibles;
    private Especie especiePropia;
    private Especie especieRival;
    private MapaBase mapa;
    private int idManadaPropia;
    private DatosFinJuego fin;
    private String avisoPendiente;

    public ModeloJuego() {
        this.observadores = new ArrayList<>();
        this.estadoJuego = new EstadoJuegoCliente();
        this.seleccion = new Seleccion();
        this.especiesDisponibles = new ArrayList<>();
        this.fase = FaseCliente.INICIO;
        this.idManadaPropia = SIN_MANADA;
    }

    public void agregarObservador(IObservadorModelo pObservador) {
        observadores.add(pObservador);
    }

    public void notificarObservadores() {
        for (IObservadorModelo observador : observadores) {
            observador.actualizar(this);
        }
    }

    // ---------- Cambios que hacen los procesadores ----------

    public void registrarPartidaCreada(String pCodigo) {
        this.codigo = pCodigo;
        this.fase = FaseCliente.ESPERANDO_RIVAL;
        notificarObservadores();
    }

    public void registrarPartidaUnida(DatosPartidaUnida pDatos) {
        this.codigo = pDatos.getCodigo();
        this.nicknameRival = pDatos.getNicknameRival();
        notificarObservadores();
    }

    public void actualizarSeleccion(DatosSeleccionEspecies pDatos) {
        this.especiesDisponibles = pDatos.getDisponibles();
        this.especiePropia = pDatos.getEspeciePropia();
        this.especieRival = pDatos.getEspecieRival();
        this.fase = FaseCliente.SELECCION_ESPECIE;
        notificarObservadores();
    }

    public void iniciarJuego(DatosInicioJuego pDatos) {
        this.mapa = pDatos.getMapa();
        this.idManadaPropia = pDatos.getIdManadaPropia(); // Aqui se actualiza el id de la manada propia, que antes era SIN_MANADA (la constante de la clase)
        this.especiePropia = pDatos.getEspeciePropia();
        this.estadoJuego = new EstadoJuegoCliente();
        seleccion.limpiar();
        this.fase = FaseCliente.JUGANDO;
        notificarObservadores();
    }

    public void actualizarEstado(DatosEstado pDatos) {
        estadoJuego.actualizarDesde(pDatos, System.currentTimeMillis());
        notificarObservadores();
    }

    public void terminarJuego(DatosFinJuego pDatos) {
        this.fin = pDatos;
        this.fase = FaseCliente.RESUMEN;
        notificarObservadores();
    }

    public void rivalAbandono(String pTexto) {
        this.avisoPendiente = pTexto;
        volverAlInicio();
    }

    public void mostrarAviso(String pTexto) {
        this.avisoPendiente = pTexto;
        notificarObservadores();
    }

    public void conexionPerdida(String pTexto) {
        this.avisoPendiente = pTexto;
        volverAlInicio();
    }

    public void volverAlInicio() { // Lo pueden usar: rivalAbandono, conexionPerdida o botonVolverAlInicio. 
        this.codigo = null;
        this.nicknameRival = null;
        this.especiesDisponibles = new ArrayList<>();
        this.especiePropia = null;
        this.especieRival = null;
        this.mapa = null;
        this.idManadaPropia = SIN_MANADA;
        this.fin = null;
        this.estadoJuego = new EstadoJuegoCliente();
        seleccion.limpiar();
        this.fase = FaseCliente.INICIO;
        notificarObservadores();
    }

    // Devuelve el aviso pendiente (o null) y lo borra, para mostrarlo una sola vez. 
    public String consumirAviso() {
        String aviso = avisoPendiente;
        avisoPendiente = null;
        return aviso;
    }

    public boolean esPropio(AnimalBase pAnimal) {
        return pAnimal.getIdManada() == idManadaPropia;
    }

    // ---------- Getters ----------

    public FaseCliente getFase() {
        return fase;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String pNickname) {
        this.nickname = pNickname;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNicknameRival() {
        return nicknameRival;
    }

    public List<Especie> getEspeciesDisponibles() {
        return especiesDisponibles;
    }

    public Especie getEspeciePropia() {
        return especiePropia;
    }

    public Especie getEspecieRival() {
        return especieRival;
    }

    public MapaBase getMapa() {
        return mapa;
    }

    public int getIdManadaPropia() {
        return idManadaPropia;
    }

    public EstadoJuegoCliente getEstadoJuego() {
        return estadoJuego;
    }

    public Seleccion getSeleccion() {
        return seleccion;
    }

    public DatosFinJuego getFin() {
        return fin;
    }
}
