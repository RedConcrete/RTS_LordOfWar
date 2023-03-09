PHONY: prepare run test

all:

prepare:
	go mod tidy
	go fmt ./...
	go vet ./...

build:
	go build

run: prepare build
	./go-server

test:
	go test