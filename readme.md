
# App for money trasfer between accounts

## Test run

```
mvn clean package
mvn exec:java -DsettingsDir=.
```

App starts on port specified in `moneytransfer.properties` file (8080 now) 


## Http api example

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

## Brief description

Http requests are handled by jetty + jersey servlet.  
All account data is stored in memory in simple HashMap.  
Google Guice is used for dependency injection.  
Note: Integer type is used for money amount instead of BigDecimal for simplicity.
