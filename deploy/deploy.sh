#!/bin/bash

sbt dist
scp -i ~/.ssh/aws-priv.pem target/universal/easylancer-core-1.0-SNAPSHOT.zip ubuntu@18.219.163.221:/home/ubuntu/app
ssh -i ~/.ssh/aws-priv.pem ubuntu@18.219.163.221 "pkill -9 java"
ssh -i ~/.ssh/aws-priv.pem ubuntu@18.219.163.221 "rm /home/ubuntu/app/easylancer-core-1.0-SNAPSHOT/RUNNING_PID"
ssh -i ~/.ssh/aws-priv.pem ubuntu@18.219.163.221 "rm -rf /home/ubuntu/app/easylancer-core-1.0-SNAPSHOT"
ssh -i ~/.ssh/aws-priv.pem ubuntu@18.219.163.221 "unzip /home/ubuntu/app/easylancer-core-1.0-SNAPSHOT.zip -d /home/ubuntu/app"
ssh -i ~/.ssh/aws-priv.pem ubuntu@18.219.163.221 "nohup /home/ubuntu/app/easylancer-core-1.0-SNAPSHOT/bin/easylancer-core"
