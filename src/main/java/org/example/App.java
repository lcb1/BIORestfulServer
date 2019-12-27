package org.example;

import com.superlcb.HttpServer;

import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        HttpServer server=new HttpServer();
        server.setPort(8080);

        server.get("/Hello/MyHttpServer",(request,response)->{
            response.send("<hr/><hr/>Hello");
            response.end();
        });
        server.start();
    }
}
