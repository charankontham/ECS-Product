package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.SubCategoryDto;
import com.ecs.ecs_product.service.interfaces.ISubCategoryService;
import com.ecs.ecs_product.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subCategory")
public class SubCategoryController {
    @Autowired
    private ISubCategoryService subCategoryService;

    @GetMapping("/")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        List<SubCategoryDto> subCategories = subCategoryService.getAllSubCategories();
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @GetMapping("/getByCategoryId/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoriesByCategoryId(
            @PathVariable("categoryId") Integer categoryId) {
        List<SubCategoryDto> subCategories = subCategoryService.getSubCategoriesByCategoryId(categoryId);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @GetMapping("/getAllSubCategoriesByPagination")
    public ResponseEntity<Page<SubCategoryDto>> getAllSubCategoriesByPagination(
            @RequestParam(defaultValue = "0", name = "currentPage") Integer pageNumber,
            @RequestParam(defaultValue = "5", name = "offset") Integer itemSize,
            @RequestParam(required = false, name="categoryId") Integer categoryId,
            @RequestParam(required = false, name="searchValue") String searchValue
    ) {
        Pageable pageable = PageRequest.of(pageNumber, itemSize);
        Page<SubCategoryDto> subCategories = subCategoryService.
                getAllSubCategoriesWithPagination(pageable, categoryId, searchValue);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @GetMapping("/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> getSubCategoryById(@PathVariable("subCategoryId") Integer subCategoryId) {
        SubCategoryDto subCategory = subCategoryService.getSubCategoryById(subCategoryId);
        return new ResponseEntity<>(subCategory, HttpStatus.OK);
    }

    @GetMapping("/getAllBySubCategoryName/{subCategoryName}")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategoriesByName(@PathVariable("subCategoryName") String subCategoryName) {
        List<SubCategoryDto> subCategories = subCategoryService.getAllSubCategoriesByName(subCategoryName);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        Object result = subCategoryService.addSubCategory(subCategoryDto);
        if (result instanceof SubCategoryDto) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return HelperFunctions.getResponseEntity(result);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        Object result = subCategoryService.updateSubCategory(subCategoryDto);
        return HelperFunctions.getResponseEntity(result);
    }

    @DeleteMapping("/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Integer subCategoryId) {
        subCategoryService.deleteSubCategory(subCategoryId);
        return new ResponseEntity<>("SubCategory deleted successfully!", HttpStatus.OK);
    }
}
