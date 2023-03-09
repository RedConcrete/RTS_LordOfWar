FROM golang:latest
WORKDIR /server
COPY ./ ./
RUN go mod download
RUN go build -o ./bin ./cmd/main.go
CMD [ "./bin/main" ]