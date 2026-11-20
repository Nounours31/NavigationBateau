# Stage 1: Build the binary
FROM golang:1.26-alpine3.23 AS builder

WORKDIR /app

# Copy dependency files first for better layer caching
COPY go.mod go.sum ./
RUN go mod download

# Copy source code and build a statically linked binary
COPY . .
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
    go build -ldflags="-s -w" -o server ./cmd/server

# Stage 2: Create the minimal runtime image
FROM alpine:3.23

# Add ca-certificates for HTTPS and a non-root user
RUN apk --no-cache add ca-certificates && \
    adduser -D -g '' appuser

WORKDIR /app
COPY --from=builder /app/server .

# Run as non-root
USER appuser

EXPOSE 8080
CMD ["./server"]
