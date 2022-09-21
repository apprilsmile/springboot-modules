package com.appril.service.impl;

import com.appril.entity.Goods;
import com.appril.service.IGoodsService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {


    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public void save(Goods goods) {
        restTemplate.save(goods);
    }

    @Override
    public void batchSave(List<Goods> goodsList) {
        restTemplate.save(goodsList);
    }

    /**
     * 使用QueryBuilder
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     * matchAllQuery();         匹配所有文件
     * wildcardQuery()  模糊查询
     *
     * 组合查询
     * must(QueryBuilders) :   AND
     * mustNot(QueryBuilders): NOT
     * should:               : OR
     * filter:   以过滤模式进行，不评分，但是必须匹配
     *
     */
    @Override
    public void search() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("title", titles)).should(QueryBuilders.matchQuery("title", "iphone"));
        queryBuilder.withQuery(QueryBuilders.matchAllQuery());
        //桶聚合查询
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("brand").field("title.keyword")////以title分组(必须加.keyword)，组名为：brand
                .subAggregation(AggregationBuilders.terms("type").field("name.keyword")
//                        .subAggregation(AggregationBuilders.terms("publishDateCollection").field("publishDate"))
                );
        FieldSortBuilder publishDateSort = SortBuilders.fieldSort("publishDate").order(SortOrder.DESC);//倒序
        queryBuilder.addAggregation(aggregation);
//        queryBuilder.withSort(publishDateSort);
//        queryBuilder.withPageable(PageRequest.of(1,2));//分页查询
        SearchHits<Goods> searchResult = restTemplate.search(queryBuilder.build(), Goods.class);
        Aggregations aggregations = searchResult.getAggregations();
        Terms brand = aggregations.get("brand");//指定返回类型为Aggregation的实现类Terms,否则无法遍历
        for(Terms.Bucket term:brand.getBuckets()) {
            System.out.println(term.getKey()+"  "+term.getDocCount());
            Aggregations nameAggregations= term.getAggregations();
            Terms name =   nameAggregations.get("type");
            for(Terms.Bucket nameTerm:name.getBuckets()) {
                System.out.println(nameTerm.getKey() + "  " + nameTerm.getDocCount());
            }
        }
        //桶聚合 Sum + 排序
        TermsAggregationBuilder sumAggregation = AggregationBuilders.terms("brand").field("title.keyword")
                .order(BucketOrder.compound(BucketOrder.aggregation("sum_price",true)));//按照sum_price 升序
        SumAggregationBuilder sumPrice = AggregationBuilders.sum("sum_price").field("price");
        sumAggregation.subAggregation(sumPrice);
        queryBuilder.addAggregation(sumAggregation);
//        queryBuilder.withSort(publishDateSort);
//        queryBuilder.withPageable(PageRequest.of(1,2));//分页查询
        searchResult = restTemplate.search(queryBuilder.build(), Goods.class);
        Aggregations brandAggregations = searchResult.getAggregations();
        Terms brandTerm = brandAggregations.get("brand");//指定返回类型为Aggregation的实现类Terms,否则无法遍历
        for(Terms.Bucket term:brandTerm.getBuckets()) {
            System.out.println(term.getKey()+"  "+term.getDocCount());
            Aggregations priceAggregations= term.getAggregations();
            ParsedSum price =   priceAggregations.get("sum_price");
            System.out.println(price.getValueAsString() + "\t" + price.getValue() + "\t"+price.getName());
//            for(Terms.Bucket nameTerm:price.getBuckets()) {
//                System.out.println(nameTerm.getKey() + "  " + nameTerm.getDocCount());
//            }
        }

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {
        restTemplate.delete("",Goods.class);
    }

    /**
     *  group by deviceId,siteTag limit 1 分组后获取分组排序中的一条记录
     *{
     *         BoolQueryBuilder boolQuery = ElasticUtil.buildBoolQueryBuilder(esQueryVo);
     *         SearchSourceBuilder searchSourceBuilder = ElasticUtil.initSearchSourceBuilder(boolQuery, esQueryVo);
     *         SearchRequest request = new SearchRequest(esQueryVo.getIdxName());
     *         searchSourceBuilder.size(0);
     *         int size = 50000;
     *         AggregationBuilder aggregation = AggregationBuilders.terms("deviceIds").field("deviceId").size(size)
     *                 .subAggregation(AggregationBuilders.terms("siteTags").field("siteTag").size(size)
     *                         .subAggregation(AggregationBuilders.topHits("ones").size(1).sort("alarmTime", SortOrder.DESC)));//获取分组后时间倒排序第一条记录
     *         searchSourceBuilder.aggregation(aggregation);
     *         request.source(searchSourceBuilder);
     *         try {
     *             SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
     *             if (response == null) {
     *                 return Collections.emptyList();
     *             }
     *             Terms deviceIds = response.getAggregations().get("deviceIds");
     *             if (deviceIds == null) {
     *                 return null;
     *             }
     *             List<Map<String, Object>> rn = new ArrayList<>();
     *             for (Terms.Bucket bucket : deviceIds.getBuckets()) {
     *                 Terms siteTags = bucket.getAggregations().get("siteTags");
     *                 List<? extends Terms.Bucket> siteTagsBuckets = siteTags.getBuckets();
     *                 if (!siteTagsBuckets.isEmpty()) {
     *                     for (Terms.Bucket siteTag : siteTagsBuckets) {
     *                         TopHits tophits = siteTag.getAggregations().get("ones");
     *                         SearchHits topHits = tophits.getHits();
     *                         SearchHit hit = topHits.getHits()[0];
     *                         // 得到相关的数据
     *                         Map<String, Object> deviceInfo = hit.getSourceAsMap();
     *                         rn.add(deviceInfo);
     *                     }
     *                 }
     *             }
     *             return rn;
     *         } catch (Exception e) {
     *             throw new RuntimeException(e);
     *         }
     *     }
     */


    /** 按天分组
     * {
     *         BoolQueryBuilder boolQuery = ElasticUtil.buildBoolQueryBuilder(esQueryVo);
     *         SearchSourceBuilder searchSourceBuilder = ElasticUtil.initSearchSourceBuilder(boolQuery, esQueryVo);
     *         SearchRequest request = new SearchRequest(esQueryVo.getIdxName());
     *         searchSourceBuilder.size(0);
     *         AggregationBuilder aggregation = AggregationBuilders.terms("fieldTypes").field(fieldType).size(1000)//group 条件
     *                 .subAggregation(AggregationBuilders.dateHistogram("alarmTimes").field("alarmTime") //按天分组
     *                         .fixedInterval(DateHistogramInterval.DAY)
     *                         .minDocCount(0)
     *                         .timeZone(ZoneId.of("Asia/Shanghai"))
     *                         .subAggregation(AggregationBuilders.terms("alarmTypes").field("alarmType").size(128)//再按其他条件分组
     *                                 .subAggregation(AggregationBuilders.terms("deviceIds").field("deviceId").size(8000)))
     *                 );
     *         searchSourceBuilder.aggregation(aggregation);
     *         request.source(searchSourceBuilder);
     *         try {
     *             SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
     *             if (response == null) {
     *                 return Collections.emptyMap();
     *             }
     *             // 时间聚合
     *             Terms fieldTypeTerms = response.getAggregations().get("fieldTypes");
     *             List<? extends Terms.Bucket> fieldBuckets = fieldTypeTerms.getBuckets();
     *             if (fieldBuckets.isEmpty()) {
     *                 return Collections.emptyMap();
     *             }
     *             Map<String, Map<String, Integer>> map = new HashMap<>(fieldBuckets.size());
     *             for (Terms.Bucket fieldBucket : fieldBuckets) {
     *                 String fieldId = fieldBucket.getKeyAsString();
     *                 ParsedDateHistogram alarmTimes = fieldBucket.getAggregations().get("alarmTimes");
     *                 List<? extends Histogram.Bucket> alarmTimeBuckets = alarmTimes.getBuckets();
     *                 if (alarmTimeBuckets.isEmpty()) {
     *                     map.put(fieldId, Collections.emptyMap());
     *                 } else {
     *                     Map<String, Integer> alarmTypesMap = new HashMap<>();
     *                     for (Histogram.Bucket alarmTime : alarmTimeBuckets) {
     *                         Terms alarmTypes = alarmTime.getAggregations().get("alarmTypes");
     *                         List<? extends Terms.Bucket> alarmTypesBuckets = alarmTypes.getBuckets();
     *                         if (!alarmTypesBuckets.isEmpty()) {
     *                             for (Terms.Bucket alarmType : alarmTypesBuckets) {
     *                                 String alarmTypeCode = alarmType.getKey().toString();
     *                                 Integer count = alarmTypesMap.get(alarmTypeCode) != null ? alarmTypesMap.get(alarmTypeCode) : 0;
     *                                 Terms deviceIds = alarmType.getAggregations().get("deviceIds");
     *                                 alarmTypesMap.put(alarmTypeCode, count + deviceIds.getBuckets().size());
     *                             }
     *                         }
     *                     }
     *                     map.put(fieldId, alarmTypesMap);
     *                 }
     *             }
     *             return map;
     *         } catch (Exception e) {
     *             throw new RuntimeException(e);
     *         }
     *     }
     */
}
