#!/bin/bash

gradle build

dockerImageName=ymw-pay
dockerContainerName=ymw-pay-c
dockerContainerPort=8143


docker stop $dockerContainerName
docker rm $dockerContainerName
docker rmi $dockerImageName

docker build -t $dockerImageName .

docker run --name $dockerContainerName -it -p ${dockerContainerPort}:8080 -d $dockerImageName
