//Importo i package
import java.net.*;
import java.io.*;

//Classe Server per attivare la Socket
public class TCPParallelServer {
  public void start() throws Exception {
    ServerSocket serverSocket = new ServerSocket(7777);
    //Ciclo infinito di ascolto dei Client
    while(true)
    {
      System.out.println(" Attesa primo giocatore");
      Socket socket = serverSocket.accept();
      System.out.println("Ricezione una chiamata di apertura da:\n" + socket);
      System.out.println("Attesa secondo giocatore");
      Socket socket2 = serverSocket.accept();
      System.out.println("Ricezione una chiamata di apertura da:\n" + socket2);
      //avvia il processo per ogni client 
      System.out.println("Partita avviata");
      ServerThread partita = new ServerThread(socket, socket2);
      partita.start();
    }
  }

  public static void main (String[] args) throws Exception {
    TCPParallelServer tcpServer = new TCPParallelServer();
    tcpServer.start();
  }
}
