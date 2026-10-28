package cliente.controlador;

import java.io.IOException;
import comun.modelo.Especie;
import comun.modelo.Posicion;
import comun.modelo.MapaBase;
import comun.modelo.Zona;
import comun.protocolo.Mensaje;
import comun.protocolo.TipoMensaje;
import comun.protocolo.datos.DatosAtacar;
import comun.protocolo.datos.DatosCrearPartida;
import comun.protocolo.datos.DatosMoverAnimales;
import comun.protocolo.datos.DatosSeleccionarEspecie;
import comun.protocolo.datos.DatosUnirsePartida;
import comun.protocolo.datos.DatosVerZona;
import cliente.constantes.Constantes;
import cliente.modelo.ModeloJuego;
import cliente.modelo.Seleccion;
import cliente.red.ConexionServidor;

/**
 * MVC (controlador): convierte las acciones del jugador en mensajes al servidor.
 * Las pantallas y el mouse le avisan, él valida, envía, y a veces cambia la selección.
 */
public class ControladorJuego {

    private final ModeloJuego modelo;
    private final ValidadorEntradas validador;
    private ConexionServidor conexion;

    public ControladorJuego(ModeloJuego pModelo) {
        this.modelo = pModelo;
        this.validador = new ValidadorEntradas();
    }

    public void crearPartida(String pNickname) {
        if (!validador.esNicknameValido(pNickname)) {
            modelo.mostrarAviso(Constantes.TEXTO_NICKNAME_INVALIDO);
            return;
        }
        if (!conectar()) {
            modelo.mostrarAviso(Constantes.TEXTO_SIN_CONEXION);
            return;
        }
        String nickname = pNickname.trim();
        modelo.setNickname(nickname);
        enviar(TipoMensaje.CREATE_MATCH, new DatosCrearPartida(nickname));
    }

    public void unirsePartida(String pNickname, String pCodigo) {
        if (!validador.esNicknameValido(pNickname)) {
            modelo.mostrarAviso(Constantes.TEXTO_NICKNAME_INVALIDO);
            return;
        }
        if (!validador.esCodigoValido(pCodigo)) {
            modelo.mostrarAviso(Constantes.TEXTO_CODIGO_INVALIDO);
            return;
        }
        if (!conectar()) {
            modelo.mostrarAviso(Constantes.TEXTO_SIN_CONEXION);
            return;
        }
        String nickname = pNickname.trim();
        modelo.setNickname(nickname);
        enviar(TipoMensaje.JOIN_MATCH, new DatosUnirsePartida(nickname, pCodigo.trim()));
    }

    public void seleccionarEspecie(Especie pEspecie) {
        enviar(TipoMensaje.SELECT_ANIMAL, new DatosSeleccionarEspecie(pEspecie));
    }

    public void moverSeleccionados(Posicion pDestino) {
        Seleccion seleccion = modelo.getSeleccion();
        if (seleccion.estaVacia()) {
            return;
        }
        enviar(TipoMensaje.MOVE_ANIMALS,
                new DatosMoverAnimales(seleccion.getIds(), pDestino.getX(), pDestino.getY()));
    }

    /** Mueve los seleccionados al centro de otra zona; el servidor calcula el camino con Dijkstra. */
    public void moverAZona(int pIdZona) {
        MapaBase mapa = modelo.getMapa();
        if (mapa == null) {
            return;
        }
        Zona zona = mapa.getZona(pIdZona);
        if (zona == null) {
            return;
        }
        moverSeleccionados(zona.getCentro());
    }

    public void atacar(int pIdObjetivo) {
        Seleccion seleccion = modelo.getSeleccion();
        if (seleccion.estaVacia()) {
            return;
        }
        enviar(TipoMensaje.ATTACK, new DatosAtacar(seleccion.getIds(), pIdObjetivo));
    }

    public void verZona(int pIdZona) {
        modelo.getSeleccion().limpiar();
        enviar(TipoMensaje.VIEW_ZONE, new DatosVerZona(pIdZona));
        modelo.notificarObservadores();
    }

    public void seleccionarTodos() {
        int idManada = modelo.getIdManadaPropia();
        modelo.getSeleccion().seleccionarTodos(modelo.getEstadoJuego().getIdsAnimalesDeManada(idManada));
        modelo.notificarObservadores();
    }

    /** La pantalla ya pidió la confirmación antes de llamar a este método. */
    public void abandonarPartida() {
        volverAlInicio();
    }

    public void volverAlInicio() {
        cerrarConexion();
        modelo.volverAlInicio();
    }

    /** Una conexión por partida: se cierra la anterior (si quedó abierta) y se abre una nueva. */
    private boolean conectar() {
        cerrarConexion();
        ConexionServidor nueva = new ConexionServidor(modelo);
        try {
            nueva.conectar();
        } catch (IOException e) {
            return false;
        }
        conexion = nueva;
        return true;
    }

    private void cerrarConexion() {
        if (conexion != null) {
            conexion.desconectar();
            conexion = null;
        }
    }

    private void enviar(TipoMensaje pTipo, Object pDatos) {
        if (conexion != null) {
            conexion.enviar(new Mensaje(pTipo, pDatos));
        }
    }

    public ModeloJuego getModelo() {
        return modelo;
    }
}