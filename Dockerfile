FROM java:8 

RUN apt-get update
RUN apt-get install -y maven

# RUN mvn archetype:generate \
# 		-B \
# 		-DgroupId=com.mycompany \
# 		-DartifactId=my-app \
# 		-Dversion=1.0-SNAPSHOT \
# 		-DarchetypeArtifactId=jooby-archetype \
# 		-DarchetypeGroupId=org.jooby \
# 		-DarchetypeVersion=1.0.1

# COPY pom.xml my-app/pom.xml

# RUN cd my-app && \
# 	mvn package -DskipTests && \
# 	cd .. && \
# 	rm -rf my-app

COPY . /app

WORKDIR /app
RUN mvn package -DskipTests
RUN yes 2 | update-alternatives --config java

EXPOSE 8080

# CMD [ "java", "-jar", "target/app-0.0.1-SNAPSHOT.jar" ]
CMD [ "bash", "-c", "sleep 10 && java -jar target/app-0.0.1-SNAPSHOT.jar" ]