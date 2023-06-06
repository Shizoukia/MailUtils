package catmoe.shizoukia.MailUtils;

import catmoe.shizoukia.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class MailUtilsCommand extends Command {
    private Plugin plugin;

    public MailUtilsCommand(Plugin plugin) {
        super("sendmail");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("This command can only be executed by a player.");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!player.hasPermission("mail.send")) {
            player.sendMessage("You don't have permission to use this command.");
            return;
        }

        if (args.length < 3) {
            player.sendMessage("Usage: /sendmail <recipient> <subject> <content>");
            return;
        }

        String recipient = args[0];
        String subject = args[1];
        String content = args[2];

        // 发送邮件
        MailUtils.sendMail(recipient, subject, content);

        player.sendMessage("邮件已发送！");
    }
}
