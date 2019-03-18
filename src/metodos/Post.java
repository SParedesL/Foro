/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author sebas
 */
public class Post implements Serializable{
    private String creador, titulo, fecha, foto, contenido, categoria;

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
    
    public static ArrayList<Post> generarLista(File file){
        File[] list = file.listFiles();
        ArrayList<Post> al = new ArrayList<>();
        if(list!=null){
            for (File fil : list)
            {
                if (fil.isDirectory())
                {
                    generarLista(fil);
                }
                else
                {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fil));
                        al.add((Post)ois.readObject());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return al;
    }
}
