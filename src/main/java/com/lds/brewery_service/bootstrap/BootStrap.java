package com.lds.brewery_service.bootstrap;

import com.lds.brewery_service.web.entity.Beer;
import com.lds.brewery_service.web.repository.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BootStrap implements CommandLineRunner {

    private final BeerRepository beerRepository;

    public static final String BEER_1_UPC = "123123123123";
    public static final String BEER_2_UPC = "073785456888";
    public static final String BEER_3_UPC = "763487556709";

    public BootStrap(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
        System.out.println("Beer count : "+beerRepository.count());
    }
    private void loadBeerObjects() {
        if (beerRepository.count() == 0) {
            beerRepository.save(Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle("IPA")
                    .quantityToBrew(200)
                    .price(new BigDecimal("10.50"))
                    .minOnHand(12)
                    .upc(BEER_1_UPC)
                    .build());

            beerRepository.save(Beer.builder()
                    .beerName("Galaxy cat")
                    .beerStyle("PALE_ALE")
                    .quantityToBrew(200)
                    .price(new BigDecimal("11.50"))
                    .minOnHand(12)
                    .upc(BEER_2_UPC)
                    .build());

            beerRepository.save(Beer.builder()
                    .beerName("No Hammers on the bar")
                    .beerStyle("PALE_ALE")
                    .quantityToBrew(200)
                    .price(new BigDecimal("11.50"))
                    .minOnHand(12)
                    .upc(BEER_3_UPC)
                    .build());
        }
    }
}
