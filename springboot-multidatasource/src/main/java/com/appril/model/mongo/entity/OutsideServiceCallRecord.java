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
package com.appril.model.mongo.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * <p>
 * 调用记录表
 * </p>
 *
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "outside_service_call_record")
@CompoundIndexes(@CompoundIndex(name = "compound_cache_index", def = "{'outsideServiceId':1,'paramHash':1,'respTime':1}"))
public class OutsideServiceCallRecord {

    private static final long serialVersionUID = 1L;

    @Id
    private String            id;

    /**
     * 外部服务ID
     */
    @Indexed(direction = IndexDirection.ASCENDING)
    private Long              outsideServiceId;

    /**
     * 外数自己的流水号
     */
    @Indexed(direction = IndexDirection.ASCENDING)
    private String            outsideServiceSerialNo;

    /**
     * 决策流水号
     */
    @Indexed(direction = IndexDirection.ASCENDING)
    private String            decisionSerialNo;

    /**
     * 领域编码
     */
    private String            domainCode;

    /**
     * 领域名称
     */
    private String            domainName;

    /**
     * 调用时间
     */
    @Indexed(direction = IndexDirection.ASCENDING)
    private Date              respTime;

    /**
     * 响应时长
     */
    private Integer           respLength;

    /**
     * 重试次数
     */
    private Integer           tryTimes;

    /**
     * 是否来源于测试:0（策略执行） 1（服务测试） 2（策略测试）3(变量空间)
     */
    private Integer           testSource;

    /**
     * 是否命中缓存:0否 1是
     */
    private Integer           hitCache;

    /**
     * 网络请求状态：0：未请求，1：成功，2：失败
     */
    private Integer           netState;

    /**
     * 查询状态:0失败 1成功
     */
    private Integer           callSuccess;

    /**
     * 是否查得:0否 1是
     */
    private Integer           businessSuccess;

    /**
     * 变量传入字段json对象
     */
    private JSONObject paramJson;

    /**
     * 变量传入json字符串hash值，用于命中索引
     */
    @Indexed(direction = IndexDirection.ASCENDING)
    private String            paramHash;

    /**
     * 响应描述
     */
    private String            respDesc;

    /**
     * 请求报文
     */
    private String            reqMessage;

    /**
     * 响应报文
     */
    private String            respMessage;

    /**
     * 异常堆栈信息记录字段
     */
    private String            exceptionStackTrace;

    @CreatedDate
    private Date              createdTime;

}
