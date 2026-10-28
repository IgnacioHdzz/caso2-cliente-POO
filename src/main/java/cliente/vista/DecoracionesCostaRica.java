package cliente.vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Decoraciones inspiradas en Costa Rica. No son obstaculos y no intervienen en el juego.
// Se crean una sola vez por zona y se pintan en la imagen de fondo del renderizador.
final class DecoracionesCostaRica {
    private enum Tipo { CARRETA, ESTADIO, CATARATA, CAFETAL }

    private static final class Adorno {
        final Tipo tipo;
        final int x;
        final int y;
        final double escala;
        final Rectangle2D.Double caja;

        Adorno(Tipo tipo, int x, int y, double escala, double ancho, double alto) {
            this.tipo = tipo;
            this.x = x;
            this.y = y;
            this.escala = escala;
            this.caja = new Rectangle2D.Double(x - ancho / 2, y - alto / 2, ancho, alto);
        }
    }

    private final List<Adorno> adornos = new ArrayList<>();
    private final List<Line2D.Double> caminos;
    private final int ancho;
    private final int alto;
    private final int idZona;
    private final double escala;

    DecoracionesCostaRica(int idZona, int ancho, int alto, List<Line2D.Double> caminos) {
        this.idZona = idZona;
        this.ancho = ancho;
        this.alto = alto;
        this.caminos = caminos;
        this.escala = Math.max(0.48, Math.min(1.40,
                Math.min(ancho / 1000.0, alto / 620.0)));
        ubicarAdornos();
    }

    // Esta reserva deja espacio libre alrededor de los elementos culturales.
    boolean espacioLibre(int x, int y, int radio) {
        for (Adorno adorno : adornos) {
            if (adorno.caja.intersects(x - radio - 5, y - radio - 5,
                    2.0 * (radio + 5), 2.0 * (radio + 5))) return false;
        }
        return true;
    }

    private void ubicarAdornos() {
        // Cada zona tiene los cuatro tipos, pero en sectores diferentes.
        // Si no cabe un adorno fuera del camino, se omite en vez de tapar la ruta.
        Tipo[] tipos = Tipo.values();
        double[][] sectores = {
            {0.17, 0.19}, {0.82, 0.18}, {0.81, 0.78}, {0.18, 0.78}
        };
        double[][] alternativas = {
            {0, 0}, {0.08, 0}, {-0.08, 0}, {0, 0.12}, {0, -0.12},
            {0.10, 0.11}, {-0.10, -0.10}, {0.13, -0.12}, {-0.13, 0.12}
        };
        for (int sector = 0; sector < sectores.length; sector++) {
            Tipo tipo = tipos[(sector + Math.floorMod(idZona, 4)) % tipos.length];
            double anchoFigura = (tipo == Tipo.ESTADIO ? 160 : tipo == Tipo.CARRETA ? 142 : 124) * escala;
            double altoFigura = (tipo == Tipo.ESTADIO ? 110 : tipo == Tipo.CARRETA ? 100 : 110) * escala;
            for (double[] alternativa : alternativas) {
                int x = (int) Math.round((sectores[sector][0] + alternativa[0]) * ancho);
                int y = (int) Math.round((sectores[sector][1] + alternativa[1]) * alto);
                Adorno candidato = new Adorno(tipo, x, y, escala, anchoFigura, altoFigura);
                if (cabe(candidato)) {
                    adornos.add(candidato);
                    break;
                }
            }
        }
    }

    private boolean cabe(Adorno candidato) {
        Rectangle2D.Double r = candidato.caja;
        if (r.x < 13 || r.y < 13 || r.x + r.width > ancho - 13
                || r.y + r.height > alto - 13) return false;
        Rectangle2D.Double exterior = new Rectangle2D.Double(r.x - 13, r.y - 13,
                r.width + 26, r.height + 26);
        for (Line2D.Double camino : caminos) {
            // Se respetan tambien los bordes del sendero, no solo su linea central.
            Rectangle2D.Double margenCamino = new Rectangle2D.Double(
                    exterior.x - 20, exterior.y - 20,
                    exterior.width + 40, exterior.height + 40);
            if (margenCamino.intersectsLine(camino)) return false;
        }
        for (Adorno existente : adornos) {
            if (exterior.intersects(existente.caja)) return false;
        }
        return true;
    }

    void dibujar(Graphics2D original) {
        Graphics2D g = (Graphics2D) original.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Detalles pequenos: plantas de cafe, flores de heliconia y flores tropicales.
            Random azar = new Random(519127L + idZona * 48611L);
            int total = Math.max(20, ancho * alto / 18000);
            for (int i = 0; i < total; i++) {
                int x = 18 + azar.nextInt(Math.max(1, ancho - 36));
                int y = 18 + azar.nextInt(Math.max(1, alto - 36));
                int r = 8 + azar.nextInt(6);
                if (!espacioLibre(x, y, r + 4) || !lejosDelCamino(x, y, r + 20)) continue;
                Graphics2D figura = (Graphics2D) g.create();
                try {
                    figura.translate(x, y);
                    double factor = 0.6 + azar.nextDouble() * 0.35;
                    figura.scale(factor, factor);
                    if (i % 6 == 0) dibujarPlantaCafe(figura);
                    else if (i % 4 == 0) dibujarHeliconia(figura);
                    else dibujarFlor(figura, i % 3 == 0 ? new Color(240, 103, 72) : new Color(244, 183, 56));
                } finally {
                    figura.dispose();
                }
            }
            // Los elementos principales van encima de la textura, pero nunca de las unidades.
            for (Adorno adorno : adornos) {
                Graphics2D figura = (Graphics2D) g.create();
                try {
                    figura.translate(adorno.x, adorno.y);
                    figura.scale(adorno.escala, adorno.escala);
                    switch (adorno.tipo) {
                        case CARRETA -> dibujarCarreta(figura);
                        case ESTADIO -> dibujarEstadio(figura);
                        case CATARATA -> dibujarCatarata(figura);
                        case CAFETAL -> dibujarCafetal(figura);
                    }
                } finally {
                    figura.dispose();
                }
            }
        } finally {
            g.dispose();
        }
    }

    private boolean lejosDelCamino(int x, int y, double distancia) {
        for (Line2D.Double camino : caminos) {
            if (camino.ptSegDist(x, y) < distancia) return false;
        }
        return true;
    }

    private static void dibujarCarreta(Graphics2D g) {
        // Carreta tipica costarricense: tonos rojos, ruedas policromadas y dos bueyes.
        g.setColor(new Color(8, 37, 22, 105));
        g.fillOval(-71, -30, 143, 74);
        g.setColor(new Color(97, 55, 27));
        g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(16, -11, 62, -17);
        g.drawLine(16, 18, 62, 20);
        // Bueyes vistos ligeramente desde arriba.
        dibujarBuey(g, 56, -18);
        dibujarBuey(g, 56, 20);
        g.setColor(new Color(85, 43, 25));
        g.fillRoundRect(-61, -33, 81, 62, 12, 12);
        g.setColor(new Color(191, 59, 34));
        g.fillRoundRect(-59, -34, 76, 58, 9, 9);
        g.setColor(new Color(238, 139, 43));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(-58, -33, 75, 57, 9, 9);
        g.setColor(new Color(40, 111, 152));
        g.fillRoundRect(-49, -25, 57, 39, 6, 6);
        g.setColor(new Color(245, 183, 65));
        g.drawRoundRect(-49, -25, 57, 39, 6, 6);
        // Flores y mandalas pintados en la madera.
        for (int k = 0; k < 3; k++) {
            int xx = -38 + k * 20;
            pintarFlorCarreta(g, xx, -6, k);
        }
        g.setColor(new Color(238, 167, 79));
        g.fillRect(-63, -39, 83, 7);
        g.fillRect(-63, 25, 83, 7);
        // Ruedas decoradas con radios de multiples colores.
        rueda(g, -41, 31, 17);
        rueda(g, 4, 31, 17);
        g.setStroke(new BasicStroke(1));
    }

    private static void dibujarBuey(Graphics2D g, int x, int y) {
        g.setColor(new Color(238, 223, 185));
        g.fillOval(x - 13, y - 11, 23, 22);
        g.setColor(new Color(156, 134, 100));
        g.drawOval(x - 13, y - 11, 23, 22);
        g.setColor(new Color(247, 238, 207));
        g.fillOval(x + 5, y - 7, 15, 14);
        g.setColor(new Color(100, 73, 47));
        g.fillOval(x + 14, y - 4, 3, 3);
        g.fillOval(x + 14, y + 2, 3, 3);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawArc(x + 5, y - 14, 19, 10, 20, 150);
        g.drawArc(x + 5, y + 4, 19, 10, 190, 150);
        g.setStroke(new BasicStroke(1));
    }

    private static void rueda(Graphics2D g, int x, int y, int r) {
        g.setColor(new Color(73, 35, 27));
        g.fillOval(x - r - 2, y - r - 2, (r + 2) * 2, (r + 2) * 2);
        g.setColor(new Color(246, 154, 37));
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.setColor(new Color(19, 116, 180));
        g.fillOval(x - r + 4, y - r + 4, (r - 4) * 2, (r - 4) * 2);
        Color[] colores = {new Color(247, 221, 69), new Color(239, 70, 61),
                new Color(106, 209, 149), new Color(251, 246, 223)};
        for (int j = 0; j < 12; j++) {
            double a = j * Math.PI / 6;
            g.setColor(colores[j % colores.length]);
            g.setStroke(new BasicStroke(2.8f));
            g.drawLine(x + (int)(5 * Math.cos(a)), y + (int)(5 * Math.sin(a)),
                    x + (int)(14 * Math.cos(a)), y + (int)(14 * Math.sin(a)));
        }
        g.setColor(new Color(243, 190, 49));
        g.fillOval(x - 5, y - 5, 10, 10);
        g.setColor(new Color(99, 38, 45));
        g.drawOval(x - 5, y - 5, 10, 10);
    }

    private static void pintarFlorCarreta(Graphics2D g, int x, int y, int indice) {
        Color petalo = indice % 2 == 0 ? new Color(245, 80, 64) : new Color(245, 216, 68);
        for (int i = 0; i < 8; i++) {
            double angulo = Math.PI * i / 4.0;
            int px = x + (int)(8 * Math.cos(angulo));
            int py = y + (int)(8 * Math.sin(angulo));
            g.setColor(petalo);
            g.fillOval(px - 4, py - 4, 8, 8);
        }
        g.setColor(new Color(255, 249, 203));
        g.fillOval(x - 5, y - 5, 10, 10);
    }

    private static void dibujarEstadio(Graphics2D g) {
        // Miniestadio: graderias, cancha con franjas y reflectores.
        g.setColor(new Color(10, 36, 25, 120));
        g.fillRoundRect(-79, -53, 162, 111, 9, 9);
        g.setColor(new Color(79, 65, 51));
        g.fillRoundRect(-77, -52, 155, 103, 6, 6);
        g.setColor(new Color(158, 128, 92));
        for (int j = 0; j < 3; j++) {
            g.fillRoundRect(-72, -51 + j * 5, 144, 3, 2, 2);
            g.fillRoundRect(-72, 44 - j * 5, 144, 3, 2, 2);
        }
        // Cespet a franjas.
        g.setColor(new Color(42, 129, 65));
        g.fillRect(-65, -34, 130, 68);
        for (int j = 0; j < 8; j++) {
            g.setColor(j % 2 == 0 ? new Color(59, 153, 74) : new Color(48, 138, 69));
            g.fillRect(-65 + j * 16, -34, j == 7 ? 18 : 16, 68);
        }
        g.setColor(new Color(248, 248, 220));
        g.setStroke(new BasicStroke(1.8f));
        g.drawRect(-63, -32, 126, 64);
        g.drawLine(0, -32, 0, 32);
        g.drawOval(-11, -11, 22, 22);
        g.drawRect(-63, -18, 21, 36);
        g.drawRect(42, -18, 21, 36);
        g.drawRect(-63, -10, 8, 20);
        g.drawRect(55, -10, 8, 20);
        g.drawLine(-66, -11, -66, 11);
        g.drawLine(66, -11, 66, 11);
        g.setColor(new Color(235, 228, 205));
        g.fillOval(-2, -2, 4, 4);
        // Torres de iluminacion, visibles desde la vista superior.
        dibujarLuz(g, -74, -45);
        dibujarLuz(g, 74, -45);
        dibujarLuz(g, -74, 45);
        dibujarLuz(g, 74, 45);
        g.setStroke(new BasicStroke(1));
    }

    private static void dibujarLuz(Graphics2D g, int x, int y) {
        g.setColor(new Color(17, 46, 34, 90));
        g.fillOval(x - 2, y + 2, 16, 9);
        g.setColor(new Color(93, 103, 107));
        g.fillRoundRect(x - 6, y - 7, 12, 14, 3, 3);
        g.setColor(new Color(238, 240, 211));
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                g.fillRect(x - 4 + j * 5, y - 5 + k * 6, 3, 4);
            }
        }
    }

    private static void dibujarCatarata(Graphics2D g) {
        g.setColor(new Color(9, 43, 31, 100));
        g.fillOval(-53, -34, 111, 81);
        // Poza turquesa.
        g.setColor(new Color(26, 104, 109));
        g.fillOval(-48, -9, 93, 54);
        g.setColor(new Color(41, 158, 172));
        g.fillOval(-40, -6, 75, 43);
        g.setColor(new Color(96, 207, 209, 130));
        g.fillOval(-31, 0, 55, 28);
        // Bloques de roca a ambos lados.
        roca(g, -41, -25, 25);
        roca(g, -17, -39, 24);
        roca(g, 13, -39, 25);
        roca(g, 35, -24, 25);
        roca(g, -52, 13, 19);
        roca(g, 38, 13, 20);
        // Chorro y espuma.
        g.setColor(new Color(36, 126, 166));
        g.fillRoundRect(-12, -23, 25, 44, 9, 9);
        g.setColor(new Color(119, 221, 240));
        g.fillRoundRect(-9, -22, 19, 40, 8, 8);
        g.setColor(new Color(229, 252, 250, 210));
        g.fillRoundRect(-4, -23, 5, 34, 4, 4);
        for (int i = 0; i < 5; i++) {
            g.fillOval(-21 + i * 9, 13 + (i % 2) * 4, 10, 6);
        }
        // Heliconias en la orilla.
        Graphics2D flor = (Graphics2D) g.create();
        flor.translate(-44, 31);
        flor.scale(0.8, 0.8);
        dibujarHeliconia(flor);
        flor.dispose();
    }

    private static void roca(Graphics2D g, int x, int y, int diametro) {
        g.setColor(new Color(49, 70, 58));
        g.fillOval(x + 2, y + 4, diametro, diametro * 3 / 4);
        g.setColor(new Color(116, 133, 123));
        g.fillOval(x, y, diametro, diametro * 3 / 4);
        g.setColor(new Color(174, 186, 171));
        g.fillOval(x + 3, y + 1, diametro / 2, diametro / 4);
    }

    private static void dibujarCafetal(Graphics2D g) {
        // Pequenio cafetal; la fruta roja y las hojas brillantes se ven incluso a escala.
        g.setColor(new Color(15, 43, 28, 102));
        g.fillOval(-57, -42, 112, 89);
        for (int i = 0; i < 5; i++) {
            Graphics2D planta = (Graphics2D) g.create();
            try {
                int x = -33 + (i % 3) * 32;
                int y = -18 + (i / 3) * 35;
                planta.translate(x, y);
                planta.scale(1.00, 1.00);
                dibujarPlantaCafe(planta);
            } finally {
                planta.dispose();
            }
        }
        // Pequeno saco de cafe en el borde del cultivo.
        g.setColor(new Color(105, 74, 46));
        g.fillRoundRect(25, 23, 24, 15, 6, 6);
        g.setColor(new Color(192, 161, 111));
        g.fillRoundRect(25, 21, 23, 13, 5, 5);
        g.setColor(new Color(100, 72, 45));
        g.drawLine(28, 25, 44, 25);
    }

    private static void dibujarPlantaCafe(Graphics2D g) {
        g.setStroke(new BasicStroke(2.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(18, 75, 39));
        g.drawLine(-3, 11, 2, -11);
        for (int k = 0; k < 5; k++) {
            double a = k * Math.PI * 2 / 5;
            double xx = Math.cos(a) * 12;
            double yy = Math.sin(a) * 12;
            g.setColor(new Color(21, 82, 41));
            g.fillOval((int)xx - 8, (int)yy - 7, 16, 14);
            g.setColor(k % 2 == 0 ? new Color(42, 139, 68) : new Color(35, 115, 55));
            g.fillOval((int)xx - 7, (int)yy - 6, 13, 11);
            g.setColor(new Color(129, 188, 94, 110));
            g.drawArc((int)xx - 6, (int)yy - 6, 12, 10, 35, 105);
        }
        g.setColor(new Color(210, 39, 45));
        g.fillOval(-4, -4, 7, 7);
        g.fillOval(1, 0, 7, 7);
        g.fillOval(-7, 1, 7, 7);
        g.setColor(new Color(250, 115, 101));
        g.fillOval(-2, -3, 2, 2);
        g.setStroke(new BasicStroke(1));
    }

    private static void dibujarFlor(Graphics2D g, Color color) {
        g.setColor(new Color(17, 76, 36));
        g.fillOval(-10, -5, 22, 13);
        for (int i = 0; i < 5; i++) {
            double a = i * Math.PI * 2 / 5;
            g.setColor(color);
            g.fillOval((int)(Math.cos(a) * 6) - 5, (int)(Math.sin(a) * 6) - 5, 10, 10);
        }
        g.setColor(new Color(255, 217, 72));
        g.fillOval(-4, -4, 8, 8);
    }

    private static void dibujarHeliconia(Graphics2D g) {
        g.setColor(new Color(26, 104, 48));
        g.setStroke(new BasicStroke(2.8f));
        g.drawLine(-2, 12, 3, -15);
        g.setColor(new Color(36, 126, 62));
        g.fillOval(-16, 1, 20, 11);
        g.fillOval(1, 0, 18, 10);
        g.setColor(new Color(227, 68, 42));
        Path2D.Double roja = new Path2D.Double();
        roja.moveTo(2, -14);
        roja.lineTo(-12, -19);
        roja.lineTo(-5, -5);
        roja.closePath();
        g.fill(roja);
        g.setColor(new Color(243, 151, 36));
        Path2D.Double naranja = new Path2D.Double();
        naranja.moveTo(4, -12);
        naranja.lineTo(13, -20);
        naranja.lineTo(10, -3);
        naranja.closePath();
        g.fill(naranja);
        g.setColor(new Color(243, 211, 72));
        g.fillOval(1, -20, 5, 8);
        g.setStroke(new BasicStroke(1));
    }
}
