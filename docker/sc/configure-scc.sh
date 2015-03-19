#!/bin/bash

cd /var/scc

mkdir /var/scc/data
echo "localhost"   >> /var/scc/data/appserver.ini
echo "19122"       >> /var/scc/data/appserver.ini

export >> /var/scc/data/env.log

echo "<?xml version='1.0' encoding='UTF-8'?>                     " >> /var/scc/data/SccDatabaseConf.xml
echo "<Database>                                                 " >> /var/scc/data/SccDatabaseConf.xml
echo "   <IsUserDefined>true</IsUserDefined>                     " >> /var/scc/data/SccDatabaseConf.xml
echo "   <RdbmsType>MSSQL</RdbmsType>                            " >> /var/scc/data/SccDatabaseConf.xml
echo "   <ServerName>10.5.11.119</ServerName>                    " >> /var/scc/data/SccDatabaseConf.xml
echo "   <Port>1433</Port>                                       " >> /var/scc/data/SccDatabaseConf.xml
echo "   <MasterLogicalDbName>sctm</MasterLogicalDbName>         " >> /var/scc/data/SccDatabaseConf.xml
echo "   <UserName>sa</UserName>                                 " >> /var/scc/data/SccDatabaseConf.xml
echo "   <UserPwd>eCRwoQBWY9g=</UserPwd>                         " >> /var/scc/data/SccDatabaseConf.xml
echo "   <LastModifiedByBuildNo>6041</LastModifiedByBuildNo>     " >> /var/scc/data/SccDatabaseConf.xml
echo "   <ReadOnlyUserName></ReadOnlyUserName>                   " >> /var/scc/data/SccDatabaseConf.xml
echo "   <ReadOnlyUserPwd></ReadOnlyUserPwd>                     " >> /var/scc/data/SccDatabaseConf.xml
echo "</Database>                                                " >> /var/scc/data/SccDatabaseConf.xml

chmod 777 .*.

