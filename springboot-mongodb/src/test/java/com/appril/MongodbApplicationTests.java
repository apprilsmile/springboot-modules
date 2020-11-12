package com.appril;

import com.alibaba.fastjson.JSONObject;
import com.appril.entity.Article;
import com.appril.service.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MongodbApplicationTests {


    @Resource
    private IArticleService articleService;

    @Test
    void save(){
        Article article = new Article();
        article.setTitle("博客园");
        article.setAuthor("xialuo");
        article.setUrl("https://www.cnblogs.com/");
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add(".Net");
        tags.add("C#");
        tags.add("Blog");
        article.setTags(tags);
        article.setAddTime(new Date());
        article.setVisitCount(18448484L);
        articleService.save(article);
    }

    @Test
    void batchSave(){
        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        article.setTitle("CSDN");
        article.setAuthor("appril");
        article.setUrl("https://blog.csdn.net/");
        List<String> tags = new ArrayList<>();
        tags.add("Python");
        tags.add("区块链");
        tags.add("大数据");
        tags.add("人工智能");
        tags.add("移动开发");
        tags.add("游戏开发");
        article.setTags(tags);
        article.setAddTime(new Date());
        article.setVisitCount(1024L);
        articles.add(article);

        article = new Article();
        article.setTitle("OSCHINA");
        article.setAuthor("appril");
        article.setUrl("https://www.oschina.net/");
        tags = new ArrayList<>();
        tags.add("云计算");
        tags.add("程序人生");
        tags.add("信息安全");
        tags.add("DevOps");
        article.setTags(tags);
        article.setAddTime(new Date());
        article.setVisitCount(4984848L);
        articles.add(article);

        article = new Article();
        article.setTitle("码云");
        article.setAuthor("xialuo");
        article.setUrl("https://gitee.com/");
        tags = new ArrayList<>();
        tags.add("开源软件");
        tags.add("微信开发");
        tags.add("建站系统");
        article.setTags(tags);
        article.setAddTime(new Date());
        article.setVisitCount(8764498L);
        articles.add(article);

        articleService.batchSave(articles);
    }

    @Test
    void search(){
        Article article = new Article();
        article.setAuthor("appril");
        List<Article> articles = articleService.search(article);
        System.out.println(JSONObject.toJSONString(articles));
    }

    @Test
    void update(){
        articleService.update(null);
        Article article = new Article();
        article.setAuthor("xialuo");
        List<Article> articles = articleService.search(article);
        System.out.println(JSONObject.toJSONString(articles));
    }

    @Test
    void delete(){
        Article article = new Article();
        article.setTitle("博客园");
        articleService.delete(article);
    }

}
