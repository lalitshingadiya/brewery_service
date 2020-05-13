package com.lds.brewery_service.web.service;

import com.lds.brewery_service.web.controller.NotFoundException;
import com.lds.brewery_service.web.entity.Beer;
import com.lds.brewery_service.web.mapper.BeerMapper;
import com.lds.brewery_service.web.model.BeerDto;
import com.lds.brewery_service.web.model.BeerPagedList;
import com.lds.brewery_service.web.model.BeerStyleEnum;
import com.lds.brewery_service.web.repository.BeerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand){
        System.out.println("========beerListCache========");
        BeerPagedList beerPagedList;
        Page<Beer> page;
        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            page = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        }else if(!StringUtils.isEmpty(beerName)){
            page = beerRepository.findAllByBeerName(beerName,pageRequest);
        }else if(!StringUtils.isEmpty(beerStyle)){
            page = beerRepository.findAllByBeerStyle(beerStyle,pageRequest);
        }else{
            page = beerRepository.findAll(pageRequest);
        }
        if(showInventoryOnHand){
            beerPagedList = new BeerPagedList(page.getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithQuntity)
                    .collect(Collectors.toList()),
                    PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize())
                    , page.getTotalPages());
        }else {
            beerPagedList = new BeerPagedList(page.getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize())
                    , page.getTotalPages());
        }
        return beerPagedList;
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    public BeerDto findByID(UUID beerId, Boolean showInventoryOnHand) {
        System.out.println("========"+beerId);
        if(showInventoryOnHand){
            return beerMapper.beerToBeerDtoWithQuntity(beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
        }else {
            return beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
        }
    }

    public BeerDto saveNewBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());
        beer.setMinOnHand(beerDto.getQuantityOnHand());
        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }
}
