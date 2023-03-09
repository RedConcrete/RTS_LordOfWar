PHONY: prepare run test docker-start docker-stop

all:

prepare:
	go mod tidy
	go fmt ./...
	go vet ./...
	go mod download

build:
	go build -o ./bin ./cmd/main.go

run: prepare
	go run ./cmd/main.go

# todo: adjust, when testing is completed
test:
	go test

docker-start:
	docker build -t rts-go-server .
	docker run --name lord-of-war rts-go-server

docker-stop:
	docker stop lord-of-war
	docker rm lord-of-war
	docker image rm rts-go-server