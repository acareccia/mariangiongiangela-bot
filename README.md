# Mariangiongiangela Bot
Dumb Telegram Bot

## Telegram Bot API
Using [Telegram bot API](https://core.telegram.org/bots)

## Build && Run

### Build 

`mvn clean install`

### Run

`docker-compose.yml up -d`

### Build optimized container:
`mvn spring-boot:build-image`

or

`docker build -f Dockerfile.optimized -t com.ilfalsodemetrio/mariangiongiangela/mariangiongiangela:0.0.1 .`

### List layers

`java -Djarmode=layertools -jar target//mariangiongiangela-0.0.1.jar list`

