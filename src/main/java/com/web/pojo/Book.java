package com.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Book implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String bookName;

	@ManyToOne
	@JoinColumn(name = "categoryId",referencedColumnName = "id")
	private Category category;

	private BigDecimal price;

	private String author;

	private String publisher;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Timestamp publishTime;

	private String description;

	private String picture;

	/**
	 * 品相（书的新旧程度）
	 */
	private String conditions;

	private Integer state;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "userId",referencedColumnName = "id")
	private User user;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Timestamp createTime;

}
