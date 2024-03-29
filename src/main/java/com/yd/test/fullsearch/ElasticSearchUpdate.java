package com.yd.test.fullsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ElasticSearchUpdate {
	private static Client client;

    public static void main(String[] args) {

        try {
            // client startup
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("114.215.138.69"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        upMethod1();

    }

    public static void upMethod1() {
        try {
            // 方法一:创建一个UpdateRequest,然后将其发送给client.
            UpdateRequest uRequest = new UpdateRequest();
            uRequest.index("blog");
            uRequest.type("article");
            uRequest.id("22");
            uRequest.doc(jsonBuilder().startObject().field("content", "学习目标 掌握java泛型的产生意义ssss").endObject());
            client.update(uRequest).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static void upMethod2() {
        // 方法二:prepareUpdate() 使用脚本更新索引
        client.prepareUpdate("blog", "article", "1")
                .setScript(new Script("ctx._source.title = \"git入门\"", ScriptService.ScriptType.INLINE, null, null))
                .get();
    }

    public static void upMethod3() {
        // 方法三:prepareUpdate() 使用doc更新索引
        try {
            client.prepareUpdate("blog", "article", "1")
                    .setDoc(jsonBuilder().startObject().field("content", "SVN与Git对比。。。").endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void upMethod4() {
        // 方法四: 增加新的字段

        try {
            UpdateRequest updateRequest = new UpdateRequest("blog", "article", "1")
                    .doc(jsonBuilder().startObject().field("commet", "0").endObject());
            client.update(updateRequest).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void upMethod5() {
        // 方法五：upsert 如果文档不存在则创建新的索引
        try {
            IndexRequest indexRequest = new IndexRequest("blog", "article", "10").source(jsonBuilder().startObject()
                    .field("title", "Git安装10").field("content", "学习目标 git。。。10").endObject());

            UpdateRequest uRequest2 = new UpdateRequest("blog", "article", "10").doc(
                    jsonBuilder().startObject().field("title", "Git安装").field("content", "学习目标 git。。。").endObject())
                    .upsert(indexRequest);
            client.update(uRequest2).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
