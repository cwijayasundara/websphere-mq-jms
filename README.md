You need a Websphere mq broker running in your local.

From Docker execute

docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 \
             --publish 1414:1414 \
             --publish 9443:9443 \
             --detach \
             ibmcom/mq