import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

import java.io.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Escribir {
    public static void main(String[] args) throws InterruptedException {
        JSch jsch = new JSch();
        String file = "/Users/matthew/.ssh/id_rsa";

        try{
            KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA, 2048);
            kpair.writePrivateKey(file);
            kpair.writePublicKey(file+".pub", "jmatthew");
            kpair.dispose();
            File pri = new File(file);
            pri.setReadable(false, false); // Quitar lectura para todos
            pri.setWritable(false, false); // Quitar escritura para todos
            pri.setExecutable(false, false); // Quitar ejecución para todos

            pri.setReadable(true, true); // Permitir lectura solo para el propietario
            pri.setWritable(true, true); // Permitir escritura solo para el propietario

            File pub = new File(file+".pub");
            pub.setReadable(false, false); // Quitar lectura para todos
            pub.setWritable(false, false); // Quitar escritura para todos
            pub.setExecutable(false, false); // Quitar ejecución para todos

            pub.setReadable(true, true); // Permitir lectura solo para el propietario
            pub.setWritable(true, true); // Permitir escritura solo para el propietario
        }catch (Exception e){

        }
    }
}
