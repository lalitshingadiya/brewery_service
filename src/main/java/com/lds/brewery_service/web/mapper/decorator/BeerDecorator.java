package com.lds.brewery_service.web.mapper.decorator;

import com.lds.brewery_service.web.entity.Beer;
import com.lds.brewery_service.web.mapper.BeerMapper;
import com.lds.brewery_service.web.model.BeerDto;
import com.lds.brewery_service.web.service.BeerInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BeerDecorator implements BeerMapper {

    private BeerMapper beerMapper;

    private BeerInventoryService beerInventoryService;

    @Autowired
    public void setBeerMapper(BeerMapper beerMapper){
        this.beerMapper = beerMapper;
    }

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService){
        this.beerInventoryService = beerInventoryService;
    }

    @Override
    public BeerDto beerToBeerDtoWithQuntity(Beer beer) {
        BeerDto beerDto = this.beerMapper.beerToBeerDto(beer);
        beerDto.setQuantityOnHand(this.beerInventoryService.getOnHandInventory(beer.getId()));
        return beerDto;
    }
}
