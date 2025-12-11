package com.ecs.ecs_product.controller;


import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.dto.SearchFilters;
import com.ecs.ecs_product.dto.SearchResultDto;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.entity.TrendingSearch;
import com.ecs.ecs_product.service.TrendingSearchService;
import com.ecs.ecs_product.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SearchController {
    private final IProductService productService;
    private final TrendingSearchService trendingSearchService;

    @GetMapping("/{keyword}")
    public ResponseEntity<List<SearchResultDto>> getSearchSuggestions(@PathVariable("keyword") String keyword) {
        List<SearchResultDto> results = productService.getSearchSuggestions(keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/globalSearch")
    public ResponseEntity<Page<ProductFinalDto>> globalSearchProducts(
            @RequestParam(name = "searchQuery") String keyword,
            @RequestParam(name = "categories", required = false) List<Integer> categories,
            @RequestParam(name = "subCategories", required = false) List<Integer> subCategories,
            @RequestParam(name = "brands", required = false) List<Integer> brands,
            @RequestParam(name = "condition", required = false) List<String> condition,
            @RequestParam(name = "colors", required = false) List<String> colors,
            @RequestParam(name = "minRating", required = false) Float minRating,
            @RequestParam(name = "minDiscount", required = false) Integer minDiscount,
            @RequestParam(name = "priceRange", required = false) List<Integer> priceRange,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SearchFilters filters = new SearchFilters();
        filters.setKeyword(keyword);
        filters.setCategories(categories);
        filters.setSubCategories(subCategories);
        filters.setBrands(brands);
        filters.setCondition(condition);
        filters.setColors(colors);
        filters.setMinRating(minRating);
        filters.setMinDiscount(minDiscount);
        filters.setPriceRange(priceRange);
        filters.setSortBy(sortBy);

        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            pageable = PageRequest.of(currentPage, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(currentPage, pageSize);
        }
        Page<ProductFinalDto> results = productService.globalSearchProducts(filters, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/getTrendingSearch")
    public ResponseEntity<?> getTrendingSearch(@RequestParam(defaultValue = "10") Integer limit){
        List<TrendingSearch> res =  trendingSearchService.getTrendingSearches(limit);
        if(res != null && !res.isEmpty()) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }
}
