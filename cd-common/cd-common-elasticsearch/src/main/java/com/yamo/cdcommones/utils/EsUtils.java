package com.yamo.cdcommones.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommones.domain.bo.DeffaultToEsDataBO;
import com.yamo.cdcommones.domain.bo.EsSearchBO;
import com.yamo.cdcommones.domain.geo.CircleRequestBO;
import com.yamo.cdcommones.domain.geo.GeoRequestBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CoordinatesBuilder;
import org.elasticsearch.common.geo.builders.PolygonBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class EsUtils {

    @Autowired
    @Qualifier("esHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    /**
     * 判断索引是否存在
     * @param index
     * @return
     */
    public boolean isExistIndex(String index){
        if(StrUtil.isBlank(index)){
            throw new BizException("索引不能为空");
        }
        GetIndexRequest indexRequest=new GetIndexRequest(index);
        boolean isExist=false;
        try{
            isExist=restHighLevelClient.indices().exists(indexRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            throw new BizException("判断索引失败");
        }
        return isExist;
    }

    /**
     * 创建索引
     * @param index
     * @throws IOException
     */
    public void createIndex(String index) {
        if(StrUtil.isBlank(index)){
            return;
        }
        try{
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 2));
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            throw new BizException("创建索引失败！");
        }
    }

    /**
     * 删除索引
     * @param index
     */
    public void delIndex(String index){
        if(!isExistIndex(index)){
            return;
        }
        //删除
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            throw new BizException("删除索引失败");
        }
    }

    /**
     * 根据id删除文档
     *
     * **/
    public void removeIndexDocById(String index,String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, id);
        try {
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            if (response != null) {
                if (response.getResult() == DocWriteResponse.Result.DELETED || response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                    log.info("es数据删除 index[{}] 成功id[{}]", index, id);
                } else {
                    log.error("es数据删除 index[{}] id[{}] 错误:{}", index, id, response.getResult().toString());
                }
            } else {
                log.error("es数据删除 index[{}] id[{}] es返回结果为空", index, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("es数据删除失败 index[{}] id[{}]", index, id, e);
        }
    }

    /**
     * 查询当日此索引某个时间字段的数据
     * */
    public EsSearchBO getEsDatainRangeDay(String index, String timeFild, String startTime, String endTime, Integer pageindex, Integer pageSize){
//        String today = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
//        String startTime = today + " 00:00:00";
//        String endTime = today + " 23:59:59";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if(pageindex!=null&&pageSize!=null){
            searchSourceBuilder.from((pageindex-1)*pageSize);
            searchSourceBuilder.size(pageSize);
        }
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(this.rangeOfEs(timeFild, startTime, endTime));
        searchSourceBuilder.query(boolQueryBuilder);
        //获取全部
        EsSearchBO esSearchBO = this.scrollSearch(index, searchSourceBuilder);
        return esSearchBO;
    }

    /**
     * 格式化单个json数据为es索引的字段格式,keySet可传为空，若空则查询对应的set
     * 用于插入或更新数据时，源对象与索引的字段不匹配的情况
     * */
    public JSONObject formatSingleEsData(JSONObject source, String index, Set<String> keySet,
                                         String sourceConnectStr, String targetConnectStr, DeffaultToEsDataBO deffaultToEsDataBO) throws IOException {
        if(CollectionUtil.isEmpty(keySet))
            keySet = this.getIndexMapping(index);
        JSONObject EsData = new JSONObject();
        Double longitude = null;
        Double latitude = null;
        String datatype=null;
        if(deffaultToEsDataBO!=null)
            datatype=deffaultToEsDataBO.getDatatype();
        for (Map.Entry<String, Object> entry : source) {
            if(deffaultToEsDataBO!=null){
                if (StringUtils.isNotEmpty(deffaultToEsDataBO.getLocationName())&&entry.getKey().equals(deffaultToEsDataBO.getLocationName())) {
                    String location = (String) entry.getValue();
                    String[] split = location.split(deffaultToEsDataBO.getLocateSplit());
                    longitude = Double.parseDouble(split[0]);
                    latitude=Double.parseDouble(split[1]);
                    EsData.put("LONGITUDE", longitude);
                    EsData.put("LONGITUDE", latitude);
                    continue;
                }else{
                    if(StringUtils.isNotEmpty(deffaultToEsDataBO.getLonAttrName())&&entry.getKey().equals(deffaultToEsDataBO.getLonAttrName())){
                        longitude= (Double) entry.getValue();
                        EsData.put("LONGITUDE", entry.getValue());
                        continue;
                    }
                    if(StringUtils.isNotEmpty(deffaultToEsDataBO.getLatAttrName())&&entry.getKey().equals(deffaultToEsDataBO.getLatAttrName())){
                        latitude= (Double) entry.getValue();
                        EsData.put("LATITUDE", entry.getValue());
                        continue;
                    }
                }
            }
            String jsonKey = entry.getKey().toUpperCase().replaceAll(sourceConnectStr, "");
            for (String key : keySet) {
                String keyUpper = key.toUpperCase().replaceAll(targetConnectStr, "");
                if (jsonKey.equals(keyUpper)) {
                    EsData.put(key, entry.getValue());
                }
            }
        }
        if(longitude!=null&&latitude!=null){
            EsData.set("LOCATION","POINT("+longitude+" "+latitude+")");
        }
        if(StringUtils.isNotEmpty(datatype)){
            EsData.set("DATA_TYPE",datatype);
        }
        if(EsData.size()>=source.size()) {
            log.info("该JSON格式化成功");
            return EsData;
        } else {
            log.info("该JSON格式化失败");
            return null;
        }
    }

    /**
     * 获取json数据字段映射到es索引mapping的hashmap
     * */
    public Map<String,String> getJsonMappingMapToEsMapping(String Index,JSONObject jsonObject,String sourceConnectStr,String targetConnectStr) throws IOException {
        Set<String> keySet = this.getIndexMapping(Index);
        HashMap<String, String> returnMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject) {
            String jsonKey = entry.getKey().toUpperCase().replaceAll(sourceConnectStr, "");
            for (String key : keySet) {
                String keyUpper = key.toUpperCase().replaceAll(targetConnectStr, "");
                if (jsonKey.equals(keyUpper)) {
                    returnMap.put(jsonKey, key);
                }
            }
        }
        if(returnMap.size()>0){
            log.info("映射获取成功");
        }else {
            log.info("映射获取失败");
        }
        return returnMap;
    }

    /**
     * 格式化json数组为es索引的字段格式,keySet可传为空，若空则查询对应的set
     * 用于插入或更新数据时，源对象与索引的字段不匹配的情况
     * */
    public JSONArray formatEsDataArray(JSONArray sourceArray,String index,Set<String> keySet) throws IOException {
        if(CollectionUtil.isEmpty(keySet))
            keySet = this.getIndexMapping(index);
        JSONArray array = new JSONArray();
        List<Integer> failIndexs = new ArrayList<>();
        boolean ifFail = false;
        for (int i = 0; i < sourceArray.size(); i++) {
            JSONObject source = (JSONObject) sourceArray.get(i);
            JSONObject EsData = new JSONObject();
            for (Map.Entry<String, Object> entry : source) {
                String jsonKey = entry.getKey().toUpperCase().replaceAll("_", "");
                for (String key : keySet) {
                    String keyUpper = key.toUpperCase().replaceAll("_", "");
                    if (jsonKey.equals(keyUpper)) {
                        EsData.put(key, entry.getValue());
                    }
                }
            }
            if(EsData.size()>=source.size())
                array.add(EsData);
            else{
                ifFail=true;
                log.info("该对象无法格式化：{},下标为：{}"+source.toString(),i);
                failIndexs.add(i);
            }
        }
        if(array.size()==0)
            log.info("该JSON数组格式化失败");
        else if(ifFail&&array.size()>0){
            log.info("该JSON数组部分格式化成功，但存在未成功格式化的json：{}",failIndexs);
        }else {
            log.info("该JSON数组格式化成功");
        }
        return array;
    }

    public Set<String> getIndexMapping(String index) throws IOException {
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(index);
        GetMappingsResponse response = restHighLevelClient.indices().getMapping(mappingsRequest, RequestOptions.DEFAULT);
        Map<String, MappingMetadata> mappingMap = response.mappings();
        Map<String, Object> mapping = mappingMap.get(index).sourceAsMap();
        Map<String, Object> properties = (Map<String, Object>) mapping.get("properties");
        return properties.keySet();
    }

    public void createInitIndex(String indexName) throws IOException {
        IndicesClient indicesClient = restHighLevelClient.indices();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")

                .startObject("TREE_LEVEL1")
                .field("type", "keyword")
                .endObject()

                .startObject("TREE_LEVEL2")
                .field("type", "keyword")
                .endObject()

                .startObject("TREE_LEVEL3")
                .field("type", "keyword")
                .endObject()

                .startObject("TREE_LEVEL4")
                .field("type", "keyword")
                .endObject()

                .startObject("TREE_LEVEL5")
                .field("type", "keyword")
                .endObject()

                .startObject("LONGITUDE")
                .field("type", "double")
                .endObject()

                .startObject("LATITUDE")
                .field("type", "double")
                .endObject()

                .startObject("LOCATION")
                .field("type", "geo_shape")
                .endObject()

                .startObject("DATA_TYPE")
                .field("type", "keyword")
                .endObject()

                    .startObject("SHOW")
                .field("type", "keyword")
                .endObject()

                .endObject()
                .endObject();

        createIndexRequest.mapping(mapping);
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));

        CreateIndexResponse response = indicesClient.create(createIndexRequest, RequestOptions.DEFAULT);
        String index = response.index();
        log.info("索引名称：{}", index);
        boolean acknowledged = response.isAcknowledged();
        log.info("索引是否创建成功：{}", acknowledged);
    }

    /**
     * 通用查询
     *
     * @param index
     * @param searchSourceBuilder
     * @return
     */
    public EsSearchBO commonSearch(String index, SearchSourceBuilder searchSourceBuilder) {
        EsSearchBO esSearchBO = new EsSearchBO();
        SearchRequest request = new SearchRequest(index);
        request.source(searchSourceBuilder);
        SearchResponse response;
        List<SearchHit> hitList = new ArrayList<>();
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("es搜索失败!");
        }
        SearchHit[] hits = response.getHits().getHits();
        long total = response.getHits().getTotalHits().value;
        Collections.addAll(hitList, hits);
        List<JSONObject> jqVOList = toList(hitList);
        esSearchBO.setTotal((int) total);
        esSearchBO.setDataList(jqVOList);
        return esSearchBO;
    }

    /**
     * 滚动查询
     *
     * @param index
     * @param searchSourceBuilder
     * @return
     */
    public EsSearchBO scrollSearch(String index, SearchSourceBuilder searchSourceBuilder) {
        EsSearchBO esSearchBO = new EsSearchBO();
        SearchRequest request = new SearchRequest(index);
        searchSourceBuilder.size(10000);
        request.source(searchSourceBuilder);
        //设置滚动超时时间为5分钟
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5L));
        request.scroll(scroll);
        SearchResponse response;
        List<SearchHit> hitList = new ArrayList<>();
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("es搜索失败!");
        }
        SearchHit[] hits = response.getHits().getHits();
        long total = hits.length;
        String scrollId = response.getScrollId();
        if (hits != null && hits.length > 0) {
            Collections.addAll(hitList, hits);
            while (hits.length > 0) {
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
                searchScrollRequest.scroll(scroll);
                try {
                    response = restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BizException("es搜索失败!");
                }
                scrollId = response.getScrollId();
                hits = response.getHits().getHits();
                Collections.addAll(hitList, hits);
                total += hits.length;
            }
        }
        esSearchBO.setTotal((int) total);
        esSearchBO.setDataList(toList(hitList));
        return esSearchBO;
    }

    public List<JSONObject> toList(List<SearchHit> hitList) {
        List<JSONObject> list = new ArrayList<>();
        for (SearchHit str : hitList) {
            JSONObject jsonObject = JSONUtil.parseObj(str.getSourceAsString());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 分页查询
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public List<SearchHit> indexNameLitList(String indexName) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1000);
        request.source(searchSourceBuilder);
        SearchResponse response;
        List<SearchHit> hitList = new ArrayList<>();
        int i = 0;
        do {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            long total = response.getHits().getTotalHits().value;
            i = i + 1;
            Collections.addAll(hitList, hits);
            log.info("第{}次查询;从{}开始查询;共{}条数据", i, searchSourceBuilder.from(), total);
            //设置下一次查询的form参数
            int from = searchSourceBuilder.from() + hits.length;
            searchSourceBuilder.from(from);
        } while (response.getHits().getHits().length != 0);
        return hitList;
    }

    /**
     * 条件更新
     * @param indexName
     * @param queryBuilder
     * @param params
     */
    public void updateByQuery(String indexName,BoolQueryBuilder queryBuilder,Map<String,Object> params) {
        UpdateByQueryRequest request=new UpdateByQueryRequest(indexName);
        request.setQuery(queryBuilder);
        StringBuilder scriptBuilder=new StringBuilder();
        for (String s : params.keySet()) {
            scriptBuilder.append("ctx._source['").append(s).append("']").append("=").append("params['").append(s).append("'];");
        }
        Script script=new Script(ScriptType.INLINE,"painless",scriptBuilder.toString(),params);
        request.setScript(script);
        request.setMaxRetries(3);
        request.setAbortOnVersionConflict(true);
        try {
            BulkByScrollResponse response=restHighLevelClient.updateByQuery(request,RequestOptions.DEFAULT);
            log.info("--------------总数："+ response.getTotal()+"-----------------");
            log.info("--------------条件已更新数量："+ response.getUpdated()+"-----------------");
            log.info("--------------失败数量："+ response.getBulkFailures().size()+"-----------------");
        } catch (IOException e) {
            throw new BizException("条件更新失败");
        }
    }

    /**
     * 更新或插入数据 推送至ES
     *
     * @param indexName
     * @param type
     * @param id
     * @param jsonObject
     */
    public void updateOrInsertToEs(String indexName, String type, String id, JSONObject jsonObject) {
        try {
            //根据ID 查询 ES是否存在这条数据，存在则更新，不存在则新增
            GetRequest getRequest = new GetRequest(indexName, type, id);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            //禁止获取存储字段
            getRequest.storedFields("_none_");
            boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
            if (exists) {
                UpdateRequest request = new UpdateRequest(indexName, type, id);
                request.fetchSource(true);
                request.docAsUpsert(true);
                request.doc(jsonObject, XContentType.JSON);
                UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
//                if (null != response) {
//                    if (response.getResult() == DocWriteResponse.Result.UPDATED) {
//                        log.info("Es更新成功,索引=" + indexName + "id=" + id + "[" + jsonObject + "]");
//                    }
//                }
            } else {
//                log.info("Es更新，数据查询不存在，则新增数据：" + "[" + jsonObject + "]");
                UpdateRequest updateRequest = new UpdateRequest(indexName, type, id);
                updateRequest.docAsUpsert(true);
                updateRequest.fetchSource(true);
                updateRequest.doc(jsonObject, XContentType.JSON);
                UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//                if (null != response) {
//                    if (response.getResult() == DocWriteResponse.Result.CREATED) {
//                        log.info("Es新增数据成功：" + response + "[" + jsonObject + "]");
//                    }
//                }
            }
        } catch (Exception e) {
            log.error("Es更新或新增失败：" + e.getMessage());
            throw new BizException("数据更新失败");
        }
    }

    /**
     * 更新 推送至ES
     *
     * @param indexName
     * @param type
     * @param id
     * @param jsonObject
     */
    public void updateToEs(String indexName, String type, String id, JSONObject jsonObject) {
        try {
            //根据ID 查询 ES是否存在这条数据，存在则更新
            GetRequest getRequest = new GetRequest(indexName, type, id);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            //禁止获取存储字段
            getRequest.storedFields("_none_");
            boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
            if (!exists) {
//                log.error("id不存在,索引=" + indexName + "id=" + id + "[" + jsonObject + "]");
                return;
            }
            UpdateRequest request = new UpdateRequest(indexName, type, id);
            request.fetchSource(true);
            request.docAsUpsert(true);
            request.doc(jsonObject, XContentType.JSON);
            //失败后重试5次
            request.retryOnConflict(5);
            UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
//            if (null != response) {
//                if (response.getResult() == DocWriteResponse.Result.UPDATED) {
//                    log.info("Es更新成功,索引=" + indexName + "id=" + id + "[" + jsonObject + "]");
//                }
//            }
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Es更新或新增失败：" + e.getMessage());
            log.error("失败数据："+jsonObject);
            throw new BizException("数据更新失败");
        }
    }


    //单条更新
    public void updateEs(String index, String type, String id, String filedName, Object value) {
        try {
            GetRequest getRequest = new GetRequest(index, type, id);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            //禁止获取存储字段
            getRequest.storedFields("_none_");
            boolean exist = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
            if (!exist) {
                return;
            }
            UpdateRequest updateRequest = new UpdateRequest(index, type, id);
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field(filedName, value).endObject();
            updateRequest.doc(builder);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//            if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
//                log.info("id为:{}成功更新", id);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("更新失败！");
        }
    }

    public void bulkUpdate(String indexName, String primaryKey, JSONArray updateJsonArray, boolean isDocAsUpsert){
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < updateJsonArray.size(); i++) {
            JSONObject jsonObject = updateJsonArray.getJSONObject(i);
            // 设置主键
            String id = "";
            if (StringUtils.isNotEmpty(primaryKey)) {
                id = jsonObject.getStr(primaryKey);
            } else {
                id = jsonObject.getStr("id");
            }
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            UpdateRequest updateRequest = new UpdateRequest(indexName, id);
            updateRequest = updateRequest.doc(jsonObject, XContentType.JSON);

            if (isDocAsUpsert) {
                updateRequest = updateRequest.docAsUpsert(true);
            }
            bulkRequest.add(updateRequest);
        }

        if (bulkRequest.requests().isEmpty()) {
            log.info("es数据批量更新 index[{}] 为空", indexName);
            return;
        }

        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new BizException("更新失败");
        }

        if (bulk.hasFailures()) {
            Iterator<BulkItemResponse> iterator = bulk.iterator();
            while (iterator.hasNext()) {
                BulkItemResponse response = iterator.next();
                if (response.isFailed()) {
                    log.info("es数据批量更新失败 index[{}] id[{}] failureMessage[{}]", indexName, response.getItemId(), response.getFailureMessage());
                }
            }
        }
    }

    /**
     * 多选值搜索
     *
     * @param fieldName
     * @param list
     * @return
     */
    public BoolQueryBuilder multSelectOfEs(String fieldName, List<String> list) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //00-代表全部
        if (list != null && !list.contains("00")) {
            for (String str : list) {
                boolQueryBuilder.should(QueryBuilders.termQuery(fieldName, str));
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 范围查询
     *
     * @param fieldName
     * @param startVal
     * @param endVal
     * @return
     */
    public BoolQueryBuilder rangeOfEs(String fieldName, String startVal, String endVal) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StrUtil.isNotBlank(startVal)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).gte(startVal));
        }
        if (StrUtil.isNotBlank(endVal)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).lte(endVal));
        }
        return boolQueryBuilder;
    }

    /**
     * 构造GeoShapeQuery对象 for 圈查
     */
    public GeoShapeQueryBuilder buildCircleGeoShapeQuery(String columnName, CircleRequestBO circle) throws Exception {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(circle.getNumPoints());
        // there are your coordinates
        shapeFactory.setCentre(new Coordinate(circle.getCircleCenterX(), circle.getCircleCenterY()));
        //
        double v = MercatorCoordinatesConverter.Mile2Degree((circle.getMeters() * 2));
        shapeFactory.setSize(v);
        Polygon circleGeo = shapeFactory.createCircle();
        //
        GeoShapeQueryBuilder geoShapeQuery = QueryBuilders.geoShapeQuery(
                columnName,
                new PolygonBuilder(
                        new CoordinatesBuilder().coordinates(circleGeo.getCoordinates())
                )
        );
        if (circle.getShapeRelation() == null) {
            circle.setShapeRelation(ShapeRelation.WITHIN.getRelationName());
        }
        // 设置GeoShape查询的图形关系
        geoShapeQuery = getShapeRelation(circle, geoShapeQuery);
        return geoShapeQuery;
    }
    private GeoShapeQueryBuilder getShapeRelation(GeoRequestBO polygon, GeoShapeQueryBuilder geoShapeQuery) {
        ShapeRelation relation = null;
        relation = ShapeRelation.getRelationByName(polygon.getShapeRelation());
        if (relation != null) {
            relation = ShapeRelation.WITHIN;
        }
        geoShapeQuery.relation(relation);
        return geoShapeQuery;
    }

    /**
     * 单个查询
     *
     * @param fieldName
     * @param val
     * @return
     */
    public BoolQueryBuilder eqOfEs(String fieldName, String val) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StrUtil.isNotBlank(val)) {
            boolQueryBuilder.must(QueryBuilders.termQuery(fieldName, val));
        }
        return boolQueryBuilder;
    }

    /**
     * 获取时间相交的数据
     * */
    public EsSearchBO getEsDataInferDay(String index, String startDateFild, String endDateFild, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(endDateFild).lt(startTime).gt(startDateFild).gt(endTime));
        if(pageNum!=null||pageSize!=null) {
            builder.from((pageNum - 1) * pageSize);
            builder.size(pageSize);
        }
        builder.query(boolQueryBuilder);
        return this.scrollSearch(index,builder);
    }

    public JSONObject getDataById(String psManageIndex, String eventId) throws IOException {
        GetRequest getRequest = new GetRequest(psManageIndex, eventId);
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        if (response.isExists()){
            return JSONUtil.parseObj(sourceAsString);
        }
        return null;
    }

    public long getTotal(String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        CountResponse count = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        return count.getCount();
    }
}
