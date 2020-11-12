package com.appril.service;

import com.appril.entity.Article;

import java.util.List;

public interface IArticleService {
    void save(Article article);

    void batchSave(List<Article> articles);

    List<Article> search(Article article);

    void update(Article article);

    void delete(Article article);
}
