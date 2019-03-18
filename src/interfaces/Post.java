/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.HashSet;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author jafet
 */
public class Post {
    public String titulo;
    public String categoria;
    public String fecha;
    public String imagen;
    public String contenido;
    public String autor;
    DefaultListModel lm = new DefaultListModel();

    public Post(String titulo, String categoria, String fecha, String imagen, String contenido, String autor) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.fecha = fecha;
        this.imagen = imagen;
        this.contenido = contenido;
        this.autor = autor;
    }

    public Post() {
    }
    
    
    public void posts(JList j, HashSet<Post> posts){
        lm.clear();
        for (Post post : posts)
            lm.addElement(post.titulo);
        
        j.setModel(lm);
    }
    
    public Post mostrar(String titulo, HashSet<Post> posts){
        Post respuesta = new Post();
        for (Post post : posts) {
            if(post.titulo.equalsIgnoreCase(titulo))
                respuesta = post;
        }
        return respuesta;
    }
    
    public HashSet<Post> llenarPost(){
        HashSet<Post> estados = new HashSet<>();
        Post a = new Post("Campeón del mundo", "Deportes", "13-02-78", "CopaDelMundo.jpg", "México es campeon del mundo por primera vez en toda la historia, gol del chicharito", "Sebas");
        Post b = new Post("Formula 1", "Deportes", "20-05-18", "CarrazoMamalon.jpg", "Checo Perez, campeon de la formula 1 por primera vez y en MÉXICO!", "Sebas");
        Post c = new Post("EDC México", "Musica", "25-02-19", "MamisEDC.jpg", "Skrillex la rompe machin en el escenario y se rapa otra vez", "Jafet");
        estados.add(a);
        estados.add(b);
        estados.add(c);
        
        return estados;
    }
}
