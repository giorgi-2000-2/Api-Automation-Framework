package org.example.Managers;

import org.example.DataFactories.CategoryDataFactory;
import org.example.DataFactories.ProductDataFactory;
import org.example.DataFactories.RandomDataFactory;


public class FactoryManager {
    private RandomDataFactory getRandomData;
    private CategoryDataFactory categoryDataFactory;
private ProductDataFactory productDataFactory;

    public CategoryDataFactory categoryFactory(){
        if(categoryDataFactory==null){
            categoryDataFactory = new CategoryDataFactory(getRandomData ());

        }
        return categoryDataFactory;
    }
public ProductDataFactory productFactory(){
        if(productDataFactory==null){
            productDataFactory=new ProductDataFactory(getRandomData ());
        }
        return productDataFactory;
}
    public RandomDataFactory getRandomData (){
        if(getRandomData==null){
            getRandomData=new RandomDataFactory();

        }
        return getRandomData;
    }


}
