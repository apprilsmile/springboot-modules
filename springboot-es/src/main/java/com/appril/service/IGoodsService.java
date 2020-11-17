package com.appril.service;

import com.appril.entity.Goods;

import java.util.List;

public interface IGoodsService {
    void save(Goods goods);

    void batchSave(List<Goods> goodsList);

    void search();

    void update();

    void delete();
}
