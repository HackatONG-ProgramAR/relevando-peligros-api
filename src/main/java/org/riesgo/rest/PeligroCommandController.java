package org.riesgo.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.riesgo.dao.PeligroRepository;
import org.riesgo.model.Imagen;
import org.riesgo.model.Peligro;
import org.riesgo.utils.PeligroSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gdata.client.media.MediaService;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.AuthenticationException;

@Controller
public class PeligroCommandController {

	@Autowired
	PeligroRepository peligroRepository;

	private static final String PICASSA_PASSWORD = "mobapp123";
	private static final String PICASSA_USERNAME = "relevandopeligrosmobapp@gmail.com";
	private static final String PICASSA_URL = "https://picasaweb.google.com/data/feed/api/user/relevandopeligrosmobapp/albumid/6055992638593216417";

	private static final String FILE_PATH = "C:\\file.jpg";
	private static final String FILE2_PATH = "C:\\file2.jpg";
	private static final String FILE3_PATH = "C:\\file3.jpg";

	@RequestMapping(value = "/danger", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/danger", method = RequestMethod.POST)
	public @ResponseBody Peligro handleFileUpload(
			@RequestParam("titulo") String titulo,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("file") MultipartFile file,
			@RequestParam("file2") MultipartFile file2,
			@RequestParam("file3") MultipartFile file3) {
		try {
			PicasawebService picassaService = getPicassaService();
			String imageUrl = null;
			String imageUrl2 = null;
			String imageUrl3 = null;

			if (!file.isEmpty()) {
				File archivo = createFile(file, FILE_PATH);
				imageUrl = uploadImageToPicassa(titulo, descripcion, archivo,
						picassaService);
			}
			if (!file2.isEmpty()) {
				File archivo2 = createFile(file2, FILE2_PATH);
				imageUrl2 = uploadImageToPicassa(titulo, descripcion, archivo2,
						picassaService);

			}
			if (!file3.isEmpty()) {
				File archivo3 = createFile(file3, FILE3_PATH);

				imageUrl3 = uploadImageToPicassa(titulo, descripcion, archivo3,
						picassaService);

			}
			return savePeligro(titulo, descripcion, imageUrl, imageUrl2,
					imageUrl3);

		} catch (Exception e) {
			System.out.println("fail to process the request");
			e.printStackTrace();
		}
		return null;
	}

	private Peligro savePeligro(String titulo, String descripcion,
			String imageUrl, String imageUrl2, String imageUrl3)
			throws MalformedURLException {
		Set<Imagen> imagenes = new HashSet<Imagen>();

		if(imageUrl != null){
			URL url = new URL(imageUrl);
			
			Imagen image = new Imagen();
			image.setPath(url);
			image.setTitulo(titulo);
			imagenes.add(image);
			
			Imagen mobileThumbnail = new Imagen();
			mobileThumbnail.setPath(url);
			mobileThumbnail.setTitulo("mobileThumbnail");
			imagenes.add(mobileThumbnail);
		}
		
		if(imageUrl2 != null){
			Imagen image2 = new Imagen();
			image2.setPath(new URL(imageUrl2));
			image2.setTitulo(titulo);
			imagenes.add(image2);			
		}

		if(imageUrl3 != null){
			Imagen image3 = new Imagen();
			image3.setPath(new URL(imageUrl3));
			image3.setTitulo(titulo);
			imagenes.add(image3);
		}

		Peligro peligro = new Peligro();
		peligro.setDescripcion(descripcion);
		peligro.setTitulo(titulo);
		peligro.setSource(PeligroSource.MOBILE);
		peligro.setImagenes(imagenes);

		return peligroRepository.save(peligro);
	}

	private File createFile(MultipartFile file, String filePath)
			throws IOException, FileNotFoundException {
		byte[] bytes = file.getBytes();
		FileOutputStream fileOuputStream = new FileOutputStream(filePath);
		fileOuputStream.write(bytes);
		fileOuputStream.close();

		File archivo = new File(filePath);
		return archivo;
	}

	private String uploadImageToPicassa(String titulo, String descripcion,
			File archivo, MediaService picassaService) throws Exception {

		URL albumPostUrl = new URL(PICASSA_URL);

		PhotoEntry myPhoto = new PhotoEntry();
		myPhoto.setTitle(new PlainTextConstruct(titulo));
		myPhoto.setDescription(new PlainTextConstruct(descripcion));
		myPhoto.setClient("myClientName");

		MediaFileSource myMedia = new MediaFileSource(archivo, "image/jpeg");
		myPhoto.setMediaSource(myMedia);

		PhotoEntry returnedPhoto = picassaService.insert(albumPostUrl, myPhoto);
		return returnedPhoto.getMediaContents().get(0).getUrl();
	}

	private PicasawebService getPicassaService() throws AuthenticationException {
		PicasawebService myService = new PicasawebService(
				"relevandopeligrosmobapp");
		myService.setUserCredentials(PICASSA_USERNAME, PICASSA_PASSWORD);
		return myService;
	}
}
