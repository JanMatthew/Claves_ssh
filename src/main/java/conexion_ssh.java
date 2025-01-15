import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class conexion_ssh {
    public static void main(String[] args) {
        Boolean exito = true;
        try {
            JSch jsch = new JSch();

            String privateKey = "/home/jparejag/.ssh/id_rsa";

            jsch.addIdentity(privateKey);
            System.out.println("Identity added");

            Session session = jsch.getSession("administrador","172.30.9.40",22);
            //session.setPassword("administrador");

            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Session created");

            session.connect();
            System.out.println("Session connected....");

            Channel channel = session.openChannel("exec");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);


            channel.connect(3*1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
