package es.rafaespillaque.ayd;

import java.io.IOException;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.youtube.Youtube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

public class YouTubeSearcher {
	private final JsonFactory jsonFactory = new JacksonFactory();

	private final HttpTransport transport = new NetHttpTransport();

	public YouTubeSearcher() {
		final JsonCParser parser = new JsonCParser(jsonFactory);
		// requestFactory = transport.createRequestFactory();
		Youtube youtube = new Youtube(transport, jsonFactory, new HttpRequestInitializer() {

			public void initialize(HttpRequest request) throws IOException {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("Google-YouTubeSample/1.0");
				headers.setGDataVersion("2");
				request.setHeaders(headers);
				request.setParser(parser);

			}
		});
//		Youtube youtube = new Youtube(transport, jsonFactory, null);
		
		try {
			Youtube.Search.List request = youtube.search().list();
			request.setQ("sabina");
			request.setKey("AI39si51i3EiMc4Rp8TyNf10seUY3fTiJix4m868iX38-Wv-Ru94A759idzzU2zT2nVYi_M67IhCmqiMoxegikelc6BuvPFTgQ");
			SearchListResponse response = request.execute();
			java.util.List<SearchResult> searchResults = response.getSearchResults();
			for (SearchResult searchResult : searchResults) {
				System.out.println(searchResult.get("title"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
