#ifndef PROTOCOL_H
#define PROTOCOL_H
/*网络协议类*/
#include <QObject>
#include <QJsonObject>
#include <QJsonDocument>
#include <QJsonArray>
#include <QCryptographicHash>
#include <QDateTime>
#include <QDebug>



class Protocol : public QObject
{
    Q_OBJECT
public:
    explicit Protocol(QObject *parent = nullptr);
    static QByteArray packJsonData(int type,
                                   QString key1="",QString value1="",
                                   QString key2="",QString value2="",
                                   QString key3="",QString value3="",
                                   QString key4="",QString value4="",
                                   QString key5="",QString value5=""
                                   );
    static QByteArray packArrayData(int type,
                                    const QString& key,
                                    const QJsonArray& array);

    static QString creatKey();
signals:

public slots:

};

#endif // PROTOCOL_H
