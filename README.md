### Задача
Есть папка с файлами.
При помощи средств Apache Camel реализовать следующий процесс:
файл достается из папки, далее,
в зависимости от типа, выполняются некоторые действия.
Если файл имеет расширение xml, то его необходимо его содержимое отправить в очередь в брокере ActiveMQ.
Если он имеет расширение txt, то его необходимо отправить в брокер, а так же записать в таблицу в БД.
Если расширение другое - выбрасывается исключение и отправлять файл в очередь invalid-queue в ActiveMQ.
При обработке каждого сотого файла отсылать письмо, содержащее количество файлов txt, количество файлов xml,
количество нераспознанных файлов, а так же время обработки пачки сообщений.
Для гарантированной доставки следует сконфигурировать и подключить менеджер распределенных транзакций (Bitronix TM).
Предполагаемое время решения - 10-15 ч.

### Пометка
В силу малого количества файлов в папке data письмо об обработке файлов отправляется каждые 3 файла.
<br>Email получения этого сообщения можно изменить в /resources/application.properties