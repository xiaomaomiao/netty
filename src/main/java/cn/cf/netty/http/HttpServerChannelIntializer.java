package cn.cf.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author miaomiao
 * @date 2021/4/14 22:41
 */
public class HttpServerChannelIntializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 添加HttpHandler
        pipeline.addLast(new HttpServerCodec());

        pipeline.addLast(new HttpServerHandler());
    }
}
