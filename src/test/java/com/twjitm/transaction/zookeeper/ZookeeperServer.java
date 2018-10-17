package com.twjitm.transaction.zookeeper;import com.twjitm.transaction.entity.ZookeeperMutexEntity;import com.twjitm.transaction.service.transaction.NettyTransactionServiceImpl;import com.twjitm.transaction.service.zookeeper.impl.NettyTransactionZookeeperServiceImpl;import com.twjitm.transaction.spring.TestSpring;import com.twjitm.transaction.transaction.enums.NettyTransactionCause;import com.twjitm.transaction.transaction.enums.NettyTransactionCommitResult;import com.twjitm.transaction.transaction.enums.NettyTransactionEntityCause;import io.netty.bootstrap.ServerBootstrap;import io.netty.channel.ChannelFuture;import io.netty.channel.ChannelInboundHandlerAdapter;import io.netty.channel.ChannelOption;import io.netty.channel.EventLoopGroup;import io.netty.channel.nio.NioEventLoopGroup;import io.netty.channel.socket.nio.NioServerSocketChannel;import org.springframework.context.support.ClassPathXmlApplicationContext;/** * @author twjitm - [Created on 2018-08-29 20:52] * @company https://github.com/twjitm * @jdk java version "1.8.0_77" */public class ZookeeperServer {    private static int port;    public ZookeeperServer(int port) {        this.port = port;    }    public static void main(String[] args) {        new ZookeeperServer(8081).start();    }    public static void start() {        ClassPathXmlApplicationContext app = TestSpring.initSpring();        NettyTransactionZookeeperServiceImpl zookeeperService = (NettyTransactionZookeeperServiceImpl) app.getBean("nettyTransactionZookeeperService");        NettyTransactionServiceImpl nettyTransactionService = (NettyTransactionServiceImpl) app.getBean("nettyTransactionServiceImpl");        NettyTransactionEntityCause cause = new NettyTransactionEntityCause("mutex");        ZookeeperMutexEntity mutexEntity = new ZookeeperMutexEntity(cause, "mutex", zookeeperService);        NettyTransactionCause transactionCause = new NettyTransactionCause("mutex");        NettyTransactionCommitResult result =                nettyTransactionService.commitTransaction(transactionCause, mutexEntity);        System.out.println(result.getResult());        EventLoopGroup group = new NioEventLoopGroup();        EventLoopGroup listenIntoGroup = new NioEventLoopGroup(); // (1)        EventLoopGroup progressGroup = new NioEventLoopGroup();        ServerBootstrap b = new ServerBootstrap(); // (2)        b.group(listenIntoGroup, progressGroup)                .channel(NioServerSocketChannel.class) // (3)                .childHandler(new ChannelInboundHandlerAdapter()) //(4)                .option(ChannelOption.SO_BACKLOG, 128)          // (5)                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)*/        // 绑定端口，开始接收进来的连接        ChannelFuture f = null; // (7)        try {            f = b.bind("127.0.0.1", port).sync();            System.out.println("服务器启动了");            f.channel().closeFuture().sync();        } catch (InterruptedException e) {            e.printStackTrace();        } finally {            listenIntoGroup.shutdownGracefully();            progressGroup.shutdownGracefully();        }    }}