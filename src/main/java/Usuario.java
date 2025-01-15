import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import com.jcraft.jsch.Session;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Usuario {
    private final String HOST = "localhost";
    private final int PORT = 50000;

    public Usuario() {

    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        usuario.start();
    }

    void start() {
        Socket socket = null;
        String comment = "";
        try {
            socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            File clave_publ = new File("/home/jparejag/.ssh/id_rsa.pub");
            File clave_priv = new File("/home/jparejag/.ssh/id_rsa");
            if (!clave_publ.exists() || !clave_priv.exists()) {
                System.out.println("Alguna de sus claves no existe.\nSe le creara una nueva.\nIntroduza un comment: ");
                comment = scanner.nextLine();
                generate_keys(comment);
                send_keys(out);
            } else {
                long miliSecDif = System.currentTimeMillis() - clave_publ.lastModified();
                long difDias = TimeUnit.MILLISECONDS.toDays(miliSecDif);
                if (difDias > 30) {
                    System.out.println("Su clave publica esta caducada.\nSe le creara una nueva.\nIntroduza un comment: ");
                    comment = scanner.nextLine();
                    generate_keys(comment);
                    send_keys(out);
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println(in.readLine());
                    comment = scanner.nextLine();
                    out.println(comment);
                    String validadcion = in.readLine();
                    if (validadcion.equals("Usuario valido")) {
                        System.out.println("Acceso autorizado");
                    } else {
                        System.out.println("Se generara una nueva clave publica y se enviara al servidor.\nIntroduza un comment: ");
                        generate_keys(comment);
                        send_keys(out);
                    }
                }
            }
            System.out.println("Introduza el usuario: ");
            String usuario = scanner.nextLine();
            System.out.println("Introduzca la contraseña: ");
            String contrasena = scanner.nextLine();
            System.out.print("Comenzamos la conexion ssh");
            for (int i = 0; i<3; i++){
                Thread.sleep(500);
                System.out.print(".");
            }
            connect_ssh(usuario,contrasena);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void generate_keys(String comment){
        JSch jsch = new JSch();
        String file = "/home/jparejag/.ssh/id_rsa";

        try{
            KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA, 2048);
            kpair.writePrivateKey(file);
            kpair.writePublicKey(file+".pub", comment);
            kpair.dispose();
            File f = new File(file);
            f.setReadable(true, true);
            f.setWritable(true, true);
            f.setExecutable(false,false);
        }catch (Exception e){

        }
    }

    void send_keys(PrintWriter out){
        File f = new File("/home/jparejag/.ssh/id_rsa.pub");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean connect_ssh(String user, String pass){
        Boolean exito = true;
        try {
            JSch jsch = new JSch();

            String privateKey = "/home/jparejag/.ssh/id_rsa";

            jsch.addIdentity(privateKey);
            System.out.println("Identity added");

            Session session = jsch.getSession(user,"",22);
            session.setPassword(pass);

            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Session created");

            session.connect();
            System.out.println("Session connected....");

            Channel channel = session.openChannel("exec");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);

            channel.connect(3*1000);

        } catch (Exception e) {
            exito = false;
        }finally {
            return exito;
        }



    }
}
