package com.tien.productservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tien.productservice.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);

    Optional<Brand> findByName(String name);
}
