import java.net.*;

public class Server {
  public void start() throws Exception {
    ServerSocket serverSocket = new ServerSocket(7777);
    while(true)
    {
      System.out.println("Attesa primo giocatore");
      Socket socket = serverSocket.accept();
      System.out.println("Ricezione una chiamata di apertura da:\n" + socket);
      System.out.println("Attesa secondo giocatore");
      Socket socket2 = serverSocket.accept();
      System.out.println("Ricezione una chiamata di apertura da:\n" + socket2);
      System.out.println("Partita avviata");
      GestorePartita partita = new GestorePartita(socket, socket2);
      partita.start();
    }
  }

  public static void main (String[] args) throws Exception {
    Server tcpServer = new Server();
    tcpServer.start();
  }
}
