import java.io.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Escribir {
    public static void main(String[] args) throws InterruptedException {
        File clave_publ = new File("/home/jparejag/.ssh/id_rsa.pub");
        try(BufferedReader br = new BufferedReader(new FileReader(clave_publ))){
            System.out.println(br.readLine().split(" ")[2]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
