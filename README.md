# Alt as Ctrl (Fabric)

Клиентский мод: действия с Ctrl (Ctrl+Q выбросить стак, Ctrl+клик, Ctrl+A/C/V/X) работают с Alt.
Только клиент: друзьям мод ставить не нужно, на чужие сервера тоже можно.

Версии Minecraft: 1.19.2 - 1.21.8 (собран под 1.19.2). Нужен Fabric API.
Для 1.21.9+ нужна отдельная версия: там Ctrl передаётся через KeyEvent.

Настройка: config/alt-as-ctrl.properties, altOnly=true -> настоящий Ctrl в GUI перестаёт работать как Ctrl.
