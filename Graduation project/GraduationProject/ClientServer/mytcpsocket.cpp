#include "mytcpsocket.h"

MyTcpSocket::MyTcpSocket(QObject *parent) : QTcpSocket(parent)
{

    //绑定当对应的客户端有消息时，发出的信号绑定到槽
    connect(this,SIGNAL(readyRead()),this,SLOT(readSlot()));
    connect(this,SIGNAL(disconnected()),this,SLOT(disConnectSlot()));
}

MyTcpSocket::~MyTcpSocket()
{

}

//读取数据
void MyTcpSocket::readSlot()
{
    QByteArray data;
    qDebug()<<"有连接";
    while (this->bytesAvailable()) {

        data.append(this->readAll());
    }
    qDebug()<<data;
    translateData(data);

}

//断开连接
void MyTcpSocket::disConnectSlot()
{
    //判断当前客户端是否登录
    if(!account.isEmpty()){
        qDebug()<<account<<"下线";
        QByteArray data = "11111";
        emit offLineSignal(data,account);
    }
    this->deleteLater();
}

//接受MytcpServer的重复登录信号
void MyTcpSocket::receiveReLoginSlot(const QString &account, MyTcpSocket *socket)
{
    if(this->account == account && this != socket){
       QString ip = this->peerAddress().toString();
       QByteArray data = Protocol::packJsonData(6,"ip",ip);
       this->write(data);
       if(this->waitForBytesWritten()){
            //this->deleteLater();
           disConnectSlot();
       }
    }
}

void MyTcpSocket::handleLight(const QJsonObject &json)
{
    QString key = json.value("key").toString();
    judgeKey(key);
}

void MyTcpSocket::handleHeater(const QJsonObject &json)
{

}

void MyTcpSocket::handleAircondition(const QJsonObject &json)
{

}

void MyTcpSocket::handleKettle(const QJsonObject &json)
{

}

//验证key
bool MyTcpSocket::judgeKey(const QString &key)
{
    if(key != this->key || key.isEmpty()){
        //验证不通过
        this->disconnect();
        this->deleteLater();
        return false;
    }
    return true;
}



//解析数据
void MyTcpSocket::translateData(const QByteArray &data)
{
    //1.格式转换 转换为json对象
    QJsonDocument jsonDoc = QJsonDocument::fromJson(data);
    QJsonObject json = jsonDoc.object();

    //2.提取数据的type值
    int type = json.value("type").toInt();
    //3.判断type值并作出相应的处理
    switch(type){
    //登录数据
    case 0:
        handleLoginData(json);
        break;
    //灯
    case 1:
        handleLight(json);
        break;
    //热水器
    case 2:
        handleHeater(json);
        break;
    //空调
    case 3:
        handleAircondition(json);
        break;
    //热水壶
    case 4:
        handleKettle(json);
        break;
    default:
        break;
    }


}

//登录成功
void MyTcpSocket::handleLoginData(const QJsonObject &json)
{
    QString account = json.value("account").toString();
    QString password =json.value("password").toString();

    if(account.isEmpty() || password.isEmpty()){//登录失败
        QByteArray data = Protocol::packJsonData(0,"result","no");
        this->write(data);
        return;
    }

    //调用数据库的接口
    bool ok = Database::getDbptr()->judgeLogin(account,password);
    QByteArray data;
    if(ok){
        //保存当前的信息到成员变量中
        this->account = account;
        this->key = Protocol::creatKey();
        data = Protocol::packJsonData(0,"result","yes","key",key);

        //发出判断重复登录的信号
        emit reLoginSignal(account,this);


        //准备上线信息，交给MyTcpServer存储hash
        QJsonObject json;
        json.insert("account",account);
        emit newOnlineSignal(account,json);

    }else{
        data = Protocol::packJsonData(0,"result","no");
    }
    this->write(data);//将处理结果反馈给客户端。
}

