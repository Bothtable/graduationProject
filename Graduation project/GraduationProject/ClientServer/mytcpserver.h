#ifndef MYTCPSERVER_H
#define MYTCPSERVER_H
/*自定义服务类*/
#include <QObject>
#include <QTcpServer>
#include <QHash>    //哈希
#include "mytcpsocket.h"


class MyTcpServer : public QTcpServer
{
    Q_OBJECT
public:
    explicit MyTcpServer(QObject *parent = nullptr);
    ~MyTcpServer();

protected:
    void incomingConnection(qintptr handle);

signals:
    void reLoginSignal(const QString& account,MyTcpSocket *socket);
public slots:
    void newOnlineSlot(const QString& account,const QJsonObject& json);
    void offLineSlot(const QByteArray& data,const QString& account);

public:
    static MyTcpServer *getServer();


private:
    QHash<QString,QJsonObject> hash;
    static MyTcpServer *server;

};

#endif // MYTCPSERVER_H
