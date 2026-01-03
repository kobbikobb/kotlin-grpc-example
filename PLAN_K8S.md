# Kubernetes Deployment Plan

## Goal
Add kind-based Kubernetes deployment to demonstrate gRPC services running in a container orchestration environment.

## Prerequisites
**Tools needed:**
- [Docker](https://docs.docker.com/get-docker/) - Container runtime
- [kubectl](https://kubernetes.io/docs/tasks/tools/) - Kubernetes CLI
- [kind](https://kind.sigs.k8s.io/docs/user/quick-start/#installation) - Kubernetes in Docker

**Installation guides:**
- Mac: `brew install kind kubectl` (Docker Desktop includes Docker)
- Linux: Follow links above for distribution-specific instructions

## What Changes

### 1. Dockerfiles (2 files)
**Documentation:**
- [Docker Multi-stage builds](https://docs.docker.com/build/building/multi-stage/)
- [Gradle Docker builds](https://docs.gradle.org/current/userguide/building_java_projects.html#sec:building_java_libraries)
- [Kotlin Docker example](https://kotlinlang.org/docs/jvm-get-started.html#run-the-application)
- [Eclipse Temurin images](https://hub.docker.com/_/eclipse-temurin)

**Files:**
- `services/server/Dockerfile`
  - Multi-stage build: Gradle build stage + runtime stage
  - Base: eclipse-temurin:21-jre
  - Expose port 50051

- `services/client/Dockerfile`
  - Similar multi-stage build
  - No exposed ports (client initiates connection)

### 2. Kubernetes Manifests (3-4 files in `k8s/` directory)
**Documentation:**
- [Kubernetes Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
- [Kubernetes Services](https://kubernetes.io/docs/concepts/services-networking/service/)
- [Kubernetes Jobs](https://kubernetes.io/docs/concepts/workloads/controllers/job/)
- [Kubernetes Namespaces](https://kubernetes.io/docs/concepts/overview/working-with-objects/namespaces/)
- [kubectl create job](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_create/kubectl_create_job/)

**Files:**
- `namespace.yaml` - Dedicated namespace (optional but clean)
- `server-deployment.yaml` - Server with 2-3 replicas
- `server-service.yaml` - ClusterIP service for internal DNS
- `client-job.yaml` - (Optional) Example Job template with parameter placeholder
  - Jobs can also be created directly with `kubectl create job` command

### 3. Configuration Changes
**Documentation:**
- [Kotlin command-line arguments](https://kotlinlang.org/docs/command-line.html)
- [Kubernetes DNS for Services](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/)
- [Environment variables in Kotlin](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.system/get-env.html)

**Changes:**
- Server: No code changes needed
- Client: Refactor to accept command-line arguments instead of interactive input
  - Change `suspend fun main()` to `suspend fun main(args: Array<String>)`
  - Take greeting name as first argument: `args.getOrNull(0) ?: "World"`
  - Change connection from `localhost:50051` to use environment variable
  - Use: `System.getenv("GRPC_SERVER") ?: "localhost"` for server address
  - Remove the `while(true)` loop and `readLine()` - send one greeting and exit

### 4. Helper Scripts (1 file)
**Documentation:**
- [kind Quick Start](https://kind.sigs.k8s.io/docs/user/quick-start/)
- [Loading images into kind](https://kind.sigs.k8s.io/docs/user/quick-start/#loading-an-image-into-your-cluster)
- [Bash scripting guide](https://www.gnu.org/software/bash/manual/bash.html)

**File:**
- `scripts/deploy-kind.sh`
  - Create kind cluster (if not exists)
  - Build Docker images
  - Load images into kind
  - Apply K8s manifests
  - Show how to check logs

## Key Decisions

**Client execution:** Run as Kubernetes Job that accepts a parameter
- Takes greeting name as command-line argument
- Sends one greeting and exits
- Can be run multiple times with different names: `kubectl create job greet-alice --from=cronjob/greet-client -- Alice`

**Service discovery:** Server accessible via DNS name `greetings-service` within cluster

**Image tags:** Use `latest` for simplicity (not production best practice, but fine for local demo)

**Namespace:** Use `grpc-demo` namespace to keep things isolated

**Local vs K8s modes:**
- **Option 1:** Single client that works both ways - if args provided, send single greeting; if no args, enter interactive mode
- **Option 2:** Separate entry points - `MainInteractive.kt` for local, `Main.kt` for K8s
- **Recommendation:** Option 1 is simpler and more flexible

## Implementation Steps

1. Refactor client to accept command-line arguments
   - Add `args: Array<String>` to main function
   - Replace interactive loop with single greeting
   - Use environment variable for server address
2. Create Dockerfiles for server and client
3. Create k8s/ directory with manifests
4. Create deployment script
5. Test locally with kind
6. Document usage in README

## Testing the Deployment

```bash
# 1. Deploy everything
./scripts/deploy-kind.sh

# 2. Check server is running
kubectl -n grpc-demo get pods
kubectl -n grpc-demo logs -l app=greetings-server

# 3. Run client job with default parameter
kubectl -n grpc-demo create job greet-world \
  --image=greet-client:latest \
  -- "World"

# 4. Check job logs
kubectl -n grpc-demo logs job/greet-world

# 5. Run with different parameters
kubectl -n grpc-demo create job greet-alice --image=greet-client:latest -- "Alice"
kubectl -n grpc-demo create job greet-bob --image=greet-client:latest -- "Bob"

# 6. View all job results
kubectl -n grpc-demo get jobs
kubectl -n grpc-demo logs -l job-name --prefix=true
```

## Estimated Effort
- Implementation: 30-45 minutes
- Testing and debugging: 15-30 minutes
- Documentation: 15 minutes

Total: ~1-1.5 hours

## Additional Resources
**gRPC and Kubernetes:**
- [gRPC on Kubernetes](https://kubernetes.io/blog/2018/11/07/grpc-load-balancing-on-kubernetes-without-tears/)
- [gRPC Health Checking](https://github.com/grpc/grpc/blob/master/doc/health-checking.md)

**Kotlin specific:**
- [Kotlin with Docker best practices](https://kotlinlang.org/docs/docker.html)
- [Gradle application plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

**Troubleshooting:**
- [kubectl cheat sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)
- [Debugging Kubernetes Deployments](https://kubernetes.io/docs/tasks/debug/debug-application/)
