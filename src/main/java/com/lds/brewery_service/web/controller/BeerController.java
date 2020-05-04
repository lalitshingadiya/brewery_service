package com.lds.brewery_service.web.controller;

import com.lds.brewery_service.web.BeerService;
import com.lds.brewery_service.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(BeerController.BASE_URL)
public class BeerController {

    public static final String BASE_URL = "/api/v1/beer";

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping({"","/"})
    ResponseEntity<List<BeerDto>> getAllBeer(){
        return new ResponseEntity(beerService.listAllBeer(), HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    ResponseEntity<BeerDto> getBeer(@PathVariable UUID beerId){
        return new ResponseEntity(beerService.findByID(beerId), HttpStatus.OK);
    }

    @PostMapping("/")
    ResponseEntity<BeerDto> saveNewBeer(@Valid @RequestBody BeerDto beerDto){
        return new ResponseEntity(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    ResponseEntity<BeerDto> updateBeer(@PathVariable UUID beerId,@Valid @RequestBody BeerDto beerDto){
        return new ResponseEntity(beerService.updateBeer(beerId,beerDto), HttpStatus.ACCEPTED);
    }
}
