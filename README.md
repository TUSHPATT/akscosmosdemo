# akscosmosdemo
Aks cosmos demo app 

Push image to your preferred  docker registry for my testing, I pushed to docker hub

If you are using AZ cli 

Get your AKS credentials by running following command

az aks get-credentials -n  <<your_aks_cluster_name>>  --resource-group  <<resource_group_where_aks_cluster_is_running>>

Create namespece object, sample uploaded part of the project

kubectl apply -f <namespace_deployment>.yaml 

Deploy akscosmosdemo using following command

kubectl apply -f <service_deploy>.yaml -n <your_namespace_where_you_want_to_deploy>

Get the loadbalancer endpoint and port by running this command

kubectl get service 

kubectl get service akscosmosdemo -n <your_namespace>

pick the external ip and port 

Test your service 

curl -H 'Content-Type: application/json' -X PUT --data '{<<YOUR_JSON_DOC>>}'  <REQUEST_URI>
