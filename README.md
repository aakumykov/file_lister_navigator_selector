Проект представляет три библиотеки:
* альтернативный диалог выбора файлов/папок для Android, который возвращает реальный путь к файлу.
Необходим в некоторых специфических случаях. Доступна сортировка и создание нового каталога.
* библиотека листинга файлов;
* библиотека навигации по файловой системе.

Всё это в облачном (пока только Яндекс.Диск) и локальном вариантах, с единообразным использованием.

### Подключение
#### Временное решение
Проект временно не собирается на сервере JitPack, поэтому для использования придётся опубликовать его в локальный m2-репозиторий, добавив и запустив gradle-конфигурацию запуска, 
![publish_to_maven_local](https://github.com/aakumykov/file_lister_navigator_selector/assets/7845834/8ddbe05b-4cfa-4f2c-84d8-9c463c5037e3)

А затем дописав источник mevenLocal() в settings.gradle, например:
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven{ url "https://jitpack.io" }
    }
}
```
#### Нормальное подключение
Через Gradle из репозитория [JitPack](https://jitpack.io/#aakumykov/file_lister_navigator_selector):
```
    implementation 'com.github.aakumykov:storage_access_helper:1.0.1-alpha'
```
### Использование

Смотрите демо-приложение в этом проекте.

|Параметры выбора|Выбор|Результаты выбора|
|----------------|-----|-----------------|
|![Screenshot_1717147030](https://github.com/aakumykov/file_lister_navigator_selector/assets/7845834/0e8f9ee3-c4c0-4429-b2ea-68f418bfffdc)|![Screenshot_1717147714](https://github.com/aakumykov/file_lister_navigator_selector/assets/7845834/9c7bb9c7-6bfd-4fa9-933e-e777fc5784ff)|![Screenshot_1717147485](https://github.com/aakumykov/file_lister_navigator_selector/assets/7845834/5babe2c8-477e-4d2c-a3d7-997d6d4f76fd)|
