import java.net.*;
import java.util.*;
import java.util.stream.*;
import java.io.*;

class GestorePartita extends Thread {
  private Socket socket;
  private Socket socket2;
  int[][] casi = {
    {0,0,0,1,1,1},
    {1,0,0,1,1,0},
    {1,1,0,0,0,0},
    {1,1,1,0,1,0},
    {1,1,1,1,0,0},
    {1,1,1,1,1,0}
  };
  int caso = (int)(Math.random()*6);
  int sparati = 6;
  int[] caricatore;
  boolean turnoG1 = true;
  DataInputStream is;
  DataOutputStream os;
  DataInputStream is2;
  DataOutputStream os2;

  public GestorePartita (Socket socket, Socket socket2) {
    this.socket = socket;
    this.socket2 = socket2;
    System.out.println("Assegnazione ruoli");
  }
  
  public void rimescola(){
    List<Integer> caricatoreLista = Arrays.stream(caricatore).boxed().collect(Collectors.toList());
    Collections.shuffle(caricatoreLista);
    for (int i = 0; i < caricatore.length; i++) {
      caricatore[i] = caricatoreLista.get(i);
    }
    System.out.println("Caricatore rimescolato: " + Arrays.toString(caricatore));
  }
  public void ricarica(){
    caso = (int)(Math.random()*6);
    caricatore = casi[caso];
    sparati = 0;
    try {
      os.writeBytes("Colpi: " + Arrays.toString(caricatore) + "\n");
      os2.writeBytes("Colpi: " + Arrays.toString(caricatore) + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      is = new DataInputStream(socket.getInputStream());
      os = new DataOutputStream(socket.getOutputStream());
      is2 = new DataInputStream(socket2.getInputStream());
      os2 = new DataOutputStream(socket2.getOutputStream());
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
      while(true) {
        if(sparati == 6){
          ricarica();
          rimescola();
        }
        //System.out.println("Punto 1");
        String in1 = is.readLine();
        String in2 = is2.readLine();
        if(in1.equals("esci")){
          os2.writeBytes("esci\n");
          is.close();
          os.close();
          is2.close();
          os2.close();
          socket.close();
          socket2.close();
          break;
        }
        else if(in2.equals("esci")){
          os.writeBytes("esci\n");
          is.close();
          os.close();
          is2.close();
          os2.close();
          socket.close();
          socket2.close();
          break;
        }
        //System.out.println("Punto 2");
        System.out.println("in1: " + in1);
        System.out.println("in2: " + in2);
        while ((turnoG1 && in1.length() < 4) || (!turnoG1 && in2.length() < 4) || ((turnoG1 && !in1.substring(0, 5).equals("Spara")) && in2.substring(0, 5) != "Spara" && in1 != "esci" && in2 != "esci")) {
          if(turnoG1){
            in1 = is.readLine();
            //System.out.println("win1: " + in1);
          }
          else{
            in2 = is2.readLine();
            //System.out.println("win2: " + in2);
          }
          //System.out.println(in2.length() < 4);
        }
        //System.out.println("Punto 3");
        if (in1.equals("esci")){
          os2.writeBytes("esci\n");
          break; 
        }
        else if(in2.equals("esci")){
          os.writeBytes("esci\n");
          break;
        }
        else if(in1.equals("Spara se stesso")){
          if(caricatore[sparati] == 1){
            os.writeBytes("Sparato se stesso rosso\n");
            os2.writeBytes("Sparato se stesso rosso\n");
            sparati++;
            turnoG2();
            System.out.println("Colpo sparato");
          }
          else{
            os.writeBytes("Sparato se stesso bianco\n");
            os2.writeBytes("Sparato se stesso bianco\n");
            sparati++;
            os.writeBytes("Turno tuo\n");
            os2.writeBytes("Turno non tuo\n");
            turnoG1();
            System.out.println("Colpo falso");
          }
        }
        else if(in1.equals("Spara avversario")){
          if(caricatore[sparati] == 1){
            os.writeBytes("Sparato avversario rosso\n");
            os2.writeBytes("Sparato avversario rosso\n");
            sparati++;
            System.out.println("Colpo sparato");
          }
          else{
            os.writeBytes("Sparato avversario bianco\n");
            os2.writeBytes("Sparato avversario bianco\n");
            sparati++;
            System.out.println("Colpo falso");
          }
          turnoG2();
        }
        else if(in2.equals("Spara se stesso")){
          if(caricatore[sparati] == 1){
            os.writeBytes("Sparato se stesso rosso\n");
            os2.writeBytes("Sparato se stesso rosso\n");
            sparati++;
            turnoG1();
            System.out.println("Colpo sparato");
          }
          else{
            os.writeBytes("Sparato se stesso bianco\n");
            os2.writeBytes("Sparato se stesso bianco\n");
            sparati++;
            turnoG2();
            System.out.println("Colpo falso");
          }
        }
        else if(in2.equals("Spara avversario")){
          if(caricatore[sparati] == 1){
            os.writeBytes("Sparato avversario rosso\n");
            os2.writeBytes("Sparato avversario rosso\n");
            sparati++;
            System.out.println("Colpo sparato");
          }
          else{
            os.writeBytes("Sparato avversario bianco\n");
            os2.writeBytes("Sparato avversario bianco\n");
            sparati++;
            System.out.println("Colpo falso");
          }
          turnoG1();
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

  public void turnoG1() throws IOException{
    turnoG1 = true;
    os.writeBytes("Turno tuo\n");
    os2.writeBytes("Turno non tuo\n");
  }

  public void turnoG2() throws IOException{
    turnoG1 = false;
    os.writeBytes("Turno non tuo\n");
    os2.writeBytes("Turno tuo\n");
  }
}