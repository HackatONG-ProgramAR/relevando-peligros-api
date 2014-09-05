package org.riesgo.utils;


import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.riesgo.model.Imagen;
import org.riesgo.model.Peligro;

public class GetPeligroDetail {

	private static final String PELIGRO_EN_TRAMITE300PX = "Peligro En tramite300px";
	private static final String PELIGRO_RECIBIDO300PX = "Peligro Recibido300px";
	private final String TITULO_SELECTOR = "h1.category-title";
	private final String DESCRIPCION_SELECTOR = "div.item-description p";
	private final String IMAGENES_SELECTOR = "a.thickbox.no_icon";
	
	public Peligro getPeligroDetail(String url) {
		
		Peligro peligro = null;
		
		try {
			Document doc = Jsoup.connect(url).timeout(30000).get();
			// System.out.println(doc.getAllElements().html());
			
			// titulo
			String cssQuery = TITULO_SELECTOR;
			Element titulo = doc.select(cssQuery).first();

			// descripcion
			cssQuery = DESCRIPCION_SELECTOR;
			Element descripcion = doc.select(cssQuery).first();

			// imagenes
			HashSet<Imagen> imagenes = new HashSet<Imagen>();
			cssQuery = IMAGENES_SELECTOR;
			boolean mobile_thumbnail = false;
			for (Element e : doc.select(cssQuery)) {
				String relHref = e.attr("href");
				String title = e.attr("title");
				Imagen img = new Imagen(title, new URL(relHref));
				imagenes.add(img);
				if (!mobile_thumbnail && img.getTitulo() != PELIGRO_RECIBIDO300PX && img.getTitulo() != PELIGRO_EN_TRAMITE300PX) {
					Imagen mob = img;
					mob.setTitulo("mobileThumbnail");
					imagenes.add(mob);
					mobile_thumbnail = true;
				}
			}
			
			// self link
			URL siteUrl = new URL(doc.baseUri());
			
			peligro = new Peligro(titulo.text(), descripcion.text(), imagenes, siteUrl, PeligroSource.WEBPAGE);
			
			System.out.println(peligro);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return peligro;
	}

}
