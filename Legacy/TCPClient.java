import java.net.*; 
import java.io.*; 
import java.awt.*;
import javax.swing.*;

public class TCPClient { 
  public int vita = 5;
  public int avversario = 5;
  private Socket socket;
  private DataOutputStream os;
  private DataInputStream is;
  JLabel statoConnessione;
  JLabel testoGiocatore;
  JLabel testoTurno;
  JLabel testoVita;
  JLabel testoVitaAvversario;
  JButton button;
  JButton button2;
  JButton uscita;

  public TCPClient (){
    
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
    //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 
    while(true){
      String proca = is.readLine();
      if(proca.equals("Sei il primo")){
        turnoMio = true;
        testoGiocatore.setText("Giocatore 1");
        os.writeBytes("OK\n");
        break;  
      }else if(proca.equals("Sei il secondo")){
        testoGiocatore.setText("Giocatore 2");
        os.writeBytes("OK\n");
        break;
      }
    }
    System.out.println("Partita avviata");
    while (true) { 
      String proca = is.readLine();
      //String userInput = stdIn.readLine(); 
      if (proca.equals("QUIT")) 
        break; 
      /*os.writeBytes(userInput + '\n');  
      System.out.println("Hai digitato: " + is.readLine()); */
      if(proca.equals("Turno tuo")){
        turnoMio = true;
        testoTurno.setText("Turno tuo");
        os.writeBytes("Turno OK\n");
        abilitaPulsanti();
        System.out.println("Turno mio");
      }else if(proca.equals("Turno non tuo")){
        turnoMio = false;
        testoTurno.setText("Turno non tuo");
        os.writeBytes("Turno OK\n");
        disabilitaPulsanti();
        System.out.println("Turno non mio");
      }
      if(!turnoMio){
        proca = is.readLine();
        if(proca.equals("Sparato se stesso rosso")){
          avversario--;
          testoVitaAvversario.setText("vita avversario: " + avversario);
          System.out.println("Ha sparato se stesso");
        }else if(proca.equals("Sparato avversario rosso")){
          vita--;
          testoVita.setText("vita: " + vita);
          System.out.println("Ha sparato l'avversario");
        }
        //System.out.println("Sparo OK");
        os.writeBytes("Sparo OK\n");
      }else{
        proca = is.readLine();
        if(proca.equals("Sparato se stesso rosso")){
          vita--;
          testoVita.setText("vita: " + vita);
          System.out.println("Ho sparato me stesso");
        }else if(proca.equals("Sparato avversario rosso")){
          avversario--;
          testoVitaAvversario.setText("vita avversario: " + avversario);
          System.out.println("Ho sparato l'avversario");
        }
        System.out.println(proca);
        if(proca.equals("Sparo OK")){
          os.writeBytes("Turno OK\n");
          System.out.println("Sparato");
        }
      }
    } 

    os.close(); 
    is.close(); 
    socket.close(); 
  } 

  public static void main (String[] args) throws Exception { 
    TCPClient tcpClient = new TCPClient(); 
    tcpClient.grafica();
    tcpClient.start(); 
  } 

  public void grafica() {
    ImageIcon sfond = new ImageIcon("Client/sfondo.gif");
    JLabel sfondo = new JLabel(sfond);
    sfondo.setSize(1400, 900);
    sfondo.setLocation(200, 270);

    JFrame frame = new JFrame("roulette");
    frame.setLayout(null);
    frame.getContentPane().setBackground(Color.BLACK);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel titolo = new JLabel("roulette");
    titolo.setLocation(100, 0);
    titolo.setFont(new Font("Serif", Font.BOLD, 28));
    titolo.setSize(1000, 100);
    titolo.setForeground(Color.red);

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

    testoGiocatore = new JLabel("dai");
    testoGiocatore.setLocation(1400, 0);
    testoGiocatore.setFont(new Font("Serif", Font.BOLD, 28));
    testoGiocatore.setSize(1000, 100);
    testoGiocatore.setForeground(Color.white);

    statoConnessione = new JLabel("stato connessione");
    statoConnessione.setLocation(600, 0);
    statoConnessione.setFont(new Font("Serif", Font.BOLD, 28));
    statoConnessione.setSize(1000, 100);
    statoConnessione.setForeground(Color.red);

    testoTurno = new JLabel("turno");
    testoTurno.setLocation(600, 100);
    testoTurno.setFont(new Font("Serif", Font.BOLD, 28));
    testoTurno.setSize(1000, 100);
    testoTurno.setForeground(Color.white);

    button = new JButton("te");
    button.setSize(100, 100);
    button.setLocation(200, 500);
    button.addActionListener(e -> {
      disabilitaPulsanti();
      try {
        os.writeBytes("Spara se stesso\n");
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
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    uscita = new JButton("esci");
    uscita.setSize(100, 100);
    uscita.setLocation(1700, 900);
    uscita.addActionListener(e -> System.exit(0));

    ImageIcon icon = new ImageIcon("Client/revolver.png");
    JLabel revolver = new JLabel(icon);
    revolver.setSize(500, 500);
    revolver.setLocation(710, 400);

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
    //frame.add(sfondo);

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