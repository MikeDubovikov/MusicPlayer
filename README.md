# Music Player

Это музыкальное приложение, которое использует запросы к [Deezer Api](https://developers.deezer.com/api). 
Приложение позволяет:

- Искать треки.
- Воспроизводить треки (в разработке).
- Хранить данные о треках в локальной базе данных с использованием Room.
- Скачивать треки в локальное хранилище для оффлайн воспроизведения.

## Libraries
- [Kotlin](https://kotlinlang.org): язык программирования.
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlinlang.org/docs/flow.html): для асинхронных операций.
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization): для сериализации данных.
- [Navigation Component](https://developer.android.com/guide/navigation): для навигации между экранами.
- [Retrofit](https://github.com/square/retrofit): для выполнения сетевых запросов.
- [Dagger](https://github.com/google/dagger): фреймворк для внедрения зависимостей.
- [Room](https://developer.android.com/training/data-storage/room): для хранения скачанных треков.
- [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview): для постраничной загрузки данных.
- [Glide](https://github.com/bumptech/glide): для загрузки и отображения изображений.
- Архитектура
    - MVVM
    - Чистая архитектура

## Установка и запуск проекта
- Клонируйте репозиторий.
- Откройте проект в Android Studio.
- Установите зависимости через Gradle Sync.
- Запустите проект на устройстве или эмуляторе.

## Скриншоты

<div style="display: flex; width: 100%">
<img src="screenshots/Tracks%20Screen.jpg" width="30%"/>
<img src="screenshots/Player%20Screen.jpg" width="30%"/>
<img src="screenshots/Downloads%20Screen.jpg" width="30%"/>
</div>

## Проблемы при разработке приложения

1. Плеер: Не успел полностью реализовать плеер. Логика сервиса написана, но дальше времени не хватило. Никогда до это с подобным не работал.

2. Тестирование: С тестированием знаком крайне мало. Чтобы протестировать многомодульный проект нужны более глубокие знания.

3. Dagger: Возникли проблемы с предоставлением контекста в сабкомпонент.