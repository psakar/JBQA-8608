*JBQA-8608*

run including EAP and spring module installation. Should be run first time or after clean

	mvn -Dinstall-eap-6.2 -Dinstall-spring-module-eap-6.2 -Dinstall-as-module-spring-3.2.x -Djbossas-managed-eap-6.2 clean verify
	
run without installation

	mvn -Djbossas-managed-eap-6.2 -Djboss.home=target/jboss-eap-6.2 verify



EAP 5.2

run test
	export JBOSS_HOME=YOUR_PATH_TO_JBOSS_INSTANCE #eg. export JBOSS_HOME=../jboss-eap-5.2/jboss-as
	mvn -Djbossas-managed-5.1 clean verify


run test on server already running in different (possibly remote) jvm
	
	#required to find dependencies
	export JBOSS_HOME=YOUR_PATH_TO_JBOSS_INSTANCE #eg. export JBOSS_HOME=../jboss-eap-5.2/jboss-as
	
	start server #eg 
	mvn -Djbossas-remote-5.1 clean verify
