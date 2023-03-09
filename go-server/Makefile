PHONY: prepare run test

all:

prepare:
	go mod tidy
	go fmt ./...
	go vet ./...

build:
	go build -o ./bin ./cmd/main.go

run: prepare build
	go run ./cmd/main.go

test:
	go test

docker:
	docker build ./build/Dockerfile