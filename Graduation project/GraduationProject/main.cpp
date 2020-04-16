#include <QCoreApplication>
#include <ClientServer/mytcpserver.h>
#include <Esp8266Server/gatewayserver.h>
#include <Database/database.h>

int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    //1.启动用户客户端服务
    MyTcpServer::getServer()->listen(QHostAddress::AnyIPv4,10086);
    //2.启动网关客户端服务
    GatewayServer::getServer()->listen(QHostAddress::AnyIPv4,10087);
    Database::getDbptr()->initMysql();
    Database::getDbptr()->createAccountTable();
    Database::getDbptr()->createElectricTable();
    Database::getDbptr()->registerUser("123456","123456");

    return a.exec();
}
