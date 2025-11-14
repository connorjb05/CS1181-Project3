package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton implements ActionListener {

    private Runnable clickEvent = null;

    /**
     *
     * @param text
     */
    public Button(String text){
        super(text);
        initialize();
    }

    /**
     *
     * @param text
     * @param clickEvent
     */
    public Button(String text, Runnable clickEvent){
        super(text);
        this.clickEvent = clickEvent;
        initialize();
    }

    /**
     *
     */
    private void initialize(){
        setFocusPainted(false);
        setAlignmentX(CENTER_ALIGNMENT);
        setFont(new Font("Arial", Font.BOLD, 18));
        setForeground(Color.WHITE);
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addActionListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                setBackground(new Color(50, 50, 50));
            }

            @Override
            public void mouseExited(MouseEvent e){
                setBackground(new Color(30, 30, 30));
            }

            @Override
            public void mousePressed(MouseEvent e){
                setBackground(new Color(70, 70, 70));
            }

            @Override
            public void mouseReleased(MouseEvent e){
                setBackground(new Color(50, 50, 50));
            }
        });
    }

    public void setAction(Runnable clickEvent){
        this.clickEvent = clickEvent;
    }

    /**
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if (clickEvent != null) clickEvent.run();
    }

    /**
     *
     * @param background
     * @param foreground
     * @return
     */
    public Button setColors(Color background, Color foreground){
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    /**
     *
     * @param name
     * @param style
     * @param size
     * @return
     */
    public Button setFontStyle(String name, int style, int size){
        setFont(new Font(name, style, size));
        return this;
    }

    // Credit to CHatGPT for this paint method

    /**
     *
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics graphics){
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(graphics);
    }
}
