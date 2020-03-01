package com.web.pojo;

import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Entity
@Data
@ToString
public class Category implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String category;

}
