package com.superlcb;

import sun.misc.Signal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private ServerSocket server=null;
    private int port=80;  //端口
    private int backlog=50; //socket accept队列长度
    private String ip="localhost";
    public static volatile boolean signal=true;
    private ExecutorService service=null;
    static ConcurrentHashMap<String,Handle> routerMap=null;
    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public void get(String path,Handle handle){
        routerMap.put("GET".concat(path),handle);
    }

    public void post(String path,Handle handle){
        routerMap.put("POST".concat(path),handle);
    }


    public boolean start(){
        boolean res=true;
        try {
            server=new ServerSocket();
            server.bind(new InetSocketAddress(ip,port),backlog);
            service= Executors.newCachedThreadPool();

            while(signal){
                Socket socket=server.accept();
                service.execute(new Worker(socket));
            }
        } catch (IOException e) {
            res=false;
            e.printStackTrace();
        }
        return res;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public HttpServer(){
        shutdownGracefully();
        routerMap=new ConcurrentHashMap<>();//路由映射
    }
    public void shutdownGracefully(){
        Signal signal=new Signal("TERM");
        Signal.handle(signal,(signal1)->{
            System.out.println("接受到信号"+signal1.getName()+" "+signal1.getNumber());
            System.exit(0);
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            this.signal=false;
            service.shutdown();
            System.out.println("关闭线程池");
            try {
                server.close();
                System.out.println("关闭服务端socket连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));


    }


    public int getPort(){
        return this.port;
    }
    public void setPort(int port){
        this.port=port;
    }

}
