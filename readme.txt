IJA UML designer - README
--------------------------
--------------------------

Pro překlad a spuštění aplikace je zapotřebí:
- Java SE 11
- Maven 3.6.3 a vyšší

Pro překlad aplikace a vytvoření programové dokumentace je zapotřebí v kořenové složce aplikace zadat následující příkaz do terminálu:

mvn package javadoc:javadoc

Po překladu aplikace lze se spustitelný jar archiv nachází ve složce dest. Dále lze aplikaci spustit pomocí zadání následujícího příkazu do terminálu v kořenové složce hierarchie souborů aplikace:

java -jar dest/ija-app.jar

Po zadání zmíněného příkazu se spustí aplikace IJA UML designer. Nápovědu lze vyvolat pomocí tlačítka Help nacházejícím se v právém horním rohu aplikace. Ukázkové příklady se nachází ve složce data.




English translation
--------------------------

To compile and run the application, you need:
- Java SE 11
- Maven 3.6.3 and higher

To translate the application and create the program documentation, it is necessary to enter the following command in the terminal in the root folder of the application:

mvn package javadoc:javadoc

After translating the application, the executable jar archive can be found in the dest folder. Additionally, the application can be started by entering the following command in a terminal at the root of the application's file hierarchy:

java -jar dest/ija-app.jar

After entering the mentioned command, the IJA UML designer application starts. Help can be called up using the Help button located in the upper right corner of the application. Sample examples are located in the data folder.
