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
}
