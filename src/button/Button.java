package button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Button extends JButton implements ActionListener {

    public Button(){
        super();
        addActionListener(this);
    }

    public abstract void onActionEvent(ActionEvent e);

    @Override
    public void actionPerformed(ActionEvent e){
        onActionEvent(e);
    }
}
