import java.net.*; 
import java.io.*; 
import java.awt.*;
import javax.swing.*;

public class Client { 
  public int vita = 5;
  public int avversario = 5;
  private Socket socket;
  private DataOutputStream os;
  private DataInputStream is;
  JFrame frame;
  ImageIcon rosaRossa;
  ImageIcon rosaBianca;
  JLabel sfondo;
  JLabel statoConnessione;
  JLabel testoGiocatore;
  JLabel testoTurno;
  JLabel testoVita;
  JLabel testoVitaAvversario;
  JLabel risultato;
  JLabel comunicazione;
  JButton button;
  JButton button2;
  JButton uscita;
  JLabel[] rose = new JLabel[6];

  public Client (){
    
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
    boolean turnoMio = false;

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
            Thread.sleep(5000);
            for(int i=0;i<6;i++){
              rose[i].setVisible(false);
            }
            comunicazione.setText("I colpi entrano in un ordine sconosciuto...");
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println("Colpi: " + azione);
          if(turnoMio){
            abilitaPulsanti();
          }
          break;
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
        }
        else if(azione.substring(5, 24).equals("to se stesso bianco")){
        }
        else if(azione.substring(5, 24).equals("to avversario rosso")){
          avversario--;
          aggiornaVitaAvversario();
        }
        else if(azione.substring(5, 25).equals("to avversario bianco")){
        }
      }
      else {
        if (azione.substring(5, 23).equals("to se stesso rosso")) {
          avversario--;
          aggiornaVitaAvversario();
        } 
        else if (azione.substring(5, 24).equals("to se stesso bianco")) {
        } 
        else if (azione.substring(5, 24).equals("to avversario rosso")) {
          vita--;
          aggiornaVita();
        } 
        else if (azione.substring(5, 25).equals("to avversario bianco")) {
        } 
      }
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
  public static void main (String[] args) throws Exception { 
    Client tcpClient = new Client(); 
    tcpClient.grafica();
    tcpClient.start(); 
  } 

  public void grafica() {
    /*JPanel panel = new JPanel();
    panel.setSize(1400, 900);
    panel.setLocation(0, 0);*/

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

    button = new JButton("te");
    button.setSize(100, 100);
    button.setLocation(200, 500);
    button.addActionListener(e -> {
      disabilitaPulsanti();
      try {
        os.writeBytes("Spara se stesso\n");
        System.out.println("Ho sparato me stesso");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    button2 = new JButton("avversario");
    button2.setSize(100, 100);
    button2.setLocation(1500, 500);
    button2.addActionListener(e -> {
      disabilitaPulsanti();
      try {
        os.writeBytes("Spara avversario\n");
        System.out.println("Ho sparato l'avversario");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    uscita = new JButton("esci");
    uscita.setSize(100, 100);
    uscita.setLocation(1700, 900);
    uscita.addActionListener(e -> {
      try {
        os.writeBytes("esci");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      System.exit(0);});

    ImageIcon icon = new ImageIcon("Client/revolver.png");
    JLabel revolver = new JLabel(icon);
    revolver.setSize(500, 500);
    revolver.setLocation(710, 400);

    for(int i=0;i<6;i++){
      rose[i] = new JLabel(rosaBianca);
      rose[i].setSize(128, 128);
      rose[i].setLocation(600+i*100, 925);
      frame.add(rose[i]);
      rose[i].setVisible(false);
    }
    frame.add(comunicazione);
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