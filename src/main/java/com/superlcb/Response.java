package com.superlcb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private OutputStream out=null;
    private ByteArrayOutputStream byteOut=null;
    private HashMap<String,String> responseHeader=null;
    private String protocol=null;
    private String statusCode=null;
    private String msg=null;
    Response(OutputStream out){
        this.out=out;
        byteOut=new ByteArrayOutputStream();
        responseHeader=new HashMap<>();
        protocol="HTTP/1.1";
        statusCode="200";
        msg="OK";
        responseHeader.put("Server","MyHttpServer/1.0");
        responseHeader.put("Content-Type","text/HTML;Charset=utf-8");
    }
    public OutputStream getOutputStream(){
        return this.byteOut;
    }
    public void send(String content){
        try {
            byteOut.write(content.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void end(){
        byte[] bytes=byteOut.toByteArray();
        System.out.println(bytes.length);
        responseHeader.put("Content-Length",String.valueOf(bytes.length+1));
        String line=protocol.concat(" ").concat(statusCode).concat(msg).concat("\n\r");
        try {
            out.write(line.getBytes());
            for(Map.Entry<String,String> entry: responseHeader.entrySet()){
                String header=entry.getKey().concat(": ").concat(entry.getValue()).concat("\n\r");
                out.write(header.getBytes());

            }
            out.write("\n\r".getBytes());
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setHeader(String key,String value){
        responseHeader.put(key,value);
    }
}
