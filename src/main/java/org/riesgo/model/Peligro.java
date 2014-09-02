package org.riesgo.model;

import java.net.URL;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class Peligro {
	
	@Id
	private String id;
	private String titulo;
	private String descripcion;
	private Set<Imagen> imagenes;
	private URL siteUrl;	
	
	public Peligro() {
		
	}

	public Peligro(String titulo, String descripcion, Set<Imagen> imagenes, URL siteUrl) {
		
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.imagenes = imagenes;
		this.siteUrl = siteUrl;
	}
	
	
	@Override
	public String toString() {
		return "Peligro [id=" + id + ", titulo=" + titulo + ", descripcion="
				+ descripcion + ", siteUrl=" + siteUrl + "]";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Set<Imagen> getImagenes() {
		return imagenes;
	}
	public void setImagenes(Set<Imagen> imagenes) {
		this.imagenes = imagenes;
	}

	public URL getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(URL siteUrl) {
		this.siteUrl = siteUrl;
	}

}
