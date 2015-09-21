package com.ussc.serialreader;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

@SuppressWarnings("serial")
class ImagePanel extends JComponent {
    private Image image;
    public ImagePanel(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
