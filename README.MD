## This is a microservice-based application built with Spring Boot and Kubernetes using Google Kubernetes Engine (GKE)

_NOTE: If you're interested, Google Cloud Platform offers 300$ worth of cloud services for free when signing up. I used it to develop this app with GKE._

### Detailed tech stack:
- Kubernetes for container orchestration
- Spring Cloud OpenFeign (open-source successor of Netflix Feign) for inter-service communication and load balancing - between the currency and exchange services
- Resilience4j (inspired by Netflix Hystrix) for mechanisms like retry, circuit breaker, rate limiter, bulkhead
- Docker for containerization
- Spring Data JPA for interacting with data
- H2 Database for in-memory database
- Lombok for logging and for eliminating boilerplate POJO coding
- Mapstruct for mapping entities to DTOs and vice-versa

## Purpose
The purpose of this app is to demonstrate the use of the tech stack described above. It's focused on the
technical infrastructure rather than business logic. Therefore, there are no automated tests, but it can be tested manually
by running `watch -n 0.1 curl http://34.66.241.150:8100/conversion/from/USD/to/EUR/quantity/10` in the google cloud shell (to stress test the CPUs of the nodes and force horizontal autoscaling)

## Deployment:
1. Install Google Cloud SDK Shell and connect to your cloud project using `gcloud container clusters get-credentials devmission-cluster --zone us-central1-c --project <project-name`
2. Create Kubernetes deployments by navigating to `currency-conversion-service/deployments` and `currency-exchange-service/deployments` folders and run `kubectl apply -f <deployment-file-name>.yaml`. 

_NOTE: Both services provide multiple yaml files you can play with. The higher their number, the more advanced the deployment strategy. It's best if you use the advanced ones. I kept the rest for personal learning purposes._

3. If not already exposed (no external ip is available when running `kubectl get services`), run `kubectl expose deployment currency-conversion --type=LoadBalancer --port=8000`. Use port `8000` for currency-exchange-service
and `8100` for currency-conversion-service.
4. Go to the Google Cloud Console (UI) and open a cloud shell terminal, then run `watch -n 0.1 curl http://34.66.241.150:8100/conversion/from/USD/to/EUR/quantity/10`. This will perform a request every 0.1 seconds and stress test the CPUs of the nodes, forcing horizontal autoscaling.

_NOTE: Horizontal autoscaling is configured for the `currency-exchange-service` only in `deployment-04-autoscale-configured.yaml`._

_NOTE: All the docker images you need wil be pulled from my public docker registry https://hub.docker.com/u/eduardn08. The images are listed below._

## Images

Reuse these images instead of creating and pushing new container images:

- Currency Exchange Service 
  - v11 - eduardn08/microservices-poc-currency-exchange-service:0.0.11-SNAPSHOT
  - v12 - eduardn08/microservices-poc-currency-exchange-service:0.0.12-SNAPSHOT
- Currency Conversion Service
    - eduardn08/microservices-poc-currency-conversion-service:0.0.11-SNAPSHOT
      - Uses CURRENCY_EXCHANGE_SERVICE_HOST - env variable created by Kubernetes
    - eduardn08/microservices-poc-currency-conversion-service:0.0.12-SNAPSHOT
      - Uses CURRENCY_EXCHANGE_URI - custom env variable (hardcoded in deployment 02; created with a configmap in deployment 03)
      
Again, these images are publicly available at https://hub.docker.com/u/eduardn08.

## URLS

_NOTE: If the apps are locally deployed use `localhost`. Otherwise, use the cluster external ip address._

#### Currency Exchange Service
- http://localhost:8000/exchange/from/USD/to/EUR
- http://cluster-external-ip:8000/exchange/from/USD/to/EUR


#### Currency Conversion Service
- http://localhost:8100/conversion/from/USD/to/EUR/quantity/10
- http://cluster-external-ip:8100/conversion/from/USD/to/EUR/quantity/10

#### Health
- http://cluster-external-ip:port/actuator/health/
- http://cluster-external-ip:port/actuator/health/liveness
- http://cluster-external-ip:port/actuator/health/readiness

#### Commands

The following commands were used in the development process.

```
gcloud container clusters get-credentials devmission-cluster --zone us-central1-c --project data-rush-341916

kubectl create deployment currency-exchange --image=eduardn08/microservices-poc-currency-exchange-service:0.0.11-SNAPSHOT
kubectl expose deployment currency-exchange --type=LoadBalancer --port=8000
kubectl get svc
kubectl get services
kubectl get pods
kubectl get po
kubectl get replicaset
kubectl get rs
kubectl get all

kubectl create deployment currency-conversion --image=eduardn08/microservices-poc-currency-conversion-service:0.0.11-SNAPSHOT
kubectl expose deployment currency-conversion --type=LoadBalancer --port=8100

kubectl get svc --watch

kubectl get deployments

kubectl get deployment currency-exchange -o yaml >> deployment.yaml 
kubectl get service currency-exchange -o yaml >> service.yaml 

kubectl diff -f deployment.yaml
kubectl apply -f deployment.yaml

kubectl delete all -l app=currency-exchange
kubectl delete all -l app=currency-conversion

kubectl rollout history deployment currency-conversion
kubectl rollout history deployment currency-exchange
kubectl rollout undo deployment currency-exchange --to-revision=1

kubectl logs currency-exchange-9fc6f979b-2gmn8
kubectl logs -f currency-exchange-9fc6f979b-2gmn8 

kubectl autoscale deployment currency-exchange --min=1 --max=3 --cpu-percent=5 (autoscale horizontally to max 3 pods if CPU usage is greater than 5%)
kubectl get hpa

kubectl top pod
kubectl top nodes
kubectl get hpa
kubectl delete hpa currency-exchange

kubectl create configmap currency-conversion --from-literal=CURRENCY_EXCHANGE_URI=http://exchange
kubectl get configmap

kubectl get configmap currency-conversion -o yaml >> configmap.yaml

watch -n 0.1 curl http://34.66.241.150:8100/conversion/from/USD/to/EUR/quantity/10 (in the cloud shell; to stress test the CPUs of the pods and force horizontal autoscaling)

docker push eduardn08/microservices-poc-currency-conversion-service:0.0.12-SNAPSHOT
docker push eduardn08/microservices-poc-currency-exchange-service:0.0.12-SNAPSHOT
```
