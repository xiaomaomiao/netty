package cn.cf.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author miaomiao
 * @date 2021/4/15 22:24
 */
public class GroupChatServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当连接处于活跃状态时

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 当连接处于非活跃状态时

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 表示有新的连接建立
        String message = LocalDateTime.now().format(DATE_TIME_FORMATTER) + "->客户端:" + channel.remoteAddress() + "加入群聊！";
        System.out.println(message);
        // 并且需要将此消息广播给当前在线的所有客户端
        CHANNEL_GROUP.writeAndFlush(message);
        //将当前客户端加入群聊中
        CHANNEL_GROUP.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 客户端连接断开
        Channel channel = ctx.channel();
        String message = LocalDateTime.now().format(DATE_TIME_FORMATTER) + "->客户端:" + channel.remoteAddress() + "推出群聊！";
        System.out.println(message);
        // 并且需要将此消息广播给当前在线的所有客户端
        CHANNEL_GROUP.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        // 收到客户端发送的消息时，将消息广播给所有客户端
        CHANNEL_GROUP.forEach(channel -> {
            if (channel != ctx.channel()) {
                //需要将发送者排除掉
                String message = String.format("%s -> 客户端【%s】说：%s", LocalDateTime.now().format(DATE_TIME_FORMATTER), ctx.channel().remoteAddress(), msg);
                channel.writeAndFlush(message);
            }
        });
    }
}
