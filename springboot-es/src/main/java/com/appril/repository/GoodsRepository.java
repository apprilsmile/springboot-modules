package com.appril.repository;

import com.appril.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public  interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
