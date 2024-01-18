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

      while (true) {
        os.writeBytes("Sei il primo\n");
        System.out.println(socket.getPort() + " è il primo");
        os2.writeBytes("Sei il secondo\n");
        System.out.println(socket2.getPort() + " è il secondo");
        if(is.readLine().equals("OK") && is2.readLine().equals("OK")){
          System.out.println("Ruoli assegnati OK");
          break;
        }
      }
      os.writeBytes("Turno tuo\n");
      os2.writeBytes("Turno non tuo\n");          
      //Turno primo giocatore
      while(true) {
        String userInput = is.readLine();
        String userInput2 = is2.readLine();
        if (userInput == null || userInput.equals("QUIT"))
          break;
        /*os.writeBytes(userInput + '\n');
        System.out.println("Il Client "+ socket.getInetAddress() +" "
        + socket.getPort() +" "
        + socket.getLocalPort() +" ha scritto: " + userInput);*/
        if(!userInput.equals("Turno OK") && !userInput2.equals("Turno OK")){
          os.writeBytes("Turno tuo\n");
          os2.writeBytes("Turno non tuo\n");          
        }
        System.out.println("Turno G1 OK");
        userInput = is.readLine();
        if(userInput.equals("Spara se stesso")){
          os2.writeBytes("Sparato se stesso\n");
          //togli vita
        }
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