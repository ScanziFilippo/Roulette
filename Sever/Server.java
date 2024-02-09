import java.net.*;

public class Server {
  public void start() throws Exception {
    ServerSocket serverSocket = new ServerSocket(7777);
    while(true)
    {
      Socket[] giocatori = new Socket[2];
      while(giocatori[0] == null || giocatori[1] == null || !giocatori[0].isConnected() || !giocatori[1].isConnected()){
        Socket socket = serverSocket.accept();
        System.out.println("Ricezione una chiamata di apertura da:\n" + socket);
        if(giocatori[0] == null || !giocatori[0].isConnected())
        {
          giocatori[0] = socket;
        }
        else if(giocatori[1] == null || !giocatori[1].isConnected())
        {
          giocatori[1] = socket;
        }
      }
      /*System.out.println("Attesa primo giocatore");
      Socket socket = serverSocket.accept();
      System.out.println("Ricezione una chiamata di apertura da:\n" + socket);
      System.out.println("Attesa secondo giocatore");
      Socket socket2 = serverSocket.accept();
      System.out.println("Ricezione una chiamata di apertura da:\n" + socket2);*/
      System.out.println("Partita avviata");
      GestorePartita partita = new GestorePartita(giocatori[0], giocatori[1]);
      partita.start();
    }
  }

  public static void main (String[] args) throws Exception {
    Server tcpServer = new Server();
    tcpServer.start();
  }
}
