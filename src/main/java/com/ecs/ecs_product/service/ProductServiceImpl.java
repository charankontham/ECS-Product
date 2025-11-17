package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.*;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.entity.ProductBrand;
import com.ecs.ecs_product.entity.ProductCategory;
import com.ecs.ecs_product.entity.SubCategory;
import com.ecs.ecs_product.exception.ResourceNotFoundException;
import com.ecs.ecs_product.feign.ProductReviewService;
import com.ecs.ecs_product.mapper.ProductMapper;
import com.ecs.ecs_product.repository.*;
import com.ecs.ecs_product.service.interfaces.*;
import com.ecs.ecs_product.util.Constants;
import com.ecs.ecs_product.util.HelperFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final IProductCategoryService productCategoryService;
    private final ISubCategoryService subCategoryService;
    private final IProductBrandService productBrandService;
    private final CheckDependencies checkDependencies;
    private final GlobalSearchDao globalSearchDao;
    private final ProductReviewService productReviewService;

    @Override
    public ProductFinalDto getProduct(Integer productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        return ProductMapper.mapToProductFinalDto(product, subCategoryService, productBrandService);
    }

    @Override
    public List<ProductFinalDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((product) -> ProductMapper.
                mapToProductFinalDto(
                        product,
                        subCategoryService,
                        productBrandService)).toList();
    }

    @Override
    public Page<ProductFinalDto> getProductsByPagination(
            Pageable pageable,
            Integer categoryId,
            Integer subCategoryId,
            Integer brandId,
            String searchValue) {
        return productRepository.findFilteredProducts(pageable, categoryId, subCategoryId, brandId, searchValue)
                .map((product) -> ProductMapper.
                        mapToProductFinalDto(
                                product,
                                subCategoryService,
                                productBrandService));
    }

    @Override
    public Page<ProductFinalDto> getAllOutOfStockProducts(Pageable pageable) {
        return productRepository.findAllOutOfStockProducts(pageable)
                .map((product ->
                        ProductMapper.mapToProductFinalDto(
                                product,
                                subCategoryService,
                                productBrandService))
                );
    }

    @Override
    public List<ProductFinalDto> getProductsByCategoryId(Integer categoryId) {
        List<Product> products = productRepository.getProductsByProductCategoryId(categoryId);
        return products.stream().map((product) -> ProductMapper.
                mapToProductFinalDto(
                        product,
                        subCategoryService,
                        productBrandService)).toList();
    }

    @Override
    public List<ProductFinalDto> getProductsByBrandId(Integer brandId) {
        List<Product> products = productRepository.getProductsByProductBrandId(brandId);
        return products.stream().map((product) -> ProductMapper.
                mapToProductFinalDto(
                        product,
                        subCategoryService,
                        productBrandService)).toList();
    }

    @Override
    public List<ProductDto> getSimilarProductsById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        final int TARGET_SIZE = 10;
        final double PRICE_THRESHOLD = product.getProductPrice() * 0.2;
        double minPrice = product.getProductPrice() - PRICE_THRESHOLD;
        double maxPrice = product.getProductPrice() + PRICE_THRESHOLD;
        /* Filtering all the product category items **/
        List<Product> similarProducts = productRepository.findByProductCategoryIdAndProductIdNot(
                product.getProductCategoryId(), productId);
        System.out.println("similar products after category matching: ");
        System.out.println(similarProducts.stream().map(Object::toString).toList());

        /* Filter products with sub category or price range of +-50 or brand **/
        if (similarProducts.size() > TARGET_SIZE) {
            similarProducts = similarProducts.stream()
                    .filter(
                            (currProduct) ->
                                    (currProduct.getProductPrice() >= minPrice &&
                                            currProduct.getProductPrice() <= maxPrice) ||
                                            (Objects.equals(currProduct.getProductBrandId(),
                                                    product.getProductBrandId()) ||
                                                    (Objects.equals(currProduct.getSubCategoryId(),
                                                            product.getSubCategoryId())))).toList();
        }
        /* Filter products with sub category and brand only **/
        if (similarProducts.size() > TARGET_SIZE) {
            similarProducts = similarProducts.stream()
                    .filter((currProduct) ->
                            (Objects.equals(currProduct.getProductBrandId(),
                                    product.getProductBrandId()) ||
                                    (Objects.equals(currProduct.getSubCategoryId(),
                                            product.getSubCategoryId())))).toList();
        }
        /* Filter products with same price range only **/
        if (similarProducts.size() > TARGET_SIZE) {
            similarProducts = similarProducts.stream()
                    .filter((currProduct) ->
                            (currProduct.getProductPrice() < maxPrice &&
                                    currProduct.getProductPrice() > minPrice)).toList();
        }
//        similarProducts = similarProducts.stream()
//                .filter(p -> Objects.equals(p.getProductBrandId(), product.getProductBrandId()) ||
//                        Math.abs(p.getProductPrice() - product.getProductPrice()) <= product.getProductPrice() * 0.2)
//                .toList();
//        System.out.println("similar products after brand or price matching: ");
//        System.out.println(similarProducts.stream().map(Object::toString).toList());
        return similarProducts.stream().map(ProductMapper::mapToProductDto).
                toList().
                subList(0, Math.min(similarProducts.size(), 10));
    }

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
    public Object addProduct(ProductDto productDto) {
        boolean productIdExists = Objects.nonNull(productDto.getProductId());
        if (productIdExists && productDto.getProductId() != 0) {
            if (productRepository.existsById(productDto.getProductId())) {
                return HttpStatus.CONFLICT;
            }
        }
        productDto.setDateAdded(LocalDateTime.now(ZoneId.of("UTC")));
        productDto.setDateModified(LocalDateTime.now(ZoneId.of("UTC")));
        return validateAndSaveOrUpdateProduct(List.of(productDto));
    }

    @Override
    public Object updateProducts(List<ProductFinalDto> productFinalDtoList) {
        List<ProductFinalDto> productExistsList = productFinalDtoList.stream().filter(
                productFinalDto -> productRepository.existsById(productFinalDto.getProductId())
        ).toList();
        if (productExistsList.size() == productFinalDtoList.size()) {
            List<ProductDto> productDtoList = productFinalDtoList.stream()
                    .map(productDto -> {
                        ProductDto updatedProduct = ProductMapper.mapToProductDto(productDto);
                        updatedProduct.setDateModified(LocalDateTime.now(ZoneId.of("UTC")));
                        return updatedProduct;
                    }).toList();
            return validateAndSaveOrUpdateProduct(productDtoList);
        }
        return Constants.ProductNotFound;
    }

    @Override
    public HttpStatus deleteProduct(Integer productId) {
        if (productId != 0 && productRepository.existsById(productId)) {
            if (!checkDependencies.productDependenciesExists(productId)) {
                productRepository.deleteById(productId);
                return HttpStatus.OK;
            }
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public Page<ProductFinalDto> globalSearchProducts(SearchFilters searchFilters, Pageable pageable) {
        List<ProductWithRelevanceProjection> products = productRepository.globalSearchProducts(searchFilters.getKeyword());
        List<ProductWithRelevanceProjection> filteredProducts = new ArrayList<>(products.stream()
                .filter(p -> searchFilters.getCategories() == null || searchFilters.getCategories().isEmpty() || searchFilters.getCategories().contains(p.getSubCategoryId()))
                .filter(p -> searchFilters.getSubCategories() == null || searchFilters.getSubCategories().isEmpty() || searchFilters.getSubCategories().contains(p.getSubCategoryId()))
                .filter(p -> searchFilters.getBrands() == null || searchFilters.getBrands().isEmpty() || searchFilters.getBrands().contains(p.getProductBrandId()))
                .filter(p -> searchFilters.getPriceRange() == null ||
                        (p.getProductPrice() >= searchFilters.getPriceRange().get(0) && p.getProductPrice() <= searchFilters.getPriceRange().get(1)))
                .filter(p -> searchFilters.getMinRating() == null || getProductAvgReview(p.getProductId()) >= searchFilters.getMinRating())
                .filter(p -> searchFilters.getColors() == null || searchFilters.getColors().isEmpty() ||
                        searchFilters.getColors().stream().anyMatch(color -> color.equalsIgnoreCase(p.getProductColor())))
                .filter(p -> searchFilters.getCondition() == null || searchFilters.getCondition().isEmpty() ||
                        searchFilters.getCondition().stream().anyMatch(condition -> condition.equalsIgnoreCase(p.getProductCondition())))
//                .filter(p -> minDiscount == null || p.getProduct().getDiscount() >= minDiscount)
                .toList());
        if ("relevance".equalsIgnoreCase(searchFilters.getSortBy())) {
            filteredProducts.sort(Comparator.comparing(ProductWithRelevanceProjection::getRelevance).reversed());
        } else if ("low-to-high".equalsIgnoreCase(searchFilters.getSortBy())) {
            filteredProducts.sort(Comparator.comparing(ProductWithRelevanceProjection::getProductPrice));
        } else if ("high-to-low".equalsIgnoreCase(searchFilters.getSortBy())) {
            filteredProducts.sort(Comparator.comparing(ProductWithRelevanceProjection::getProductPrice, Comparator.reverseOrder()));
        }
//        else if ("rating".equalsIgnoreCase(searchFilters.getSortBy())) {
//            filteredProducts.sort(Comparator.comparing(p -> p.getRating(), Comparator.reverseOrder()));
//        }

        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductWithRelevanceProjection> paginatedProducts = start <= end ? filteredProducts.subList(start, end) : new ArrayList<>();
        List<ProductFinalDto> pageContent = paginatedProducts.stream()
                .map(p -> ProductMapper.mapToProductFinalDto(p, subCategoryService, productBrandService)).toList();
        return new PageImpl<>(pageContent, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), products.size());
    }

    private Float getProductAvgReview(Integer productId) {
        List<ProductReviewDto> result = productReviewService.getProductReviewsByProductId(productId).getBody();
        assert result != null;
        if(result.isEmpty()){
            return 0f;
        }
        return (float) (result.stream().mapToInt(ProductReviewDto::getProductRating).sum() / result.size());
    }

    private Object validateAndSaveOrUpdateProduct(List<ProductDto> productDtoList) {
        if (!HelperFunctions.getProductValidationStatus(productDtoList)) {
            return HttpStatus.BAD_REQUEST;
        } else if (!HelperFunctions.getProductBrandExistsStatus(productDtoList, productBrandService)) {
            return Constants.ProductBrandNotFound;
        } else if (!HelperFunctions.getProductCategoryExistsStatus(productDtoList, productCategoryService)) {
            return Constants.ProductCategoryNotFound;
        } else {
            List<Product> products = productRepository.
                    saveAll(productDtoList.stream().map(ProductMapper::mapToProduct).collect(toList()));
            return products.stream().map((product) ->
                    ProductMapper.mapToProductFinalDto(product, subCategoryService, productBrandService)).toList();
        }
    }
}
