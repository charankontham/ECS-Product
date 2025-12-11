package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.dto.SearchFilters;
import com.ecs.ecs_product.dto.SearchResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISearchService {

    List<SearchResultDto> getSearchSuggestions(String searchValue);

    Page<ProductFinalDto> globalSearchProducts(SearchFilters searchFilters, Pageable pageable);
}
