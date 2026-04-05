# Avito QA Internship — API Tests

Автотесты для API сервиса объявлений `https://qa-internship.avito.com`.

*Выполнил: Богоявленский Александр*

## Требования

- Java 17+
- Gradle 9+

## Быстрый старт

Склонируйте репозиторий:
```bash
git clone https://github.com/your-username/AvitoQATest.git
cd AvitoQATest
```

Запустите тесты:
```bash
./gradlew test
```


## Отчёт Allure

Сгенерировать и открыть отчёт в браузере:
```bash
./gradlew allureServe
```

Или сохранить отчёт локально в `build/reports/allure-report/`:
```bash
./gradlew allureReport
```

Пример итогового отчёта:
<img width="1920" height="892" alt="image" src="https://github.com/user-attachments/assets/591772bf-b7e1-446d-a0bd-924131074ec1" />


## Линтер и форматтер

Проект использует [Spotless](https://github.com/diffplug/spotless) с правилами Google Java Format.

Проверить форматирование:
```bash
./gradlew spotlessCheck
```

Автоматически исправить:
```bash
./gradlew spotlessApply
```

Правила зафиксированы в блоке `spotless` файла `build.gradle`.
