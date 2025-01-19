import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class conexion_ssh {
    public static void main(String[] args) {

        Boolean exito = true;
        try {
            JSch jsch = new JSch();

            String privateKey = "/Users/matthew/.ssh/id_rsa";

            jsch.addIdentity(privateKey);
            System.out.println("Identity added");

            //Session session = jsch.getSession("administrador","172.30.9.40",22);
            Session session = jsch.getSession("jmatthew", "192.168.0.40", 22);
            session.setPassword("110777");

            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Session created");

            session.connect();
            System.out.println("Session connected....");

            Channel channelShell =  session.openChannel("shell");

            channelShell.setInputStream(System.in);
            channelShell.setOutputStream(System.out);



            channelShell.connect(3*1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
