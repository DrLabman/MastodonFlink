package net.gluonporridge;


import com.sys1yagi.mastodon4j.api.entity.Account;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.entity.Tag;

import java.util.List;
import java.util.Properties;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class LiveStream {

	public static void main(String[] args) throws Exception {
        ParameterTool parameters = ParameterTool.fromArgs(args);
        String accessToken = parameters.getRequired("accessToken");
        String instance = parameters.getRequired("instance");

		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties config = new Properties();
		config.setProperty("accessToken", accessToken);
		config.setProperty("instance", instance);
        MastodonSource mastodonSource = new MastodonSource(config);

        env.setParallelism(1);

        DataStreamSource<Status> streamSource = env.addSource(mastodonSource).setParallelism(1);

        streamSource.addSink(new SinkFunction<Status>() {
            @Override
            public void invoke(Status status) throws Exception {
                System.out.println("=============");
                System.out.println(status.getCreatedAt());
                if (status.getApplication() != null) {
                    System.out.println(status.getApplication());
                }
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

                List<Tag> tags = status.getTags();
                if (tags.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Tags: ");
                    for (Tag t : tags) {
                        sb.append(t.getName());
                        sb.append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length() - 1);
                    System.out.println(sb.toString());
                }
            }
        });

		// execute program
		env.execute("Mastodon Source Test");
	}
}