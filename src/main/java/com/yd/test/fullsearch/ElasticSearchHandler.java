package com.yd.test.fullsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class ElasticSearchHandler {
	public static void main(String[] args) {
        try {
            /* 创建客户端 */
            // client startup
            Client client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("114.215.138.69"), 9300));

            List<String> jsonData = DataFactory.getInitJsonData();

            for (int i = 0; i < jsonData.size(); i++) {
                IndexResponse response = client.prepareIndex("blog", "article").setSource(jsonData.get(i)).get();
                if (response.isCreated()) {
                   System.out.println("创建成功!");
                }
            }
            client.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
