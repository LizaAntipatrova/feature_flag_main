package org.redflag.services;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
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

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
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

//    @Transactional
//    public SdkClientResponse updateLogin(Long id, UUID newLogin) {
//        SdkClient client = repository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundCustomException("SDK Client not found"));
//
//        if (!client.getLogin().equals(newLogin) && repository.findByLogin(newLogin).isPresent()) {
//            throw new ConflictCustomException("Login already taken");
//        }
//
//        client.setLogin(newLogin);
//        repository.update(client);
//        return new SdkClientResponse(client.getId(), client.getLogin(), null);
//    }
//
//    @Transactional
//    public SdkClientResponse regeneratePassword(Long id) {
//        SdkClient client = repository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundCustomException("SDK Client not found"));
//
//        String newPassword = generateRandomPassword();
//        client.setPassword(passwordEncoder.encode(newPassword));
//        repository.update(client);
//
//        return new SdkClientResponse(client.getId(), client.getLogin(), newPassword);
//    }

    @Transactional
    public void deleteWithKafka(UUID login) {
        SdkClient sdkClient = repository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundCustomException("SdkClient not found"));
        kafkaService.cleanupKafka(login.toString(), passwordEncoder.encode(sdkClient.getPassword()));
        delete(sdkClient.getId());
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
