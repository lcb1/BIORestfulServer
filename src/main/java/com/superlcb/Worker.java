package com.superlcb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker implements Runnable {
    private Socket socket=null;
    public Worker(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            InputStream in=socket.getInputStream();
            OutputStream out=socket.getOutputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            String line=reader.readLine();
            System.out.println(line);
            String[] lines=line.split(" "); //解析http请求行

            if(!HttpServer.routerMap.containsKey(lines[0].concat(lines[1]))){
                return;
            }

            String header=null;
            Request request=new Request(in);

            request.method=lines[2];


            Response response=new Response(out);
            while(!(header=reader.readLine()).isEmpty()){  //解析http请求头
                String[] kAndV=header.split(": ");
                request.header.put(kAndV[0],kAndV[1]);
            }
            HttpServer.routerMap.get(lines[0].concat(lines[1])).handle(request,response); //获取对应的处理函数
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
