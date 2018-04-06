package com.example.Bai3Tools;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import static spark.Spark.get;
import static spark.Spark.internalServerError;

public class Songuyento {
    private HashMap<Integer,List<Integer>> result;
    private LoadingCache<Integer,List<Integer>> cache;
    private CacheLoader loader;
    public Songuyento() {
        result = new HashMap<>();
        loader = new CacheLoader<Integer,List<Integer>>() {
            @Override
            public List<Integer> load(Integer index) throws Exception {
                return result.get(index);
            }
        };
        cache = CacheBuilder.newBuilder().expireAfterAccess(10,TimeUnit.SECONDS).build(loader);
    }

    public LoadingCache<Integer, List<Integer>> getCache() {
        return cache;
    }

    public void ListSNT(int n){
        List<Integer> list = new Vector<>();
        for(int i=1;i<n;i++){
            if(Check(i)){
                list.add(i);
            }
        }
        this.result.put(n,list);
    }
    public List<Integer> getDSsnbByIndex(int index){
        List<Integer> list = new Vector<>();
        list=result.get(index);
        return list;
    }
    private boolean Check(int i) {
        if(i<2){
            return false;
        }
        else{
            int n= (int) Math.sqrt(i);
            for(int j=2;j<=n;j++){
                if(i%j==0){
                    return false;
                }
            }
            return true;
        }
    }
    public static void main(String[] args) {
        Songuyento snt = new Songuyento();
        System.out.println("From cache: ");
        get("Songuyento/prime", "text/xml", (request, response)->{
            String id = request.queryParams("n");
            snt.ListSNT(Integer.parseInt(id));
            LoadingCache<Integer,List<Integer>> cache = snt.getCache();
            System.out.println(cache.get(Integer.parseInt(id)));
            System.out.println(cache.size());
            return snt.getDSsnbByIndex(Integer.parseInt(id));
        });
        }
}
