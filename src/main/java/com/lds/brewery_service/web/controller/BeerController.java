package com.lds.brewery_service.web.controller;

import com.lds.brewery_service.web.model.BeerPagedList;
import com.lds.brewery_service.web.model.BeerStyleEnum;
import com.lds.brewery_service.web.service.BeerService;
import com.lds.brewery_service.web.model.BeerDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(BeerController.BASE_URL)
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    public static final String BASE_URL = "/api/v1/beer";

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping(value = {"","/"},produces = {"application/json"})
    ResponseEntity<BeerPagedList> listBeers(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
            @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand)
    {
        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if(showInventoryOnHand == null){
            showInventoryOnHand = false;
        }
        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize),showInventoryOnHand);

        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    ResponseEntity<BeerDto> getBeer(@PathVariable UUID beerId,@RequestParam(value = "showInventoryOnHand",required = false) Boolean showInventoryOnHand){
        if(showInventoryOnHand == null){
            showInventoryOnHand = false;
        }
        return new ResponseEntity(beerService.findByID(beerId,showInventoryOnHand), HttpStatus.OK);
    }

    @PostMapping({"/",""})
    ResponseEntity<BeerDto> saveNewBeer(@Valid @RequestBody BeerDto beerDto){
        return new ResponseEntity(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    ResponseEntity<BeerDto> updateBeer(@PathVariable UUID beerId,@Valid @RequestBody BeerDto beerDto){
        return new ResponseEntity(beerService.updateBeer(beerId,beerDto), HttpStatus.NO_CONTENT);
    }
}
