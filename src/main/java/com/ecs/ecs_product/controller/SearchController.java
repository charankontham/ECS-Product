package com.ecs.ecs_product.controller;


import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.dto.SearchResultDto;
import com.ecs.ecs_product.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SearchController {
    private final IProductService productService;

    @GetMapping("/{keyword}")
    public ResponseEntity<List<SearchResultDto>> getSearchSuggestions(@PathVariable("keyword") String keyword) {
        List<SearchResultDto> results = productService.getSearchSuggestions(keyword);
        return ResponseEntity.ok(results);
    }
}
