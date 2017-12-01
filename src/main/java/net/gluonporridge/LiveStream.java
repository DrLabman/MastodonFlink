package net.gluonporridge;


import com.sys1yagi.mastodon4j.api.entity.*;

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
                MastodonHelper.printAccount(account);

                if (status.getInReplyToId() != null) {
                    System.out.println(String.format("In Reply To: %1d", status.getInReplyToId()));
                }

                MastodonHelper.printMentions(status);
                MastodonHelper.printContent(status);
                MastodonHelper.printTags(status);
                MastodonHelper.printAttachments(status);
            }


        });

		// execute program
		env.execute("Mastodon Source Test");
	}
}