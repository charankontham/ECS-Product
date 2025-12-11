Here is the SQL SP script for global search:

	CREATE DEFINER=`root`@`localhost` PROCEDURE `globalSearch`(
    	IN search_keyword VARCHAR(255)
	)
	COMMENT 'Powerful search with synonym and fuzzy matching'
	BEGIN
    DECLARE synonym_word VARCHAR(255) DEFAULT NULL;
    DECLARE brandId INT DEFAULT NULL;
    DECLARE categoryId INT DEFAULT NULL;
    DECLARE subCategoryId INT DEFAULT NULL;
    SET @boolean_keyword = CONCAT('+', REPLACE(search_keyword, ' ', ' +'));
    
	-- Try to find synonym
    SELECT synonym INTO synonym_word
    FROM search_synonyms
    WHERE LOWER(search_synonyms.keyword) = LOWER(search_keyword)
    LIMIT 1;

    -- If synonym found, use both terms for searching
    IF synonym_word IS NOT NULL THEN
        SET @search_term_1 = search_keyword;
        SET @search_term_2 = synonym_word;
    ELSE
        SET @search_term_1 = search_keyword;
        SET @search_term_2 = search_keyword;
    END IF;

    -- Temporary table
    CREATE TEMPORARY TABLE IF NOT EXISTS tmp_search_results (
        product_id INT PRIMARY KEY,
        relevance DECIMAL(10,4)
    ) ENGINE=MEMORY;
    TRUNCATE tmp_search_results;

    -- 1. Product Name + Description Matching
    INSERT IGNORE INTO tmp_search_results (product_id, relevance)
    SELECT
        p.product_id,
        (
            ((MATCH(p.product_name, p.product_description) AGAINST (@search_term_1 IN NATURAL LANGUAGE MODE)) * 4.0)
            + (CASE
                WHEN LOWER(p.product_name) = LOWER(@search_term_1) THEN 3.0
                WHEN LOWER(p.product_name) LIKE LOWER(CONCAT(@search_term_1, '%')) THEN 2.0
                WHEN LOWER(p.product_name) LIKE LOWER(CONCAT('%', @search_term_1)) THEN 2.0
                WHEN LOWER(p.product_name) LIKE LOWER(CONCAT('%', @search_term_1, '%')) THEN 1.0
                WHEN LOWER(p.product_description) LIKE LOWER(CONCAT(@search_term_1, '%')) THEN 1.5
                WHEN LOWER(p.product_description) LIKE LOWER(CONCAT('%', @search_term_1)) THEN 1.5
                WHEN LOWER(p.product_description) LIKE LOWER(CONCAT('%', @search_term_1, '%')) THEN 1.0
                WHEN p.product_id IN ( 
                    SELECT psw.product_id 
                    FROM product_soundex_words psw 
                    WHERE psw.word_soundex = SOUNDEX(@search_term_1)
                    ) THEN 0.8
                ELSE 0.0
            END)
        ) AS relevance
    FROM product p
    WHERE
        MATCH(p.product_name, p.product_description) AGAINST (@boolean_keyword IN BOOLEAN MODE)
        OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', @search_term_1, '%'))
        OR LOWER(p.product_description) LIKE LOWER(CONCAT('%', @search_term_1, '%'))
        OR p.product_id IN ( 
                    SELECT psw.product_id 
                    FROM product_soundex_words psw 
                    WHERE psw.word_soundex = SOUNDEX(@search_term_1)
                    )
    ORDER BY relevance DESC;

    -- 2. If synonym is found, search again with synonym term
    IF @search_term_1 <> @search_term_2 THEN
        INSERT INTO tmp_search_results (product_id, relevance)
        SELECT
            p.product_id,
            (
                (MATCH(p.product_name, p.product_description) AGAINST (@search_term_2 IN NATURAL LANGUAGE MODE)) * 3.0
                + (CASE
                    WHEN LOWER(p.product_name) = LOWER(@search_term_2) THEN 3.0
                    WHEN LOWER(p.product_name) LIKE LOWER(CONCAT(@search_term_2, '%')) THEN 2.0
                    WHEN LOWER(p.product_name) LIKE LOWER(CONCAT('%', @search_term_2)) THEN 2.0
                    WHEN LOWER(p.product_name) LIKE LOWER(CONCAT('%', @search_term_2, '%')) THEN 1.0
                    WHEN LOWER(p.product_description) LIKE LOWER(CONCAT(@search_term_2, '%')) THEN 1.5
                    WHEN LOWER(p.product_description) LIKE LOWER(CONCAT('%', @search_term_2)) THEN 1.5
                    WHEN LOWER(p.product_description) LIKE LOWER(CONCAT('%', @search_term_2, '%')) THEN 1.0
                    WHEN p.product_id IN ( 
                        SELECT psw.product_id 
                        FROM product_soundex_words psw 
                        WHERE psw.word_soundex = SOUNDEX(@search_term_2)
                        ) THEN 0.8
                    ELSE 0.0
                END)
            ) AS relevance
        FROM product p
        WHERE 
            MATCH(p.product_name, p.product_description) AGAINST (@search_term_2 IN BOOLEAN MODE)
            OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', @search_term_2, '%'))
            OR LOWER(p.product_description) LIKE LOWER(CONCAT('%', @search_term_2, '%'))
            OR p.product_id IN ( 
                        SELECT psw.product_id 
                        FROM product_soundex_words psw 
                        WHERE psw.word_soundex = SOUNDEX(@search_term_2)
                        )
		ON DUPLICATE KEY UPDATE relevance = GREATEST(relevance, VALUES(relevance));
    END IF;

    -- 3. Brand match
    SELECT pb.brand_id INTO brandId
    FROM product_brand pb
    WHERE LOWER(pb.brand_name) = LOWER(@search_term_1)
       OR LOWER(pb.brand_name) = LOWER(@search_term_2)
       OR SOUNDEX(pb.brand_name) = SOUNDEX(@search_term_1)
       OR SOUNDEX(pb.brand_name) = SOUNDEX(@search_term_2)
    LIMIT 1;

    IF brandId IS NOT NULL THEN
        INSERT INTO tmp_search_results (product_id, relevance)
        SELECT p.product_id, 2.5
        FROM product p
        WHERE p.product_brand_id = brandId
        ON DUPLICATE KEY UPDATE relevance = GREATEST(relevance, VALUES(relevance));
    END IF;

    -- 4. Category match
    SELECT pc.category_id INTO categoryId
    FROM product_category pc
    WHERE LOWER(pc.category_name) = LOWER(@search_term_1)
       OR LOWER(pc.category_name) = LOWER(@search_term_2)
       OR SOUNDEX(pc.category_name) = SOUNDEX(@search_term_1)
       OR SOUNDEX(pc.category_name) = SOUNDEX(@search_term_2)
    LIMIT 1;

    IF categoryId IS NOT NULL THEN
        INSERT IGNORE INTO tmp_search_results (product_id, relevance)
        SELECT p.product_id, 2.0
        FROM product p
        WHERE p.product_category_id = categoryId
        ON DUPLICATE KEY UPDATE relevance = GREATEST(relevance, VALUES(relevance));
    END IF;

    -- 5. Subcategory match
    SELECT sc.sub_category_id INTO subCategoryId
    FROM sub_category sc
    WHERE LOWER(sc.sub_category_name) = LOWER(@search_term_1)
       OR LOWER(sc.sub_category_name) = LOWER(@search_term_2)
       OR SOUNDEX(sc.sub_category_name) = SOUNDEX(@search_term_1)
       OR SOUNDEX(sc.sub_category_name) = SOUNDEX(@search_term_2)
    LIMIT 1;

    IF subCategoryId IS NOT NULL THEN
        INSERT IGNORE INTO tmp_search_results (product_id, relevance)
        SELECT p.product_id, 2.0
        FROM product p
        WHERE p.sub_category_id = subCategoryId
        ON DUPLICATE KEY UPDATE relevance = GREATEST(relevance, VALUES(relevance));
    END IF;
    
    DROP TEMPORARY TABLE IF EXISTS tmp_final_results;
    CREATE TEMPORARY TABLE tmp_final_results AS
        SELECT * FROM tmp_search_results;

    -- 6. Final result
    SELECT p.product_id as productId,
    p.product_category_id as productCategoryId,
    p.sub_category_id as subCategoryId,
    p.product_brand_id as productBrandId,
    p.product_name as productName,
    p.product_description as productDescription,
    p.product_price as productPrice,
    p.product_quantity as productQuantity,
    p.product_image as productImage,
    p.product_color as productColor,
    p.product_weight as productWeight,
    p.date_added as dateAdded,
    p.date_modified as dateModified,
    p.product_dimensions as productDimensions,
    p.product_condition as productCondition,
    t.relevance
    FROM tmp_final_results t
    JOIN product p ON p.product_id = t.product_id
    ORDER BY t.relevance DESC, p.product_name ASC;

    DROP TEMPORARY TABLE IF EXISTS tmp_search_results;
	END
