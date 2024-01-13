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

  public void start() throws IOException { 
    socket = new Socket("localhost", 7777); 
    os = new DataOutputStream(socket.getOutputStream()); 
    is = new DataInputStream(socket.getInputStream()); 
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 

    while (true) { 
      String userInput = stdIn.readLine(); 
      if (userInput.equals("QUIT")) 
        break; 
      os.writeBytes(userInput + '\n');  
      System.out.println("Hai digitato: " + is.readLine()); 
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
    JFrame frame = new JFrame("Roulette");
    frame.setLayout(null);
    frame.getContentPane().setBackground(Color.BLACK);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel titolo = new JLabel("Buckshot Roulette");
    titolo.setLocation(100, 0);
    titolo.setFont(new Font("Serif", Font.BOLD, 28));
    titolo.setSize(1000, 100);
    titolo.setForeground(Color.red);

    JButton button = new JButton("Te");
    button.setSize(100, 100);
    button.setLocation(200, 500);
    button.addActionListener(e -> {
      vita--;
      try {
        os.writeBytes("Se stesso");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    JButton button2 = new JButton("Avversario");
    button2.setSize(100, 100);
    button2.setLocation(1500, 500);
    button2.addActionListener(e -> {
      avversario--;
      try {
        os.writeBytes("Avversario");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    JButton uscita = new JButton("Esci");
    uscita.setSize(100, 100);
    uscita.setLocation(1700, 900);
    uscita.addActionListener(e -> System.exit(0));

    ImageIcon icon = new ImageIcon("Client/revolver.png");
    JLabel revolver = new JLabel(icon);
    revolver.setSize(500, 500);
    revolver.setLocation(500, 500);

    frame.add(titolo);
    frame.add(button);
    frame.add(button2);
    frame.add(uscita);
    frame.add(revolver);

    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setUndecorated(true);
    frame.setVisible(true);
  }
}