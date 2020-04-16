#include "protocol.h"

Protocol::Protocol(QObject *parent) : QObject(parent)
{

}

QByteArray Protocol::packJsonData(int type, QString key1, QString value1, QString key2, QString value2, QString key3, QString value3, QString key4, QString value4, QString key5, QString value5)
{
    QJsonObject json;

    json.insert("type",type);
    if(!key1.isEmpty()){
        json.insert(key1,value1);
    }
    if(!key2.isEmpty()){
        json.insert(key2,value2);
    }
    if(!key3.isEmpty()){
        json.insert(key3,value3);
    }
    if(!key4.isEmpty()){
        json.insert(key4,value4);
    }
    if(!key5.isEmpty()){
        json.insert(key5,value5);
    }

    QJsonDocument jsonDoc(json);

    return jsonDoc.toJson();


}

QByteArray Protocol::packArrayData(int type, const QString &key, const QJsonArray &array)
{
    QJsonObject json;
    json.insert("type",type);
    json.insert(key,array);
    QJsonDocument jsonDoc(json);
    return jsonDoc.toJson();
}

//根据时间生成一个随机字符串，然后编码生成一个密钥
QString Protocol::creatKey()
{
    QString time = QDateTime::currentDateTime()
            .toString("yyyy-M月-dd h:mm:szzz");
    int num = rand()%time.size();//
    time = time.mid(time.size()-num-1,num+1);
    //MD5编码
    QByteArray data = time.toLocal8Bit();
    data = QCryptographicHash::hash(data,QCryptographicHash::Md5);
    return data.toHex();
}
