package local.luke.crypto.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import local.luke.crypto.exceptions.CryptoException;
import local.luke.crypto.model.Crypto;
import local.luke.crypto.model.SortType;

@Service
public class CryptoService {

    private List<Crypto> cryptos = new ArrayList<>();

    @PostConstruct
    public void initCryptos() throws CryptoException {
        cryptos.add(new Crypto("Ethereum", "ETH", 3447.35, 0.25));
        cryptos.add(new Crypto("Bitcoin", "BTC", 92888.11, 0.25));
    }

    public void addCrypto(Crypto crypto) throws CryptoException {
        cryptos.add(crypto);
    }

    public List<Crypto> getCryptos() {
        return cryptos;
    }

    public List<Crypto> getSortCryptos(String sortParam) throws CryptoException {
        try {
            SortType sortType = SortType.valueOf(sortParam.toUpperCase());
            switch (sortType) {
                case NAME:
                    return cryptos.stream().sorted(Comparator.comparing(Crypto::getName)).collect(Collectors.toList());
                case PRICE:
                    return cryptos.stream().sorted(Comparator.comparing(Crypto::getPrice)).collect(Collectors.toList());
                case QUANTITY:
                    return cryptos.stream().sorted(Comparator.comparing(Crypto::getQuantity)).collect(Collectors.toList());
                default:
                    return cryptos.stream().sorted(Comparator.comparing(Crypto::getName)).collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            throw new CryptoException("Zadal jsi parametr " + sortParam + ". " + e.getLocalizedMessage());
        }
    }

    public Crypto getCryptoById(Integer id) {
        for (Crypto crypto : cryptos) {
            if (crypto.getId() == id) {
                return crypto;
            }
        }
        return null;
    }

    public void updateCrypto(Integer id, Crypto newCrypto) throws CryptoException {
        Crypto oldCrypto = getCryptoById(id);

        if (newCrypto.getName() != null) {
            oldCrypto.setName(newCrypto.getName());
        }
        if (newCrypto.getSymbol() != null) {
            oldCrypto.setSymbol(newCrypto.getSymbol());
        }
        if (newCrypto.getPrice() != null) {
            oldCrypto.setPrice(newCrypto.getPrice());
        }
        if (newCrypto.getQuantity() != null) {
            oldCrypto.setQuantity(newCrypto.getQuantity());
        }
    }

    public String getTotalValue() {
        double totalValue = 0;

        for (Crypto crypto : cryptos) {
            totalValue += (crypto.getPrice() * crypto.getQuantity());
        }
        return String.format("%.2f", totalValue);
    }

}