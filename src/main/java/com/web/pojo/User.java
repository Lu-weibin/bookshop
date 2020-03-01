package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 * 用户实体类
 * JsonIgnoreProperties 不加此注解 -> 有属性为空时，Json序列化报错
 */
@Entity
@Data
@ToString
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private String phone;

    private Integer userType;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    private String remarks;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.MERGE)
    private List<Book> books;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.MERGE)
    private List<Address> addresses;

}
