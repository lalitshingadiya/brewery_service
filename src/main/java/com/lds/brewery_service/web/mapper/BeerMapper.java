package com.lds.brewery_service.web.mapper;

import com.lds.brewery_service.web.entity.Beer;
import com.lds.brewery_service.web.model.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {DateMapper.class})
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    @Mapping(source = "minOnHand",target = "quantityOnHand")
    BeerDto beerToBeerDto(Beer beer);

    @Mapping(source = "quantityOnHand",target = "minOnHand")
    Beer beerDtoToBeer(BeerDto beerDto);
}
