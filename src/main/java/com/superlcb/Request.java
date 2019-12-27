package com.superlcb;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class Request {


    HashMap<String,String> header=new HashMap<>();
    private InputStream in=null;
    String method=null;
    Request(InputStream in){
        this.in=in;
    }
    public String getHeaderValue(String key){
        return  this.header.get(key);
    }
    public Set<String> getHeaderKeys(){
        return  this.header.keySet();
    }

    public InputStream getInputStream(){
        return this.in;
    }

    public String getMethod(){
        return this.method;
    }
}
