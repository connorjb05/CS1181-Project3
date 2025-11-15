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
     * Creates a button with the specified text and default styling
     * @param text the text displayed on the button
     */
    public Button(String text){
        super(text);
        initialize();
    }

    /**
     * Creates a button with the specified text and a click event callback
     * @param text the text displayed on the button
     * @param clickEvent the action to execute when the button is clicked
     */
    public Button(String text, Runnable clickEvent){
        super(text);
        this.clickEvent = clickEvent;
        initialize();
    }

    /**
     * Initializes the buttons default appearance, behavior, mouse hover effects,
     * and assigns the ActionListener used to trigger click events.
     *
     * Credit to ChatGPT for assistance in the mouse hovering effect
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

    /**
     * Sets the action executed when the button is clicked.
     * @param e the Runnable/action to run on click
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if (clickEvent != null) clickEvent.run();
    }

    /**
     * Sets the background and foreground colors of the button.
     * @param background the background color to apply
     * @param foreground the text color to apply
     * @return this button instance
     */
    public Button setColors(Color background, Color foreground){
        setBackground(background);
        setForeground(foreground);
        return this;
    }

    /**
     * Sets the font style, weight, and size for the button's text.
     * @param name  the name of the font family
     * @param style the font style (Font.BOLD)
     * @param size  the font size in points
     * @return this button instance
     */
    public Button setFontStyle(String name, int style, int size){
        setFont(new Font(name, style, size));
        return this;
    }

    public void setAction(Runnable clickEvent){
        this.clickEvent = clickEvent;
    }

    /**
     * Credit to ChatGPT for this paint method
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
