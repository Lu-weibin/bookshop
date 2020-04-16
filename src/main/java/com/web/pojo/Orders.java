package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Entity
@Data
@ToString
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderNumber;

    private Integer userId;

    private BigDecimal payPrice;

    /**
     * 收件人
     */
    private String addressee;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp payTime;

    private Integer state;

    private String phone;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

}
