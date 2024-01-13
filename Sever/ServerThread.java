import java.net.*;
import java.io.*;

class ServerThread extends Thread {
  private Socket socket;
  
  public ServerThread (Socket socket) {
    this.socket = socket;
    System.out.println("  Stato    Tipo Richiesta  Porta Server  Porta Client  Indirizzo Cliernt\n");
  }

  public void run() {
    try {
      DataInputStream is = new DataInputStream(socket.getInputStream());
      DataOutputStream os = new DataOutputStream(socket.getOutputStream());
      while(true) {
        String userInput = is.readLine();
        if (userInput == null || userInput.equals("QUIT"))
          break;
        os.writeBytes(userInput + '\n');
        System.out.println("Il Client "+ socket.getInetAddress() +" "
        + socket.getPort() +" "
        + socket.getLocalPort() +" ha scritto: " + userInput);
      }
      os.close();
      is.close();
      System.out.println("Ricezione una chiamata di chiusura da:\n" + socket + "\n");
      socket.close();
    }
    catch (IOException e) {
      System.out.println("IOException: " + e);
    }
  }
}