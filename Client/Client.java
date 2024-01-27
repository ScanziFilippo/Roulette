import java.net.*; 
import java.io.*; 
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

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
  int intervallo = /*5000*/ 1000;
  List inventario = new List();

  private Socket socket;
  private DataOutputStream os;
  private DataInputStream is;

  boolean turnoMio = false;
  boolean manette = false;

  JFrame frame;
  ImageIcon rosaRossa;
  ImageIcon rosaBianca;
  ImageIcon pistolaTavolo;
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
            Thread.sleep(intervallo);
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
          if(azione.substring(6, 7).equals("1")){
            rose[0].setIcon(rosaRossa);
          }
          else{
            rose[0].setIcon(rosaBianca);
          }
          rose[0].setVisible(true);
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          rose[0].setVisible(false);
        }
        else if(azione.equals("Usato cuore")){
          avversario++;
          aggiornaVitaAvversario();
        }
        else if(azione.substring(0,12).equals("Quadrifoglio")){
          if(azione.substring(13, 14).equals("1")){
            rose[0].setIcon(rosaRossa);
          }
          else{
            rose[0].setIcon(rosaBianca);
          }
          rose[0].setVisible(true);
          try {
            Thread.sleep(intervallo);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          rose[0].setVisible(false);
        }
        
        azione = is.readLine();
      }
      comunicazione.setText("");
      if (azione.equals("esci")){
        risultato.setText("Hai vinto");
        break; 
      }
      if(turnoMio){
        if (azione.substring(5, 23).equals("to se stesso rosso")){
          vita--;
          aggiornaVita();
          System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            vita--;
            aggiornaVita();
          }
        }
        else if(azione.substring(5, 24).equals("to se stesso bianco")){
        }
        else if(azione.substring(5, 24).equals("to avversario rosso")){
          avversario--;
          aggiornaVitaAvversario();
          System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            avversario--;
            aggiornaVitaAvversario();
          }
        }
        else if(azione.substring(5, 25).equals("to avversario bianco")){
        }
      }
      else {
        if (azione.substring(5, 23).equals("to se stesso rosso")) {
          avversario--;
          aggiornaVitaAvversario();
          System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            avversario--;
            aggiornaVitaAvversario();
          }
        } 
        else if (azione.substring(5, 24).equals("to se stesso bianco")) {
        } 
        else if (azione.substring(5, 24).equals("to avversario rosso")) {
          vita--;
          aggiornaVita();
          System.out.println("/" + azione.substring(azione.length()-6, azione.length()) + "/");
          if(azione.substring(azione.length()-6, azione.length()).equals("doppio")){
            vita--;
            aggiornaVita();
          }
        } 
        else if (azione.substring(5, 25).equals("to avversario bianco")) {
        } 
      }
      pistola.setIcon(pistolaTavolo);
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

  private void aggiornaVita() {
    testoVita.setText("vita: " + vita);
    if (vita == 0) {
      testoVita.setText("Hai perso");
      testoVita.setForeground(Color.red);
      testoVitaAvversario.setText("Ha vinto");
      testoVitaAvversario.setForeground(Color.green);
    }
  }

  private void aggiornaVitaAvversario() {
    testoVitaAvversario.setText("vita avversario: " + avversario);
    if (avversario == 0) {
      testoVitaAvversario.setText("Ha perso");
      testoVitaAvversario.setForeground(Color.red);
      testoVita.setText("Hai vinto");
      testoVita.setForeground(Color.green);
    }
  }

  private void nuovoOggetto(){
    if(inventario.getItemCount() != 6){
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

    for(int i=0;i<6;i++){
      oggettiInventario[i] = new JLabel();
      oggettiInventario[i].setSize(96, 96);
      oggettiInventario[i].setLocation(50, 300+i*120);
      oggettiInventario[i].setVisible(true);
      frame.add(oggettiInventario[i]);
    }

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
    button.setSize(100, 100);
    button.setLocation(200, 500);
    button.addActionListener(e -> {(
      new Thread() {
        public void run() {
          disabilitaPulsanti();
          try {
            pistola.setIcon(null);
            Thread.sleep(2000);
            os.writeBytes("Spara se stesso\n");
            System.out.println("Ho sparato me stesso");
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }
      }).start();
    });

    button2 = new JButton("avversario");
    button2.setSize(100, 100);
    button2.setLocation(1500, 500);
    button2.addActionListener(e -> {(
      new Thread() {
      public void run() {
        disabilitaPulsanti();
        try {
          pistola.setIcon(null);
          Thread.sleep(2000);
          os.writeBytes("Spara avversario\n");
          System.out.println("Ho sparato l'avversario");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        }
     }).start();
    });

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

    ImageIcon icon = new ImageIcon("Client/revolver.png");
    JLabel revolver = new JLabel(icon);
    revolver.setSize(500, 500);
    revolver.setLocation(710, 400);

    pistolaTavolo = new ImageIcon("Client/pistolar.png");
    pistola = new JLabel(pistolaTavolo);
    pistola.setSize(400, 400);
    pistola.setLocation(700, 800);

    for(int i=0;i<6;i++){
      rose[i] = new JLabel(rosaBianca);
      rose[i].setSize(128, 128);
      rose[i].setLocation(600+i*100, 925);
      frame.add(rose[i]);
      rose[i].setVisible(false);
    }
    frame.add(comunicazione);
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