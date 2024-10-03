package cn.translation.components;

import javax.swing.*;
import java.awt.*;

public class JTextPaneA extends JTextPane {

    public JTextPaneA() {
        super.setSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getUI().getPreferredSize(this).width <= getParent().getSize().width;
    }

    @Override
    public Dimension getPreferredSize() {
        return getUI().getPreferredSize(this);
    }

    @Override
    public synchronized void setText(String t) {
        super.setText(t);
        this.setCaretPosition(this.getText().length());
    }

    @Override
    public synchronized String getText() {
        return super.getText();
    }
}
