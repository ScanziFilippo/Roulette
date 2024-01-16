import java.net.*;
import java.io.*;

class ServerThread extends Thread {
  private Socket socket;
  private Socket socket2;
  public ServerThread (Socket socket, Socket socket2) {
    this.socket = socket;
    this.socket2 = socket2;
    System.out.println("Assegnazione ruoli");
  }

  public void run() {
    try {
      DataInputStream is = new DataInputStream(socket.getInputStream());
      DataOutputStream os = new DataOutputStream(socket.getOutputStream());
      DataInputStream is2 = new DataInputStream(socket2.getInputStream());
      DataOutputStream os2 = new DataOutputStream(socket2.getOutputStream());

      os.writeBytes("Sei il primo");
      System.out.println(socket.getPort() + " è il primo");
      os2.writeBytes("Sei il secondo");
      System.out.println(socket2.getPort() + " è il secondo");

      while(true) {
        String userInput = is.readLine();
        if (userInput == null || userInput.equals("QUIT"))
          break;
        /*os.writeBytes(userInput + '\n');
        System.out.println("Il Client "+ socket.getInetAddress() +" "
        + socket.getPort() +" "
        + socket.getLocalPort() +" ha scritto: " + userInput);*/
        
        //Turno primo giocatore
      }
      os.close();
      is.close();
      os2.close();
      is2.close();
      System.out.println("Ricezione una chiamata di chiusura da:\n" + socket + "\n");
      socket.close();
    }
    catch (IOException e) {
      System.out.println("IOException: " + e);
    }
  }
}