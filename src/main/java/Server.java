import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int NUM_PUERTO = 50000;

    public Server() {

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    void start() {
        try {

            ServerSocket serverSocket = new ServerSocket(NUM_PUERTO);
            Socket s = serverSocket.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            if(br.readLine().equals("Clave Nueva")){
                File f = new File("/home/jparejag/.ssh/authorized_keys");
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(f,true))){
                    bw.newLine();
                    bw.write(br.readLine());
                }
            }
            else{
                out.println("Introduzca el comment de su clave publica");
                String comment = br.readLine();
                ProcessBuilder pb = new ProcessBuilder("grep","-w", "-c",comment, "/home/jparejag/.ssh/authorized_keys");
                Process p = pb.start();
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                if(Integer.parseInt(in.readLine()) > 0){
                    out.println("Usuario valido");
                }
                else {
                    out.println("Usuario invalido");
                    File f = new File("/home/jparejag/.ssh/authorized_keys");
                    try(BufferedWriter bw = new BufferedWriter(new FileWriter(f,true))){
                        bw.newLine();
                        bw.write(br.readLine());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

