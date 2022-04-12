IJA UML designer - README
--------------------------

Pro překlad a spuštění aplikace je zapotřebí:
- Java SE 11
- Maven 3.6.3 a vyšší

Pro překlad aplikace a vytvoření programové dokumentace je zapotřebí v kořenové složce aplikace zadat následující příkaz do terminálu:

mvn package javadoc:javadoc

Po překladu aplikace lze se spustitelný jar archiv nachází ve složce dest. Dále lze aplikaci spustit pomocí zadání následujícího příkazu do terminálu v kořenové složce hierarchie souborů aplikace:

java -jar dest/VUT_FIT_IJA-1.0.jar

Po zadání zmíněného příkazu se spustí aplikace IJA UML designer. Nápovědu lze vyvolat pomocí tlačítka Help nacházejícím se v právém horním rohu aplikace. Ukázkový příklad se nachází ve složce data.


V případě jakýchkoliv dotazů se neváhejte obrátit na autory aplikace na následující e-mailové adrese:

xchoch09@stud.fit.vutbr.cz