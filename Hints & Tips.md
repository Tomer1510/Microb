## starting new tomcat server in eclipse
* create new server in Eclipse on top of the existing Tomcat installation.
* configure the server to use the existing folders of Tomcat for the server by
	* double-click on the server
	* change the `server location` to `Use Tomcat installation`.
	
## fixing JRE 'un-bound' issue
* right-click on the project
* `Configure build path` under `Build path`
* edit the JRE library under `libraries` to JRE 7.

## add derby to the tomcat server
* copy `derby.jar` and `derbyclient.jar` to `Tomcat/lib`.

## add derby to new project in eclipse
* add to `ServerFolder/ServerName/context.xml` and to `Tomcat/conf/context.xml` the same lines, allways! in this section both of the files would be called `context`.
* to set new DataBase, add the following string to `context` files: `<Resource auth="Container" driverClassName="org.apache.derby.jdbc.EmbeddedDriver" maxActive="20" maxIdle="10" maxWait="-1" name="jdbc/YYY" password="password" type="javax.sql.DataSource" url="jdbc:derby:XXX;create=true" username="username"/>` where **YYY** is the name of the that represents our Derby database and **XXX** is the Derby database name.
* add to `ProjectName/WebContent/WEB-INF/web.xml` the following string: `<resource-ref>
    <description>my connection</description>
    <res-ref-name>jdbc/UserDatasource</res-ref-name>
    <res-type>javax.sql.DataSource</res-type>
    <res-auth>Container</res-auth>
  </resource-ref>`
	 