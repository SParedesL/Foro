package cs;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import metodos.Post;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sebas
 */
public class Servidor {
    int pto = 1234;
    ServerSocket s;
    String dir;
    Socket cl;
    BufferedReader br;
    String opc;
    PrintWriter pw;
    ObjectInputStream ois;
    public static void main(String[] args) {
        Servidor sx = new Servidor();
    }
    
    public Servidor(){
        try {
            s = new ServerSocket(pto);
            s.setReuseAddress(true);
            System.out.println("Servicio Iniciado... Esperando Cliente");
            for(;;){
                cl = s.accept();
                System.out.println("Cliente conectado desde" + cl.getInetAddress() + ": " + cl.getPort());
                ois = new ObjectInputStream(cl.getInputStream());
                opc = ois.readUTF();
                System.out.println(opc);
                if(opc.equals("1"))
                    nuevoPost();
                else if(opc.equals("2"))
                    subirImagen();
                else if(opc.equals("3"))
                    enviarPost();
                else if(opc.equals("4"))
                    enviarLista();
                else
                    break;
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void nuevoPost(){ //Opcion 1
        String dir;
        Post p = new Post();
        try {
            String post = ois.readUTF();
            System.out.println(post);
            p = (Post)ois.readObject();
            if(post.equals("-1"))
                dir = ".\\Posts\\" + p.getCategoria();
            else
                dir = ".\\Coments\\"+post;
            File f = new File(dir);
            System.out.println(f.getAbsolutePath());
            if (!(f.exists() && f.isDirectory())) {
                f.mkdirs();
            }
            ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(dir + "\\" + p.getTitulo()));
            oos.writeObject(p);
            oos.flush();
            ois.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void subirImagen(){ //Opcion 2
        String dir = ".\\Imagenes\\";
        try {
            String fecha = ois.readUTF();
            String nombre = ois.readUTF();
            long tam = ois.readLong();
            long recibidos=0;
            int n=0;
            String path = dir + fecha + "\\";
            File f = new File(path);
            System.out.println(f.getAbsolutePath());
            if (!(f.exists() && f.isDirectory())) {
                f.mkdirs();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(path + nombre));

            byte[] b = new byte[1024];

            while(recibidos < tam) {
                 n = ois.read(b);
                 dos.write(b, 0, n);
                 dos.flush();
                 recibidos += n;
            }
            System.out.println("Archivo " + nombre + " recibido");
            System.out.println("Esperando cliente...");
            ois.close();
            dos.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void enviarPost(){
        try {
            String name = ois.readUTF();
            File f = Post.buscarPost(name, new File(".\\Posts\\"));
            ObjectInputStream oisf = new ObjectInputStream(new FileInputStream(f));
            Post p = (Post)oisf.readObject();
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject(p);
            oos.flush();
            oos.close();
            ois.close();
            oisf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void enviarLista(){
        try {
            String post = ois.readUTF();
            ArrayList<Post> l = new ArrayList<>();
            if(post.equals("-1"))
                Post.generarLista(new File(".\\Posts\\"), l);
            else
                Post.generarLista(new File(".\\Coments\\"+post), l);
            
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject(l);
            oos.flush();
            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
