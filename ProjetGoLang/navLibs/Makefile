# choco install make
VERSION=0.0.1

hello:
	echo "Hello $(VERSION)"

build:
	go mod tidy
	go build -o bin/main cmd/main.go

run:
	build
	go run cmd/main.go

telemetry:
	go telemetry [off|local|on]

compile:
	echo "Compiling for every OS and Platform"
	GOOS=linux   GOARCH=arm   go build -o bin/main-linux-arm   main.go
	GOOS=linux   GOARCH=arm64 go build -o bin/main-linux-arm64 main.go
	GOOS=freebsd GOARCH=386   go build -o bin/main-freebsd-386 main.go

# https://go.dev/wiki/CodeTools
lint:
	# https://golangci-lint.run/docs/configuration/file/ 
	# choco install golangci-lint
	golangci-lint fmt
	golangci-lint run ./...

test:
	go test ./...

vet:	
	# Vet examines Go source code and reports suspicious constructs
	go vet ./...

all: 
	hello 
	vet
	build
	test