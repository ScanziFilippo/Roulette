import java.net.*; 
import java.io.*; 
import java.awt.*;
import javax.swing.*;

public class TCPClient { 
  public int vita = 10;
  public int avversario = 10;
  private Socket socket;
  private DataOutputStream os;
  private DataInputStream is;
  JLabel statoConnessione;
  JLabel testoGiocatore;
  JLabel testoTurno;

  public TCPClient (){
    
  }

  public void start() throws IOException { 
    try{
      socket = new Socket("localhost", 7777); 
      statoConnessione.setText("connesso");
      statoConnessione.setForeground(Color.green);
      System.out.println("Punto 1");
    }catch(Exception e){
      statoConnessione.setText("non connesso");
      statoConnessione.setForeground(Color.red);
    }
    System.out.println("Punto 2");
    os = new DataOutputStream(socket.getOutputStream()); 
    is = new DataInputStream(socket.getInputStream()); 
    boolean turnoMio = false;
    //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 
    System.out.println("Punto 3");
    String proca = is.readLine();
    while(true){
      System.out.println("Punto 4");
      if(proca.equals("Sei il primo")){
        turnoMio = true;
        testoGiocatore.setText("Giocatore 1");
        break;  
      }
    }
    /*
    while(is.readLine() == null || (!is.readLine().equals("Sei il primo") && !is.readLine().equals("Sei il secondo"))){
      System.out.println("Ancora niente");
    }*/
    System.out.println("VA?");
    if(is.readLine().equals("Sei il primo")){
      turnoMio = true;
      testoGiocatore.setText("Giocatore 1");
    }else{
      testoGiocatore.setText("Giocatore 2");
    }
    while (true) { 
      //String userInput = stdIn.readLine(); 
      if (is.readLine().equals("QUIT")) 
        break; 
      /*os.writeBytes(userInput + '\n');  
      System.out.println("Hai digitato: " + is.readLine()); */
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

    JLabel testoVita = new JLabel("vita: " + this.vita);
    testoVita.setLocation(100, 100);
    testoVita.setFont(new Font("Serif", Font.BOLD, 28));
    testoVita.setSize(1000, 100);
    testoVita.setForeground(Color.white);

    JLabel testoVitaAvversario = new JLabel("vita avversario: " + this.avversario);
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

    JButton button = new JButton("te");
    button.setSize(100, 100);
    button.setLocation(200, 500);
    button.addActionListener(e -> {
      vita--;
      try {
        os.writeBytes("se stesso");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    JButton button2 = new JButton("avversario");
    button2.setSize(100, 100);
    button2.setLocation(1500, 500);
    button2.addActionListener(e -> {
      avversario--;
      try {
        os.writeBytes("avversario");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    JButton uscita = new JButton("esci");
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
    frame.add(statoConnessione);
    frame.add(button);
    frame.add(button2);
    frame.add(uscita);
    //frame.add(revolver);
    //frame.add(sfondo);

    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setUndecorated(true);
    frame.setVisible(true);
  }
}