package com.appril.service.impl;

import com.appril.entity.Article;
import com.appril.repository.ArticleRepository;
import com.appril.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Article article) {
        mongoTemplate.save(article);
    }

    @Override
    public void batchSave(List<Article> articles) {
        mongoTemplate.insertAll(articles);
    }

    @Override
    public List<Article> search(Article article) {
        //根据作者查询所有符合条件的数据，返回List
        Query query = Query.query(Criteria.where("author").is(article.getAuthor()));
        return mongoTemplate.find(query,Article.class);
    }

    @Override
    public void update(Article article) {
        /*//修改author 为appril 的所有文章，将title 变更为 MongoTemplate，将 visitCount 变更为 10
        Query query = Query.query(Criteria.where("author").is("appril"));
        Update update = Update.update("title", "MongoTemplate").set("visitCount", 10);*/
        Query query = Query.query(Criteria.where("author").is("xialuo").andOperator(Criteria.where("tags").size(4)));
        Update update = Update.update("visitCount", 1479870).pull("tags", "Blog");
        mongoTemplate.updateMulti(query, update, Article.class);
    }

    @Override
    public void delete(Article article) {
        //title
        Query query = Query.query(Criteria.where("title").is(article.getTitle()));
        mongoTemplate.remove(query, Article.class);
    }
}
