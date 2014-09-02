package org.riesgo.model;

import java.net.URL;

import org.springframework.data.annotation.Id;

/**
 * TODO: Cambiar implementacion acorde al storage, por ahora es un 
 * placeholder para el url. 
 *
 */
public class Imagen {
	
	@Id
	private String id;
	private String titulo;
	private URL path;

	public Imagen() {
	}
	
	public Imagen(String titulo, URL path) {
		this.titulo = titulo;
		this.path = path;
	}
	
	@Override
	public String toString() {
		return "Imagen [id=" + id + ", titulo=" + titulo + ", path=" + path
				+ "]";
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public URL getPath() {
		return path;
	}

	public void setPath(URL path) {
		this.path = path;
	}

}
