package net.gluonporridge;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Pageable;
import com.sys1yagi.mastodon4j.api.Range;
import com.sys1yagi.mastodon4j.api.entity.Account;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Public;
import com.sys1yagi.mastodon4j.api.method.Timelines;
import okhttp3.OkHttpClient;

import java.util.List;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class LiveStream {

	public static void main(String[] args) throws Exception {
        ParameterTool parameters = ParameterTool.fromArgs(args);
        String accessToken = parameters.getRequired("accessToken");

		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		MastodonClient.Builder builder = new MastodonClient.Builder("mastodon.technology", new OkHttpClient.Builder(), new Gson());
		builder.accessToken(accessToken);
		MastodonClient client = builder.build();

		Timelines timelines = new Timelines(client);
		Public publicObj = new Public(client);

		try {
			Pageable<Status> statusPageable = publicObj.getFederatedPublic().execute();//timelines.getHome(new Range()).execute();
			List<Status> statuses = statusPageable.getPart();
			statuses.forEach(status->{
				System.out.println("=============");
				System.out.println(status.getApplication());
				Account account = status.getAccount();
				System.out.format("%1s (%2s) [%3s]\n", account.getDisplayName(), account.getAcct(), account.getId());
				String content = status.getContent();

//                System.out.println(content);
				// convert <br> tag to new line
                String cleaned = String.join("\n", content.split("<br>"));
				// remove junk around links
                cleaned = cleaned.replaceAll("<a href=\"(.+?)\".*?</a>", "$1");
				// convert <p> into paragraphs
                cleaned = cleaned.replaceAll("<p>(.*?)</p>(?!$)", "$1\n\n");
                cleaned = cleaned.replaceAll("<p>(.*?)</p>", "$1");

				// wrap after 80 char
				//content = String.join("\n", content.split("(?<=\\G.{80})"));

				System.out.println(cleaned);
			});
		} catch (Mastodon4jRequestException e) {
			e.printStackTrace();
		}

		//"This is some text <a href=\"https://vulpine.club/tags/selfie\" class=\"mention hashtag\" rel=\"nofollow noopener\" target=\"_blank\">#<span>selfie</span></a> some more text"

		// execute program
		env.execute("Flink Streaming Java API Skeleton");
	}
}