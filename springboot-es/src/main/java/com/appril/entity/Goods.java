package com.appril.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "goods")
public class Goods implements Serializable {
    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;

    @Field
    private BigDecimal price;

    @Field(type = FieldType.Date)
    private Long publishDate;
}

