package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.*;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.feign.ProductReviewService;
import com.ecs.ecs_product.mapper.ProductMapper;
import com.ecs.ecs_product.repository.GlobalSearchDao;
import com.ecs.ecs_product.repository.ProductRepository;
import com.ecs.ecs_product.service.interfaces.IProductBrandService;
import com.ecs.ecs_product.service.interfaces.ISearchService;
import com.ecs.ecs_product.service.interfaces.ISubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SearchServiceImpl implements ISearchService {
    private final ProductRepository productRepository;
    private final GlobalSearchDao globalSearchDao;
    private final ISubCategoryService subCategoryService;
    private final IProductBrandService productBrandService;
    private final ProductReviewService productReviewService;

    @Override
    public List<SearchResultDto> getSearchSuggestions(String searchValue) {
        List<SearchResultDto> finalResults = new ArrayList<>();
        finalResults.addAll(globalSearchDao.globalSearch(searchValue, "products"));
        finalResults.addAll(globalSearchDao.globalSearch(searchValue, "categories"));
        finalResults.addAll(globalSearchDao.globalSearch(searchValue, "subcategories"));
        finalResults.addAll(globalSearchDao.globalSearch(searchValue, "brands"));
        for (SearchResultDto dto : finalResults) {
            double score = dto.getRelevanceScore();
            switch (dto.getItemType().toUpperCase()) {
                case "PRODUCT":
                    score *= 1.0;
                    break;
                case "CATEGORY":
                    score *= 0.8;
                    break;
                case "SUBCATEGORY", "BRAND":
                    score *= 0.9;
                    break;
            }
            dto.setRelevanceScore(score);
        }
        finalResults.sort(Comparator.comparingDouble(SearchResultDto::getRelevanceScore).reversed());
        return finalResults.stream().limit(10).toList();
    }

    @Override
    public Page<ProductFinalDto> globalSearchProducts(SearchFilters searchFilters, Pageable pageable) {
        String searchQuery = searchFilters.getKeyword().trim();
        List<Product> allProducts = new ArrayList<>();
        List<ProductWithRelevanceProjection> products = new ArrayList<>();
        if(searchQuery.isEmpty()){
            allProducts = productRepository.findAll();
            allProducts = new ArrayList<>(allProducts.stream()
                    .filter(p -> searchFilters.getCategories() == null || searchFilters.getCategories().isEmpty() || searchFilters.getCategories().contains(p.getProductCategoryId()))
                    .filter(p -> searchFilters.getSubCategories() == null || searchFilters.getSubCategories().isEmpty() || searchFilters.getSubCategories().contains(p.getSubCategoryId()))
                    .filter(p -> searchFilters.getBrands() == null || searchFilters.getBrands().isEmpty() || searchFilters.getBrands().contains(p.getProductBrandId()))
                    .filter(p -> searchFilters.getPriceRange() == null ||
                            (p.getProductPrice() >= searchFilters.getPriceRange().get(0) && p.getProductPrice() <= searchFilters.getPriceRange().get(1)))
                    .filter(p -> searchFilters.getMinRating() == null || getProductAvgReview(p.getProductId()) >= searchFilters.getMinRating())
                    .filter(p -> searchFilters.getColors() == null || searchFilters.getColors().isEmpty() ||
                            searchFilters.getColors().stream().anyMatch(color -> color.equalsIgnoreCase(p.getProductColor())))
                    .filter(p -> searchFilters.getCondition() == null || searchFilters.getCondition().isEmpty() ||
                            searchFilters.getCondition().stream().anyMatch(condition -> condition.equalsIgnoreCase(p.getProductCondition())))
                /* .filter(p -> minDiscount == null || p.getProduct().getDiscount() >= minDiscount) */
                    .toList());
        }else{
            products = productRepository.globalSearchProducts(searchFilters.getKeyword());
            products = new ArrayList<>(products.stream()
                    .filter(p -> searchFilters.getCategories() == null || searchFilters.getCategories().isEmpty() || searchFilters.getCategories().contains(p.getProductCategoryId()))
                    .filter(p -> searchFilters.getSubCategories() == null || searchFilters.getSubCategories().isEmpty() || searchFilters.getSubCategories().contains(p.getSubCategoryId()))
                    .filter(p -> searchFilters.getBrands() == null || searchFilters.getBrands().isEmpty() || searchFilters.getBrands().contains(p.getProductBrandId()))
                    .filter(p -> searchFilters.getPriceRange() == null ||
                            (p.getProductPrice() >= searchFilters.getPriceRange().get(0) && p.getProductPrice() <= searchFilters.getPriceRange().get(1)))
                    .filter(p -> searchFilters.getMinRating() == null || getProductAvgReview(p.getProductId()) >= searchFilters.getMinRating())
                    .filter(p -> searchFilters.getColors() == null || searchFilters.getColors().isEmpty() ||
                            searchFilters.getColors().stream().anyMatch(color -> color.equalsIgnoreCase(p.getProductColor())))
                    .filter(p -> searchFilters.getCondition() == null || searchFilters.getCondition().isEmpty() ||
                            searchFilters.getCondition().stream().anyMatch(condition -> condition.equalsIgnoreCase(p.getProductCondition())))
                    /* .filter(p -> minDiscount == null || p.getProduct().getDiscount() >= minDiscount) */
                    .toList());
        }
        if(searchQuery.isEmpty()) {
            if ("low-to-high".equalsIgnoreCase(searchFilters.getSortBy())) {
                allProducts.sort(Comparator.comparing(Product::getProductPrice));
            } else if ("high-to-low".equalsIgnoreCase(searchFilters.getSortBy())) {
                allProducts.sort(Comparator.comparing(Product::getProductPrice, Comparator.reverseOrder()));
            }
         /* else if ("rating".equalsIgnoreCase(searchFilters.getSortBy())) {
             filteredProducts.sort(Comparator.comparing(p -> p.getRating(), Comparator.reverseOrder()));
         }*/
        }else{
            if ("relevance".equalsIgnoreCase(searchFilters.getSortBy())) {
                products.sort(Comparator.comparing(ProductWithRelevanceProjection::getRelevance).reversed());
            }else if ("low-to-high".equalsIgnoreCase(searchFilters.getSortBy())) {
                products.sort(Comparator.comparing(ProductWithRelevanceProjection::getProductPrice));
            } else if ("high-to-low".equalsIgnoreCase(searchFilters.getSortBy())) {
                products.sort(Comparator.comparing(ProductWithRelevanceProjection::getProductPrice, Comparator.reverseOrder()));
            }
        /* else if ("rating".equalsIgnoreCase(searchFilters.getSortBy())) {
            filteredProducts.sort(Comparator.comparing(p -> p.getRating(), Comparator.reverseOrder()));
        } */
        }

        int listSize = searchQuery.isEmpty() ? allProducts.size() : products.size();
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), listSize );
        List<ProductFinalDto> finalMappedResults;
        Map<String, ?> pageC = new HashMap<>();
        if(searchQuery.isEmpty()){
            allProducts = (0 <= start && start <= end && end <= listSize) ?
                    new ArrayList<>(allProducts.subList(start, end)) : new ArrayList<>();
            finalMappedResults = allProducts.stream()
                    .map(p -> ProductMapper.mapToProductFinalDto(p, subCategoryService, productBrandService)).toList();
        }else{
            products = (0 <= start && start <= end && end <= listSize) ?
                    new ArrayList<>(products.subList(start, end)) : new ArrayList<>();
            finalMappedResults = products.stream()
                    .map(p -> ProductMapper.mapToProductFinalDto(p, subCategoryService, productBrandService)).toList();
        }
        pageC.put("products", finalMappedResults);

        return new PageImpl<>(pageC, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), listSize);
    }

    private Float getProductAvgReview(Integer productId) {
        List<ProductReviewDto> result = productReviewService.getProductReviewsByProductId(productId).getBody();
        assert result != null;
        if(result.isEmpty()){
            return 0f;
        }
        return (float) (result.stream().mapToInt(ProductReviewDto::getProductRating).sum() / result.size());
    }
}
