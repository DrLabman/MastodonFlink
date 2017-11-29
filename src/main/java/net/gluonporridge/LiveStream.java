package net.gluonporridge;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.Pageable;
import com.sys1yagi.mastodon4j.api.Range;
import com.sys1yagi.mastodon4j.api.Shutdownable;
import com.sys1yagi.mastodon4j.api.entity.Account;
import com.sys1yagi.mastodon4j.api.entity.Notification;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.entity.Tag;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Public;
import com.sys1yagi.mastodon4j.api.method.Streaming;
import com.sys1yagi.mastodon4j.api.method.Timelines;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Properties;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class LiveStream {

    public static class MastodonSource extends RichSourceFunction<Status> {

        private String instance;
        private String accessToken;

        MastodonClient client;
        Streaming streaming;
        Shutdownable shutdownable;

        public MastodonSource(Properties config) {
            accessToken = config.getProperty("accessToken");
            instance = config.getProperty("instance");

            if (accessToken == null) {
                throw new IllegalArgumentException("accessToken must be set in configuration of MastodonSource");
            }

            if (instance == null) {
                throw new IllegalArgumentException("instance must be set in configuration of MastodonSource");
            }
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            MastodonClient.Builder builder = new MastodonClient.Builder(instance, new OkHttpClient.Builder(), new Gson());
            builder.accessToken(accessToken).useStreamingApi();
            client = builder.build();

            streaming = new Streaming(client);

//            timelines = new Timelines(client);
//            publicObj = new Public(client);
//            publicObj.getFederatedPublic();
        }

        @Override
        public void run(SourceContext<Status> sourceContext) throws Exception {
            try {
                shutdownable = streaming.federatedPublic(new Handler () {

                    @Override
                    public void onDelete(long l) {

                    }

                    @Override
                    public void onNotification(Notification notification) {

                    }

                    @Override
                    public void onStatus(Status status) {
                        sourceContext.collect(status);
                    }
                });
                //Thread.sleep(10000L);
                synchronized (this) {
                    this.wait();
                }
            } catch(Mastodon4jRequestException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                // Ignore
            }
        }

        @Override
        public void close() throws Exception {
            this.notify();
            shutdownable.shutdown();
        }

        @Override
        public void cancel() {
            this.notify();
        }
    }

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

        streamSource.addSink(status -> {
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
        });

		// execute program
		env.execute("Mastodon Source Test");
	}
}