package ua.profitsoft.library.services;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class PasswordGeneratorService {
    public static String generate(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
