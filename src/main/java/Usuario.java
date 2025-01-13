import java.io.PrintWriter;
import java.net.Socket;

public class Usuario {
    private final String HOST = "172.30.9.40";
    private final int PORT = 50000;

    public Usuario() {

    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        usuario.start();
    }

    void start() {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("jmatthew");

        }catch (Exception e) {
            e.printStackTrace();

        }
    }
}
