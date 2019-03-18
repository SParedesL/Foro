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
                br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                opc = br.readLine();
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
        String dir = ".\\Posts\\";
        Post p = new Post();
        try {
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            p = (Post)ois.readObject();
            dir = dir + p.getCategoria();
            File f = new File(dir);
            System.out.println(f.getAbsolutePath());
            if (!(f.exists() && f.isDirectory())) {
                f.mkdirs();
            }
            ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(dir + "\\" + p.getTitulo()));
            oos.writeObject(p);
            ois.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void subirImagen(){ //Opcion 2
        String dir = ".\\Imagenes\\";
        try {
            
            DataInputStream dis = new  DataInputStream(cl.getInputStream());
            String fecha = dis.readUTF();
            String nombre = dis.readUTF();
            long tam = dis.readLong();
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
                 n = dis.read(b);
                 dos.write(b, 0, n);
                 dos.flush();
                 recibidos += n;
            }
            System.out.println("Archivo " + nombre + " recibido");
            System.out.println("Esperando cliente...");
            dis.close();
            dos.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void enviarPost(){
        try {
            String name = br.readLine();
            File f = Post.buscarPost(name, new File(".\\Posts\\"));
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Post p = (Post)ois.readObject();
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject(p);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void enviarLista(){
        try {
            ArrayList<Post> l = Post.generarLista(new File(".\\Posts\\"));
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject(l);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
