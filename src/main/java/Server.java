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
            System.out.println("Nuevo cliente");
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("Introduzca el comment de su clave publica");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String comment = br.readLine();
            ProcessBuilder pb = new ProcessBuilder("grep","-w", "-c",comment, "/home/jparejag/.ssh/authorized_keys");
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if(in.readLine().equals("1")){
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


// jparejag@lmde5:~/.ssh$ grep -c "jparejag" id_rsa.pub. Hay que hacer algo como esto y ver si hay alguna clasve que coincida
// La clave se debe agregar a ~/.ssh/authorized_keys
//Tengo que comprobar si exite mi clave privada y mi clave publica. Si no crearlas.
//Pasarle la privada solo y que se la guarde.
//Tambien comprobar si la tiene en su authorized_keys.
//Si no generarla y pasarsela.