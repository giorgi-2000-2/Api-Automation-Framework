package org.example.Managers;

import org.example.DataFactories.CategoryDataFactory;
import org.example.DataFactories.ProductDataFactory;


public class FactoryManager {

    private CategoryDataFactory categoryDataFactory;
private ProductDataFactory productDataFactory;

    public CategoryDataFactory categoryFactory(){
        if(categoryDataFactory==null){
            categoryDataFactory = new CategoryDataFactory(this);

        }
        return categoryDataFactory;
    }
public ProductDataFactory productFactory(){
        if(productDataFactory==null){
            productDataFactory=new ProductDataFactory(this);
        }
        return productDataFactory;
}



}
