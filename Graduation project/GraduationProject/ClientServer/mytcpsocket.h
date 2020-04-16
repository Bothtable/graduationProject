#ifndef MYTCPSOCKET_H
#define MYTCPSOCKET_H
/*自定义套接字类*/
#include <QObject>
#include <QTcpSocket>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>
#include <QHostAddress>
#include <QDateTime>
#include "Database/database.h"
#include "protocol.h"

class MyTcpSocket : public QTcpSocket
{
    Q_OBJECT
public:
    explicit MyTcpSocket(QObject *parent = nullptr);
    ~MyTcpSocket();
signals:

    void reLoginSignal(const QString& account,MyTcpSocket *socket);

    void newOnlineSignal(const QString& account,
                         const QJsonObject& data);

    void offLineSignal(QByteArray json,QString account);
public slots:
    void readSlot();//读取数据
    void disConnectSlot();//断开连接是的槽函数
    void receiveReLoginSlot(const QString& account,
                            MyTcpSocket *socket);

    void handleLight(const QJsonObject& json);
    void handleHeater(const QJsonObject& json);
    void handleAircondition(const QJsonObject& json);
    void handleKettle(const QJsonObject& json);

private:
    //验证密钥
    bool judgeKey(const QString& key);
    //数据解析
    void translateData(const QByteArray& data);

    //登录处理
    void handleLoginData(const QJsonObject& json);

private:
    QString account;//当前套接字对应的学号
    QString key;//登陆成功后 生成的密钥

};

#endif // MYTCPSOCKET_H
