package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.ProductCategoryDto;
import com.ecs.ecs_product.dto.SubCategoryDto;
import com.ecs.ecs_product.dto.SubCategoryEnriched;
import com.ecs.ecs_product.entity.SubCategory;
import com.ecs.ecs_product.exception.ResourceNotFoundException;
import com.ecs.ecs_product.mapper.SubCategoryMapper;
import com.ecs.ecs_product.repository.SubCategoryRepository;
import com.ecs.ecs_product.service.interfaces.IProductCategoryService;
import com.ecs.ecs_product.service.interfaces.ISubCategoryService;
import com.ecs.ecs_product.util.Constants;
import com.ecs.ecs_product.validations.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements ISubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private IProductCategoryService productCategoryService;

    @Override
    public List<SubCategoryDto> getAllSubCategories() {
        return subCategoryRepository.findAll().stream()
                .map(SubCategoryMapper::mapToSubCategoryDto).toList();
    }

    @Override
    public List<SubCategoryDto> getSubCategoriesByCategoryId(Integer categoryId) {
        return subCategoryRepository.findAllByCategoryId(categoryId)
                .stream().map(SubCategoryMapper::mapToSubCategoryDto)
                .toList();
    }

    @Override
    public SubCategoryDto getSubCategoryById(Integer subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found!"));
        return SubCategoryMapper.mapToSubCategoryDto(subCategory);
    }

    @Override
    public SubCategoryEnriched getEnrichedSubCategoryById(Integer subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found!"));
        ProductCategoryDto categoryDto = productCategoryService.getProductCategoryById(subCategory.getCategoryId());
        return SubCategoryMapper.mapToEnrichedSubCategoryDto(subCategory, categoryDto);
    }

    @Override
    public List<SubCategoryDto> getAllSubCategoriesByName(String subCategoryName) {
        return subCategoryRepository.findAllBySubCategoryName(subCategoryName)
                .stream().map(SubCategoryMapper::mapToSubCategoryDto)
                .toList();
    }

    @Override
    public Object addSubCategory(SubCategoryDto subCategoryDto) {
        boolean subCategoryExists = subCategoryRepository.existsById(subCategoryDto.getCategoryId());
        if (subCategoryExists) {
            return HttpStatus.CONFLICT;
        } else {
            if (
                    subCategoryDto.getCategoryId() != null &&
                            BasicValidation.stringValidation(subCategoryDto.getSubCategoryName())
            ) {
                SubCategory savedSubCategory = subCategoryRepository.save(SubCategoryMapper.mapToSubCategory(subCategoryDto));
                return SubCategoryMapper.mapToSubCategoryDto(savedSubCategory);
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        }
    }

    @Override
    public Object updateSubCategory(SubCategoryDto subCategoryDto) {
        boolean subCategoryExists = subCategoryRepository.existsById(subCategoryDto.getCategoryId());
        if(subCategoryExists) {
            if(BasicValidation.stringValidation(subCategoryDto.getSubCategoryName())){
                SubCategory updatedSubCategory = subCategoryRepository.save(SubCategoryMapper.mapToSubCategory(subCategoryDto));
                return SubCategoryMapper.mapToSubCategoryDto(updatedSubCategory);
            }else{
                return HttpStatus.BAD_REQUEST;
            }
        }else{
            return Constants.SubCategoryNotFound;
        }
    }

    @Override
    public void deleteSubCategory(Integer subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
    }
}
