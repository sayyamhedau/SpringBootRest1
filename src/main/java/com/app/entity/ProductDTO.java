package com.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "PRODUCT_DETAILS_TBL")
@Audited
@XmlRootElement
public class ProductDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PRODUCT_ID")
	private Long id;

	@NonNull
	@Column(name = "PRODUCT_CODE")
	private String productCode;

	@NonNull
	@Column(name = "PRODUCT_NAME")
	private String productName;

	@NonNull
	@Column(name = "PRODUCT_TYPE")
	private String productType;
	
	@NonNull
	@Column(name = "PRODUCT_COST")
	private Double productCost;
	
	@NonNull
	@Column(name = "PRODUCT_DISCOUNT")
	private Double productDiscount;
	
	@NonNull
	@Column(name = "PRODUCT_GST")
	private Double productGst;
	
	@NonNull
	@Column(name = "PRODUCT_QUANTITY")
	private Integer productQuantity;
}
