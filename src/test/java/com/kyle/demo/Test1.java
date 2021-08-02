package com.kyle.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.Language;

import java.util.*;

public class Test1 {
    public static void main(String[] args) {

        JSONObject nodesJson = JSONObject.parseObject(nodes).getJSONObject("nodes");
        JSONObject res = JSONObject.parseObject(json);
        JSONObject nodesObj = res.getJSONObject("routing_nodes").getJSONObject("nodes");
        // indexList, 要修改为从 api 获取index 相关的信息
        Set<String> indexSet = new TreeSet<>();
        // shards list
        List<JSONObject> result = getShards(nodesObj, indexSet, nodesJson);
        JSONArray array = res.getJSONObject("routing_nodes").getJSONArray("unassigned");
        if (array.size() > 0) {
            result.add(getUnassigned(array, indexSet));
        }
        JSONObject result1 = new JSONObject();
        result1.put("shards", result);
        result1.put("columns", indexSet);
        System.out.println(JSONObject.toJSONString(result1));
    }

    public static List<JSONObject> getShards(JSONObject jsonObject, Set<String> indexSet, JSONObject nodesJson) {
        List<JSONObject> result = new ArrayList<>(100);
        jsonObject.forEach((id, array) -> {
            JSONObject rowMap = new JSONObject();
            JSONArray shardsArray = (JSONArray) array;
            rowMap.put("_node", nodesJson.getJSONObject(id));
            shardsArray.forEach(o -> {
                JSONObject shard = (JSONObject) o;
                String index = shard.getString("index");
                JSONArray indexShardsList = rowMap.containsKey(index) ? rowMap.getJSONArray(index) : new JSONArray();
                indexShardsList.add(shard);
                rowMap.put(index, indexShardsList);
                indexSet.add(index);
            });
            result.add(rowMap);
        });
        return result;
    }

    public static JSONObject getUnassigned(JSONArray array, Set<String> indexSet) {
        if (array.size() > 0) {
            JSONObject shardsMap = new JSONObject();
            array.forEach(o -> {
                JSONObject shard = (JSONObject) o;
                String index = shard.getString("index");
                JSONArray indexShardsList = shardsMap.containsKey(index) ? shardsMap.getJSONArray(index) : new JSONArray();
                indexShardsList.add(shard);
                shardsMap.put(index, indexShardsList);
                indexSet.add(index);
            });
            return shardsMap;
        }
        return null;
    }

    @Language("JSON")
    static String nodes = "{\n" +
            "  \"_nodes\": {\n" +
            "    \"total\": 1,\n" +
            "    \"successful\": 1,\n" +
            "    \"failed\": 0\n" +
            "  },\n" +
            "  \"cluster_name\": \"elasticsearch\",\n" +
            "  \"nodes\": {\n" +
            "    \"dT1a6QdIQ3yPUbiCbBvGyQ\": {\n" +
            "      \"name\": \"DESKTOP-N5TMIF6\",\n" +
            "      \"transport_address\": \"127.0.0.1:9300\",\n" +
            "      \"host\": \"127.0.0.1\",\n" +
            "      \"ip\": \"127.0.0.1\",\n" +
            "      \"version\": \"7.12.1\",\n" +
            "      \"build_flavor\": \"default\",\n" +
            "      \"build_type\": \"zip\",\n" +
            "      \"build_hash\": \"3186837139b9c6b6d23c3200870651f10d3343b7\",\n" +
            "      \"roles\": [\n" +
            "        \"data\",\n" +
            "        \"data_cold\",\n" +
            "        \"data_content\",\n" +
            "        \"data_frozen\",\n" +
            "        \"data_hot\",\n" +
            "        \"data_warm\",\n" +
            "        \"ingest\",\n" +
            "        \"master\",\n" +
            "        \"ml\",\n" +
            "        \"remote_cluster_client\",\n" +
            "        \"transform\"\n" +
            "      ],\n" +
            "      \"attributes\": {\n" +
            "        \"ml.machine_memory\": \"8497946624\",\n" +
            "        \"xpack.installed\": \"true\",\n" +
            "        \"transform.node\": \"true\",\n" +
            "        \"ml.max_open_jobs\": \"20\",\n" +
            "        \"ml.max_jvm_size\": \"4248829952\"\n" +
            "      },\n" +
            "      \"jvm\": {\n" +
            "        \"pid\": 10892,\n" +
            "        \"version\": \"16\",\n" +
            "        \"vm_name\": \"OpenJDK 64-Bit Server VM\",\n" +
            "        \"vm_version\": \"16+36\",\n" +
            "        \"vm_vendor\": \"AdoptOpenJDK\",\n" +
            "        \"bundled_jdk\": true,\n" +
            "        \"using_bundled_jdk\": true,\n" +
            "        \"start_time_in_millis\": 1627729419198,\n" +
            "        \"mem\": {\n" +
            "          \"heap_init_in_bytes\": 4248829952,\n" +
            "          \"heap_max_in_bytes\": 4248829952,\n" +
            "          \"non_heap_init_in_bytes\": 7667712,\n" +
            "          \"non_heap_max_in_bytes\": 0,\n" +
            "          \"direct_max_in_bytes\": 0\n" +
            "        },\n" +
            "        \"gc_collectors\": [\n" +
            "          \"G1 Young Generation\",\n" +
            "          \"G1 Old Generation\"\n" +
            "        ],\n" +
            "        \"memory_pools\": [\n" +
            "          \"CodeHeap 'non-nmethods'\",\n" +
            "          \"Metaspace\",\n" +
            "          \"CodeHeap 'profiled nmethods'\",\n" +
            "          \"Compressed Class Space\",\n" +
            "          \"G1 Eden Space\",\n" +
            "          \"G1 Old Gen\",\n" +
            "          \"G1 Survivor Space\",\n" +
            "          \"CodeHeap 'non-profiled nmethods'\"\n" +
            "        ],\n" +
            "        \"using_compressed_ordinary_object_pointers\": \"true\",\n" +
            "        \"input_arguments\": [\n" +
            "          \"-Des.networkaddress.cache.ttl=60\",\n" +
            "          \"-Des.networkaddress.cache.negative.ttl=10\",\n" +
            "          \"-XX:+AlwaysPreTouch\",\n" +
            "          \"-Xss1m\",\n" +
            "          \"-Djava.awt.headless=true\",\n" +
            "          \"-Dfile.encoding=UTF-8\",\n" +
            "          \"-Djna.nosys=true\",\n" +
            "          \"-XX:-OmitStackTraceInFastThrow\",\n" +
            "          \"-XX:+ShowCodeDetailsInExceptionMessages\",\n" +
            "          \"-Dio.netty.noUnsafe=true\",\n" +
            "          \"-Dio.netty.noKeySetOptimization=true\",\n" +
            "          \"-Dio.netty.recycler.maxCapacityPerThread=0\",\n" +
            "          \"-Dio.netty.allocator.numDirectArenas=0\",\n" +
            "          \"-Dlog4j.shutdownHookEnabled=false\",\n" +
            "          \"-Dlog4j2.disable.jmx=true\",\n" +
            "          \"-Djava.locale.providers=SPI,COMPAT\",\n" +
            "          \"--add-opens=java.base/java.io=ALL-UNNAMED\",\n" +
            "          \"-XX:+UseG1GC\",\n" +
            "          \"-Djava.io.tmpdir=C:\\\\Users\\\\kyle\\\\AppData\\\\Local\\\\Temp\\\\elasticsearch\",\n" +
            "          \"-XX:+HeapDumpOnOutOfMemoryError\",\n" +
            "          \"-XX:HeapDumpPath=data\",\n" +
            "          \"-XX:ErrorFile=logs/hs_err_pid%p.log\",\n" +
            "          \"-Xlog:gc*,gc+age=trace,safepoint:file=logs/gc.log:utctime,pid,tags:filecount=32,filesize=64m\",\n" +
            "          \"-Xms4052m\",\n" +
            "          \"-Xmx4052m\",\n" +
            "          \"-XX:MaxDirectMemorySize=2124414976\",\n" +
            "          \"-XX:G1HeapRegionSize=4m\",\n" +
            "          \"-XX:InitiatingHeapOccupancyPercent=30\",\n" +
            "          \"-XX:G1ReservePercent=15\",\n" +
            "          \"-Delasticsearch\",\n" +
            "          \"-Des.path.home=E:\\\\dev-software\\\\elasticsearch-7.12.1\",\n" +
            "          \"-Des.path.conf=E:\\\\dev-software\\\\elasticsearch-7.12.1\\\\config\",\n" +
            "          \"-Des.distribution.flavor=default\",\n" +
            "          \"-Des.distribution.type=zip\",\n" +
            "          \"-Des.bundled_jdk=true\"\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Language("JSON")
    static String json = "{\n" +
            "  \"cluster_name\": \"elasticsearch\",\n" +
            "  \"cluster_uuid\": \"0CuGS4xRRymCSB5_HlkOdg\",\n" +
            "  \"master_node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "  \"routing_nodes\": {\n" +
            "    \"unassigned\": [\n" +
            "      {\n" +
            "        \"state\": \"UNASSIGNED\",\n" +
            "        \"primary\": false,\n" +
            "        \"node\": null,\n" +
            "        \"relocating_node\": null,\n" +
            "        \"shard\": 0,\n" +
            "        \"index\": \"kyle\",\n" +
            "        \"recovery_source\": {\n" +
            "          \"type\": \"PEER\"\n" +
            "        },\n" +
            "        \"unassigned_info\": {\n" +
            "          \"reason\": \"CLUSTER_RECOVERED\",\n" +
            "          \"at\": \"2021-07-31T11:03:55.837Z\",\n" +
            "          \"delayed\": false,\n" +
            "          \"allocation_status\": \"no_attempt\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"state\": \"UNASSIGNED\",\n" +
            "        \"primary\": false,\n" +
            "        \"node\": null,\n" +
            "        \"relocating_node\": null,\n" +
            "        \"shard\": 0,\n" +
            "        \"index\": \"kyle1\",\n" +
            "        \"recovery_source\": {\n" +
            "          \"type\": \"PEER\"\n" +
            "        },\n" +
            "        \"unassigned_info\": {\n" +
            "          \"reason\": \"CLUSTER_RECOVERED\",\n" +
            "          \"at\": \"2021-07-31T11:03:55.837Z\",\n" +
            "          \"delayed\": false,\n" +
            "          \"allocation_status\": \"no_attempt\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"state\": \"UNASSIGNED\",\n" +
            "        \"primary\": false,\n" +
            "        \"node\": null,\n" +
            "        \"relocating_node\": null,\n" +
            "        \"shard\": 0,\n" +
            "        \"index\": \"test\",\n" +
            "        \"recovery_source\": {\n" +
            "          \"type\": \"PEER\"\n" +
            "        },\n" +
            "        \"unassigned_info\": {\n" +
            "          \"reason\": \"CLUSTER_RECOVERED\",\n" +
            "          \"at\": \"2021-07-31T11:03:55.837Z\",\n" +
            "          \"delayed\": false,\n" +
            "          \"allocation_status\": \"no_attempt\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"state\": \"UNASSIGNED\",\n" +
            "        \"primary\": false,\n" +
            "        \"node\": null,\n" +
            "        \"relocating_node\": null,\n" +
            "        \"shard\": 0,\n" +
            "        \"index\": \".ds-my-data-stream-000001\",\n" +
            "        \"recovery_source\": {\n" +
            "          \"type\": \"PEER\"\n" +
            "        },\n" +
            "        \"unassigned_info\": {\n" +
            "          \"reason\": \"CLUSTER_RECOVERED\",\n" +
            "          \"at\": \"2021-07-31T11:03:55.837Z\",\n" +
            "          \"delayed\": false,\n" +
            "          \"allocation_status\": \"no_attempt\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"state\": \"UNASSIGNED\",\n" +
            "        \"primary\": false,\n" +
            "        \"node\": null,\n" +
            "        \"relocating_node\": null,\n" +
            "        \"shard\": 0,\n" +
            "        \"index\": \".ds-my-data-stream-2021.05.13-000001\",\n" +
            "        \"recovery_source\": {\n" +
            "          \"type\": \"PEER\"\n" +
            "        },\n" +
            "        \"unassigned_info\": {\n" +
            "          \"reason\": \"CLUSTER_RECOVERED\",\n" +
            "          \"at\": \"2021-07-31T11:03:55.837Z\",\n" +
            "          \"delayed\": false,\n" +
            "          \"allocation_status\": \"no_attempt\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"state\": \"UNASSIGNED\",\n" +
            "        \"primary\": false,\n" +
            "        \"node\": null,\n" +
            "        \"relocating_node\": null,\n" +
            "        \"shard\": 0,\n" +
            "        \"index\": \".ds-my-data-stream-2021.05.13-000002\",\n" +
            "        \"recovery_source\": {\n" +
            "          \"type\": \"PEER\"\n" +
            "        },\n" +
            "        \"unassigned_info\": {\n" +
            "          \"reason\": \"CLUSTER_RECOVERED\",\n" +
            "          \"at\": \"2021-07-31T11:03:55.837Z\",\n" +
            "          \"delayed\": false,\n" +
            "          \"allocation_status\": \"no_attempt\"\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"nodes\": {\n" +
            "      \"dT1a6QdIQ3yPUbiCbBvGyQ\": [\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".kibana_task_manager_7.12.1_001\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"8tiAGUynRbyF5v9n3guuTQ\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \"kyle\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"rJkC48zRQYSVShE5QWa5wQ\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".ds-ilm-history-5-2021.05.10-000001\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"ekD2YlnYSFyyO0N3iRmmfg\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".kibana_7.12.1_001\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"hhjTcfXxTym8Zhr8ezV8rg\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \"kyle1\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"TI6t_mkYScukRxsrEJOLag\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \"test\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"lGj5mv8ATjqp4GXUYFlbSA\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".kibana-event-log-7.12.1-000001\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"qGoNsm1PQ0-JhBHgTFqrYw\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".ds-my-data-stream-000001\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"t_kJYv9tTg6dJgMY3nVg2A\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".apm-agent-configuration\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"o7boBiF2Rq2_Ux51g8tT0A\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".ds-my-data-stream-2021.05.13-000001\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"bAGlkaXtRgayeBrbOQ6j2g\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".tasks\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"gWTYMyYnRJemzDXfchHDeg\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".apm-custom-link\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"-1PUJZXXT_ukgy5CP3N4Tg\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"state\": \"STARTED\",\n" +
            "          \"primary\": true,\n" +
            "          \"node\": \"dT1a6QdIQ3yPUbiCbBvGyQ\",\n" +
            "          \"relocating_node\": null,\n" +
            "          \"shard\": 0,\n" +
            "          \"index\": \".ds-my-data-stream-2021.05.13-000002\",\n" +
            "          \"allocation_id\": {\n" +
            "            \"id\": \"SjEUJcHdT2mepMW4JQkxiQ\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
