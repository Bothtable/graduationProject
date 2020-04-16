#ifndef GatewaySocket_H
#define GatewaySocket_H
/*网关套接字类*/
#include <QObject>
#include <QTcpSocket>
#include <QIODevice>
#include <QJsonObject>
#include <QJsonDocument>
#include <QBuffer>
#include <QDateTime>
#include "Database/database.h"

class GatewaySocket : public QTcpSocket
{
    Q_OBJECT
public:
    explicit GatewaySocket(QObject *parent = nullptr);
    void tranSlateData(const QJsonObject& json);

signals:

public slots:
    void readSlot();//读数据
    void dicConnectSlot();//断开连接
    void recevieCtrlDataSlot(QJsonObject &json);

private:
    bool flag = false;
    QString     boxID;  //网关编号
    QByteArray  data;

    //

};

#endif // GatewaySocket_H
