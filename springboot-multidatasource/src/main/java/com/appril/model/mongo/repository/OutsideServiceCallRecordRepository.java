/*
 * Licensed to the Wiseco Software Corporation under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appril.model.mongo.repository;

import com.appril.model.mongo.entity.OutsideServiceCallRecord;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * 外数调用记录操作repository
 *
 * @author yangyunsen
 * @date 2022/1/12
 */
public interface OutsideServiceCallRecordRepository
                                                   extends
                                                   MongoRepository<OutsideServiceCallRecord, String> {

    /**
     * insert
     *
     * @param entity
     * @return OutsideServiceCallRecord
     */
    @Override
    OutsideServiceCallRecord insert(OutsideServiceCallRecord entity);

    /**
     * 查询有效未过期缓存数据
     *
     * @param outsideServiceId
     * @param paramHash
     * @param hitCache
     * @param respTimeOldest   最早可用数据的截止时间
     * @return OutsideServiceCallRecord
     */
    OutsideServiceCallRecord findFirstByOutsideServiceIdAndParamHashAndHitCacheAndRespTimeAfter(Long outsideServiceId,
                                                                                                String paramHash,
                                                                                                Integer hitCache,
                                                                                                Date respTimeOldest);

    /**
     * 查询有效未过期缓存数据
     *
     * @param outsideServiceId
     * @param paramHash
     * @param hitCache
     * @param callSuccess
     * @param businessSuccess
     * @param respTimeOldest   最早可用数据的截止时间
     * @return OutsideServiceCallRecord
     */
    OutsideServiceCallRecord findFirstByOutsideServiceIdAndParamHashAndHitCacheAndCallSuccessAndBusinessSuccessAndRespTimeAfter(Long outsideServiceId,
                                                                                                                                String paramHash,
                                                                                                                                Integer hitCache,
                                                                                                                                Integer callSuccess,
                                                                                                                                Integer businessSuccess,
                                                                                                                                Date respTimeOldest);

    /**
     * 查询有效记录无时间限制
     *
     * @param outsideServiceId
     * @param paramHash
     * @param hitCache
     * @param callSuccess
     * @param businessSuccess
     * @return OutsideServiceCallRecord
     */
    OutsideServiceCallRecord findFirstByOutsideServiceIdAndParamHashAndHitCacheAndCallSuccessAndBusinessSuccess(Long outsideServiceId,
                                                                                                                String paramHash,
                                                                                                                Integer hitCache,
                                                                                                                Integer callSuccess,
                                                                                                                Integer businessSuccess);

    /**
     * 按照外部服务 ID, 分组统计外部服务调用记录
     * <p>用 _id 字段保存外部服务 ID</p>
     * <p>用 tryTimes (重试次数) 字段保存调用记录数量</p>
     *
     * @return 统计结果 Map
     * <p>key: 外部服务 ID, value: 策略执行调用的外部服务次数</p>
     */
    @Aggregation(pipeline = { "{$group: {_id: \"$outsideServiceId\", tryTimes: {$sum: 1}}}" })
    List<OutsideServiceCallRecord> findOutsideServiceCallRecord();

    /**
     * 根据决策引擎生成的内部全局流水号查询外部服务调用记录
     *
     * @param decisionSerialNo       决策引擎生成流水号
     * @param outsideServiceSerialNo 外部服务流水号
     * @return 外部服务调用记录
     */
    OutsideServiceCallRecord findByDecisionSerialNoAndOutsideServiceSerialNo(String decisionSerialNo,
                                                                             String outsideServiceSerialNo);
}
