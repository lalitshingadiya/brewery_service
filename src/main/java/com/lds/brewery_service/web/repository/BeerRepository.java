package com.lds.brewery_service.web.repository;

import com.lds.brewery_service.web.entity.Beer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {
}
