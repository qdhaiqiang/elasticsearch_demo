package com.yd.test.fullsearch;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
public class ElasticSearchCreate {
	private static String ServerIP = "114.215.138.69";// ElasticSearch server ip
    private static int ServerPort = 9300;// port
    private Client client;

    public static void main(String[] args) {

        try {
            Client client = TransportClient.builder().build().addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName(ServerIP), ServerPort));

            DeleteResponse dResponse = client.prepareDelete("blog", "article", "11").execute()
                    .actionGet();

            if (dResponse.isFound()) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }

            QueryBuilder qb1 = termQuery("title", "hibernate");


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

         deleteIndex("test");//删除名为test的索引库
    }

    // 删除索引库

    public static void deleteIndex(String indexName) {

        try {
            if (!isIndexExists(indexName)) {
                System.out.println(indexName + " not exists");
            } else {
                Client client = TransportClient.builder().build().addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName(ServerIP),
                                ServerPort));

                DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                        .execute().actionGet();
                if (dResponse.isAcknowledged()) {
                    System.out.println("delete index "+indexName+"  successfully!");
                }else{
                    System.out.println("Fail to delete index "+indexName);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // 创建索引库
    public static void createIndex(String indexName) {
        try {
            Client client = TransportClient.builder().build().addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName(ServerIP), ServerPort));

            // 创建索引库

            if (isIndexExists("indexName")) {
                System.out.println("Index  " + indexName + " already exits!");
            } else {
                CreateIndexRequest cIndexRequest = new CreateIndexRequest("indexName");
                CreateIndexResponse cIndexResponse = client.admin().indices().create(cIndexRequest)
                        .actionGet();
                if (cIndexResponse.isAcknowledged()) {
                    System.out.println("create index successfully！");
                } else {
                    System.out.println("Fail to create index!");
                }

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    // 判断索引是否存在 传入参数为索引库名称
    public static boolean isIndexExists(String indexName) {
        boolean flag = false;
        try {
            Client client = TransportClient.builder().build().addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName(ServerIP), ServerPort));

            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);

            IndicesExistsResponse inExistsResponse = client.admin().indices()
                    .exists(inExistsRequest).actionGet();

            if (inExistsResponse.isExists()) {
                flag = true;
            } else {
                flag = false;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
