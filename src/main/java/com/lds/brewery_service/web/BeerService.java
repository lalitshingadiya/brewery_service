package com.lds.brewery_service.web;

import com.lds.brewery_service.web.entity.Beer;
import com.lds.brewery_service.web.mapper.BeerMapper;
import com.lds.brewery_service.web.model.BeerDto;
import com.lds.brewery_service.web.repository.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BeerService {

    final BeerRepository beerRepository;

    final BeerMapper beerMapper;

    public BeerService(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    public List<BeerDto> listAllBeer(){
        List<BeerDto> list = new ArrayList<>();
        beerRepository.findAll().forEach(beer -> {
            list.add(beerMapper.beerToBeerDto(beer));
        });
        return list;
    }

    public BeerDto findByID(UUID beerId) {
        BeerDto beerDto = null;
        Optional<Beer> optionalBeer = beerRepository.findById(beerId);
        if(optionalBeer.isPresent()){
            beerDto = beerMapper.beerToBeerDto(optionalBeer.get());
        }
        return beerDto;
    }

    public BeerDto saveNewBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Optional<Beer> optionalBeer  = beerRepository.findById(beerId);
        if(optionalBeer.isPresent()){
            beerDto.setId(optionalBeer.get().getId());
            beerRepository.save(beerMapper.beerDtoToBeer(beerDto));
        }
        return beerDto;
    }
}
