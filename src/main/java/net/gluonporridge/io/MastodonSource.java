package net.gluonporridge.io;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.Shutdownable;
import com.sys1yagi.mastodon4j.api.entity.Notification;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Streaming;
import okhttp3.OkHttpClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Properties;

public class MastodonSource extends RichSourceFunction<Status> {

    public enum MastodonTimeline {
        FEDERATED_PUBLIC,
        LOCAL_PUBLIC,
        USER
    }

    private String instance;
    private String accessToken;
    private MastodonTimeline timeline;

    private MastodonClient client;
    private Streaming streaming;
    private Shutdownable shutdownable;

    public MastodonSource(Properties config) {
        accessToken = config.getProperty("accessToken");
        instance = config.getProperty("instance");
        String tl = config.getProperty("timeline", "USER");
        timeline = MastodonTimeline.valueOf(tl);

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
    }

    @Override
    public void run(SourceContext<Status> sourceContext) throws Exception {
        try {
            Handler handler = new Handler() {

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
            };

            switch (timeline) {
                case USER:
                    shutdownable = streaming.user(handler);
                    break;
                case LOCAL_PUBLIC:
                    shutdownable = streaming.localPublic(handler);
                    break;
                case FEDERATED_PUBLIC:
                    shutdownable = streaming.federatedPublic(handler);
                    break;
                default:
                    throw new IllegalStateException("Unknown timeline type");
            }
            
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