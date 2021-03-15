import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DB_GUI extends JFrame {
      // Anfang Attribute
      public JButton jButton1 = new JButton();
      public JLabel jLabel7 = new JLabel();
      public JLabel jLabel8 = new JLabel();
      public JTextField jTextField1 = new JTextField();
      public JProgressBar jProgressBar = new JProgressBar();
      public Archiver archiver = new Archiver();
      // Ende Attribute

      public DB_GUI() {
            // Frame-Initialisierung
            super();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            int frameWidth = 280;
            int frameHeight = 250;
            setSize(frameWidth, frameHeight);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (d.width - getSize().width) / 2;
            int y = (d.height - getSize().height) / 2;
            setLocation(x, y);
            setTitle("DB-Anwendung");
            setResizable(false);
            Container cp = getContentPane();
            cp.setLayout(null);
            // Anfang Komponenten

            jButton1.setBounds(65, 90, 130, 25);
            jButton1.setText("Starten");
            jButton1.setMargin(new Insets(2, 2, 2, 2));

            jProgressBar.setValue(0);
            jProgressBar.setStringPainted(true);
            jProgressBar.setBounds(16, 150, 233, 25);

            jButton1.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent evt) {
                        new SwingWorker<Void, Void>() {
                              @Override
                              protected Void doInBackground() throws Exception {
                                    jButton1_ActionPerformed();
                                    return null;
                              }
                        }.execute();
                  }
            });

            jLabel7.setBounds(16, 16, 214, 20);
            jLabel7.setText("Pfad des Überodner angeben!");
            jTextField1.setBounds(16, 40, 233, 25);
            jLabel8.setBounds(16, 120, 250, 20);

            cp.add(jButton1);
            cp.add(jLabel7);
            cp.add(jTextField1);
            cp.add(jLabel8);
            cp.add(jProgressBar);

            setVisible(true);
      }

      public void jButton1_ActionPerformed() throws IOException, InterruptedException {
            jProgressBar.setValue(0);
            String path = jTextField1.getText();
            File startFile = new File(path);
            if (!startFile.exists()) {
                  jLabel8.setText("ERR: Dateipfad existiert nicht.");
                  return;
            } else { jLabel8.setText("SUCC: Dateipfad existiert!"); }
            jLabel8.setText("Starte...");
            archiver.startCheck(startFile, jProgressBar, jLabel8);
      }


}
