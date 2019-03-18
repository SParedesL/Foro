/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos;

import java.io.Serializable;

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
    
    
}
