package com.ebookstore.microservices.priceservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebookstore.microservices.priceservice.models.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>{

}
