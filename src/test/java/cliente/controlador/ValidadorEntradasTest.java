package cliente.controlador;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ValidadorEntradasTest {

    private final ValidadorEntradas validador = new ValidadorEntradas();

    @Test
    void nicknameNormalEsValido() {
        assertTrue(validador.esNicknameValido("Ignacio"));
    }

    @Test
    void nicknameVacioOSoloEspaciosNoEsValido() {
        assertFalse(validador.esNicknameValido(""));
        assertFalse(validador.esNicknameValido("   "));
    }

    @Test
    void nicknameDemasiadoLargoNoEsValido() {
        assertFalse(validador.esNicknameValido("UnNicknameDemasiadoLargo"));
    }

    @Test
    void codigoDeCuatroLetrasONumerosEsValido() {
        assertTrue(validador.esCodigoValido("FE4L"));
        assertTrue(validador.esCodigoValido(" fe4l "));
    }

    @Test
    void codigoConOtroLargoOSimbolosNoEsValido() {
        assertFalse(validador.esCodigoValido("ABC"));
        assertFalse(validador.esCodigoValido("ABCDE"));
        assertFalse(validador.esCodigoValido("AB-1"));
    }
}