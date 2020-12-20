package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryGui extends JFrame {

    public static void main(String[] args) {
        EntryGui gui=new EntryGui();
        gui.print_entry();
    }


    public EntryGui(){

    }

    public void print_entry(){
        //הגדרת איך המסך יראה
        this .setSize(300, 100);
        this.getContentPane().setLayout(new FlowLayout());
        JLabel id=new JLabel("Id ");
        JLabel level=new JLabel("Level[0,23] ");
        JTextField id_text =new JTextField("",8);
        JTextField level_text =new JTextField("",8);
        //הגדרתי כפתור
        JButton start= new JButton("Start");
        this.add(id);
        this.add(id_text);
        this.add(level);
        this.add(level_text);
        //הוספתי כפתור למסך
        this.add(start);
        //הפעלתי את המסך
        this.setVisible(true);
    //מה קורה שאני לוחץ על הכפתור start
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ex2 ex2=new Ex2();
                ex2.setId(Integer.parseInt(id_text.getText()));
                ex2.setScenario_num(Integer.parseInt(level_text.getText()));
                Thread client = new Thread(ex2);
                client.start();

            }
        });

    }





}
