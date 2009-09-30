mvn clean install 					\
  && mkdir -p ~/tmp/test-jdbc-proc-archetype 		\
  && cd ~/tmp/test-jdbc-proc-archetype 			\
  && rm -fr my-artifactId 				\
  && mvn archetype:generate -B                         	\
    -DarchetypeGroupId=com.googlecode                	\
    -DarchetypeArtifactId=jdbc-proc-archetype        	\
    -DarchetypeVersion=1.0-SNAPSHOT                  	\
    -DarchetypeRepository=http://jdbc-proc.googlecode.com/svn/maven2/release \
    -DgroupId=my.groupid                             	\
    -DartifactId=my-artifactId 				\
    -Dversion=1.0-1-SNAPSHOT				\
  && cd my-artifactId 					\
  && mvn clean install    
  