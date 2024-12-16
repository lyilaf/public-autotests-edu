package com.tcs.vetclinic;

import com.tcs.vetclinic.IntegrationTest;
import com.tcs.vetclinic.domain.person.Person;
import feign.FeignException;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static io.qameta.allure.Allure.step;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Epic("publicApi")
@Feature("person controller")
@Story("Get /person/{id}")
public class GetPersonTests extends IntegrationTest {

    RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("РџРѕР»СѓС‡РµРЅРёРµ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ РїРѕ СЃСѓС‰РµСЃС‚РІСѓСЋС‰РµРјСѓ id")
    @AllureId("1")
    public void GetPersonWithId() {

        String postUrl = "http://localhost:8080/api/person";

        String userName = "Alpha";

        step("РЎРѕР·РґР°РµРј РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ СЃ РїРѕРјРѕС‰СЊСЋ РјРµС‚РѕРґР° POST /person СЃ РїР°СЂР°РјРµС‚СЂР°РјРё: name = '" + userName + "'", () -> {
            Person personToCreate = new Person(userName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Person> requestEntity = new HttpEntity<>(personToCreate, headers);

            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                    postUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Long.class
            );

            step("РџСЂРѕРІРµСЂСЏРµРј, С‡С‚Рѕ РІ РѕС‚РІРµС‚ РѕС‚ Р·Р°РїСЂРѕСЃР° СЃ РїРѕРјРѕС‰СЊСЋ POST /person РїРѕР»СѓС‡Р°РµРј id РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ", () -> {
                assertNotNull(createPersonResponse.getBody());
            });
            step("РћС‚РїСЂР°РІР»СЏРµРј GET person/{id} РїРѕ СЃРѕР·РґР°РЅРЅРѕРјСѓ id РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ", () -> {
                String getUrl = "http://localhost:8080/api/person/%s".formatted(createPersonResponse.getBody());
                ResponseEntity<Person> getResponse = restTemplate.getForEntity(getUrl, Person.class);

                step("РџСЂРѕРІРµСЂСЏРµРј, С‡С‚Рѕ Р·Р°РїСЂРѕСЃ РІРѕР·РІСЂР°С‰Р°РµС‚ id Рё name РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ", () -> {
                    assertEquals(HttpStatus.OK, getResponse.getStatusCode());

                    Person retrievedPerson = getResponse.getBody();
                    assertNotNull(retrievedPerson);
                    assertEquals(createPersonResponse.getBody(), retrievedPerson.getId());
                    assertEquals(userName, retrievedPerson.getName());
                });
            });
        });
    }

    @Test
    @DisplayName("РџРѕР»СѓС‡РµРЅРёРµ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ РїРѕ СЃСѓС‰РµСЃС‚РІСѓСЋС‰РµРјСѓ id")
    @AllureId("2")
    public void GetPersonWithAnotherId() {

        String getUrl = "http://localhost:8080/api/person/";

        step("РЎРѕР·РґР°РµРј РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ СЃ РїРѕРјРѕС‰СЊСЋ РјРµС‚РѕРґР° POST /person Рё СЃРѕС…СЂР°РЅСЏРµРј РµРіРѕ id", () -> {
            String postUrl = "http://localhost:8080/api/person";

            Person person = new Person("Ben");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);

            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                    postUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Long.class
            );

            long id = createPersonResponse.getBody();

            step("РЈРґР°Р»СЏРµРј РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ СЃ РїРѕРјРѕС‰СЊСЋ РјРµС‚РѕРґР° DELETE /person/{id}", () -> {
                String deleteUrl = getUrl + id;
                restTemplate.delete(deleteUrl);

                step("РџСЂРѕРІРµСЂСЏРµРј, С‡С‚Рѕ РјРµС‚РѕРґ GET /person/{id} РІРѕР·СЂР°С‰Р°РµС‚ РѕС€РёР±РєСѓ 404", () -> {
                    assertThrows(FeignException.NotFound.class, () -> testPersonClient.findById(id));
                });
            });
        });
    }
;
};
