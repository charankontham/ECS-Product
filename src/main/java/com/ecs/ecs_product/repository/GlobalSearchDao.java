package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.dto.SearchResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GlobalSearchDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<SearchResultDto> globalSearch(String keyword, String procedureName) {
        String spName;
        switch (procedureName.toLowerCase()) {
            case "products":
                spName = "searchProducts";
                break;
            case "categories":
                spName = "searchCategories";
                break;
            case "subcategories":
                spName = "searchSubCategories";
                break;
            case "brands":
                spName = "searchBrands";
                break;
            default:
                throw new IllegalArgumentException("Invalid procedure name: " + procedureName);
        }

        String sql = "CALL " + spName + "(:keyword)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("keyword", keyword);

        List<Object[]> results = query.getResultList();
        List<SearchResultDto> finalResults = new ArrayList<>();

        for (Object[] row : results) {
            SearchResultDto dto = new SearchResultDto();
            dto.setItemType((String) row[0]);
            dto.setItemId(((Number) row[1]).intValue());
            dto.setItemName((String) row[2]);
            dto.setItemCategory((String) row[3]);
            dto.setRelevanceScore(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0);
            finalResults.add(dto);
        }

        return finalResults;
    }
}
