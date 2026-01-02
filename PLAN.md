# gRPC Client-Server Demo Plan

## Current State

### What You Have

- ✅ Proto definitions with `GreetingService` (Greet RPC)
  - Location: `proto/src/main/proto/greeting/v1/greeting.proto`
  - Service: Unary RPC that takes a name and returns a greeting message
- ✅ Proto module with proper Gradle build configuration
- ✅ Server module scaffold at `services/server/`
- ✅ All dependencies configured (gRPC, Kotlin coroutines, Netty)

### What's Missing

- ❌ Actual gRPC server implementation
- ❌ Client module and implementation
- ❌ Configuration for server port
- ❌ Instructions for running the demo

---

## Implementation Plan

### Phase 1: Implement the gRPC Server

**File:** `services/server/src/main/kotlin/org/example/App.kt`

**Tasks:**

1. Create `GreetingServiceImpl` class that extends `GreetingServiceCoroutineImplBase`
2. Override the `greet()` method to:
   - Receive a `GreetRequest` with a name
   - Return a `GreetResponse` with a personalized greeting message
3. Update the `main()` function to:
   - Create a gRPC server instance using `ServerBuilder`
   - Add the `GreetingServiceImpl` as a service
   - Configure the server to listen on a specific port (default: 50051)
   - Start the server and wait for termination
4. Add proper logging for server startup/shutdown

**Dependencies:** Already in place (grpc-netty, proto module)

---

### Phase 2: Create the Client Module

**Location:** `services/client/`

**Tasks:**

1. Add `include("services:client")` to `settings.gradle.kts`
2. Create `services/client/build.gradle.kts` with:
   - Kotlin JVM plugin
   - Application plugin
   - Dependencies: `:proto`, `grpc-netty`, `grpc-kotlin-stub`
   - Main class configuration
3. Create `services/client/src/main/kotlin/org/example/Client.kt` with:
   - Channel creation (connects to localhost:50051)
   - Stub creation (`GreetingServiceCoroutineStub`)
   - Call to `greet()` method with a sample name
   - Display the response message
   - Proper channel shutdown

---

### Phase 3: Testing the Demo

**Tasks:**

1. Build the entire project: `./gradlew build`
2. Start the server: `./gradlew :services:server:run`
3. In a separate terminal, run the client: `./gradlew :services:client:run`
4. Verify the client receives the greeting from the server
5. Update README.md with demo instructions

---

## Design Decisions - CONFIRMED ✅

### 1. Client Module Structure

- ✅ **SELECTED:** Create separate `services/client` module
- Clean separation, can run independently, follows microservices pattern

### 2. Server Port Configuration

- ✅ **SELECTED:** Port 50051 (gRPC standard for demos)
- Hardcoded in both server and client for simplicity

### 3. Client Behavior

- ✅ **SELECTED:** Simple one-shot client
- Sends a single request with a name (from command-line arg or default), displays response, exits

### 4. Error Handling

- ✅ **SELECTED:** Minimal error handling
- Basic try-catch for connection errors, keeps demo code clean and focused

---

## Detailed Implementation Steps

### Step 1: Implement the Server

**File:** `services/server/src/main/kotlin/org/example/App.kt`

**What to implement:**

1. Create `GreetingServiceImpl` class extending `GreetingServiceCoroutineImplBase`
   - Override `greet(request: GreetRequest): GreetResponse`
   - Create greeting message using `request.name`
   - Return `GreetResponse` with the greeting
2. Modify `main()` function to:
   - Create gRPC server using `ServerBuilder.forPort(50051)`
   - Add `GreetingServiceImpl` service
   - Start server and log startup message
   - Block waiting for termination with shutdown hook

**Estimated code:** ~40 lines

---

### Step 2: Create Client Module Structure

**Actions:**

1. Add to `settings.gradle.kts`: `include("services:client")`
2. Create `services/client/build.gradle.kts`:
   - Apply `kotlin("jvm")` and `application` plugins
   - Set main class to `org.example.ClientKt`
   - Add dependencies:
     - `implementation(project(":proto"))`
     - `implementation("io.grpc:grpc-netty:1.60.1")`
     - `implementation("com.google.guava:guava:33.3.1-jre")`
3. Create directory structure: `services/client/src/main/kotlin/org/example/`

**Estimated work:** Gradle config + directory setup

---

### Step 3: Implement the Client

**File:** `services/client/src/main/kotlin/org/example/Client.kt`

**What to implement:**

1. Create `main()` function with:
   - Get name from args or use default ("World")
   - Create ManagedChannel to `localhost:50051`
   - Create `GreetingServiceCoroutineStub` from channel
   - Use `runBlocking` to call `stub.greet()` with name
   - Print the response message
   - Shutdown channel properly in try-finally block

**Estimated code:** ~30 lines

---

### Step 4: Build and Test

**Commands:**

```bash
# Build everything
./gradlew build

# Terminal 1 - Start server
./gradlew :services:server:run

# Terminal 2 - Run client
./gradlew :services:client:run

# Or with custom name
./gradlew :services:client:run --args="Alice"
```

**Expected output:**

- Server: "Server started, listening on port 50051"
- Client: "Greeting: Hello, World!" (or "Hello, Alice!")

---

### Step 5: Update Documentation

**File:** `README.md`

**Add:**

- Demo instructions with commands for running server and client
- Expected output
- How to pass custom name to client

---

## Success Criteria

- ✅ Server starts and listens on configured port
- ✅ Client connects to server successfully
- ✅ Client sends a GreetRequest with a name
- ✅ Server receives request and sends back GreetResponse
- ✅ Client displays the greeting message
- ✅ Both modules can be run via Gradle commands
- ✅ README documents how to run the demo

---

## Implementation Checklist

- [ ] **Step 1:** Implement server gRPC service (`GreetingServiceImpl`)
- [ ] **Step 2:** Update server main function with gRPC server setup
- [ ] **Step 3:** Create client module Gradle configuration
- [ ] **Step 4:** Implement client code to call the service
- [ ] **Step 5:** Test the demo (run server + client)
- [ ] **Step 6:** Update README with demo instructions
