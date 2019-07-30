
# Приложение по переводу денег от одного аккаунта к другому

## Запуск

```
mvn clean package
mvn exec:java -DsettingsDir=.
```

Приложение будет слушать на порту прописанном в файле `moneytransfer.properties` (сейчас 8080) 


## Примеры вызовов http api

```bash
curl --data "money=1000" 127.0.0.1:8080/account
  # {"id":0,"money":1000}
  
curl --data "money=2000" 127.0.0.1:8080/account
  # {"id":1,"money":2000}

curl 127.0.0.1:8080/account/0
  # {"id":0,"money":1000}

curl 127.0.0.1:8080/account/1
  # {"id":1,"money":2000}


curl --data "money=1000" 127.0.0.1:8080/moneyTransfer/from/1/to/0

curl 127.0.0.1:8080/account/0
  # {"id":0,"money":2000}

curl 127.0.0.1:8080/account/1
  # {"id":1,"money":1000}
  
curl --data "money=2000" 127.0.0.1:8080/moneyTransfer/from/0/to/1

curl 127.0.0.1:8080/account/0
  # {"id":0,"money":0}

curl 127.0.0.1:8080/account/1
  # {"id":1,"money":3000}
```

## Краткое описание технологий

Для приема запросов используется jetty + jersey сервлет.  
Все данные по аккаунтам хранятся в обычной мапе в памяти.  
В качестве IoC контейнера используется Guice.
 