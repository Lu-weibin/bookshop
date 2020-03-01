package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;

    private Integer state;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid",referencedColumnName = "id")
    private User user;

}
