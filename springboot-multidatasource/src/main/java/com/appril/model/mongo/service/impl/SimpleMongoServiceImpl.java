package com.appril.model.mongo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appril.model.mongo.service.SimpleMongoService;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.apache.poi.ss.formula.functions.T;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2023/10/7 18:15
 */
@Service
public class SimpleMongoServiceImpl implements SimpleMongoService<T> {

    /**
     * 注入template，减少重复代码
     */
    @Resource
    private MongoTemplate       mongoTemplate;

    private static final String ID_STR = "id";

    /**
     * 功能描述: 创建一个集合
     * 同一个集合中可以存入多个不同类型的对象，我们为了方便维护和提升性能，
     * 后续将限制一个集合中存入的对象类型，即一个集合只能存放一个类型的数据
     *
     * @param name 集合名称，相当于传统数据库的表名
     * @since: v1.0
     * @Date: 2019/3/20 17:27
     */
    @Override
    public void createCollection(String name) {
        mongoTemplate.createCollection(name);
    }

    /**
     * 功能描述: 创建索引
     * 索引是顺序排列，且唯一的索引
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     * @param filedName      对象中的某个属性名
     * @return java.lang.String
     * @since: v1.0
     * @Date: 2019/3/20 16:13
     */
    @Override
    public String createIndex(String collectionName, String filedName) {
        //配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置为唯一
        options.unique(true);
        //创建按filedName升序排的索引
        return mongoTemplate.getCollection(collectionName).createIndex(
            Indexes.ascending(filedName), options);
    }

    /**
     * 功能描述: 获取当前集合对应的所有索引的名称
     *
     * @param collectionName
     * @return java.util.List<java.lang.String>
     * @since: v1.0
     * @Date: 2019/3/20 16:46
     */
    @Override
    public List<String> getAllIndexes(String collectionName) {
        ListIndexesIterable<Document> list = mongoTemplate.getCollection(collectionName).listIndexes();
        //上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
        List<String> indexes = new ArrayList<>();
        for (Document document : list) {
            document.entrySet().forEach((key) -> {
                //提取出索引的名称
                if ("name".equals(key.getKey())) {
                    indexes.add(key.getValue().toString());
                }
            });
        }
        return indexes;
    }

    /**
     * 功能描述: 往对应的集合中插入一条数据
     *
     * @param info           存储对象
     * @param collectionName 集合名称
     * @return:T
     * @since: v1.0
     */
    @Override
    public T insertObject(T info, String collectionName) {
        return mongoTemplate.insert(info, collectionName);
    }

    /**
     * 功能描述: 往对应的集合中插入一条数据
     *
     * @param info           存储对象
     * @param collectionName 集合名称
     * @since: v1.0
     * @Date: 2019/3/20 16:46
     */
    @Override
    public void insert(T info, String collectionName) {

        mongoTemplate.insert(info, collectionName);

    }

    /**
     * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
     *
     * @param infos 对象列表
     * @since: v1.0
     * @Date: 2019/3/20 16:47
     */
    @Override
    public void insertMulti(List<T> infos, String collectionName) {
        mongoTemplate.insert(infos, collectionName);
    }

    /**
     * 功能描述: 使用索引信息精确更改某条数据
     *
     * @param id             唯一键
     * @param collectionName 集合名称
     * @param info           待更新的内容
     * @since: v1.0
     * @Date: 2019/3/20 18:42
     */
    @Override
    public void updateById(String id, String collectionName, T info) {
        Query query = new Query(Criteria.where(ID_STR).is(id));
        Update update = new Update();
        String str = JSON.toJSONString(info);
        JSONObject jQuery = JSON.parseObject(str);
        jQuery.forEach((key, value) -> {
            //因为id相当于传统数据库中的主键，这里使用时就不支持更新，所以需要剔除掉
            if (!ID_STR.equals(key)) {
                update.set(key, value);
            }
        });
        mongoTemplate.updateMulti(query, update, info.getClass(), collectionName);
    }

    /**
     * 功能描述: 根据id删除集合中的内容
     *
     * @param id             序列id
     * @param collectionName 集合名称
     * @param clazz          集合中对象的类型
     * @since: v1.0
     * @Date: 2019/3/20 16:47
     */
    @Override
    public void deleteById(String id, Class<T> clazz, String collectionName) {
        // 设置查询条件，当id=#{id}
        Query query = new Query(Criteria.where(ID_STR).is(id));
        // mongodb在删除对象的时候会判断对象类型，如果你不传入对象类型，只传入了集合名称，它是找不到的
        // 上面我们为了方便管理和提升后续处理的性能，将一个集合限制了一个对象类型，所以需要自行管理一下对象类型
        // 在接口传入时需要同时传入对象类型
        mongoTemplate.remove(query, clazz, collectionName);
    }

    /**
     * 功能描述: 根据id查询信息
     *
     * @param id             注解
     * @param clazz          类型
     * @param collectionName 集合名称
     * @return java.util.List<T>
     * @since: v1.0
     * @Date: 2019/3/20 16:47
     */
    @Override
    public T selectById(String id, Class<T> clazz, String collectionName) {
        // 查询对象的时候，不仅需要传入id这个唯一键，还需要传入对象的类型，以及集合的名称
        return mongoTemplate.findById(id, clazz, collectionName);
    }

    /**
     * 功能描述: 查询列表信息
     * 将集合中符合对象类型的数据全部查询出来
     *
     * @param collectName 集合名称
     * @param clazz       类型
     * @return java.util.List<T>
     * @since: v1.0
     * @Date: 2019/3/21 10:38
     */
    @Override
    public List<T> selectList(String collectName, Class<T> clazz) {
        return selectList(collectName, clazz, null, null);
    }

    /**
     * 条件查询
     *
     * @param collectName 集合名称
     * @param clazz       类型
     * @param conditions  查询条件，目前查询条件处理的比较简单，仅仅做了相等匹配，没有做模糊查询等复杂匹配
     * @return List<T>
     */
    @Override
    public List<T> selectListByCondition(String collectName, Class<T> clazz, Map<String, String> conditions) {
        if (ObjectUtils.isEmpty(conditions)) {
            return selectList(collectName, clazz, null, null);
        } else {
            //设置分页参数
            Query query = new Query();
            // 往query中注入查询条件
            conditions.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));

            return mongoTemplate.find(query, clazz, collectName);
        }
    }

    /**
     * 功能描述: 分页查询列表信息
     *
     * @param collectName 集合名称
     * @param clazz       对象类型
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     * @return java.util.List<T>
     * @since: v1.0
     * @Date: 2019/3/21 10:38
     */
    @Override
    public List<T> selectList(String collectName, Class<T> clazz, Integer currentPage,
                              Integer pageSize) {
        //设置分页参数
        Query query = new Query();
        //设置分页信息
        if (!ObjectUtils.isEmpty(currentPage) && !ObjectUtils.isEmpty(pageSize)) {
            query.limit(pageSize);
            query.skip((long) pageSize * (currentPage - 1));
        }
        return mongoTemplate.find(query, clazz, collectName);
    }

    /**
     * selectList
     *
     * @param collectName collectName
     * @param clazz       clazz
     * @param currentPage currentPage
     * @param pageSize    pageSize
     * @param field       field
     * @return List<T>
     */
    @Override
    public List<T> selectList(String collectName, Class<T> clazz, Integer currentPage,
                              Integer pageSize, String field) {
        //设置分页参数
        Query query = new Query();
        //设置分页信息
        if (!ObjectUtils.isEmpty(currentPage) && !ObjectUtils.isEmpty(pageSize)) {
            query.limit(pageSize);
            query.skip((long) pageSize * (currentPage - 1));
        }
        if (!StringUtils.isEmpty(field)) {
            query.with(Sort.by(Sort.Direction.DESC, field));
        }
        return mongoTemplate.find(query, clazz, collectName);
    }

    /**
     * selectByCondition
     *
     * @param collectName 集合名称
     * @param conditions  查询条件，目前查询条件处理的比较简单，仅仅做了相等匹配，没有做模糊查询等复杂匹配
     * @param clazz       对象类型
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     * @param field       倒叙排列字段
     * @return List<T>
     */
    @Override
    public List<T> selectByCondition(String collectName, Map<String, String> conditions, Class<T> clazz, Integer currentPage, Integer pageSize, String field) {
        if (ObjectUtils.isEmpty(conditions)) {
            return selectList(collectName, clazz, currentPage, pageSize);
        } else {
            //设置分页参数
            Query query = new Query();
            query.limit(pageSize);
            query.skip((long) (currentPage - 1) * pageSize);
            if (!StringUtils.isEmpty(field)) {
                query.with(Sort.by(Sort.Direction.DESC, field));
            }
            // 往query中注入查询条件
            conditions.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));

            return mongoTemplate.find(query, clazz, collectName);
        }
    }

    /**
     * selectOneByCondition
     *
     * @param collectName  集合名称
     * @param clazz        对象类型
     * @param conditionMap 查询条件
     * @return T
     */
    @Override
    public T selectOneByCondition(String collectName, Class<T> clazz, Map<String, String> conditionMap) {
        Query query = new Query();
        conditionMap.forEach((k, v) -> {
            query.addCriteria(Criteria.where(k).is(v));
        });

        return mongoTemplate.findOne(query, clazz, collectName);
    }

    /**
     * count
     *
     * @param collectName collectName
     * @return long
     */
    @Override
    public long count(String collectName) {
        return mongoTemplate.getCollection(collectName).estimatedDocumentCount();
    }

    /**
     * getByDistinct
     *
     * @param collectName collectName
     * @param resultClass resultClass
     * @param fieldName   fieldName
     * @return List<T>
     */
    @Override
    public List<T> getByDistinct(String collectName, Class<T> resultClass, String fieldName) {
        DistinctIterable<T> distinct = mongoTemplate.getCollection(collectName).distinct(fieldName,
            resultClass);
        MongoCursor<T> iterator = distinct.iterator();
        List<T> myList = new ArrayList<>();
        while (iterator.hasNext()) {
            myList.add(iterator.next());
        }
        return myList;
    }

    /**
     * selectByCondition
     *
     * @param collectName collectName
     * @param clazz       clazz
     * @param conditionIn conditionIn
     * @param sortField   sortField
     * @return List<T>
     */
    @Override
    public List<T> selectByCondition(String collectName, Class<T> clazz,
                                     Map<String, List<String>> conditionIn,
                                     String sortField) {
        //设置分页参数
        Query query = new Query();

        if (!StringUtils.isEmpty(sortField)) {
            query.with(Sort.by(Sort.Direction.DESC, sortField));
        }

        if (!ObjectUtils.isEmpty(conditionIn)) {
            conditionIn.forEach((key, value) -> query.addCriteria(Criteria.where(key).in(value)));
        }

        return mongoTemplate.find(query, clazz, collectName);
    }
}
