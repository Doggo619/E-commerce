package com.base.e_com;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProductWithImages {
    @Embedded
    public ProductEntity product;

    @Relation(parentColumn = "id", entityColumn = "productId")
    public List<ProductImageEntity> images;
}

