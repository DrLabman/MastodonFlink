package net.gluonporridge;

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
    }

    @Override
    public void run(SourceContext<Status> sourceContext) throws Exception {
        try {
            shutdownable = streaming.federatedPublic(new Handler() {

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