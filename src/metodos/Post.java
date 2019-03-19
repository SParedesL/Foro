/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;


/**
 *
 * @author sebas
 */
public class Post implements Serializable, Comparable<Post>{
    public String creador, titulo, fecha, foto, contenido, categoria;
    

    public Post(String creador, String titulo, String fecha, String foto, String contenido, String categoria) {
        this.creador = creador;
        this.titulo = titulo;
        this.fecha = fecha;
        this.foto = foto;
        this.contenido = contenido;
        this.categoria = categoria;
    }

    public Post() {
    }

    public String getCategoria() {
        return categoria;
    }
    
    public String getCreador() {
        return creador;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getFoto() {
        return foto;
    }

    public String getContenido() {
        return contenido;
    }
    
    public static File buscarPost(String name, File file){
        File[] list = file.listFiles();
        File r = null;
        if(list!=null){
            for (File fil : list)
            {
                if (fil.isDirectory())
                {
                    r = buscarPost(name, fil);
                }
                else if (name.equalsIgnoreCase(fil.getName()))
                {
                    return fil;
                }
            }
        }
        return r;
    }
    
    public static void generarLista(File file, ArrayList<Post> al){
        File[] list = file.listFiles();
        if(list!=null){
            for (File fil : list)
            {
                if (fil.isDirectory())
                {
                    generarLista(fil, al);
                }
                else
                {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fil));
                        Post p = (Post)ois.readObject();
                        al.add(p);
                        ois.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void posts(JList j, ArrayList<Post> posts){
        DefaultListModel lm = new DefaultListModel();
        lm.clear();
        for (int i = 0; i < posts.size(); i++) {
            lm.add(i, posts.get(i).titulo);
            System.out.println(posts.get(i).titulo+"\n");
        }
        j.setModel(lm);
    }
    
      public void showImg(String rute, JLabel label) throws IOException{
        File f = new File(rute);
        Image image = ImageIO.read(f);
        ImageIcon icon = new ImageIcon(image);
        Icon icono = new ImageIcon(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH));
        label.setText(null);
        label.setIcon(icono);
    }
    
    @Override
    public int compareTo(Post o) {
        if (Integer.parseInt(fecha.substring(0, 1)) < Integer.parseInt(o.fecha.substring(0, 1))) {
            if (Integer.parseInt(fecha.substring(3, 4)) < Integer.parseInt(o.fecha.substring(3, 4))) {
                if (Integer.parseInt(fecha.substring(6, 7)) < Integer.parseInt(o.fecha.substring(6, 7))) {
                    return -1;
                }
            }
        }
        if (Integer.parseInt(fecha.substring(0, 1)) > Integer.parseInt(o.fecha.substring(0, 1))) {
            if (Integer.parseInt(fecha.substring(3, 4)) > Integer.parseInt(o.fecha.substring(3, 4))) {
                if (Integer.parseInt(fecha.substring(6, 7)) > Integer.parseInt(o.fecha.substring(6, 7))) {
                    return -1;
                }
            }
        }
        return 0;
    }
}
