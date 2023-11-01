package com.appril.model.mongo.service;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * SimpleMongoService
 */
public interface SimpleMongoService<T> {
    /**
     * 功能描述: 创建一个集合
     * 同一个集合中可以存入多个不同类型的对象，我们为了方便维护和提升性能，
     * 后续将限制一个集合中存入的对象类型，即一个集合只能存放一个类型的数据
     *
     * @param name 集合名称，相当于传统数据库的表名
     */
    void createCollection(String name);

    /**
     * 功能描述: 创建索引
     * 索引是顺序排列，且唯一的索引
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     * @param filedName      对象中的某个属性名
     * @return java.lang.String
     */
    String createIndex(String collectionName, String filedName);

    /**
     * 功能描述: 获取当前集合对应的所有索引的名称
     *
     * @param collectionName
     * @return java.util.List<java.lang.String>
     */
    List<String> getAllIndexes(String collectionName);

    /**
     * 功能描述: 往对应的集合中插入一条数据
     *
     * @param info           存储对象
     * @param collectionName 集合名称
     * @return T
     */
    T insertObject(T info, String collectionName);

    /**
     * 功能描述: 往对应的集合中插入一条数据
     *
     * @param info           存储对象
     * @param collectionName 集合名称
     */
    void insert(T info, String collectionName);

    /**
     * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
     *
     * @param infos 对象列表
     * @param collectionName 集合名称
     */
    void insertMulti(List<T> infos, String collectionName);

    /**
     * 功能描述: 使用索引信息精确更改某条数据
     *
     * @param id             唯一键
     * @param collectionName 集合名称
     * @param info           待更新的内容
     */
    void updateById(String id, String collectionName, T info);

    /**
     * 功能描述: 根据id删除集合中的内容
     *
     * @param id             序列id
     * @param collectionName 集合名称
     * @param clazz          集合中对象的类型
     */
    void deleteById(String id, Class<T> clazz, String collectionName);

    /**
     * 功能描述: 根据id查询信息
     *
     * @param id             注解
     * @param clazz          类型
     * @param collectionName 集合名称
     * @return java.util.List<T>
     */
    T selectById(String id, Class<T> clazz, String collectionName);

    /**
     * 功能描述: 查询列表信息
     * 将集合中符合对象类型的数据全部查询出来
     *
     * @param collectName 集合名称
     * @param clazz       类型
     * @return java.util.List<T>
     */
    List<T> selectList(String collectName, Class<T> clazz);

    /**
     * 功能描述: 根据条件查询列表信息
     * 将集合中符合对象类型的数据全部查询出来
     *
     * @param collectName 集合名称
     * @param clazz       类型
     * @param conditions  查询条件，目前查询条件处理的比较简单，仅仅做了相等匹配，没有做模糊查询等复杂匹配
     * @return java.util.List<T>
     */
    List<T> selectListByCondition(String collectName, Class<T> clazz, Map<String, String> conditions);

    /**
     * 功能描述: 分页查询列表信息
     *
     * @param collectName 集合名称
     * @param clazz       对象类型
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     * @return java.util.List<T>
     */
    List<T> selectList(String collectName, Class<T> clazz, Integer currentPage, Integer pageSize);

    /**
     * 功能描述: 分页排序查询列表信息
     *
     * @param collectName
     * @param clazz
     * @param currentPage
     * @param pageSize
     * @param field
     * @return List<T>
     */
    List<T> selectList(String collectName, Class<T> clazz, Integer currentPage, Integer pageSize,
                       String field);

    /**
     * 根据条件查询单笔记录
     *
     * @param collectName  集合名称
     * @param conditionMap 查询条件
     * @param clazz        对象类型
     * @return java.util.List<T>
     */
    T selectOneByCondition(String collectName, Class<T> clazz, Map<String, String> conditionMap);

    /**
     * 根据条件查询集合
     *
     * @param collectName 集合名称
     * @param conditions  查询条件，目前查询条件处理的比较简单，仅仅做了相等匹配，没有做模糊查询等复杂匹配
     * @param clazz       对象类型
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     * @param field       倒叙排列字段
     * @return java.util.List<T>
     */
    List<T> selectByCondition(String collectName, Map<String, String> conditions, Class<T> clazz,
                              Integer currentPage, Integer pageSize, String field);

    /**
     * 统计数量
     *
     * @param collectName collectName
     * @return 统计数量
     */
    long count(String collectName);

    /**
     * 根据字段查询过滤出不重复的数据
     *
     * @param collectName collectName
     * @param resultClass resultClass
     * @param fieldName fieldName
     * @return List<T>
     */
    List<T> getByDistinct(String collectName, Class<T> resultClass, String fieldName);

    /**
     * 条件查询
     * @param collectName  collectName
     * @param clazz clazz
     * @param conditionIn conditionIn
     * @param sortField sortField
     * @return List<T>
     */
    List<T> selectByCondition(String collectName, Class<T> clazz,
                              Map<String, List<String>> conditionIn, String sortField);
}
