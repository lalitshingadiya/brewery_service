package com.lds.brewery_service.web.mapper;

import com.lds.brewery_service.web.entity.Beer;
import com.lds.brewery_service.web.mapper.decorator.BeerDecorator;
import com.lds.brewery_service.web.model.BeerDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {DateMapper.class})
@DecoratedWith(BeerDecorator.class)
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);

    BeerDto beerToBeerDtoWithQuntity(Beer beer);
}
