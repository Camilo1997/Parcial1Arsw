package edu.eci.arsw.api.primesrepo;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.arsw.api.primesrepo.model.FoundPrime;
import edu.eci.arsw.api.primesrepo.service.PrimeService;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Santiago Carrillo 2/22/18.
 */
@RestController
public class PrimesController {

    PrimeService primeService;

    @RequestMapping(value = "/primes", method = GET)
    public List<FoundPrime> getPrimes() {
        return primeService.getFoundPrimes();
    }

    @RequestMapping(value = "/primes/{primeNumber}", method = GET)
    public FoundPrime getPrime(@PathVariable String primeNumber) {
        return primeService.getPrime(primeNumber);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void setPrime(@RequestBody String foundPrime) throws IOException {
        ObjectMapper objectMap = new ObjectMapper();
        FoundPrime fp = objectMap.readValue(foundPrime, FoundPrime.class);
        primeService.addFoundPrime(fp);

    }
}
