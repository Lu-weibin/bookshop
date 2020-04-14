package com.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String category;

    /**
     * 该分类下图书数量
     */
    private Long totalCount;

    public Category(Integer id) {
        this.id = id;
    }

}
