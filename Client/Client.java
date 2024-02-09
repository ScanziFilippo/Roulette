import java.net.*; 
import java.io.*; 
import java.awt.*;
import java.awt.event.*;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class Client { 
  public int vita = 5;
  public int avversario = 5;
  String[] oggetti = {
    "lente",
    "quadrifoglio",
    "cuore",
    "teschio",
    "manette"
  };
  int intervallo = /*5000*/ 3000;
  List inventario = new List();

  private Socket socket;
  private DataOutputStream os;
  private DataInputStream is;

  boolean turnoMio = false;
  boolean manette = false;

  JFrame frame;
  JLabel morte;
  ImageIcon rosaRossa;
  ImageIcon rosaBianca;
  ImageIcon pistolaTavolo;
  ImageIcon pistolaMe;
  ImageIcon pistolaMeAvversario;
  ImageIcon pistolaAvversario;
  ImageIcon pistolaAvversarioTe;
  JLabel oggetto;
  JLabel pistola;
  JLabel sfondo;
  JLabel statoConnessione;
  JLabel testoGiocatore;
  JLabel testoTurno;
  JLabel testoVita;
  JLabel testoVitaAvversario;
  JLabel risultato;
  JLabel comunicazione;
  JLabel[] oggettiInventario = new JLabel[6];
  JButton button;
  JButton button2;
  JButton uscita;
  JLabel[] rose = new JLabel[6];

  public Client () throws IOException{
    grafica();
    start();
    inventario.add("lente");
    aggiornaOggetti();
  }

  public void start() throws IOException { 
    try{
      socket = new Socket("localhost", 7777); 
      statoConnessione.setText("connesso");
      statoConnessione.setForeground(Color.green);
    }catch(Exception e){
      statoConnessione.setText("non connesso");
      statoConnessione.setForeground(Color.red);
    }
    os = new DataOutputStream(socket.getOutputStream()); 
    is = new DataInputStream(socket.getInputStream()); 
    while (true) {
      String azione = is.readLine();
      System.out.println("Azione: " + azione);
      while (!azione.substring(0, 5).equals("Spara") && !azione.equals("esci")) {
        if (azione.equals("Sei il primo")) {
          testoGiocatore.setText("Sei il primo");
          os.writeBytes("OK\n");
        } 
        else if (azione.equals("Sei il secondo")) {
          testoGiocatore.setText("Sei il secondo");
          os.writeBytes("OK\n");
        } 
        else if (azione.equals("Turno tuo")) {
          turnoMio = true;
          manette = false;
          testoTurno.setText("Turno tuo");
          abilitaPulsanti();
          os.writeBytes("OK\n");
        }
        else if (azione.equals("Turno non tuo")) {
          turnoMio = false;
          testoTurno.setText("Turno non tuo");
          disabilitaPulsanti();
          os.writeBytes("OK\n");
        }
        else if (azione.substring(0,5).equals("Colpi")) {
          disabilitaPulsanti();
          //comunicazione.setText(azione);
          pistola.setVisible(false);
          for(int i=0;i<6;i++){
            if(azione.substring(8+i*3, 9+i*3).equals("1")){
              rose[i].setIcon(rosaRossa);
            }
            else{
              rose[i].setIcon(rosaBianca);
            }
            rose[i].setVisible(true);
          }
          try {
            Thread.sleep(intervallo);
            for(int i=0;i<6;i++){
              rose[i].setVisible(false);
            }
            comunicazione.setOpaque(true);
            comunicazione.setBorder(BorderFactory.createLineBorder(Color.red, 5));
            comunicazione.setText("I colpi entrano in un ordine sconosciuto...");
            Thread.sleep(intervallo/2);
            comunicazione.setBorder(null);
            comunicazione.setOpaque(false);
            pistola.setVisible(true);
            nuovoOggetto();
            nuovoOggetto();
            nuovoOggetto();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println("Colpi: " + azione);
          if(turnoMio){
            abilitaPulsanti();
          }
          break;
        }
        else if(azione.substring(0,5).equals("Lente")){
          if(turnoMio){
            if(azione.substring(6, 7).equals("1")){
              rose[0].setIcon(rosaRossa);
            }
            else{
              rose[0].setIcon(rosaBianca);
            }
            rose[0].setVisible(true);
            oggetto.setIcon(new ImageIcon("Client/lente.png"));
            oggetto.setLocation(500,950);
            oggetto.setVisible(true);
            try {
              Thread.sleep(intervallo);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            rose[0].setVisible(false);
            oggetto.setVisible(false);
          }
          else{
            System.out.println("Lente avversario");
            oggetto.setIcon(new ImageIcon("Client/lente.png"));
            oggetto.setLocation(1100,700);
            oggetto.setVisible(true);
            try {
              Thread.sleep(intervallo);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            oggetto.setVisible(false);
          }
        }
        else if(azione.equals("Usato cuore")){
          oggetto.setIcon(new ImageIcon("Client/cuore.png"));
          oggetto.setLocation(1100,700);
          oggetto.setVisible(true);
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          oggetto.setVisible(false);
          avversario++;
          aggiornaVitaAvversario();
        }
        else if(azione.substring(0,12).equals("Quadrifoglio")){
          if(azione.substring(13, 14).equals("1")){
            rose[0].setIcon(rosaRossa);
            if(!turnoMio){
              rose[0].setLocation(1100, 900);
            }
          }
          else{
            rose[0].setIcon(rosaBianca);
            if(!turnoMio){
              rose[0].setLocation(1100, 900);
            }
          }
          rose[0].setVisible(true);
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          //scomparsa(rose[0]);
          rose[0].setVisible(false);
          rose[0].setLocation(600, 925);
        }
        else if(azione.equals("Usato manette")){
          oggetto.setIcon(new ImageIcon("Client/manette2.png"));
          if(turnoMio)
            oggetto.setLocation(850,750);
          else
            oggetto.setLocation(850,1000);
          oggetto.setVisible(true);
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        else if(azione.equals("Usato teschio")){
          oggetto.setSize(300, 96);
          oggetto.setIcon(new ImageIcon("Client/barra.png"));
          oggetto.setLocation(780,470);
          oggetto.setVisible(true);
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          /*oggetto.setSize(96, 96);
          oggetto.setVisible(false);*/
        }

        azione = is.readLine();
      }
      comunicazione.setText("");
      if (azione.equals("esci")){
        risultato.setText("Hai vinto");
        break; 
      }
      if(turnoMio){
        if(azione.substring(5, 10).equals("to se")){
          pistola.setIcon(pistolaMe);
          pistola.setVisible(true);
          pistola.setLocation(700, 800);
          tremolio();
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        else if(azione.substring(5, 10).equals("to av")){
          pistola.setIcon(pistolaMeAvversario);
          pistola.setVisible(true);
          pistola.setLocation(1200, 600);
          tremolio();
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          pistola.setVisible(false);
          pistola.setVisible(true);
        }
        if (azione.substring(5, 23).equals("to se stesso rosso")){
          playSound("pistola.wav");
          vita--;
          //System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            vita--;
          }
          aggiornaVita();
        }
        else if(azione.substring(5, 24).equals("to se stesso bianco")){
        }
        else if(azione.substring(5, 24).equals("to avversario rosso")){
          playSound("pistola.wav");
          avversario--;
          //System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            avversario--;
          }
          aggiornaVitaAvversario();
          sfondo.setIcon(new ImageIcon("Client/sfondo2.png"));
          try {
            Thread.sleep(intervallo/2);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          sfondo.setIcon(new ImageIcon("Client/sfondo.gif"));
        }
        else if(azione.substring(5, 25).equals("to avversario bianco")){
        }
      }
      else {
        if(azione.substring(5, 10).equals("to se")){
          pistola.setIcon(pistolaAvversario);
          pistola.setVisible(true);
          pistola.setLocation(650, 550);
          tremolio();
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        else if(azione.substring(5, 10).equals("to av")){
          pistola.setIcon(pistolaAvversarioTe);
          pistola.setVisible(true);
          pistola.setLocation(620, 550);
          tremolio();
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        if (azione.substring(5, 23).equals("to se stesso rosso")) {
          playSound("pistola.wav");
          avversario--;
          //System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            avversario--;
          }
          aggiornaVitaAvversario();
          sfondo.setIcon(new ImageIcon("Client/sfondo2.png"));
          try {
            Thread.sleep(intervallo/2);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          sfondo.setIcon(new ImageIcon("Client/sfondo.gif"));
        } 
        else if (azione.substring(5, 24).equals("to se stesso bianco")) {
        } 
        else if (azione.substring(5, 24).equals("to avversario rosso")) {
          playSound("pistola.wav");
          vita--;
          //System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            vita--;
          }
          aggiornaVita();
        } 
        else if (azione.substring(5, 25).equals("to avversario bianco")) {
        } 
      }
      pistola.setLocation(700, 800);
      pistola.setIcon(pistolaTavolo);
      oggetto.setSize(96, 96);
      oggetto.setVisible(false);
      if (vita == 0) {
        risultato.setText("Hai perso");
        os.writeBytes("esci\n");
        break;
      }
    }

    os.close(); 
    is.close(); 
    socket.close(); 
  } 

  private void scomparsa(JLabel jLabel) {
    // Fade out ImageIcon
    new Thread() {
      public void run() {
        int alpha = 255;
        while (alpha > 0) {
          alpha -= 5;
          jLabel.setBackground(new Color(0, 0, 0, alpha));
          jLabel.repaint();
          jLabel.getIcon().paintIcon(jLabel, jLabel.getGraphics(), 0, 0);
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        jLabel.setVisible(false);
      }
    }.start();
  }

  private void tremolio() {
    new Thread() {
      public void run() {
        int y = pistola.getY();
        int intervallino = 120;
        while(pistola.getY() == y){
          pistola.setLocation(pistola.getX()-5, pistola.getY()-5);
          try {
            Thread.sleep(intervallino);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          pistola.setLocation(pistola.getX()+5, pistola.getY()+5);
          try {
            Thread.sleep(intervallino);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }.start();
  }

  private void aggiornaVita() {
    testoVita.setText("vita: " + vita);
    if (vita <= 0) {
      vita = 0;
      testoVita.setText("Hai perso");
      testoVita.setForeground(Color.red);
      testoVitaAvversario.setText("Ha vinto");
      testoVitaAvversario.setForeground(Color.green);
      morte.setVisible(true);
      morte.setText("Hai perso");
      morte.setForeground(Color.white);
      morte.setFont(new Font("Arial", Font.BOLD, 100));
      //centra
      morte.setHorizontalAlignment(JLabel.CENTER);
    } else {
      morte.setVisible(true);
      /*new Thread() {
        public void run() {*/
          morte.setVisible(true);
          float alpha = 1;
          while(alpha > 0.02){
            alpha -= 0.01;
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            //System.out.println(alpha + " " + morte.isVisible());
            morte.setBackground(new Color(1.0f,0.0f,0.0f,alpha));
          }
          morte.setVisible(false);
          morte.setBackground(new Color(1,0,0,1));/*
        }
      }.start();*/
    }
  }

  private void aggiornaVitaAvversario() {
    testoVitaAvversario.setText("vita avversario: " + avversario);
    if (avversario <= 0) {
      avversario = 0;
      testoVitaAvversario.setText("Ha perso");
      testoVitaAvversario.setForeground(Color.red);
      testoVita.setText("Hai vinto");
      testoVita.setForeground(Color.green);
    }else{
      pistola.setLocation(pistola.getX(), pistola.getY()-1);
    }
  }

  private void nuovoOggetto(){
    if(inventario.getItemCount() < 6){
      inventario.add(oggetti[(int)(Math.random()*oggetti.length)]);
      oggettiInventario[inventario.getItemCount()-1].setIcon(new ImageIcon("Client/" + inventario.getItem(inventario.getItemCount()-1) + ".png"));
      oggettiInventario[inventario.getItemCount()-1].setVisible(true);
      System.out.println("Inventario: " + inventario.getItemCount() + " " + inventario.getItem(inventario.getItemCount()-1));
    }
  }

  private void aggiornaOggetti(){
    for(int i=0;i<6;i++){
      if(i < inventario.getItemCount()){
        oggettiInventario[i].setIcon(new ImageIcon("Client/" + inventario.getItem(i) + ".png"));
        oggettiInventario[i].setVisible(true);
      }
      else{
        oggettiInventario[i].setVisible(false);
      }
    }
  }

  public static synchronized void playSound(final String url) {
    File file = new File("Client/" + url);
    if (!file.exists()) {
      System.out.println("'Client/" + url + "' File not found");
      return;
    }else{
      System.out.println("'Client/" + url + "' File found");
      new Thread(new Runnable() {
        public void run() {
          try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);  
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start(); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
        }
      }).start();    
    }
  }
    
  public static void main (String[] args) throws Exception { 
    Client tcpClient = new Client(); 
    //tcpClient.grafica();
    //tcpClient.start(); 
  } 

  public void grafica() {

    ImageIcon sfond = new ImageIcon("Client/sfondo.gif");
    sfondo = new JLabel(sfond);
    sfondo.setSize(1400, 900);
    sfondo.setLocation(200, 270);

    oggetto = new JLabel();
    oggetto.setSize(96, 96);
    oggetto.setLocation(50, 300);
    oggetto.setVisible(false);

    rosaRossa = new ImageIcon("Client/rosa_rossar.png");
    rosaBianca = new ImageIcon("Client/rosa_biancar.png");

    frame = new JFrame("roulette");
    frame.setLayout(null);
    frame.getContentPane().setBackground(Color.BLACK);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if(turnoMio){
          if(e.getX()>50 && e.getX()<150 && e.getY()>300 && e.getY()<1020){
            int posizione = (e.getY()-300)/120;
            if(posizione < inventario.getItemCount()){
              try {
                if(inventario.getItem(posizione).equals("manette")){
                  if(!manette){
                    manette = true;
                  }else{
                    return;
                  }
                }
                os.writeBytes("Usa " + inventario.getItem(posizione) + "\n");
                System.out.println("Usato " + inventario.getItem(posizione));
                if(inventario.getItem(posizione).equals("cuore")){
                  vita++;
                  aggiornaVita();
                }
                inventario.remove(posizione);
                aggiornaOggetti();
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            }
          }
        }
      }
    });


    JLabel titolo = new JLabel("roulette");
    titolo.setLocation(100, 0);
    titolo.setFont(new Font("Serif", Font.BOLD, 28));
    titolo.setSize(1000, 100);
    titolo.setForeground(Color.red);

    risultato = new JLabel("");
    risultato.setLocation(500, 500);
    risultato.setFont(new Font("Serif", Font.BOLD, 28));
    risultato.setSize(1000, 100);
    risultato.setForeground(Color.white);

    testoVita = new JLabel("vita: " + this.vita);
    testoVita.setLocation(100, 100);
    testoVita.setFont(new Font("Serif", Font.BOLD, 28));
    testoVita.setSize(1000, 100);
    testoVita.setForeground(Color.white);

    testoVitaAvversario = new JLabel("vita avversario: " + this.avversario);
    testoVitaAvversario.setLocation(1500, 100);
    testoVitaAvversario.setFont(new Font("Serif", Font.BOLD, 28));
    testoVitaAvversario.setSize(1000, 100);
    testoVitaAvversario.setForeground(Color.white);

    testoGiocatore = new JLabel("Giocatore ?");
    testoGiocatore.setLocation(1400, 0);
    testoGiocatore.setFont(new Font("Serif", Font.BOLD, 28));
    testoGiocatore.setSize(1000, 100);
    testoGiocatore.setForeground(Color.white);

    statoConnessione = new JLabel("stato connessione");
    statoConnessione.setLocation(600, 0);
    statoConnessione.setFont(new Font("Serif", Font.BOLD, 28));
    statoConnessione.setSize(1000, 100);
    statoConnessione.setForeground(Color.red);

    testoTurno = new JLabel("");
    testoTurno.setLocation(600, 100);
    testoTurno.setFont(new Font("Serif", Font.BOLD, 28));
    testoTurno.setSize(1000, 100);
    testoTurno.setForeground(Color.white);

    comunicazione = new JLabel("");
    comunicazione.setLocation(420, 900);
    comunicazione.setFont(new Font("Serif", Font.BOLD, 32));
    comunicazione.setSize(1000, 100);
    comunicazione.setForeground(Color.white);
    comunicazione.setHorizontalAlignment(JLabel.CENTER);
    comunicazione.setBackground(Color.black);

    button = new JButton("te");
    button.setSize(200, 100);
    button.setLocation(200, 500);
    button.addActionListener(e -> {(
      new Thread() {
        public void run() {
          disabilitaPulsanti();
          try {
            pistola.setIcon(null);
            //Thread.sleep(2000);
            os.writeBytes("Spara se stesso\n");
            System.out.println("Ho sparato me stesso");
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }
      }).start();
    });
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 20));

    button2 = new JButton("avversario");
    button2.setSize(200, 100);
    button2.setLocation(1500, 500);
    button2.addActionListener(e -> {(
      new Thread() {
      public void run() {
        disabilitaPulsanti();
        try {
          pistola.setIcon(null);
          //Thread.sleep(2000);
          os.writeBytes("Spara avversario\n");
          System.out.println("Ho sparato l'avversario");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        }
     }).start();
    });
    button2.setFocusPainted(false);
    button2.setFont(new Font("Arial", Font.BOLD, 20));
    /*button2.setForeground(Color.red);
    button2.setBorder(BorderFactory.createLineBorder(Color.red, 5));
    button2.setBackground(Color.black);*/

    uscita = new JButton("esci");
    uscita.setSize(100, 100);
    uscita.setLocation(1700, 900);
    uscita.addActionListener(e -> {
      try {
        os.writeBytes("esci");
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      System.exit(0);});
    //uscita.setBorder(BorderFactory.createLineBorder(Color.red, 5));
    uscita.setFocusPainted(false);
    uscita.setFont(new Font("Arial", Font.BOLD, 20));
    /*uscita.setForeground(Color.red);
    uscita.setBorder(BorderFactory.createLineBorder(Color.red, 5));
    uscita.setBackground(Color.black);*/
    //uscita.setContentAreaFilled(false);
    uscita.setUI(new BasicButtonUI());

    ImageIcon icon = new ImageIcon("Client/revolver.png");
    JLabel revolver = new JLabel(icon);
    revolver.setSize(500, 500);
    revolver.setLocation(710, 400);

    pistolaMe = new ImageIcon("Client/pistolaMe.png");
    pistolaMeAvversario = new ImageIcon("Client/pistolaMeAvversario.png");
    pistolaAvversario = new ImageIcon("Client/pistolaAvversario.png");
    pistolaAvversarioTe = new ImageIcon("Client/pistolaAvversarioTe.png");
    pistolaTavolo = new ImageIcon("Client/pistolar.png");
    pistola = new JLabel(pistolaTavolo);
    pistola.setSize(400, 400);
    pistola.setLocation(700, 800);

    morte = new JLabel();
    morte.setSize(1920, 1080);
    morte.setLocation(0, 0);
    morte.setBackground(Color.red);
    morte.setVisible(false);
    morte.setOpaque(true);
    frame.add(morte);

    for(int i=0;i<6;i++){
      oggettiInventario[i] = new JLabel();
      oggettiInventario[i].setSize(96, 96);
      oggettiInventario[i].setLocation(50, 300+i*120);
      oggettiInventario[i].setVisible(true);
      frame.add(oggettiInventario[i]);
    }

    for(int i=0;i<6;i++){
      rose[i] = new JLabel(rosaBianca);
      rose[i].setSize(128, 128);
      rose[i].setLocation(600+i*100, 925);
      frame.add(rose[i]);
      rose[i].setVisible(false);
    }

    frame.add(comunicazione);
    frame.add(oggetto);
    frame.add(pistola);
    //frame.add(risultato);
    frame.add(titolo);
    frame.add(testoVita);
    frame.add(testoVitaAvversario);
    frame.add(testoGiocatore);
    frame.add(testoTurno);
    frame.add(statoConnessione);
    frame.add(button);
    frame.add(button2);
    frame.add(uscita);
    //frame.add(revolver);
    frame.add(sfondo);

    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setUndecorated(true);
    frame.setVisible(true);
    disabilitaPulsanti();
  }
  public void disabilitaPulsanti(){
    button.setEnabled(false);
    button2.setEnabled(false);
  }
  public void abilitaPulsanti(){
    button.setEnabled(true);
    button2.setEnabled(true);
  }
}