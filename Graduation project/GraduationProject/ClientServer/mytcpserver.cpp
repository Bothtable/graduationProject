#include "mytcpserver.h"
MyTcpServer*MyTcpServer::server = NULL;
MyTcpServer::MyTcpServer(QObject *parent) : QTcpServer(parent)
{

}

MyTcpServer::~MyTcpServer()
{

}

//为客户端分配套接字
void MyTcpServer::incomingConnection(qintptr handle)
{
    MyTcpSocket *socket = new MyTcpSocket;

    //设置套接字描述符
    socket->setSocketDescriptor(handle);

    //socket -> tcpserver 的绑定
    //
    connect(socket ,SIGNAL(reLoginSignal(QString,MyTcpSocket*)),
            this,SIGNAL(reLoginSignal(QString,MyTcpSocket*)));
    connect(this,SIGNAL(reLoginSignal(QString,MyTcpSocket*)),
            socket,SLOT(receiveReLoginSlot(QString,MyTcpSocket*)));
    //

    //保存新上线信息1
    connect(socket,SIGNAL(newOnlineSignal(QString,QJsonObject)),
            this,SLOT(newOnlineSlot(QString,QJsonObject)));
    //下线信息绑定1   
    connect(socket,SIGNAL(offLineSignal(QByteArray,QString)),
            this,SLOT(offLineSlot(QByteArray,QString)));

}

void MyTcpServer::newOnlineSlot(const QString &account, const QJsonObject &json)
{
    //保存信息
    hash.insert(account,json);
}

//删除hash中的下线者得信息
void MyTcpServer::offLineSlot(const QByteArray &data, const QString &account)
{
    Q_UNUSED(data);
    hash.remove(account);
}

MyTcpServer *MyTcpServer::getServer()
{
    if(server == NULL){
        server = new MyTcpServer;
    }
    return server;
}













