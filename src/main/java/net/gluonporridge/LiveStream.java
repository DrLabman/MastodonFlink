package net.gluonporridge;

import java.util.Properties;

import net.gluonporridge.io.JPASink;
import net.gluonporridge.io.MastodonSource;
import net.gluonporridge.io.StatusConsoleSink;
import net.gluonporridge.jpa.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

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

        DataStreamSource<com.sys1yagi.mastodon4j.api.entity.Status> streamSource = env.addSource(mastodonSource).setParallelism(1);
        streamSource
                .map(new MapFunction<com.sys1yagi.mastodon4j.api.entity.Status, Status>() {
                    @Override
                    public Status map(com.sys1yagi.mastodon4j.api.entity.Status status) throws Exception {
                        return new Status(status);
                    }
                })
                .addSink(new JPASink());
                //.addSink(new StatusConsoleSink());

        // execute program
        env.execute("Mastodon Source Test");
    }
}