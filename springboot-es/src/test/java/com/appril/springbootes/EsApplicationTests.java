package com.appril.springbootes;

import com.appril.entity.Goods;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EsApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Test
    void EsTest()throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Goods> goodsList = new ArrayList<>();
        Goods goods1 = Goods.builder().id(1L).name("iphone12").title("iphone").price(new BigDecimal("5400")).publishDate(sdf.parse("2020-10-10").getTime()).build();
        Goods goods2 = Goods.builder().id(2L).name("Redmi 10X").title("小米").price(new BigDecimal("1499")).publishDate(sdf.parse("2019-12-10").getTime()).build();
        Goods goods3 = Goods.builder().id(3L).name("小米10 青春版").title("小米").price(new BigDecimal("1999")).publishDate(sdf.parse("2020-11-11").getTime()).build();
        Goods goods4 = Goods.builder().id(4L).name("nova 8 SE").title("华为").price(new BigDecimal("2699")).publishDate(sdf.parse("2020-02-10").getTime()).build();
        Goods goods5 = Goods.builder().id(5L).name("HUAWEI Mate40").title("华为").price(new BigDecimal("6499")).publishDate(sdf.parse("2020-09-25").getTime()).build();
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);
        goodsList.add(goods4);
        goodsList.add(goods5);
        restTemplate.save(goodsList);
    }

    @Test
    void search(){
        List<String> titles = new ArrayList<>();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        titles.add("华为");
        titles.add("小米");
       // BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("title", titles)).should(QueryBuilders.matchQuery("title", "iphone"));
//        BoolQueryBuilder titleQuery = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("title", titles));
//        queryBuilder.withQuery(QueryBuilders.matchQuery("title","华为"));
        TermsQueryBuilder titleQuery = QueryBuilders.termsQuery("title", titles);
        queryBuilder.withQuery(QueryBuilders.matchAllQuery());
        SearchHits<Goods> search = restTemplate.search(queryBuilder.build(), Goods.class);
        search.get().forEach( e-> System.out.println(e.getContent().toString()));
    }

    @Test
    void searchSpecial(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("title", titles)).should(QueryBuilders.matchQuery("title", "iphone"));
        queryBuilder.withQuery(QueryBuilders.matchAllQuery());
        //桶聚合查询
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("brand").field("title.keyword")////以title分组(必须加.keyword)，组名为：brand
                .subAggregation(AggregationBuilders.terms("name").field("name.keyword")
//                        .subAggregation(AggregationBuilders.terms("publishDateCollection").field("publishDate"))
                );
        FieldSortBuilder publishDateSort = SortBuilders.fieldSort("publishDate").order(SortOrder.DESC);//倒序
        queryBuilder.addAggregation(aggregation);
//        queryBuilder.withSort(publishDateSort);
//        queryBuilder.withPageable(PageRequest.of(1,2));//分页查询
        /*SearchHits<Goods> searchResult = restTemplate.search(queryBuilder.build(), Goods.class);
        Aggregations aggregations = searchResult.getAggregations();
        Terms brand = aggregations.get("brand");//指定返回类型为Aggregation的实现类Terms,否则无法遍历
        for(Terms.Bucket term:brand.getBuckets()) {
            System.out.println(term.getKey()+"  "+term.getDocCount());
            Aggregations nameAggregations= term.getAggregations();
            Terms name =   nameAggregations.get("name");
            for(Terms.Bucket nameTerm:name.getBuckets()) {
                System.out.println(nameTerm.getKey() + "  " + nameTerm.getDocCount());
            }
        }*/
        queryBuilder = new NativeSearchQueryBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("title", titles)).should(QueryBuilders.matchQuery("title", "iphone"));
        queryBuilder.withQuery(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder sumAggregation = AggregationBuilders.terms("brand").field("title.keyword") .order(BucketOrder.compound(BucketOrder.aggregation("sum_price",true)));//按照sum_price 升序;
        SumAggregationBuilder sumPrice = AggregationBuilders.sum("sum_price").field("price");
        sumAggregation.subAggregation(sumPrice);
        queryBuilder.addAggregation(sumAggregation);
//        queryBuilder.withSort(publishDateSort);
//        queryBuilder.withPageable(PageRequest.of(1,2));//分页查询
        SearchHits<Goods> searchResult = restTemplate.search(queryBuilder.build(), Goods.class);
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
//        searchResult.forEach(e->System.out.println(e.getContent().toString()));
    }

}
