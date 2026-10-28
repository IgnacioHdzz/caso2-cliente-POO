package cliente.red;

import java.io.IOException;
import java.net.Socket;
import javax.swing.SwingUtilities;
import comun.constantes.ConstantesComunes;
import comun.protocolo.ConvertidorJson;
import comun.protocolo.Mensaje;
import comun.red.Conexion;
import comun.red.EscritorMensajes;
import comun.red.IReceptorMensajes;
import comun.red.LectorMensajes;
import cliente.constantes.Constantes;
import cliente.modelo.ModeloJuego;
import cliente.procesadores.DespachadorMensajes;

/**
 * La conexión de este cliente con el servidor. Se abre al presionar Crear o Unirse
 * y se cierra al abandonar o al terminar la partida (una conexión por partida).
 */
public class ConexionServidor implements IReceptorMensajes {

    private final ModeloJuego modelo;
    private final DespachadorMensajes despachador;
    private final ConvertidorJson convertidor;
    private Conexion conexion;
    private EscritorMensajes escritor;
    private volatile boolean cierreVoluntario;

    public ConexionServidor(ModeloJuego pModelo) {
        this.modelo = pModelo;
        this.despachador = new DespachadorMensajes();
        this.convertidor = new ConvertidorJson();
    }

    public void conectar() throws IOException {
        Socket socket = new Socket(ConstantesComunes.HOST, ConstantesComunes.PUERTO);
        conexion = new Conexion(socket);
        escritor = new EscritorMensajes(conexion, convertidor);
        LectorMensajes lector = new LectorMensajes(conexion, convertidor, this);
        new Thread(escritor).start();
        new Thread(lector).start();
    }

    public void enviar(Mensaje pMensaje) {
        if (escritor != null) {
            escritor.enviar(pMensaje);
        }
    }

    public void desconectar() {
        cierreVoluntario = true;
        if (escritor != null) {
            escritor.detener();
        }
        if (conexion != null) {
            conexion.cerrar();
        }
    }

    @Override
    public void recibir(Mensaje pMensaje) {
        SwingUtilities.invokeLater(() -> despachador.despachar(pMensaje, modelo));
    }

    @Override
    public void alDesconectarse() {
        escritor.detener();
        if (cierreVoluntario) {
            return;
        }
        SwingUtilities.invokeLater(() -> modelo.conexionPerdida(Constantes.TEXTO_CONEXION_PERDIDA));
    }
}