/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import metodos.Post;

/**
 *
 * @author sebas
 */
public class Cliente {
    String host = "127.0.0.1";
    int pto = 1234;
    Socket cl;
    BufferedReader br;
    PrintWriter pw;
        
    public void nuevoPost(String creador, String titulo, String fecha, String foto, String contenido, String categoria){
        try {
            cl = new Socket(host, pto);
            br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            pw.println("1");
            pw.flush();
            Post p =  new Post(creador, titulo, fecha, foto, contenido, categoria);
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject(p);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String enviarArch(String fecha){
        String path = null;
        try{
            File f;
            cl = new Socket(host, pto);
            br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            pw.println("2");
            pw.flush();
            JFileChooser jf = new JFileChooser();
            int r = jf.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                f = jf.getSelectedFile();
                String nombre = f.getName();
                long tam = f.length();
                path = f.getAbsolutePath();
                System.out.println("Se enviara el archivo " + path + " con " + tam + "bytes");
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(new FileInputStream(path));
                dos.writeUTF(fecha);
                dos.flush();
                dos.writeUTF(nombre);
                dos.flush();
                dos.writeLong(tam);
                dos.flush();
                long enviados = 0;
                int n = 0, porciento = 0;
                byte[] b = new byte[1024];
                while(enviados < tam){
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    enviados+=n;
                    porciento = (int)(enviados*100/tam);
                    System.out.print("\r Enviando el " + porciento + "%");
                }
                System.out.println("Esperando Mensaje");
                JOptionPane.showMessageDialog(null, "El archivo: "+nombre+" se subió correctamente.");
                dos.close();
                dis.close();
        }
        }catch(Exception e){
            e.printStackTrace();
            path = "fallo";
        }
        return path;
    }
    
    public Post pedirPost(String nombrePost){
        Post p = new Post();
        try {
            cl = new Socket(host, pto);
            br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            pw.println("3");
            pw.flush();
            pw.println(nombrePost);
            pw.flush();
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            p = (Post)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    
    public ArrayList<Post> listaPost(){
        ArrayList<Post> alp = new ArrayList<>();
        try {
            cl = new Socket(host, pto);
            pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            pw.println("4");
            pw.flush();
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            alp = (ArrayList<Post>)ois.readObject();
            System.out.println(alp.size());
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alp;
    }
}
