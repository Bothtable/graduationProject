#include "gatewaysocket.h"

GatewaySocket::GatewaySocket(QObject *parent) : QTcpSocket(parent)
{
    connect(this,SIGNAL(readyRead()),this,SLOT(readSlot()));
    connect(this,SIGNAL(disconnected()),this,SLOT(dicConnectSlot()));
}

void GatewaySocket::tranSlateData(const QJsonObject &json)
{
    QString type = json.value("type").toString();

}

void GatewaySocket::readSlot()
{
    data.append(this->readAll());
    while (data.contains("\n}\n")) {
        QByteArray temp = data.mid(0,data.indexOf("\n}\n")+3);
        data.remove(0,temp.size());
        QJsonDocument jsonDoc = QJsonDocument::fromJson(temp);
        QJsonObject json = jsonDoc.object();
        tranSlateData(json);

    }
}

//断开连接
void GatewaySocket::dicConnectSlot(){
    this->disconnect();
    this->deleteLater();

}

//接受到控制数据的槽函数（
void GatewaySocket::recevieCtrlDataSlot(QJsonObject &json)
{
    //1、判断控制数据是不是属于自己对应的网关，即this.boxID和json中boxID是否一直
    QString boxID = json.value("boxId").toString();//你们是不是boxId
    if(boxID == this->boxID){//说明本条控制数据属于自己对应的网关
        //
        QJsonDocument jsonDoc(json);
        this->write(jsonDoc.toJson());

    }
}
