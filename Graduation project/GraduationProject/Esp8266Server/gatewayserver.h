#ifndef GatewayServer_H
#define GatewayServer_H

/*视频服务类*/
#include <QObject>
#include <QTcpServer>
#include "Gatewaysocket.h"

class GatewayServer : public QTcpServer
{
    Q_OBJECT
private:
    explicit GatewayServer(QObject *parent = nullptr);
public:
    static GatewayServer *getServer();

signals:
    void sendImageSignal(const QImage& image);
protected:
    void incomingConnection(qintptr handle);
signals:
    void ctrlDataSignal(const QJsonObject &json);
public slots:
private:
    static GatewayServer *server;
};

#endif // GatewayServer_H
