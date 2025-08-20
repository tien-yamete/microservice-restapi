package com.tien.productservice.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.tien.productservice.entity.Product;

public class ProductSpecifications {

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like),
                    cb.like(cb.lower(root.get("sku")), like));
        };
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Product> hasBrandId(Long brandId) {
        return (root, query, cb) -> {
            if (brandId == null) return null;
            return cb.equal(root.get("brand").get("id"), brandId);
        };
    }

    public static Specification<Product> priceGte(BigDecimal min) {
        return (root, query, cb) -> {
            if (min == null) return null;
            return cb.greaterThanOrEqualTo(root.get("price"), min);
        };
    }

    public static Specification<Product> priceLte(BigDecimal max) {
        return (root, query, cb) -> {
            if (max == null) return null;
            return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    public static Specification<Product> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return null;
            return cb.equal(root.get("active"), active);
        };
    }
}
