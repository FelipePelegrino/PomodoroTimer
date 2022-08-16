# PomodoroTimer

* Uma aplicação desenvolvida para o estudo da linguagem kotlin e diversas técnicas da programação mobile para Android.

<img src="/github/Running.gif" width="500">

### Tecnologias | Ferramentas

* CustomView
* DialogFragment
* AlertDialog with NumberPicker
* MediaPlayer
* CountDownTimer
* Service
  * Foreground service
* Notification
* RoomDatabase
* LiveData
* StateFlow
* Coroutines
* KotlinExtensions
* DarkTheme
* Strings pt/en

### Funcionamento

* O app respeita os ciclos Pomodoros conforme configurado na tela de configurações (...)
* Configuração default starta o app com:
  * 25min de foco
  * 5min de intervalo curto
  * 15 de intervalo longo
  * 4 ciclos pomodoros até o intervalo longo
  * Temporizador automático habilitado
  * Som desabilitado
* Ao miminizar o app continua contabilizando o tempo em foreground, exibindo notificação com o tempo atualizado
* Caso o temporizador esteja pausado/parado e o usuário não fez nenhuma ação nos ultimos 90segundos, a notificação será desabilitada automaticamente.

<img src="/github/Settings.gif" width="500">

### Referências

* Layout presente no [figma](https://www.figma.com/community/file/1112830528857083939)
