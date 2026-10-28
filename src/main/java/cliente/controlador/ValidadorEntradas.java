package cliente.controlador;

import comun.constantes.ConstantesComunes;
import cliente.constantes.Constantes;

// Revisa lo que escribe el jugador antes de enviarlo al servidor.
// Asi no molesta al servidor con cosas que no son válidas.
public class ValidadorEntradas {

    public boolean esNicknameValido(String pNickname) {
        if (pNickname == null) {
            return false;
        }
        String limpio = pNickname.trim(); // Quita espacios al principio y al final.
        return !limpio.isEmpty() && limpio.length() <= Constantes.LARGO_MAXIMO_NICKNAME;
    }

    public boolean esCodigoValido(String pCodigo) {
        if (pCodigo == null) {
            return false;
        }
        String limpio = pCodigo.trim();
        if (limpio.length() != ConstantesComunes.LONGITUD_CODIGO) {
            return false;
        }
        for (int i = 0; i < limpio.length(); i++) {
            if (!Character.isLetterOrDigit(limpio.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}