#include "database.h"
#include <QDebug>

Database::~Database()
{

}

Database::Database(QObject *parent) : QObject(parent)
{

}

Database *Database::getDbptr()
{
    static Database db;//栈区--->数据区
    return &db;

}

void Database::initMysql()
{
    //1.加载驱动
    QSqlDatabase db = QSqlDatabase::addDatabase("QMYSQL");
    //2.设置IP地址
    db.setHostName("127.0.0.1");//ip或者域名都行
    //3.设置端口号
    db.setPort(3306);
    //4.设置用户名
    db.setUserName("root");
    //5.密码
    db.setPassword("root");
    //6.指定 库名
    db.setDatabaseName("bjpowernode");
    //7.打开数据库
    if(db.open()){
        qDebug()<<"数据库打开成功！";

    }else{
        qDebug()<<"打开数据库失败:"<<db.lastError().text();

    }
}

void Database::createAccountTable()
{
    QString cmd = "CREATE TABLE user("
                  "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                  "account CHAR(9) UNIQUE,"
                  "password CHAR(50) NOT NULL);";

    QSqlQuery query;

    if(query.exec(cmd)){
        qDebug()<<"user建表成功";

    }else{
        qDebug()<<"user建表失败："<<query.lastError().text();
    }
}

void Database::createElectricTable()
{
    QString cmd = "CREATE TABLE Electric("
                  "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                  "name CHAR(50) UNIQUE,"
                  "state CHAR(50) NOT NULL);";

    QSqlQuery query;

    if(query.exec(cmd)){
        qDebug()<<"Electric建表成功";

    }else{
        qDebug()<<"Electric建表失败："<<query.lastError().text();
    }
}

//注册
bool Database::registerUser(const QString &account, const QString &password)
{
    QString cmd = "insert into user(account,password) "
                  "values('%1','%2');";
    cmd = cmd.arg(account,password);
    QSqlQuery query;
    if(query.exec(cmd)){
        qDebug()<<account<<password<<"注册成功！";
        return true;
    }else{
        qDebug()<<account<<"注册失败："<<query.lastError().text();
        return false;
    }
}

bool Database::judgeLogin(const QString &account, const QString &password)
{
    QString cmd = "select password from user where account = '%1';";
    cmd = cmd.arg(account);
    QSqlQuery query;
    if(query.exec(cmd)){//语法没错

        if(query.next()){ //账号存在

                if(query.value(0).toString() == password){//密码也正确
                    qDebug()<<account<<"登录成功！";
                    return true;
                 }
        }
    }else{

        qDebug()<<"登录SQL语法错误:"<<query.lastError().text();

    }
    return false;
}


