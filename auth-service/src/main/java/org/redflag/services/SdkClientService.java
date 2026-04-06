package org.redflag.services;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.redflag.dto.CreateServiceAccessResponse;
import org.redflag.dto.SdkClientResponse;
import org.redflag.entities.SdkClient;
import org.redflag.exception.ConflictCustomException;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.repositories.SdkClientRepository;
import org.redflag.services.kafkaServices.KafkaGeneralService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class SdkClientService {

    private final SdkClientRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaGeneralService kafkaService;

    public CreateServiceAccessResponse createSdkWithKafka(UUID login) {
        SdkClientResponse sdkClient = create(login);
        return kafkaService.setupKafka(login.toString(), sdkClient.password());
    }

    @Transactional
    public SdkClientResponse create(UUID login) {
        if (repository.findByLogin(login).isPresent()) {
            throw new ConflictCustomException("Login already taken");
        }

        String password = generateRandomPassword();
        SdkClient client = new SdkClient();
        client.setLogin(login);
        client.setPassword(passwordEncoder.encode(password));

        repository.save(client);

        return new SdkClientResponse(client.getId(), client.getLogin(), password);
    }

    @Transactional
    public void deleteWithKafka(UUID login) {
        SdkClient sdkClient = repository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundCustomException("SdkClient not found"));
        kafkaService.cleanupKafka(login.toString(), passwordEncoder.encode(sdkClient.getPassword()));
        delete(sdkClient.getId());
    }

    public void deleteWithKafkaListSdk(List<UUID> logins) {
        long count = repository.countByLoginIn(logins);
        if (count == 0) {
            throw new ResourceNotFoundCustomException("No sdks found");
        }
        for (UUID login : logins) {
            try {
                deleteWithKafka(login);
            } catch (ResourceNotFoundCustomException e) {
                log.warn("Not found for login={}: {}", login, e.getMessage(), e);
            }
        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public String generateRandomPassword() {
        PasswordGenerator gen = new PasswordGenerator();

        CharacterRule lowerCase = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule upperCase = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit, 2);

        return gen.generatePassword(8, lowerCase, upperCase, digits);
    }

}
