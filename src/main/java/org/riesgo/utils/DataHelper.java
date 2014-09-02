package org.riesgo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.riesgo.model.Peligro;

public class DataHelper {

	private final static String MAIN_PRODUCTS_ID = "main-products";
	private final static String MAIN_PRODUCTS_CSS = "div.main-product";
	private final static String MAIN_PRODUCTS_LINK = "a";
	private final static String MAIN_PRODUCTS_LINK_HREF = "href";

	private final static GetPeligroDetail getDetail = new GetPeligroDetail();
	private static List<String> peligrosWebURLS = null;

	public static List<Peligro> buildPeligrosFromSite() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		Collection<Future<Peligro>> piResultFutures = new ArrayList<Future<Peligro>>();

		for (final String url : getPeligrosWebURLS()) {

			FutureTask<Peligro> futurePiTask = new FutureTask<Peligro>(
					new WebDocumentLoadTask(url));

			// Launching gathering tasks
			executorService.execute(futurePiTask);

			piResultFutures.add(futurePiTask);
		}

		List<Peligro> peligrosWeb = new ArrayList<Peligro>();
		for (Future<Peligro> futureTask : piResultFutures) {
			try {
				peligrosWeb.add(futureTask.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		return peligrosWeb;
	}

	private static class WebDocumentLoadTask implements Callable<Peligro> {

		private String url;

		public WebDocumentLoadTask(String url) {
			this.url = url;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Peligro call() throws Exception {
			return getDetail.getPeligroDetail(this.url);
		}

	}

	private static List<String> getPeligrosWebURLS() {
		System.out.println("start to getting urls");

		Document doc;
		peligrosWebURLS = new ArrayList<String>();

		try {
			doc = Jsoup.connect("http://relevandopeligros.org/").timeout(30000)
					.get();
			// System.out.println(doc.getAllElements().html());

			Element mainProducts = doc.getElementById(MAIN_PRODUCTS_ID);
			String cssQuery = MAIN_PRODUCTS_CSS;
			for (Element e : mainProducts.select(cssQuery)) {
				cssQuery = MAIN_PRODUCTS_LINK;
				String relHref = e.select(cssQuery).first()
						.attr(MAIN_PRODUCTS_LINK_HREF);
				// System.out.println(relHref);
				peligrosWebURLS.add(relHref);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("end to getting urls");

		return peligrosWebURLS;
	}
}