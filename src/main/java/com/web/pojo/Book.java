package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author luwb
 * @date 2020/02/25
 * 图书实体类
 */
@Entity
@Data
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Book implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String bookName;

	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name = "categoryid",referencedColumnName = "id")
	private Category category;

	private BigDecimal price;

	private String author;

	private String publisher;

	private String description;

	private String picture;

	private Integer state;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "userid",referencedColumnName = "id")
	private User user;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createTime;

}
