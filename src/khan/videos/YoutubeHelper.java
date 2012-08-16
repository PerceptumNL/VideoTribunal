package khan.videos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YoutubeHelper {

	public static final String getTitle(String youtubeId) {
		try {
			URL url = new URL(String.format("https://gdata.youtube.com/feeds/api/videos/%s?alt=json", youtubeId));
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
			return jsonObject.getAsJsonObject("entry").getAsJsonObject("title").get("$t").getAsString();
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(
					"IOException while loading youtube title by id: " + youtubeId);
			return null;
		}
	}
}
