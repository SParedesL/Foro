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
    ObjectOutputStream oos;
    ObjectInputStream ois;
    /*public static void main(String[] args) {
        Cliente c =  new Cliente();
        /*c.nuevoPost("El sebas", "Ayuda", "14-03-2019", c.enviarArch("14-03-2019"), "Que tranza?", "Viajes");
        c.nuevoPost("Sebas", "Campeón del mundo", "13-02-1978", c.enviarArch("13-02-1978"), "México es campeon del mundo por primera vez en toda la historia, gol del chicharito", "Deportes");
        c.nuevoPost("Sebas", "Formula 1", "20-05-2018", c.enviarArch("20-05-2018"), "Checo Perez, campeon de la formula 1 por primera vez y en MÉXICO!", "Deportes");
        c.nuevoPost("Jafet", "EDC México", "25-02-2019", c.enviarArch("25-02-2019"), "Skrillex la rompe machin en el escenario y se rapa otra vez", "Musica");
        c.nuevoPost("El sebas", "Respuesta", "10-01-2019", c.enviarArch("14-03-2019"), "Que tranza?", "Viajes");
        ArrayList<Post> alp = c.listaPost();
        alp.sort((o1, o2) -> o1.compareTo(o2));
        for(Post pt : alp)
            System.out.println(pt.getTitulo());
        Post p = c.pedirPost("Ayuda");
        
    }*/
        
    public void nuevoPost(String creador, String titulo, String fecha, String foto, String contenido, String categoria, String post){
        try {
            cl = new Socket(host, pto);
            oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeUTF("1");
            oos.flush();
            oos.writeUTF(titulo);
            oos.flush();
            Post p =  new Post(creador, titulo, fecha, foto, contenido, categoria, post);
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
            oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeUTF("2");
            oos.flush();
            JFileChooser jf = new JFileChooser();
            int r = jf.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                f = jf.getSelectedFile();
                String nombre = f.getName();
                long tam = f.length();
                path = f.getAbsolutePath();
                System.out.println("Se enviara el archivo " + path + " con " + tam + "bytes");
                DataInputStream dis = new DataInputStream(new FileInputStream(path));
                oos.writeUTF(fecha);
                oos.flush();
                oos.writeUTF(nombre);
                oos.flush();
                oos.writeLong(tam);
                oos.flush();
                long enviados = 0;
                int n = 0, porciento = 0;
                byte[] b = new byte[1024];
                while(enviados < tam){
                    n = dis.read(b);
                    oos.write(b, 0, n);
                    oos.flush();
                    enviados+=n;
                    porciento = (int)(enviados*100/tam);
                    System.out.print("\r Enviando el " + porciento + "%");
                }
                System.out.println("Esperando Mensaje");
                JOptionPane.showMessageDialog(null, "El archivo: "+nombre+" se subió correctamente.");
                dis.close();
                oos.close();
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
            oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeUTF("3");
            oos.flush();
            oos.writeUTF(nombrePost);
            oos.flush();
            ois = new ObjectInputStream(cl.getInputStream());
            p = (Post)ois.readObject();
            ois.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    
    public ArrayList<Post> listaPost(String comment){
        ArrayList<Post> alp = new ArrayList<>();
        try {
            cl = new Socket(host, pto);
            oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeUTF("4");
            oos.flush();
            oos.writeUTF(comment);
            oos.flush();
            ois = new ObjectInputStream(cl.getInputStream());
            alp = (ArrayList<Post>)ois.readObject();
            ois.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alp;
    }
}
