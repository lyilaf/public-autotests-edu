package com.tcs.vetclinic;

import static io.qameta.allure.Allure.step;

import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePersonITTests {

    @Test
    @AllureId("1")
    @DisplayName("Создание пользователя с name и id")
    public void newTestCase() {
        step("Отправляем запрос POST /person с параметрам: 1=2", () -> {

            step("Проверяем ...", () -> {});
        });
        step("Отправляем запрос POST /person с параметрам: 1=2", () -> {});
    }

    @Test
    @AllureId("2")
    @DisplayName("Создание пользователя с name и без id")
    public void newTestCase2() {
    }

}
