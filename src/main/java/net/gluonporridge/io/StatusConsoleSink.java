package net.gluonporridge.io;

import net.gluonporridge.MastodonHelper;
import net.gluonporridge.jpa.Account;
import net.gluonporridge.jpa.Status;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class StatusConsoleSink implements SinkFunction<Status> {

    @Override
    public void invoke(Status status) {
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
}
