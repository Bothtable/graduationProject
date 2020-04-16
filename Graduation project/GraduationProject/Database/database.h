#ifndef DATABASE_H
#define DATABASE_H
/*数据库操作文件*/
#include <QObject>
#include <QSqlDatabase> //数据库驱动类
#include <QSqlQuery>    //SQL执行类
#include <QSqlError>    //SQL出错信息

class Database : public QObject
{
    Q_OBJECT
private:
    ~Database();
    explicit Database(QObject *parent = nullptr);
public:
    static Database* getDbptr();
    void initMysql();//初始化Mysql
    void createAccountTable(); //建用户表
    void createElectricTable();
    //注册
    bool registerUser(const QString& account, const QString& password);
    //登录接口
    bool judgeLogin(const QString& account, const QString& password);


signals:

public slots:
};

#endif // DATABASE_H
