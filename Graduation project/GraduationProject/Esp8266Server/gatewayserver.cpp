#include "gatewayserver.h"
GatewayServer*GatewayServer::server = NULL;

GatewayServer::GatewayServer(QObject *parent) : QTcpServer(parent)
{

}

GatewayServer *GatewayServer::getServer()
{
    if(server == NULL){
        server = new GatewayServer;
    }
    return server;
}

void GatewayServer::incomingConnection(qintptr handle)
{
    GatewaySocket *socket = new GatewaySocket;
    socket->setSocketDescriptor(handle);

    //
    connect(this,SIGNAL(ctrlDataSignal(QJsonObject)),
            socket,SLOT(recevieCtrlDataSlot(QJsonObject)));


}
