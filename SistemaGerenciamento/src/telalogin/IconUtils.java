package telalogin;

import javax.swing.*;
import java.awt.*;

public class IconUtils {
    public static ImageIcon carregarIcone(String caminho, int largura, int altura) {
        try {
            ImageIcon icon = new ImageIcon(IconUtils.class.getResource(caminho));
            Image img = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + caminho);
            return null;
        }
    }
}