AppGameBall
---

Forked [here](https://github.com/Denis-Gluhov/GameBall)

------

Внесенные изменения:

- Изменен способ смена активности([`changeCompatActivity`](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/Manager.java#L8))
- Поля отображения очков и оставшегося времени работают корректно.
- Изменена система проигрывания звуков ([отдельно](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/MainActivity.java#L155) для последних `sdk`)
- Изменена [логика](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/Ball.java#L39) "убийства" мячей (Теперь попадание тапом по мячу гарантирует его уничтожение)

Добавленные корректировки:

- Режим игры "Бесконечный" (Отсутствует ограничение по времени)
- Возможность гибкой настройки размера мячов и выбора режима игры [перед непосредственным ее началом](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/ChooseDifficultyActivity.java#L15)
- [Таймер обратного отсчета](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/MainActivity.java#L126)
- [Функционал](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/PhoneDataStorage.java) для взаимодействия с локальным файлом настроек приложения
- Добавлена возможность возобновить незавершенную игру (Также в [MainActivity](https://github.com/Velkonost/GameBall/blob/master/app/src/main/java/ru/myitschool/appgameball/MainActivity.java#L35))
- Добавлена система подсчета очков([подробнее](#score))

---

<a name="score">Набор очков</a> :

При уничтожении мяча, к уже набранным очкам добавляется время, оставшееся до конца игры.

Тем самым, чем раньше игрок лопнет все мячи, тем больше очков он наберет.

---

Все строковые данные вынесены из выполняемых файлов в `values`

Для констант образован отдельный класс